package com.arman.kotboy.memory.rtc

import java.util.concurrent.TimeUnit

class VirtualClock : Clock {

    private var time: Long = System.currentTimeMillis()

    override fun currentTimeMillis(): Long {
        return this.time
    }

    fun forward(i: Long, unit: TimeUnit) {
        this.time += unit.toMillis(i)
    }

    fun forward(millis: Long) {
        this.time += millis
    }

}