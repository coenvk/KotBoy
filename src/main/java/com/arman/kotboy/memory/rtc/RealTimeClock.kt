package com.arman.kotboy.memory.rtc

class RealTimeClock : Clock {

    private var clockStart: Long = currentTimeMillis()
    private var latchStart: Long = 0L

    private var haltSecs: Long = 0L
    private var haltMins: Long = 0L
    private var haltHours: Long = 0L
    private var haltDays: Long = 0L

    private var offsetSec: Long = 0L

    var halt: Boolean = false
        set(value) {
            if (value && !field) {
                latch()
                haltSecs = currentTimeSeconds()
                haltMins = currentTimeMins()
                haltHours = currentTimeHours()
                haltDays = currentTimeDays()
                unlatch()
            } else if (!value && field) {
                this.offsetSec = haltSecs + haltMins * 60 + haltHours * 3600 + haltDays * 86400
                this.clockStart = currentTimeMillis()
            }
            field = value
        }

    fun setSeconds(seconds: Long) {
        if (this.halt) {
            haltSecs = seconds
        }
    }

    fun setMinutes(minutes: Long) {
        if (this.halt) {
            haltMins = minutes
        }
    }

    fun setHours(hours: Long) {
        if (this.halt) {
            haltHours = hours
        }
    }

    fun setDays(days: Long) {
        if (this.halt) {
            haltDays = days
        }
    }

    fun isCounterOverflow(): Boolean {
        return this.clockTimeInSecs() >= 44236800
    }

    fun clearCounterOverflow() {
        while (isCounterOverflow()) {
            this.offsetSec -= 44236800
        }
    }

    private fun clockTimeInSecs(): Long {
        val now = if (this.latchStart == 0L) {
            currentTimeMillis()
        } else {
            this.latchStart
        }
        return (now - this.clockStart) / 1000 + this.offsetSec
    }

    fun latch() {
        this.latchStart = currentTimeMillis()
    }

    fun unlatch() {
        this.latchStart = 0
    }

    fun currentTimeSeconds(): Long {
        return clockTimeInSecs().rem(60)
    }

    fun currentTimeMins(): Long {
        return clockTimeInSecs().rem(3600) / 60
    }

    fun currentTimeHours(): Long {
        return clockTimeInSecs().rem(86400) / 3600
    }

    fun currentTimeDays(): Long {
        return clockTimeInSecs().rem(44236800) / 86400
    }

    fun deserialize(data: LongArray) {
        val secs = data[0]
        val mins = data[1]
        val hours = data[2]
        val days = data[3]
        val daysHigh = data[4]
        val timestamp = data[10]

        this.clockStart = timestamp * 1000
        this.offsetSec = secs + mins * 60 + hours * 3600 + days * 86400 + daysHigh * 22118400
    }

    fun serialze(): LongArray {
        val data = LongArray(11)
        latch()
        data[0] = currentTimeSeconds()
        data[5] = data[0]
        data[1] = currentTimeMins()
        data[6] = data[1]
        data[2] = currentTimeHours()
        data[7] = data[2]
        data[3] = currentTimeDays().rem(256)
        data[8] = data[3]
        data[4] = currentTimeDays().div(256)
        data[9] = data[4]
        data[10] = latchStart / 1000
        unlatch()
        return data
    }

    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

}