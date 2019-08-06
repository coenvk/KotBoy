package com.arman.kotboy.consoles.cgb.memory

import com.arman.kotboy.consoles.cgb.io.CgbIoReg
import com.arman.kotboy.core.memory.Address
import com.arman.kotboy.core.memory.AddressSpace

class UndocumentedSpace : AddressSpace(CgbIoReg.FF72.address, CgbIoReg.FF77.address) {

    private var ff6c: Address = Address(CgbIoReg.FF6C.address)

    init {
        this.reset()
    }

    override fun reset() {
        super.reset()
        this.ff6c.set(0xFE)
        super.set(CgbIoReg.FF74.address, 0xFF)
        super.set(CgbIoReg.FF75.address, 0x8F)
    }

    override fun set(address: Int, value: Int): Boolean {
        return when (address) {
            CgbIoReg.FF6C.address -> {
                this.ff6c.set(0xFE or (value and 1))
                true
            }
            in CgbIoReg.FF72.address..CgbIoReg.FF74.address -> super.set(address, value)
            CgbIoReg.FF75.address -> {
                super.set(address, 0x8F or (value and 0x70))
            }
            else -> false
        }
    }

    override fun get(address: Int): Int {
        return if (address == CgbIoReg.FF6C.address) {
            this.ff6c.get()
        } else {
            super.get(address)
        }
    }

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || ff6c.accepts(address)
    }

}