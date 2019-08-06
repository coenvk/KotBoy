package com.arman.kotboy.consoles.cgb.memory

import com.arman.kotboy.consoles.cgb.io.CgbIoReg
import com.arman.kotboy.core.memory.Address
import com.arman.kotboy.core.memory.Ram

class CgbWram : Ram(0xC000, 0xFDFF) {

    var svbk: Address = Address(CgbIoReg.SVBK.address)

    private val rams: Array<Ram> = Array(6) { Ram(0xD000, 0xDFFF) }

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || svbk.accepts(address)
    }

    override fun set(address: Int, value: Int): Boolean {
        return if (address == CgbIoReg.SVBK.address) {
            this.svbk.set(value)
            true
        } else {
            val addr = translate(address)

            if (addr in 0xD000..0xDFFF) {
                val ramBank = this.svbk.get() and 0x7
                return this.set(addr, value, ramBank)
            }

            return super.set(addr, value)
        }
    }

    override fun get(address: Int): Int {
        return if (address == CgbIoReg.SVBK.address) {
            this.svbk.get()
        } else {
            val addr = translate(address)

            if (addr in 0xD000..0xDFFF) {
                val ramBank = this.svbk.get() and 0x7
                return this[addr, ramBank]
            }

            return super.get(addr)
        }
    }

    override fun get(address: Int, bank: Int): Int {
        val i = bank and 0x7
        return if (i == 0x0 || i == 0x1) super.get(address)
        else this.rams[i - 0x2][address]
    }

    private fun set(address: Int, value: Int, bank: Int): Boolean {
        val i = bank and 0x7
        return if (i == 0x0 || i == 0x1) super.set(address, value)
        else this.rams[i - 0x2].set(address, value)
    }

    private fun translate(address: Int): Int {
        return if (address in 0xE000..0xFDFF) {
            address - 0x2000 // address - 0xE000 + 0xC000
        } else address
    }

}

class CgbVram : Ram(0x8000, 0x9FFF) {

    private val vram1 = Ram(0x8000, 0x9FFF)

    var vbk: Address = Address(CgbIoReg.VBK.address)

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || vbk.accepts(address)
    }

    override fun get(address: Int, bank: Int): Int {
        return if (bank == 0) super.get(address)
        else this.vram1[address]
    }

    override fun get(address: Int): Int {
        return when {
            address == CgbIoReg.VBK.address -> 0xFE
            (vbk.get() and 0x1) == 0x1 -> this.vram1[address]
            else -> super.get(address)
        }
    }

    override fun set(address: Int, value: Int): Boolean {
        return when {
            address == CgbIoReg.VBK.address -> this.vbk.set(value)
            (vbk.get() and 0x1) == 0x1 -> this.vram1.set(address, value)
            else -> super.set(address, value)
        }
    }

}