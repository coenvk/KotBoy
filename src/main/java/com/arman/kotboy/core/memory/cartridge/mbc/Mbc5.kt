package com.arman.kotboy.core.memory.cartridge.mbc

import com.arman.kotboy.core.cpu.util.toUnsignedInt
import com.arman.kotboy.core.memory.Ram
import com.arman.kotboy.core.memory.Rom
import com.arman.kotboy.core.memory.cartridge.mbc.battery.Battery

class Mbc5(rom: Rom, ram: Ram? = null, battery: Battery? = null) : Mbc(rom, ram, battery) {

    override fun write(address: Int, value: Int): Boolean {
        when (address) {
            in 0x0..0x1fff -> {
                this.ram?.let {
                    it.enabled = (value and 0x0F) == 0x0A
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