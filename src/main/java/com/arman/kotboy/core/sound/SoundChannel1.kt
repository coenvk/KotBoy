package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toInt
import com.arman.kotboy.core.io.IoReg
import com.arman.kotboy.core.sound.units.Envelope
import com.arman.kotboy.core.sound.units.Sweep

class SoundChannel1 : SoundChannel(IoReg.NR_10.address, IoReg.NR_14.address) {

    override var nr0: Int
        get() {
            return super.get(IoReg.NR_10.address)
        }
        set(value) {
            super.set(IoReg.NR_10.address, value)
        }
    override var nr1: Int
        get() {
            return super.get(IoReg.NR_11.address)
        }
        set(value) {
            super.set(IoReg.NR_11.address, value)
        }
    override var nr2: Int
        get() {
            return super.get(IoReg.NR_12.address)
        }
        set(value) {
            super.set(IoReg.NR_12.address, value)
        }
    override var nr3: Int
        get() {
            return super.get(IoReg.NR_13.address)
        }
        set(value) {
            super.set(IoReg.NR_13.address, value)
        }
    override var nr4: Int
        get() {
            return super.get(IoReg.NR_14.address)
        }
        set(value) {
            super.set(IoReg.NR_14.address, value)
        }

    override val period: Int
        get() = this.frequency * 4

    private val sweep: Sweep = Sweep()
    private val envelope: Envelope = Envelope()

    private var waveformIndex: Int = 0
    private var waveformBit: Int = 0

    override fun start() {
        super.start()
        this.waveformIndex = 0
        this.sweep.start()
        this.envelope.start()
    }

    override fun stop() {
        // TODO
    }

    override fun trigger() {
        super.trigger()
        waveformIndex = 0
        sweep.trigger()
    }

    override fun tick(cycles: Int): Boolean {
        super.tick(cycles)

        if (--clocks == 0) {
            this.clocks = this.period
            this.waveformBit = getWaveform().at(this.waveformIndex).toInt()
            this.waveformIndex = (this.waveformIndex + 1).rem(0x8)
        }

        // generate waveformBit * volume

        return true
    }

    override fun reset() {
        super.reset()
        this.nr0 = 0x80
        this.nr1 = 0xBF
        this.nr2 = 0xF3
        this.nr3 = 0x00
        this.nr4 = 0xBF
    }

    override fun set(address: Int, value: Int): Boolean {
        var v = value
        when (address) {
            IoReg.NR_10.address -> v = v and 0x7F
            IoReg.NR_14.address -> {
                v = v and 0xC7
                if (v.at(7)) trigger()
            }
        }
        return super.set(address, v)
    }

    override fun get(address: Int): Int {
        var value = 0x00
        when (address) {
            IoReg.NR_10.address -> value = value or 0x80
            IoReg.NR_11.address -> value = value or 0x3F
            IoReg.NR_12.address -> value = value or 0x00
            IoReg.NR_13.address -> value = value or 0xFF
            IoReg.NR_14.address -> value = value or 0xBF
        }
        return value
    }

    private fun getWaveform(): Int {
        val duty = (this.nr1 shr 6) and 0x3
        return when (duty) {
            0 -> 0b00000001
            1 -> 0b10000001
            2 -> 0b10000111
            3 -> 0b01111110
            else -> 0
        }
    }

}