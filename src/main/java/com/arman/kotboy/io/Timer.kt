package com.arman.kotboy.io

import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.memory.Address

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

    override fun tick(cycles: Int) {
        if (tac and 0b100 == 0) return
        super.tick(cycles)

        val freq = freqs[tac and 0b11]
        this.tima += this.cycles / freq
        this.cycles = this.cycles.rem(freq)

        if (tima > 0xFF) {
            tima = tma
        }
    }

    override fun set(address: Address, value: Int): Boolean {
        val v = values.toUnsignedInt()
        if (address == IoReg.TAC.address) {
            if (tac and 0b11 != v and 0b11) {
                this.cycles = 0
            }
        }
        return super.set(address, v)
    }

}