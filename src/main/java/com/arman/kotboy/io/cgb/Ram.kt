package com.arman.kotboy.io.cgb

import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.Ram

class Wram : Ram(0xC000, 0xFDFF) {

    var svbk: Address = Address(CgbReg.SVBK.address)

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || svbk.accepts(address)
    }

    override fun set(address: Int, value: Int): Boolean {
        return if (address == CgbReg.SVBK.address) {
            this.svbk.set(value)
            true
        } else super.set(translate(address), value)
    }

    override fun get(address: Int): Int {
        return if (address == CgbReg.SVBK.address) {
            this.svbk.get()
        } else super.get(translate(address))
    }

    private fun translate(address: Int): Int {
        if (address in 0xE000..0xFDFF) {
            return address - 0x2000 // address - 0xE000 + 0xC000
        }

        return if (address in 0xD000..0xDFFF) {
            var ramBank = this.svbk.get() and 0x7
            if (ramBank == 0x0) {
                ramBank = 0x1
            }
            address + ((ramBank - 1) * 0x1000)
        } else address
    }

}

class Vram : Ram(0x8000, 0x9FFF) {

    private val vram1 = Ram(0x8000, 0x9FFF) // TODO: use bg map attributes

    var vbk: Address = Address(CgbReg.VBK.address)

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || vbk.accepts(address)
    }

    override fun get(address: Int, bank: Int): Int {
        return if (bank == 0) super.get(address)
        else this.vram1[address]
    }

    override fun get(address: Int): Int {
        return when {
            (vbk.get() and 0x1) == 0x1 -> this.vram1[address]
            address == CgbReg.VBK.address -> this.vbk.get()
            else -> super.get(address)
        }
    }

    override fun set(address: Int, value: Int): Boolean {
        return when {
            (vbk.get() and 0x1) == 0x1 -> this.vram1.set(address, value)
            address == CgbReg.VBK.address -> this.vbk.set(value)
            else -> super.set(address, value)
        }
    }

}