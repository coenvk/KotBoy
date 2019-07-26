package com.arman.kotboy.memory.cartridge.mbc

import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.Rom
import java.io.File

class Mbc3(rom: Rom, ram: Ram? = null, saveFile: File? = null) : Mbc(rom, ram, saveFile) {

    // TODO: RTC

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
            in 0x2000..0x3fff -> romBank = if (value == 0) 1 else value and 0x7F
            in 0x4000..0x5fff -> if (value in 0x0..0x3) {
                ramBank = value and 0x3
            } else if (value in 0x8..0xC) {
                // TODO: Map corresponding RTC register into memory
            }
            in 0x6000..0x7fff -> {
                /* TODO:
                   When writing 00h, and then 01h to this register, the current
                   time becomes latched into the RTC registers
                   08h  RTC S   Seconds   0-59 (0-3Bh)
                   09h  RTC M   Minutes   0-59 (0-3Bh)
                   0Ah  RTC H   Hours     0-23 (0-17h)
                   0Bh  RTC DL  Lower 8 bits of Day Counter (0-FFh)
                   0Ch  RTC DH  Upper 1 bit of Day Counter, Carry Bit, Halt Flag
                         Bit 0  Most significant bit of Day Counter (Bit 8)
                         Bit 6  Halt (0=Active, 1=Stop Timer)
                         Bit 7  Day Counter Carry Bit (1=Counter Overflow) */
            }
            else -> return false
        }
        return true
    }

}