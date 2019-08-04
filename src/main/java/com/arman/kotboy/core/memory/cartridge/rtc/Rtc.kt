package com.arman.kotboy.core.memory.cartridge.rtc

class Rtc {

    companion object {

        const val SECS_PER_MIN = 60
        const val SECS_PER_HOUR = SECS_PER_MIN * 60
        const val SECS_PER_DAY = SECS_PER_HOUR * 24
    }

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
                haltSecs = currentTimeSecs()
                haltMins = currentTimeMins()
                haltHours = currentTimeHours()
                haltDays = currentTimeDays()
                unlatch()
            } else if (!value && field) {
                this.offsetSec =
                    haltSecs + haltMins * SECS_PER_MIN + haltHours * SECS_PER_HOUR + haltDays * SECS_PER_DAY
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
        return this.clockTimeInSecs() >= 512 * SECS_PER_DAY
    }

    fun clearCounterOverflow() {
        while (isCounterOverflow()) {
            this.offsetSec -= 512 * SECS_PER_DAY
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

    fun currentTimeSecs(): Long {
        return clockTimeInSecs().rem(SECS_PER_MIN)
    }

    fun currentTimeMins(): Long {
        return clockTimeInSecs().rem(SECS_PER_HOUR) / SECS_PER_MIN
    }

    fun currentTimeHours(): Long {
        return clockTimeInSecs().rem(SECS_PER_DAY) / SECS_PER_HOUR
    }

    fun currentTimeDays(): Long {
        return clockTimeInSecs().rem(512 * SECS_PER_DAY) / SECS_PER_DAY
    }

    fun deserialize(data: LongArray) {
        val secs = data[0]
        val mins = data[1]
        val hours = data[2]
        val days = data[3]
        val daysHigh = data[4]
        val timestamp = data[10]

        this.clockStart = timestamp * 1000
        this.offsetSec =
            secs + mins * SECS_PER_MIN + hours * SECS_PER_HOUR + days * SECS_PER_DAY + daysHigh * 256 * SECS_PER_DAY
    }

    fun serialize(): LongArray {
        val data = LongArray(12)
        latch()
        data[0] = currentTimeSecs()
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

    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

}