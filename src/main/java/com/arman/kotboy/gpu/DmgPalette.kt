package com.arman.kotboy.gpu

import com.arman.kotboy.io.IoReg


class DmgPalette : Palette(IoReg.BGP.address, IoReg.OBP1.address) {

    companion object {

        private const val WHITE = 0xFFFFFF
        private const val LIGHT_GRAY = 0xAAAAAA
        private const val DARK_GRAY = 0x555555
        private const val BLACK = 0x000000

    }

    private val bgpPalette: IntArray = intArrayOf(WHITE, LIGHT_GRAY, DARK_GRAY, BLACK)
    private val obp0Palette: IntArray = intArrayOf(WHITE, LIGHT_GRAY, DARK_GRAY, BLACK)
    private val obp1Palette: IntArray = intArrayOf(WHITE, LIGHT_GRAY, DARK_GRAY, BLACK)

    var bgp: Int
        get() {
            return super.get(IoReg.BGP.address)
        }
        set(value) {
            super.set(IoReg.BGP.address, value)
        }
    var obp0: Int
        get() {
            return super.get(IoReg.OBP0.address)
        }
        set(value) {
            super.set(IoReg.OBP0.address, value and 0xFC)
        }
    var obp1: Int
        get() {
            return super.get(IoReg.OBP1.address)
        }
        set(value) {
            super.set(IoReg.OBP1.address, value and 0xFC)
        }

    override fun bgp(color: Int, index: Int): Int {
        return this.bgpPalette[(this.bgp ushr (color * 2)) and 3]
    }

    override fun obp(color: Int, index: Int): Int {
        return if (index == 0) this.obp0Palette[(this.obp0 ushr (color * 2)) and 3]
        else this.obp1Palette[(this.obp1 ushr (color * 2)) and 3]
    }

    override fun reset() {
        this.bgp = 0xFC
        this.obp0 = 0xFF
        this.obp1 = 0xFF
    }

}