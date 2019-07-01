package com.arman.kotboy.io

import com.arman.kotboy.AddressSpace
import com.arman.kotboy.memory.Address

abstract class IoDevice(startAddress: Address, endAddress: Address = startAddress) :
    AddressSpace(startAddress, endAddress) {

    protected var cycles: Int = 0

    open fun tick(cycles: Int) {
        this.cycles += cycles
    }

}