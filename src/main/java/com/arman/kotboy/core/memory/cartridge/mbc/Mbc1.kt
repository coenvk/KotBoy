package com.arman.kotboy.core.memory.cartridge.mbc

import com.arman.kotboy.core.memory.Ram
import com.arman.kotboy.core.memory.Rom
import com.arman.kotboy.core.memory.cartridge.mbc.battery.Battery

class Mbc1(rom: Rom, ram: Ram? = null, battery: Battery? = null) : Mbc(rom, ram, battery) {

    private var romMode: Boolean = true

    override fun reset() {
        super.reset()
        this.romMode = true
    }

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
            in 0x2000..0x3fff -> romBank = (romBank and 0xE0) or (if (value == 0) 1 else value and 0x1F)
            in 0x4000..0x5fff -> if (romMode) {
                romBank = (romBank and 0x9F) or ((value and 0x3) shl 5)
            } else {
                ramBank = value and 0x3
            }
            in 0x6000..0x7fff -> if (value == 0x0) {
                romMode = true
                ramBank = 0
            } else if (value == 0x1) {
                romMode = false
                romBank = romBank and 0x9F
            }
            else -> return false
        }
        return true
    }

}