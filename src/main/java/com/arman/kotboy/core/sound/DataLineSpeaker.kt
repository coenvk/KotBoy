package com.arman.kotboy.core.sound

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

class DataLineSpeaker : Speaker {

    private val format = AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, Speaker.SAMPLING_RATE, 8, 2, 2, Speaker.SAMPLING_RATE, false)
    private val dataLine = AudioSystem.getSourceDataLine(format)

    private lateinit var buffer: ByteArray

    private var i: Int = 0

    override fun start() {
        this.dataLine.open(format, Speaker.BUFFER_SIZE)
        this.dataLine.start()
        this.buffer = ByteArray(this.dataLine.bufferSize)
    }

    override fun stop() {
        this.dataLine.drain()
        this.dataLine.stop()
    }

    override fun play(left: Int, right: Int) {
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
    }

}