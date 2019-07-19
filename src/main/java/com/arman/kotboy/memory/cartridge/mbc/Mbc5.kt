package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom

class Mbc5(rom: Rom, ram: Ram? = null) : Mbc(rom, ram) {

    override fun write(address: Int, value: Int): Boolean {
        when (address) {
            in 0x0..0x1fff -> {
                this.ram?.let {
                    it.enabled = (value and 0x0A) == 0x0A
                    if (!it.enabled) {
                        save()
                    }
                }
            }
            in 0x2000..0x2fff -> romBank = (romBank and 0x100) or value.toUnsignedInt()
            in 0x3000..0x3fff -> romBank = romBank.toUnsignedInt() or ((value and 1) shl 8)
            in 0x4000..0x5fff -> ramBank = value and 0xF
            else -> return false
        }
        return true
    }

}