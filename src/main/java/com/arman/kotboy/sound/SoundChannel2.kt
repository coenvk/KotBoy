package com.arman.kotboy.sound

import com.arman.kotboy.io.IoReg

class SoundChannel2 : SoundChannel(IoReg.NR_21.address, IoReg.NR_24.address) {

    var nr21: Int
        get() {
            return super.get(IoReg.NR_21.address)
        }
        set(value) {
            super.set(IoReg.NR_21.address, value)
        }
    var nr22: Int
        get() {
            return super.get(IoReg.NR_22.address)
        }
        set(value) {
            super.set(IoReg.NR_22.address, value)
        }
    var nr23: Int
        get() {
            return super.get(IoReg.NR_23.address)
        }
        set(value) {
            super.set(IoReg.NR_23.address, value)
        }
    var nr24: Int
        get() {
            return super.get(IoReg.NR_24.address)
        }
        set(value) {
            super.set(IoReg.NR_24.address, value)
        }

    override fun reset() {
        super.reset()
        this.nr21 = 0x3F
        this.nr22 = 0x00
        this.nr23 = 0x00
        this.nr24 = 0xBF
    }

}