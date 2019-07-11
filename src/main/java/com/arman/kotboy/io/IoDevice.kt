package com.arman.kotboy.io

import com.arman.kotboy.memory.AddressSpace

abstract class IoDevice(startAddress: Int, endAddress: Int = startAddress) :
    AddressSpace(startAddress, endAddress) {

    protected var cycles: Int = 0

    override fun reset() {
        this.cycles = 0
    }

    open fun tick(cycles: Int): Boolean {
        this.cycles += cycles
        return true
    }

}