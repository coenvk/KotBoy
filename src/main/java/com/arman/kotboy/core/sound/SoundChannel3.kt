package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toUnsignedInt
import com.arman.kotboy.core.io.IoReg

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

    override val frequency: Int = 2048 - (this.nr33 or ((this.nr34 and 0x7) shl 8))

    override val period: Int = (2048 - this.frequency) * 2

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
        this.nr30 = 0x7F
        this.nr31 = 0xFF
        this.nr32 = 0x9F
        this.nr33 = 0xBF
        this.nr34 = 0x00
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

    private fun isDacEnabled(): Boolean {
        return this.nr30.toByte().at(7)
    }

    private fun getSoundLength(): Int {
        val t1 = this.nr31.toUnsignedInt()
        return (64 - t1) / 256
    }

    private fun getVolumeCode(): Int {
        return (this.nr32 shr 5) and 0x3
    }

    private fun getLower8Frequency(): Int {
        return this.nr33.toUnsignedInt()
    }

    private fun isRestart(): Boolean {
        return this.nr34.toByte().at(7)
    }

    private fun isLengthEnable(): Boolean {
        return this.nr34.toByte().at(6)
    }

    private fun getHigher3Frequency(): Int {
        return this.nr34 and 0x7
    }

}