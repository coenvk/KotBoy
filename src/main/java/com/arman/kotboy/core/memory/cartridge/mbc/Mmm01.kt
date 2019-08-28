package com.arman.kotboy.core.memory.cartridge.mbc

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toInt
import com.arman.kotboy.core.memory.Ram
import com.arman.kotboy.core.memory.Rom
import com.arman.kotboy.core.memory.cartridge.mbc.battery.Battery

class Mmm01(rom: Rom, ram: Ram? = null, battery: Battery? = null) : Mbc(rom, ram, battery) { // FIXME

    private var mapEnabled: Boolean = false
    private var mbc1Mode: Boolean = false
    private var mux: Boolean = false

    private var mbc1ModeWE: Int = 0
    private var ramBankWE: Int = 0
    private var romBankWE: Int = 0

    override fun reset() {
        super.reset()
        this.mapEnabled = false
        this.mbc1Mode = false
        this.mux = false

        this.mbc1ModeWE = 0
        this.ramBankWE = 0
        this.romBankWE = 0
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
                if (!this.mapEnabled) {
                    ramBankWE = (value and 0x30) shr 4
                    this.mapEnabled = value.at(6)
                }
            }
            in 0x2000..0x3fff -> {
                romBank = (romBank and 0x1FE) or (value and 0x1)
                romBank = (romBank and 0x1E0) or ((((value and 0x1E) shr 1) and romBankWE.inv()) shl 1)
                if (!this.mapEnabled) {
                    if (mux) {
                        ramBank = (ramBank and 0xFC) or ((value and 0x3) and ramBankWE.inv())
                    } else {
                        romBank = (romBank and 0x19F) or (value and 0x60)
                    }
                }
                if (romBank == 0) romBank = 1
            }
            in 0x4000..0x5fff -> {
                if (mux) {
                    ramBank = (ramBank and 0xFC) or ((value and 0x3) and ramBankWE.inv())
                } else {
                    romBank = (romBank and 0x19F) or (value and 0x60)
                }
                if (!this.mapEnabled) {
                    ramBank = (ramBank and 0xF3) or (value and 0xC)
                    romBank = (romBank and 0x07F) or ((value and 0x30) shl 3)
                    mbc1ModeWE = value.at(6).toInt()
                    if (romBank == 0) romBank = 1
                }
            }
            in 0x6000..0x7fff -> {
                if (!mbc1ModeWE.at(0)) {
                    mbc1Mode = value.at(0)
                }
                if (!this.mapEnabled) {
                    romBankWE = (value and 0x3C) shr 2
                    mux = value.at(6)
                }
            }
            else -> return false
        }
        return true
    }

}