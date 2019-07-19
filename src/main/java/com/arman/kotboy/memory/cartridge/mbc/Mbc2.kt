package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom

class Mbc2(rom: Rom, ram: Ram? = null) : Mbc(rom, ram) {

    init {
        assert(ram?.size() == 0x200)
    }

    override fun set(address: Int, value: Int): Boolean {
        var v = value
        if (address in 0xa000..0xbfff) {
            v = v and 0xF
        }
        return super.set(address, v)
    }

    override fun write(address: Int, value: Int): Boolean {
        when (address) {
            in 0x0..0x1fff -> if ((address and 0x100) == 0) {
                this.ram?.let {
                    it.enabled = (value and 0x0A) == 0x0A
                    if (!it.enabled) {
                        save()
                    }
                }
            }
            in 0x2000..0x3fff -> if (address and 0x100 != 0) {
                romBank = if (value == 0) 1 else value and 0xF
            }
            else -> return false
        }
        return true
    }

}