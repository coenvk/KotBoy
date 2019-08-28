package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.IoReg

class SoundChannel4 : SoundChannel(IoReg.NR_41.address, IoReg.NR_44.address) {

    override var nr0: Int
        get() = 0xFF
        set(value) {}
    override var nr1: Int
        get() {
            return super.get(IoReg.NR_41.address)
        }
        set(value) {
            super.set(IoReg.NR_41.address, value)
        }
    override var nr2: Int
        get() {
            return super.get(IoReg.NR_42.address)
        }
        set(value) {
            super.set(IoReg.NR_42.address, value)
        }
    override var nr3: Int
        get() {
            return super.get(IoReg.NR_43.address)
        }
        set(value) {
            super.set(IoReg.NR_43.address, value)
        }
    override var nr4: Int
        get() {
            return super.get(IoReg.NR_44.address)
        }
        set(value) {
            super.set(IoReg.NR_44.address, value)
        }

    override val period: Int
        get() {
            val s = (this.nr3 and 0xF0) shr 4
            val d = this.nr3 and 0x07
            val div = if (d == 0) 8 else 2 * d * 8
            return div shl s
        }

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
        this.nr1 = 0xFF
        this.nr2 = 0x00
        this.nr3 = 0x00
        this.nr4 = 0xBF
    }

    override fun set(address: Int, value: Int): Boolean {
        var v = value
        when (address) {
            IoReg.NR_41.address -> v = v and 0x3F
            IoReg.NR_44.address -> {
                v = v and 0xC0
                if (v.at(7)) trigger()
            }
        }
        return super.set(address, v)
    }

    override fun get(address: Int): Int {
        var value = 0x00
        when (address) {
            IoReg.NR_41.address -> value = value or 0xFF
            IoReg.NR_42.address -> value = value or 0x00
            IoReg.NR_43.address -> value = value or 0x00
            IoReg.NR_44.address -> value = value or 0xBF
        }
        return value
    }

}