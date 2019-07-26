package com.arman.kotboy.sound

import com.arman.kotboy.cpu.util.at
import com.arman.kotboy.cpu.util.toInt
import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.io.IoReg

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

    private fun getSweepTime(): Int {
        return ((this.nr10 shr 4) and 0x7) / 128
    }

    private fun isSweepDecrease(): Boolean {
        return this.nr10.toByte().at(3)
    }

    private fun getSweepShift(): Int {
        return this.nr10 and 0x7
    }

    private fun getWavDuty(): Int {
        return (this.nr11 shr 6) and 0x3
    }

    private fun getSoundLength(): Int {
        val t1 = this.nr11 and 0x3F
        return (64 - t1) / 256
    }

    private fun getInitialEnvelopeVolume(): Int {
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

    private fun getConsecutiveSelection(): Int {
        return this.nr14.toByte().at(6).toInt()
    }

    private fun getHigher3Frequency(): Int {
        return this.nr14 and 0x7
    }

}