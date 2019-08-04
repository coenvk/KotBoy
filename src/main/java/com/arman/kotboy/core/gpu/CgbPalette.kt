package com.arman.kotboy.core.gpu

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.cgb.CgbReg

class CgbPalette : Palette(CgbReg.BGPI.address, CgbReg.OBPD.address) {

    companion object {

        private const val R_MASK = 0x001F
        private const val G_MASK = 0x03E0
        private const val B_MASK = 0x7C00

    }

    private val bgpPalette: IntArray = IntArray(8 * 4) { 0xFFFFFF }
    private val obpPalette: IntArray = IntArray(8 * 4)

    var bgpi: Int
        get() {
            return super.get(CgbReg.BGPI.address)
        }
        set(value) {
            super.set(CgbReg.BGPI.address, value)
        }
    var bgpd: Int
        get() {
            return super.get(CgbReg.BGPD.address)
        }
        set(value) {
            super.set(CgbReg.BGPD.address, value)
        }
    var obpi: Int
        get() {
            return super.get(CgbReg.OBPI.address)
        }
        set(value) {
            super.set(CgbReg.OBPI.address, value)
        }
    var obpd: Int
        get() {
            return super.get(CgbReg.OBPD.address)
        }
        set(value) {
            super.set(CgbReg.OBPD.address, value)
        }

    private fun cgbToRgb(color: Int): Int {
        val r = color and R_MASK
        val g = (color and G_MASK) shr 5
        val b = (color and B_MASK) shr 10
        return (r shl 16) and (g shl 8) and b
    }

    override fun bgp(color: Int, index: Int): Int {
        val pi = index * 4 + color
        val cgbColor = this.bgpPalette[pi]
        return cgbToRgb(cgbColor)
    }

    override fun obp(color: Int, index: Int): Int {
        val pi = index * 4 + color
        val cgbColor = this.obpPalette[pi]
        return cgbToRgb(cgbColor)
    }

    private fun getBgpIndex(): Int {
        return this.bgpi and 0x3F
    }

    private fun isBgpAutoIncrement(): Boolean {
        return this.bgpi.toByte().at(7)
    }

    private fun getObpIndex(): Int {
        return this.obpi and 0x3F
    }

    private fun isObpAutoIncrement(): Boolean {
        return this.obpi.toByte().at(7)
    }

    override fun set(address: Int, value: Int): Boolean {
        return when (address) {
            CgbReg.BGPD.address -> {
                val index = getBgpIndex()
                val pi = index / 2

                var color = this.bgpPalette[pi]
                color = if (index.rem(0x02) == 0) {
                    (color and 0xFF00) or value
                } else (color and 0x00FF) or (value shl 8)

                this.bgpPalette[pi] = color
                if (isBgpAutoIncrement()) this.bgpi = ((index + 1) and 0x3F) or 0x80
                true
            }
            CgbReg.OBPD.address -> {
                val index = getObpIndex()
                val pi = index / 2

                var color = this.obpPalette[pi]
                color = if (index.rem(0x02) == 0) {
                    (color and 0xFF00) or value
                } else (color and 0x00FF) or (value shl 8)

                this.obpPalette[pi] = color
                if (isObpAutoIncrement()) this.obpi = ((index + 1) and 0x3F) or 0x80
                true
            }
            else -> super.set(address, value)
        }
    }

    override fun get(address: Int): Int {
        return when (address) {
            CgbReg.BGPI.address -> {
                this.bgpi or 0x40
            }
            CgbReg.OBPI.address -> {
                this.obpi or 0x40
            }
            CgbReg.BGPD.address -> {
                val index = getBgpIndex()
                val pi = index / 2
                val color = this.bgpPalette[pi]
                if (index.rem(0x02) == 0) {
                    color and 0x00FF
                } else (color shr 8) and 0x00FF
            }
            CgbReg.OBPD.address -> {
                val index = getObpIndex()
                val pi = index / 2
                val color = this.obpPalette[pi]
                if (index.rem(0x02) == 0) {
                    color and 0x00FF
                } else (color shr 8) and 0x00FF
            }
            else -> super.get(address)
        }
    }

}