package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.IoReg

class SoundChannel2 : SoundChannel(IoReg.NR_21.address, IoReg.NR_24.address) {

    override var nr0: Int
        get() = 0xFF
        set(value) {}
    override var nr1: Int
        get() {
            return super.get(IoReg.NR_21.address)
        }
        set(value) {
            super.set(IoReg.NR_21.address, value)
        }
    override var nr2: Int
        get() {
            return super.get(IoReg.NR_22.address)
        }
        set(value) {
            super.set(IoReg.NR_22.address, value)
        }
    override var nr3: Int
        get() {
            return super.get(IoReg.NR_23.address)
        }
        set(value) {
            super.set(IoReg.NR_23.address, value)
        }
    override var nr4: Int
        get() {
            return super.get(IoReg.NR_24.address)
        }
        set(value) {
            super.set(IoReg.NR_24.address, value)
        }

    override val period: Int
        get() = this.frequency * 4

    override fun start() {
        // TODO
    }

    override fun stop() {
        // TODO
    }

    override fun trigger() {
        // TODO
    }

    override fun reset() {
        super.reset()
        this.nr1 = 0x3F
        this.nr2 = 0x00
        this.nr3 = 0x00
        this.nr4 = 0xBF
    }

    override fun set(address: Int, value: Int): Boolean {
        var v = value
        when (address) {
            IoReg.NR_24.address -> {
                v = v and 0xC7
                if (v.at(7)) trigger()
            }
        }
        return super.set(address, v)
    }

    override fun get(address: Int): Int {
        var value = 0x00
        when (address) {
            IoReg.NR_21.address -> value = value or 0x3F
            IoReg.NR_22.address -> value = value or 0x00
            IoReg.NR_23.address -> value = value or 0xFF
            IoReg.NR_24.address -> value = value or 0xBF
        }
        return value
    }

}