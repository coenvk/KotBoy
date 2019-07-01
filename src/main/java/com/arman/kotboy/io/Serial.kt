package com.arman.kotboy.io

import com.arman.kotboy.memory.AddressSpace
import com.arman.kotboy.memory.Address

class Serial : AddressSpace(IoReg.SB.address, IoReg.SC.address) {

    override fun set(address: Address, value: Int): Boolean {
        if (address == IoReg.SC.address) {
            if ((value and (1 shl 7)) != 0) {
                // TODO: transfer
            }
        }
        return super.set(address, value)
    }

    override fun get(address: Address): Int {
        if (address == IoReg.SC.address) {
            return super.get(address) or 0b01111110
        }
        return super.get(address)
    }

}