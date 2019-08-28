package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.Cpu

interface Speaker {

    fun start()
    fun stop()
    fun play(left: Int, right: Int)

    companion object {

        const val SAMPLING_RATE = 22050f
        const val SAMPLING_CYCLES = (Cpu.DMG_CLOCK_SPEED / SAMPLING_RATE).toInt() // FIXME: base on gb clockSpeed
        const val BUFFER_SIZE = 1024

    }

}