package com.arman.kotboy.core.io

import com.arman.kotboy.core.cpu.util.toUnsignedInt

class Timer : IoDevice(IoReg.TIMA.address, IoReg.TAC.address) { // TODO: cgb double speed mode

    companion object {

        @JvmField
        val freqs = arrayOf(1024, 16, 64, 256)

    }

    var tima: Int
        get() {
            return super.get(IoReg.TIMA.address)
        }
        set(value) {
            super.set(IoReg.TIMA.address, value)
        }
    var tma: Int
        get() {
            return super.get(IoReg.TMA.address)
        }
        set(value) {
            super.set(IoReg.TMA.address, value)
        }

    var tac: Int
        get() {
            return super.get(IoReg.TAC.address)
        }
        set(value) {
            super.set(IoReg.TAC.address, value)
        }

    override fun tick(cycles: Int): Boolean {
        if (tac and 0b100 == 0) return false
        super.tick(cycles)

        val freq = freqs[tac and 0b11]
        this.tima = this.tima + this.cycles / freq
        this.cycles = this.cycles.rem(freq)

        return if (tima > 0xFF) {
            this.tima = this.tma
            true
        } else false
    }

    override fun reset() {
        super.reset()
        this.tima = 0x00
        this.tma = 0x00
        this.tac = 0xF8 // or 0x00
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