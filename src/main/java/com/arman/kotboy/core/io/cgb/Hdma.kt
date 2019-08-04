package com.arman.kotboy.core.io.cgb

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.io.IoDevice

class Hdma(private val gb: GameBoy) : IoDevice(CgbReg.HDMA1.address, CgbReg.HDMA5.address) {

    var hdma1: Int
        get() {
            return super.get(CgbReg.HDMA1.address)
        }
        set(value) {
            super.set(CgbReg.HDMA1.address, value)
        }
    var hdma2: Int
        get() {
            return super.get(CgbReg.HDMA2.address)
        }
        set(value) {
            super.set(CgbReg.HDMA2.address, value)
        }
    var hdma3: Int
        get() {
            return super.get(CgbReg.HDMA3.address)
        }
        set(value) {
            super.set(CgbReg.HDMA3.address, value)
        }
    var hdma4: Int
        get() {
            return super.get(CgbReg.HDMA4.address)
        }
        set(value) {
            super.set(CgbReg.HDMA4.address, value)
        }
    var hdma5: Int
        get() {
            return super.get(CgbReg.HDMA5.address)
        }
        set(value) {
            super.set(CgbReg.HDMA5.address, value)
        }

    private fun getSource(): Int {
        return (this.hdma1 shl 8) or (this.hdma2 and 0xF0)
    }

    private fun getDestination(): Int {
        return (((this.hdma3 and 0x1F) shl 8) or (this.hdma4 and 0xF0)) or 0x8000
    }

    private fun getLength(): Int {
        return ((this.hdma5 and 0x7F) + 1) * 0x10
    }

    private fun getMode(): Int { // TODO: differentiate between mode 0 and mode 1 (HBlank)
        return this.hdma5 and 0x80
    }

    override fun set(address: Int, value: Int): Boolean {
        if (address == CgbReg.HDMA5.address) {
            super.set(address, value)
            transfer()
            super.set(address, 0xFF)
        }
        return super.set(address, value)
    }

    private fun transfer() {
        val src = getSource()
        val dst = getDestination()
        val len = getLength()
        for (j in 0 until len) {
            this.gb.mmu[dst + j] = this.gb.mmu[src + j]
        }
    }

}