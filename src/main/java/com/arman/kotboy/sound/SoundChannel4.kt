package com.arman.kotboy.sound

import com.arman.kotboy.io.IoReg

class SoundChannel4 : SoundChannel(IoReg.NR_41.address, IoReg.NR_44.address) {

    var nr41: Int
        get() {
            return super.get(IoReg.NR_41.address)
        }
        set(value) {
            super.set(IoReg.NR_41.address, value)
        }
    var nr42: Int
        get() {
            return super.get(IoReg.NR_42.address)
        }
        set(value) {
            super.set(IoReg.NR_42.address, value)
        }
    var nr43: Int
        get() {
            return super.get(IoReg.NR_43.address)
        }
        set(value) {
            super.set(IoReg.NR_43.address, value)
        }
    var nr44: Int
        get() {
            return super.get(IoReg.NR_44.address)
        }
        set(value) {
            super.set(IoReg.NR_44.address, value)
        }

}