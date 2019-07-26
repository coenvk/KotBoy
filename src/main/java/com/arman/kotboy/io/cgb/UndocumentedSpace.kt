package com.arman.kotboy.io.cgb

import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.AddressSpace

class UndocumentedSpace : AddressSpace(CgbReg.FF72.address, CgbReg.FF77.address) {

    private var ff6c: Address = Address(CgbReg.FF6C.address)

    init {
        this.reset()
    }

    override fun reset() {
        super.reset()
        this.ff6c.set(0xFE)
        super.set(CgbReg.FF75.address, 0x8F)
    }

    override fun set(address: Int, value: Int): Boolean {
        return when (address) {
            CgbReg.FF6C.address -> {
                this.ff6c.set(0xFE or (value and 1))
                true
            }
            in CgbReg.FF72.address..CgbReg.FF74.address -> super.set(address, value)
            CgbReg.FF75.address -> {
                super.set(address, 0x8F or (value and 0x70))
            }
            else -> false
        }
    }

    override fun get(address: Int): Int {
        return if (address == CgbReg.FF6C.address) {
            this.ff6c.get()
        } else {
            super.get(address)
        }
    }

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || ff6c.accepts(address)
    }

}