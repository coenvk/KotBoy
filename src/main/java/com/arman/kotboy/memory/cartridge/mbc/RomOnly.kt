package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom

class RomOnly(rom: Rom, ram: Ram) : Mbc(rom, ram) {

    override fun write(address: Address, value: Int): Boolean {
        return false
    }

    override fun set(address: Address, value: Int): Boolean {
        return false
    }

    override fun get(address: Address): Int {
        return if (address in 0x0..0x7fff) {
            this.rom[address]
        } else 0
    }

}