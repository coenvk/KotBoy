package com.arman.kotboy.sound

import com.arman.kotboy.io.IoDevice
import com.arman.kotboy.io.IoReg

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

}