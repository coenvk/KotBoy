package com.arman.kotboy.io.cgb

import com.arman.kotboy.memory.Ram

class Wram : Ram(0xC000, 0xFDFF) {

    var svbk: Int
        get() {
            return this[CgbReg.SVBK.address]
        }
        set(value) {
            this[CgbReg.SVBK.address] = value
        }

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || address == CgbReg.SVBK.address
    }

    override fun set(address: Int, value: Int): Boolean {
        return if (address == CgbReg.SVBK.address) {
            this.svbk = value
            true
        } else super.set(translate(address), value)
    }

    override fun get(address: Int): Int {
        return if (address == CgbReg.SVBK.address) {
            this.svbk
        } else super.get(translate(address))
    }

    private fun translate(address: Int): Int {
        if (address in 0xE000..0xFDFF) {
            return address - 0x2000 // address - 0xE000 + 0xC000
        }

        return if (address in 0xD000..0xDFFF) {
            var ramBank = this.svbk and 0x7
            if (ramBank == 0x0) {
                ramBank = 0x1
            }
            address - this.startAddress + (ramBank - 1) * this.size()
        } else address
    }

}

class Vram : Ram(0x8000, 0x9FFF) {

    private val vram1 = Ram(0x8000, 0x9FFF)

    override fun get(address: Int): Int {
        return if (true /* VBK and 1 == 1 */) {
            this.vram1[address]
        } else super.get(address)
    }

    override fun set(address: Int, value: Int): Boolean {
        return if (true /* VBK and 1 == 1 */) {
            this.vram1.set(address, value)
        } else super.set(address, value)
    }

}