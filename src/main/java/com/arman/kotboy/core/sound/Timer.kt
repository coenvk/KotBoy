package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.Cpu

class Timer(val frequency: Int) {

    private val period: Int
        get() = Cpu.DMG_CLOCK_SPEED / frequency

    private var counter: Int = 0

    fun reset() {
        this.counter = this.period
    }

    fun tick() {
        if (--this.counter == 0) {
            this.counter = this.period
        }
    }

}