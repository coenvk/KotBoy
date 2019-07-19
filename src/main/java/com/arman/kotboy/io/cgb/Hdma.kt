package com.arman.kotboy.io.cgb

import com.arman.kotboy.GameBoy
import com.arman.kotboy.io.IoDevice

class Hdma(private val gb: GameBoy) : IoDevice(CgbReg.HDMA1.address, CgbReg.HDMA5.address) {

    var hdma1: Int
        get() {
            return this[CgbReg.HDMA1.address]
        }
        set(value) {
            this[CgbReg.HDMA1.address] = value
        }
    var hdma2: Int
        get() {
            return this[CgbReg.HDMA2.address]
        }
        set(value) {
            this[CgbReg.HDMA2.address] = value
        }
    var hdma3: Int
        get() {
            return this[CgbReg.HDMA3.address]
        }
        set(value) {
            this[CgbReg.HDMA3.address] = value
        }
    var hdma4: Int
        get() {
            return this[CgbReg.HDMA4.address]
        }
        set(value) {
            this[CgbReg.HDMA4.address] = value
        }
    var hdma5: Int
        get() {
            return this[CgbReg.HDMA5.address]
        }
        set(value) {
            this[CgbReg.HDMA5.address] = value
        }

    override fun set(address: Int, value: Int): Boolean {
        if (address == CgbReg.HDMA5.address) {
            val len = value and 0x7F
            val mode = value and 0x80
            val src = (get(CgbReg.HDMA1.address) shl 8) or (get(CgbReg.HDMA2.address) and 0xF0)
            val dst = ((get(CgbReg.HDMA3.address) and 0x9F) shl 8) or (get(CgbReg.HDMA4.address) and 0xF0)
            transfer(src, dst, len)
            super.set(address, 0xFF)
        }
        return super.set(address, value)
    }

    private fun transfer(src: Int, dst: Int, len: Int) {
        for (i in len..0 step -1) {
            for (j in 0 until 0x10) {
                this.gb.mmu[dst + i * 0x10 + j] = this.gb.mmu[src + i * 0x10 + j]
            }
        }
    }

}