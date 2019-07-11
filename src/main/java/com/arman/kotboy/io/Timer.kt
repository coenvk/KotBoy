package com.arman.kotboy.io

import com.arman.kotboy.cpu.util.toUnsignedInt

class Timer : IoDevice(IoReg.TIMA.address, IoReg.TAC.address) {

    var tima: Int
        get() {
            return this[IoReg.TIMA.address]
        }
        set(value) {
            this[IoReg.TIMA.address] = value
        }
    var tma: Int
        get() {
            return this[IoReg.TMA.address]
        }
        set(value) {
            this[IoReg.TMA.address] = value
        }
    var tac: Int
        get() {
            return this[IoReg.TAC.address]
        }
        set(value) {
            this[IoReg.TAC.address] = value
        }

    companion object {
        val freqs = arrayOf(1024, 16, 64, 256)
    }

    override fun tick(cycles: Int): Boolean {
        if (tac and 0b100 == 0) return false
        super.tick(cycles)

        val freq = freqs[tac and 0b11]
        super.set(IoReg.TIMA.address, this.tima + this.cycles / freq)
        this.cycles = this.cycles.rem(freq)

        return if (tima > 0xFF) {
            super.set(IoReg.TIMA.address, tma)
            true
        } else false
    }

    override fun reset() {
        super.reset()
        super.set(IoReg.TIMA.address, 0x00)
        super.set(IoReg.TMA.address, 0x00)
        super.set(IoReg.TAC.address, 0xF8)
    }

    override fun set(address: Int, value: Int): Boolean {
        val v = value.toUnsignedInt()
        if (address == IoReg.TAC.address) {
            if (tac and 0b11 != v and 0b11) {
                this.cycles = 0
            }
        }
        return super.set(address, v)
    }
    
}