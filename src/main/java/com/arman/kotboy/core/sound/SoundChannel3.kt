package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.IoReg

class SoundChannel3 : SoundChannel(IoReg.NR_30.address, IoReg.NR_34.address) {

    override var nr0: Int
        get() {
            return super.get(IoReg.NR_30.address)
        }
        set(value) {
            super.set(IoReg.NR_30.address, value)
        }
    override var nr1: Int
        get() {
            return super.get(IoReg.NR_31.address)
        }
        set(value) {
            super.set(IoReg.NR_31.address, value)
        }
    override var nr2: Int
        get() {
            return super.get(IoReg.NR_32.address)
        }
        set(value) {
            super.set(IoReg.NR_32.address, value)
        }
    override var nr3: Int
        get() {
            return super.get(IoReg.NR_33.address)
        }
        set(value) {
            super.set(IoReg.NR_33.address, value)
        }
    override var nr4: Int
        get() {
            return super.get(IoReg.NR_34.address)
        }
        set(value) {
            super.set(IoReg.NR_34.address, value)
        }

    override val period: Int
        get() = this.frequency * 2

    override val length: Int = 256

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
        this.nr0 = 0x7F
        this.nr1 = 0xFF
        this.nr2 = 0x9F
        this.nr3 = 0xBF
        this.nr4 = 0x00
    }

    override fun set(address: Int, value: Int): Boolean {
        var v = value
        when (address) {
            IoReg.NR_30.address -> v = v and 0x80
            IoReg.NR_32.address -> v = v and 0x60
            IoReg.NR_34.address -> {
                v = v and 0xC7
                if (v.at(7)) trigger()
            }
        }
        return super.set(address, v)
    }

    override fun get(address: Int): Int {
        var value = 0x00
        when (address) {
            IoReg.NR_30.address -> value = value or 0x7F
            IoReg.NR_31.address -> value = value or 0xFF
            IoReg.NR_32.address -> value = value or 0x9F
            IoReg.NR_33.address -> value = value or 0xFF
            IoReg.NR_34.address -> value = value or 0xBF
        }
        return value
    }

}