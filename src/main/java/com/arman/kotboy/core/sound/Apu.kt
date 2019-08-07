package com.arman.kotboy.core.sound

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.IoDevice
import com.arman.kotboy.core.io.IoReg

class Apu(private val gb: GameBoy) : IoDevice(IoReg.NR_50.address, IoReg.NR_52.address) {

    companion object {

        const val CHANNELS = 4

    }

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

    private val channels: Array<SoundChannel> =
            arrayOf(SoundChannel1(), SoundChannel2(), SoundChannel3(), SoundChannel4())

    private var powerStatus: Boolean = false

    override fun tick(cycles: Int): Boolean {
        // TODO: implement sound channels
        if (!powerStatus) return false

        super.tick(cycles)
        if (this.cycles < Speaker.SAMPLING_CYCLES) return false
        this.cycles -= Speaker.SAMPLING_CYCLES

        this.channels.forEach { it.tick(cycles) }

        val (left, right) = mix()

        this.gb.speaker.play(left, right)
        return true
    }

    private fun mix(): Pair<Int, Int> {
        val enables = this[IoReg.NR_51.address]
        var left = 0
        var right = 0

        for (i in 0 until 4) {
            if (enables.at(i + 4)) {
                left += channels[i].output
            }
            if (enables.at(i)) {
                right += channels[i].output
            }
        }

        left /= 4
        right /= 4

        val masterVolume = this[IoReg.NR_50.address]
        left *= (masterVolume shr 4) and 0x7
        right *= masterVolume and 0x7

        return left to right
    }

    override fun reset() {
        super.reset()
        this.nr50 = 0x77
        this.nr51 = 0xF3
        this.nr52 = 0xF1 // TODO: 0xF0 if sgb
    }

    override fun set(address: Int, value: Int): Boolean {
        var v = value
        when (address) {
            IoReg.NR_52.address -> {
                v = v and 0x8F
                if (v.at(7)) {
                    if (!powerStatus) {
                        powerStatus = true
                        start()
                    }
                } else {
                    if (powerStatus) {
                        powerStatus = false
                        stop()
                    }
                }
            }
        }
        return super.set(address, v)
    }

    override fun get(address: Int): Int {
        var value = 0x00
        when (address) {
            IoReg.NR_50.address -> value = value or 0x00
            IoReg.NR_51.address -> value = value or 0x00
            IoReg.NR_52.address -> value = value or 0x70
        }
        return value
    }

    private fun start() {
        this.channels.forEach { it.start() }
        this.gb.speaker.start()
    }

    private fun stop() {
        this.gb.speaker.stop()
        this.channels.forEach { it.stop() }
    }

}