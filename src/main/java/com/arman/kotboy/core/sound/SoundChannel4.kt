package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toInt
import com.arman.kotboy.core.io.IoReg

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

    override val frequency: Int = 2048 - (this.nr43 or ((this.nr44 and 0x7) shl 8))

    override val period: Int = 0 // TODO

    override fun start() {
        throw UnsupportedOperationException("Operation, start, has not been implemented yet!")
    }

    override fun stop() {
        throw UnsupportedOperationException("Operation, stop, has not been implemented yet!")
    }

    override fun trigger() {
        throw UnsupportedOperationException("Operation, trigger, has not been implemented yet!")
    }

    override fun reset() {
        super.reset()
        this.nr41 = 0xFF
        this.nr42 = 0x00
        this.nr43 = 0x00
        this.nr44 = 0xBF
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

    private fun getSoundLength(): Int {
        val t1 = this.nr41 and 0x3F
        return (64 - t1) / 256
    }

    private fun getInitialVolume(): Int {
        return (this.nr42 shr 4) and 0xF
    }

    private fun isEnvelopeIncrease(): Boolean {
        return this.nr42.toByte().at(3)
    }

    private fun getEnvelopeSweep(): Int {
        return this.nr42 and 0x7
    }

    private fun getClockShift(): Int {
        return (this.nr43 shr 4) and 0xF
    }

    private fun getLfsrWidth(): Int {
        return this.nr43.toByte().at(3).toInt()
    }

    private fun getDivisorCode(): Int {
        return this.nr43 and 0x7
    }

    private fun isRestart(): Boolean {
        return this.nr44.toByte().at(7)
    }

    private fun isLengthEnable(): Boolean {
        return this.nr44.toByte().at(6)
    }

}