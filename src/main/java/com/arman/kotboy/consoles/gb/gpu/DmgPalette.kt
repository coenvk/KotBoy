package com.arman.kotboy.consoles.gb.gpu

import com.arman.kotboy.core.gpu.Palette
import com.arman.kotboy.core.gui.options.Options
import com.arman.kotboy.core.io.IoReg


class DmgPalette : Palette(IoReg.BGP.address, IoReg.OBP1.address) {

    private val bgpPalette: IntArray
    private val obp0Palette: IntArray
    private val obp1Palette: IntArray

    init {
        val palette = intArrayOf(Options.palette0, Options.palette1, Options.palette2, Options.palette3)
        bgpPalette = palette
        obp0Palette = palette
        obp1Palette = palette
    }

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