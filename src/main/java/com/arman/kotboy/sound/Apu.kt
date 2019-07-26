package com.arman.kotboy.sound

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

class Apu {

    private val format = AudioFormat(44100f, 16, 2, false, true)
    private val dataLine = AudioSystem.getSourceDataLine(format)

    private val channels: Array<SoundChannel> =
        arrayOf(SoundChannel1(), SoundChannel2(), SoundChannel3(), SoundChannel4())

    private var cycles: Int = 0
    private lateinit var buffer: ByteArray

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

}