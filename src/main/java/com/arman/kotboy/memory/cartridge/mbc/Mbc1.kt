package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom

class Mbc1(rom: Rom, ram: Ram) : Mbc(rom, ram) {

    private var romMode: Boolean = true

    override fun write(address: Address, value: Int): Boolean {
        if (address in 0x0..0x1fff) {
            this.ramEnabled = (value and 0x0A) == 0x0A
        } else if (address in 0x2000..0x3fff) {
            romBank = (romBank and 0x60) or (if (value == 0) 1 else value and 0x1F)
        } else if (address in 0x4000..0x5fff) {
            if (romMode) {
                romBank = (romBank and 0x1F) or ((value and 3) shl 5)
            } else {
                ramBank = value and 3
            }
        } else if (address in 0x6000..0x7fff) {
            if (value and 0x1 == 0x0) {
                romMode = true
                ramBank = 0
            } else if (value and 0x1 == 0x1) {
                romMode = false
                romBank = romBank and 0x1F
            }
        } else return false
        return true
    }

}