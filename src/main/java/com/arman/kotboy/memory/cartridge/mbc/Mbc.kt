package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.memory.Memory
import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom

abstract class Mbc(protected val rom: Rom, protected val ram: Ram? = null) : Memory {

    protected var romBank: Int = 1
    protected var ramBank: Int = 0
    protected var ramEnabled: Boolean = true

    private fun getRamBank(address: Int): Int {
        if (ram == null || ram.size() <= 0) {
            return -1
        }
        return ((address - 0xA000) + (ramBank * 0x2000)).rem(ram.size())
    }

    private fun ramAccessible(bank: Int): Boolean {
        if (ram == null) return false
        return ramEnabled && ram.size() > 0 && bank < ram.size()
    }

    protected abstract fun write(address: Int, value: Int): Boolean

    override fun accepts(address: Int): Boolean {
        return address in 0x0..0x7fff || address in 0xa000..0xbfff
    }

    override fun get(address: Int): Int {
        return when (address) {
            in 0x0..0x3fff -> rom[address]
            in 0x4000..0x7fff -> rom[((address - 0x4000) + (romBank * 0x4000)).rem(rom.size())]
            in 0xa000..0xbfff -> {
                val i = getRamBank(address)
                if (i > -1 && ramAccessible(i)) {
                    if (ram == null) return 0xff
                    ram[i]
                } else 0xff
            }
            else -> 0xff
        }
    }

    override fun set(address: Int, value: Int): Boolean {
        return when (address) {
            in 0x0..0x7fff -> write(address, value)
            in 0xa000..0xbfff -> {
                val i = getRamBank(address)
                if (i > -1 && ramAccessible(i)) {
                    if (ram == null) return false
                    ram[i] = value
                    return true
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

}