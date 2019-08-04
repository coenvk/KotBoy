package com.arman.kotboy.core.sound

import com.arman.kotboy.core.io.IoDevice
import com.arman.kotboy.core.io.IoReg

class SoundControl : IoDevice(IoReg.NR_50.address, IoReg.NR_52.address) {

    var nr50: Int
        get() {
            return super.get(IoReg.NR_50.address)
        }
        set(value) {
            super.set(IoReg.NR_50.address, value)
        }
    var nr51: Int
        get() {
            return super.get(IoReg.NR_51.address)
        }
        set(value) {
            super.set(IoReg.NR_51.address, value)
        }
    var nr52: Int
        get() {
            return super.get(IoReg.NR_52.address)
        }
        set(value) {
            super.set(IoReg.NR_52.address, value)
        }

    override fun reset() {
        super.reset()
        this.nr50 = 0x77
        this.nr51 = 0xF3
        this.nr52 = 0xF1 // TODO: 0xF0 if sgb
    }

    override fun set(address: Int, value: Int): Boolean {
        var v = value
        when (address) {
            IoReg.NR_52.address -> v = v and 0x8F
        }
        return super.set(address, v)
    }

    override fun get(address: Int): Int {
        var value = 0x00
        when (address) {
            IoReg.NR_50.address -> value = value or 0x00
            IoReg.NR_51.address -> value = value or 0x00
            IoReg.NR_52.address -> value = value or 0x70
        }
        return value
    }

}