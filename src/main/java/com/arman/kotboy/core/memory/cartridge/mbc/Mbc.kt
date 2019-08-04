package com.arman.kotboy.core.memory.cartridge.mbc

import com.arman.kotboy.core.memory.Memory
import com.arman.kotboy.core.memory.Ram
import com.arman.kotboy.core.memory.Rom
import com.arman.kotboy.core.memory.cartridge.battery.Battery
import com.arman.kotboy.core.memory.cartridge.rtc.Rtc

abstract class Mbc(protected val rom: Rom, protected val ram: Ram? = null, protected val battery: Battery? = null) :
        Memory {

    protected var romBank: Int = 1
    protected var ramBank: Int = 0

    private fun translate(address: Int): Int {
        if (ram == null || ram.size() <= 0) {
            return -1
        }
        return ((address - 0xA000) + (ramBank * 0x2000)).rem(ram.size())
    }

    private fun ramAccessible(bank: Int): Boolean {
        if (ram == null) return false
        return ram.size() > 0 && bank < ram.size()
    }

    protected abstract fun write(address: Int, value: Int): Boolean

    override fun accepts(address: Int): Boolean {
        return address in 0x0..0x7fff || address in 0xa000..0xbfff
    }

    protected fun save(rtc: Rtc? = null) {
        this.battery?.let {
            this.ram?.run { it.save(this, rtc) }
        }
    }

    open fun load(rtc: Rtc? = null) {
        this.battery?.let {
            this.ram?.run { it.load(this, rtc) }
        }
    }

    override fun get(address: Int): Int {
        return when (address) {
            in 0x0..0x3fff -> rom[address]
            in 0x4000..0x7fff -> rom[((address - 0x4000) + (romBank * 0x4000)).rem(rom.size())]
            in 0xa000..0xbfff -> {
                val i = translate(address)
                if (i > -1 && ramAccessible(i)) {
                    return if (ram == null) 0xff
                    else ram[i + 0xA000]
                } else 0xff
            }
            else -> 0xff
        }
    }

    override fun set(address: Int, value: Int): Boolean {
        return when (address) {
            in 0x0..0x7fff -> write(address, value)
            in 0xa000..0xbfff -> {
                val i = translate(address)
                if (i > -1 && ramAccessible(i)) {
                    return ram?.set(i + 0xA000, value) ?: false
                }
                false
            }
            else -> false
        }
    }

    override fun range(): IntRange {
        if (this.ram == null) return this.rom.range()
        return IntRange(this.rom.range().first, this.ram.range().last)
    }

    override fun fill(value: Int) {
        this.ram?.fill(value)
    }

    override fun reset() {
        this.ram?.reset()
    }

    override fun clear() {
        this.ram?.clear()
    }

    override fun toString(): String {
        return this.rom.toString() + this.ram.toString()
    }

}