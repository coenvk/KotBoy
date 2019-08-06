package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toUnsignedInt
import com.arman.kotboy.core.io.IoReg

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

    override val frequency: Int = 2048 - (this.nr23 or ((this.nr24 and 0x7) shl 8))

    override val period: Int = (2048 - this.frequency) * 4

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
        this.nr21 = 0x3F
        this.nr22 = 0x00
        this.nr23 = 0x00
        this.nr24 = 0xBF
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

    private fun getDuty(): Int {
        return (this.nr21 shr 6) and 0x3
    }

    private fun getSoundLength(): Int {
        val t1 = this.nr21 and 0x3F
        return (64 - t1) / 256
    }

    private fun getInitialVolume(): Int {
        return (this.nr22 shr 4) and 0xF
    }

    private fun isEnvelopeIncrease(): Boolean {
        return this.nr22.toByte().at(3)
    }

    private fun getEnvelopeSweep(): Int {
        return this.nr22 and 0x7
    }

    private fun getLower8Frequency(): Int {
        return this.nr23.toUnsignedInt()
    }

    private fun isRestart(): Boolean {
        return this.nr24.toByte().at(7)
    }

    private fun isLengthEnable(): Boolean {
        return this.nr24.toByte().at(6)
    }

    private fun getHigher3Frequency(): Int {
        return this.nr24 and 0x7
    }

}