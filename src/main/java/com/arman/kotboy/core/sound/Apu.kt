package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.IoReg
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

class Apu {

    private val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100f, 8, 2, 2, 44100f, true)
    private val dataLine = AudioSystem.getSourceDataLine(format)

    private val channels: Array<SoundChannel> =
            arrayOf(SoundChannel1(), SoundChannel2(), SoundChannel3(), SoundChannel4())

    private var cycles: Int = 0
    private lateinit var buffer: ByteArray

    private val control: SoundControl = SoundControl()

    fun start() {
        this.dataLine.open(format)
        this.dataLine.start()
        this.buffer = ByteArray(this.dataLine.bufferSize)
    }

    fun stop() {
        this.dataLine.drain()
        this.dataLine.stop()
    }

    fun tick(cycles: Int) {
        this.cycles += cycles
    }

    private fun mix(outputs: IntArray): Pair<Int, Int> {
        val enables = control[IoReg.NR_51.address]
        var left = 0
        var right = 0

        for (i in 0 until 4) {
            if (enables.at(i + 4)) {
                left += outputs[i]
            }
            if (enables.at(i)) {
                right += outputs[i]
            }
        }

        left /= 4
        right /= 4

        val volume = control[IoReg.NR_50.address]
        left *= (volume shr 4) and 0x7
        right *= volume and 0x7

        return left to right
    }

}