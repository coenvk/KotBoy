package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toInt
import com.arman.kotboy.core.cpu.util.toUnsignedInt
import com.arman.kotboy.core.io.IoReg
import com.arman.kotboy.core.sound.units.Envelope
import com.arman.kotboy.core.sound.units.Sweep

class SoundChannel1 : SoundChannel(IoReg.NR_10.address, IoReg.NR_14.address) {

    /*
    Bit 6-4 - Sweep Time
    Bit 3   - Sweep Increase/Decrease
                 0: Addition    (frequency increases)
                 1: Subtraction (frequency decreases)
    Bit 2-0 - Number of sweep shift (n: 0-7)
    Sweep Time:
      000: sweep off - no freq change
      001: 7.8 ms  (1/128Hz)
      010: 15.6 ms (2/128Hz)
      011: 23.4 ms (3/128Hz)
      100: 31.3 ms (4/128Hz)
      101: 39.1 ms (5/128Hz)
      110: 46.9 ms (6/128Hz)
      111: 54.7 ms (7/128Hz)

    The change of frequency (NR13,NR14) at each shift is calculated by the following formula where X(0) is initial freq & X(t-1) is last freq:
      X(t) = X(t-1) +/- X(t-1)/2^n
     */
    var nr10: Int
        get() {
            return super.get(IoReg.NR_10.address)
        }
        set(value) {
            super.set(IoReg.NR_10.address, value)
        }
    /*
    Bit 7-6 - Wave Pattern Duty (Read/Write)
    Bit 5-0 - Sound length data (Write Only) (t1: 0-63)
    Wave Duty:
      00: 12.5% ( _-------_-------_------- )
      01: 25%   ( __------__------__------ )
      10: 50%   ( ____----____----____---- ) (normal)
      11: 75%   ( ______--______--______-- )
    Sound Length = (64-t1)*(1/256) seconds
    The Length value is used only if Bit 6 in NR14 is set.
     */
    var nr11: Int
        get() {
            return super.get(IoReg.NR_11.address)
        }
        set(value) {
            super.set(IoReg.NR_11.address, value)
        }
    /*
    Bit 7-4 - Initial Volume of envelope (0-0Fh) (0=No Sound)
    Bit 3   - Envelope Direction (0=Decrease, 1=Increase)
    Bit 2-0 - Number of envelope sweep (n: 0-7)
                (If zero, stop envelope operation.)
    Length of 1 step = n*(1/64) seconds
     */
    var nr12: Int
        get() {
            return super.get(IoReg.NR_12.address)
        }
        set(value) {
            super.set(IoReg.NR_12.address, value)
        }
    /*
    Lower 8 bits of 11 bit frequency (x).
    Next 3 bit are in NR14 ($FF14)
     */
    var nr13: Int
        get() {
            return super.get(IoReg.NR_13.address)
        }
        set(value) {
            super.set(IoReg.NR_13.address, value)
        }
    /*
    Bit 7   - Initial (1=Restart Sound)     (Write Only)
    Bit 6   - Counter/consecutive selection (Read/Write)
                (1=Stop output when length in NR11 expires)
    Bit 2-0 - Frequency's higher 3 bits (x) (Write Only)
    Frequency = 131072/(2048-x) Hz
     */
    var nr14: Int
        get() {
            return super.get(IoReg.NR_14.address)
        }
        set(value) {
            super.set(IoReg.NR_14.address, value)
        }

    override val frequency: Int = 2048 - (this.nr13 or ((this.nr14 and 0x7) shl 8))
    override val period: Int = this.frequency * 4

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
        throw UnsupportedOperationException("Operation, stop, has not been implemented yet!")
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
        this.nr10 = 0x80
        this.nr11 = 0xBF
        this.nr12 = 0xF3
        this.nr13 = 0x00
        this.nr14 = 0xBF
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

    private fun getSweepTime(): Int {
        return ((this.nr10 shr 4) and 0x7) / 128
    }

    private fun isSweepDecrease(): Boolean {
        return this.nr10.toByte().at(3)
    }

    private fun getSweepShift(): Int {
        return this.nr10 and 0x7
    }

    private fun getDuty(): Int {
        return (this.nr11 shr 6) and 0x3
    }

    private fun getWaveform(): Int {
        return when (getDuty()) {
            0 -> 0b00000001
            1 -> 0b10000001
            2 -> 0b10000111
            3 -> 0b01111110
            else -> 0
        }
    }

    private fun getSoundLength(): Int {
        val t1 = this.nr11 and 0x3F
        return (64 - t1) / 256
    }

    private fun getInitialVolume(): Int {
        return (this.nr12 shr 4) and 0xF
    }

    private fun isEnvelopeIncrease(): Boolean {
        return this.nr12.toByte().at(3)
    }

    private fun getEnvelopeSweep(): Int {
        return this.nr12 and 0x7
    }

    private fun getLower8Frequency(): Int {
        return this.nr13.toUnsignedInt()
    }

    private fun isRestart(): Boolean {
        return this.nr14.toByte().at(7)
    }

    private fun isLengthEnable(): Boolean {
        return this.nr14.toByte().at(6)
    }

    private fun getHigher3Frequency(): Int {
        return this.nr14 and 0x7
    }

}