package com.arman.kotboy.core.memory.cartridge.mbc

import com.arman.kotboy.core.cpu.util.toBoolean
import com.arman.kotboy.core.memory.Ram
import com.arman.kotboy.core.memory.Rom
import com.arman.kotboy.core.memory.cartridge.mbc.battery.Battery
import com.arman.kotboy.core.memory.cartridge.mbc.rtc.Rtc

class Mbc3(rom: Rom, ram: Ram? = null, battery: Battery? = null) : Mbc(rom, ram, battery) {

    private val rtc: Rtc = Rtc()
    private var rtcRegister: Int = 0
    private var latchReady: Boolean = false

    override fun load(rtc: Rtc?) {
        super.load(this.rtc)
    }

    override fun reset() {
        super.reset()
        this.rtcRegister = 0
        this.latchReady = false
    }

    override fun write(address: Int, value: Int): Boolean {
        when (address) {
            in 0x0..0x1fff -> {
                this.ram?.let {
                    it.enabled = (value and 0x0F) == 0x0A
                    if (!it.enabled) {
                        save(rtc)
                    }
                }
            }
            in 0x2000..0x3fff -> romBank = if (value == 0) 1 else value and 0x7F
            in 0x4000..0x5fff -> {
                if (value in 0x08..0x0C) {
                    rtcRegister = value
                } else {
                    ramBank = value and 0x03
                    rtcRegister = 0x0
                }
            }
            in 0x6000..0x7fff -> {
                if (value == 0x01 && latchReady) {
                    this.rtc.tryLatch()
                }
                latchReady = !value.toBoolean()
            }
            else -> return false
        }
        return true
    }

    override fun set(address: Int, value: Int): Boolean {
        if (address in 0xa000..0xbfff && rtcRegister >= 0x08) {
            this.rtc[rtcRegister] = value
            return true
        }

        return super.set(address, value)
    }

    override fun get(address: Int): Int {
        if (address in 0xa000..0xbfff && rtcRegister >= 0x08) {
            return this.rtc[rtcRegister]
        }

        return super.get(address)
    }

}