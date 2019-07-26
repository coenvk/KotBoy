package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.cpu.util.at
import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom
import com.arman.kotboy.memory.cartridge.battery.Battery
import com.arman.kotboy.memory.cartridge.rtc.Rtc

class Mbc3(rom: Rom, ram: Ram? = null, battery: Battery? = null) : Mbc(rom, ram, battery) {

    // TODO: RTC
    private val rtc: Rtc = Rtc()
    private var mappedRegister: Int = 0
    private var registerMapped: Boolean = false
    private var latchReady: Boolean = false

    init {
        this.battery?.loadRtc(rtc)
    }

    override fun write(address: Int, value: Int): Boolean {
        when (address) {
            in 0x0..0x1fff -> {
                this.ram?.let {
                    it.enabled = (value and 0x0A) == 0x0A
                    if (!it.enabled) {
                        save()
                        this.battery?.saveRtc(rtc)
                    }
                }
            }
            in 0x2000..0x3fff -> romBank = if (value == 0) 1 else value and 0x7F
            in 0x4000..0x5fff -> if (value in 0x0..0x3) {
                ramBank = value and 0x3
                this.registerMapped = false

            } else if (value in 0x8..0xC) {
                this.mappedRegister = value - 0x8
                this.registerMapped = true
            }
            in 0x6000..0x7fff -> {
                /* When writing 00h, and then 01h to this register, the current
                   time becomes latched into the RTC registers
                   08h  RTC S   Seconds   0-59 (0-3Bh)
                   09h  RTC M   Minutes   0-59 (0-3Bh)
                   0Ah  RTC H   Hours     0-23 (0-17h)
                   0Bh  RTC DL  Lower 8 bits of Day Counter (0-FFh)
                   0Ch  RTC DH  Upper 1 bit of Day Counter, Carry Bit, Halt Flag
                         Bit 0  Most significant bit of Day Counter (Bit 8)
                         Bit 6  Halt (0=Active, 1=Stop Timer)
                         Bit 7  Day Counter Carry Bit (1=Counter Overflow) */
                if (value == 0x0) {
                    this.latchReady = true
                } else if (value == 0x1) {
                    if (this.latchReady) {
                        this.rtc.latch()
                    } else {
                        this.rtc.unlatch()
                    }
                    this.latchReady = false
                }
            }
            else -> return false
        }
        return true
    }

    override fun set(address: Int, value: Int): Boolean {
        if (address in 0xa000..0xbfff && ramBank >= 4) {
            setTimer(value)
            return true
        }

        return super.set(address, value)
    }

    override fun get(address: Int): Int {
        if (address in 0xa000..0xbfff && ramBank >= 4) {
            return getTimer()
        }

        return super.get(address)
    }

    private fun setTimer(value: Int) {
        val v = value.toLong()
        val days = rtc.currentTimeDays().toInt()
        when (ramBank) {
            0x08 -> rtc.setSeconds(v)
            0x09 -> rtc.setMinutes(v)
            0x0A -> rtc.setHours(v)
            0x0B -> rtc.setDays(((days and 0x100) or value.toUnsignedInt()).toLong())
            0x0C -> {
                rtc.setDays((days.toUnsignedInt() or ((value and 0x1) shl 8)).toLong())
                rtc.halt = value.toByte().at(6)
                if (!value.toByte().at(7)) {
                    rtc.clearCounterOverflow()
                }
            }
        }
    }

    private fun getTimer(): Int {
        return when (ramBank) {
            0x08 -> rtc.currentTimeSecs()
            0x09 -> rtc.currentTimeMins()
            0x0A -> rtc.currentTimeHours()
            0x0B -> rtc.currentTimeDays() and 0xFF
            0x0C -> {
                var ret = (rtc.currentTimeDays().toInt() and 0x100) shr 8
                ret = ret or (if (rtc.halt) (1 shl 6) else 0)
                ret = ret or (if (rtc.isCounterOverflow()) (1 shl 7) else 0)
                ret.toLong()
            }
            else -> 0L
        }.toInt()
    }

}