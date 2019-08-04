package com.arman.kotboy.core.memory.cartridge.mbc

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toUnsignedInt
import com.arman.kotboy.core.memory.Ram
import com.arman.kotboy.core.memory.Rom
import com.arman.kotboy.core.memory.cartridge.battery.Battery
import com.arman.kotboy.core.memory.cartridge.rtc.Rtc

class Mbc3(rom: Rom, ram: Ram? = null, battery: Battery? = null) : Mbc(rom, ram, battery) {

    private val rtc: Rtc = Rtc()
    private var latchRegister: Int = 0xFF
    private var latchFinished: Boolean = false

    override fun load(rtc: Rtc?) {
        super.load(this.rtc)
    }

    override fun write(address: Int, value: Int): Boolean {
        when (address) {
            in 0x0..0x1fff -> {
                this.ram?.let {
                    it.enabled = (value and 0x0A) == 0x0A
                    if (!it.enabled) {
                        save(rtc)
                    }
                }
            }
            in 0x2000..0x3fff -> romBank = if (value == 0) 1 else value and 0x7F
            in 0x4000..0x5fff -> ramBank = value
            in 0x6000..0x7fff -> {
                if (value == 0x01 && latchRegister == 0x00) {
                    this.latchFinished = if (this.latchFinished) {
                        this.rtc.unlatch()
                        false
                    } else {
                        this.rtc.latch()
                        true
                    }
                }
                latchRegister = value
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
            else -> 0xFFL
        }.toInt()
    }

}