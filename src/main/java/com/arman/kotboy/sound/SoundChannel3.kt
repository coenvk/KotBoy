package com.arman.kotboy.sound

import com.arman.kotboy.io.IoReg

class SoundChannel3 : SoundChannel(IoReg.NR_30.address, IoReg.NR_34.address) {

    var nr30: Int
        get() {
            return super.get(IoReg.NR_30.address)
        }
        set(value) {
            super.set(IoReg.NR_30.address, value)
        }
    var nr31: Int
        get() {
            return super.get(IoReg.NR_31.address)
        }
        set(value) {
            super.set(IoReg.NR_31.address, value)
        }
    var nr32: Int
        get() {
            return super.get(IoReg.NR_32.address)
        }
        set(value) {
            super.set(IoReg.NR_32.address, value)
        }
    var nr33: Int
        get() {
            return super.get(IoReg.NR_33.address)
        }
        set(value) {
            super.set(IoReg.NR_33.address, value)
        }
    var nr34: Int
        get() {
            return super.get(IoReg.NR_34.address)
        }
        set(value) {
            super.set(IoReg.NR_34.address, value)
        }

}