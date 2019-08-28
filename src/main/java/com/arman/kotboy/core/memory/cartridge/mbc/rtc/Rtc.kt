package com.arman.kotboy.core.memory.cartridge.mbc.rtc

class Rtc {

    companion object {

        const val MILLIS_PER_SEC = 1000
        const val SECS_PER_MIN = 60
        const val MINS_PER_HOUR = 60
        const val SECS_PER_HOUR = SECS_PER_MIN * MINS_PER_HOUR
        const val HOURS_PER_DAY = 24
        const val SECS_PER_DAY = SECS_PER_HOUR * HOURS_PER_DAY
    }

    private var time: Long = 0L

    private var latched: Boolean = false

    private var latchRegs: IntArray = IntArray(5)
    private var regs: IntArray = IntArray(5)

    private fun latch() {
        this.update()
        for (i in 0 until 5) {
            this.latchRegs[i] = this.regs[i]
        }
        this.latched = true
    }

    private fun unlatch() {
        this.latched = false
    }

    fun tryLatch() {
        if (latched) unlatch() else latch()
    }

    private fun update() {
        val now = this.currentTimeMillis()
        var clockTime = 0L
        if ((this.regs[4] and 0x40) == 0 && now > this.time) {
            clockTime = (now - this.time) / MILLIS_PER_SEC
        }
        clockTime += regs[0] + regs[1] * SECS_PER_MIN + regs[2] * SECS_PER_HOUR + regs[3] * SECS_PER_DAY + (regs[4] and 0x1) * SECS_PER_DAY * 256

        regs[0] = clockTime.rem(SECS_PER_MIN).toInt()
        clockTime /= SECS_PER_MIN
        regs[1] = clockTime.rem(MINS_PER_HOUR).toInt()
        clockTime /= MINS_PER_HOUR
        regs[2] = clockTime.rem(HOURS_PER_DAY).toInt()
        clockTime /= HOURS_PER_DAY
        regs[3] = clockTime.rem(256).toInt()
        clockTime /= 256
        regs[4] = (regs[4] and 0xFE) or (clockTime.rem(2).toInt())
        clockTime /= 2

        regs[4] = regs[4] or (if (clockTime > 0) 0x80 else 0x00) // overflow

        this.time = now
    }

    operator fun set(reg: Int, value: Int) {
        if (reg in 0x08..0x0C) {
            this.update()
            this.regs[reg - 0x08] = value
        }
    }

    operator fun get(reg: Int): Int {
        return when {
            reg !in 0x08..0x0C -> 0xFF
            this.latched -> this.latchRegs[reg - 0x08]
            else -> {
                this.update()
                this.regs[reg - 0x08]
            }
        }
    }

    fun deserialize(data: LongArray) {
        for (i in 0 until 5) {
            this.regs[i] = data[i].toInt()
//            this.latchRegs[i] = data[i + 5].toInt()
        }

        this.time = data[10] * MILLIS_PER_SEC
    }

    fun serialize(): LongArray {
        val data = LongArray(12)
        latch()
        for (i in 0 until 5) {
            data[i] = this.regs[i].toLong()
            data[i + 5] = this.regs[i].toLong()
        }
        data[10] = this.currentTimeMillis() / MILLIS_PER_SEC
        unlatch()
        return data
    }

    private fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

}