package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.Cpu
import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.IoDevice
import com.arman.kotboy.core.io.IoReg
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

class Apu : IoDevice(IoReg.NR_50.address, IoReg.NR_52.address) {

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

    private val samplingRate: Float = 22050f
    private val samplingCycles: Int = (Cpu.DMG_CLOCK_SPEED / samplingRate).toInt()

    private val bufferSize: Int = 1024
    private val format = AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, samplingRate, 8, 2, 2, samplingRate, false)

    private val dataLine = AudioSystem.getSourceDataLine(format)

    private val channels: Array<SoundChannel> =
            arrayOf(SoundChannel1(), SoundChannel2(), SoundChannel3(), SoundChannel4())

    private var i: Int = 0
    private var powerStatus: Boolean = false

    private lateinit var buffer: ByteArray

    fun start() {
        channels.forEach { it.start() }

        this.dataLine.open(format, bufferSize)
        this.dataLine.start()
        this.buffer = ByteArray(this.dataLine.bufferSize)
    }

    fun stop() {
        this.dataLine.drain()
        this.dataLine.stop()

        channels.forEach { it.stop() }
    }

    override fun tick(cycles: Int): Boolean {
        return false // TODO: implement sound channels
        if (!powerStatus) return false

        super.tick(cycles)
        if (this.cycles < this.samplingCycles) return false
        this.cycles -= this.samplingCycles

        channels.forEach { it.tick(cycles) }

        val (left, right) = mix()

        buffer[i++] = left.toByte()
        buffer[i++] = right.toByte()

        if (i >= buffer.size) {

            var offset = 0
            while (true) {
                offset += this.dataLine.write(buffer, offset, buffer.size)
                if (offset == buffer.size) break
            }

            i = 0
        }
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

}