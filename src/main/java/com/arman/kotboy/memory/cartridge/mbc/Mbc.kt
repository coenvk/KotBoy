package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.memory.AddressSpace
import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom

abstract class Mbc(protected val rom: Rom, protected val ram: Ram) : AddressSpace(0xFFFFFF) {

    protected var romBank: Int = 1
    protected var ramBank: Int = 0
    protected var ramEnabled: Boolean = true

    private fun getRamBank(address: Address): Int {
        if (ram.size() <= 0) {
            return -1
        }
        return ((address - 0xA000) + (ramBank * 0x2000)).rem(ram.size())
    }

    private fun ramAccessible(bank: Int): Boolean {
        return bank >= 0 && ram.size() > 0 && bank < ram.size()
    }

    protected abstract fun write(address: Address, value: Int): Boolean

    override fun accepts(address: Address): Boolean {
        return address in 0x0..0x7fff || address in 0xa000..0xbfff
    }

    override fun get(address: Address): Int {
        return when (address) {
            in 0x0..0x3fff -> rom[address]
            in 0x4000..0x7fff -> rom[((address - 0x4000) + (romBank * 0x4000)).rem(rom.size())]
            in 0xa000..0xbfff -> {
                val i = getRamBank(address)
                if (ramAccessible(i)) {
                    ram[i]
                } else 0xff
            }
            else -> 0x0
        }
    }

    override fun set(address: Address, value: Int): Boolean {
        return when (address) {
            in 0x0..0x7fff -> write(address, value)
            in 0xa000..0xbfff -> {
                if (ramEnabled) {
                    val i = getRamBank(address)
                    if (ramAccessible(i)) {
                        ram[i] = value
                        return true
                    }
                }
                false
            }
            else -> false
        }
    }

}