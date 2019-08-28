package com.arman.kotboy.core.memory.cartridge.mbc

import com.arman.kotboy.core.memory.Rom

class RomOnly(rom: Rom) : Mbc(rom, null) {

    override fun write(address: Int, value: Int): Boolean {
        return false
    }

    override fun set(address: Int, value: Int): Boolean {
        return false
    }

    override fun get(address: Int): Int {
        return if (address in 0x0..0x7fff) {
            this.rom[address]
        } else 0
    }

}