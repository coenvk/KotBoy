package com.arman.kotboy.core.memory

class InvalidRegion(startAddress: Int, endAddress: Int) : AddressSpace(startAddress, endAddress) {

    override fun get(address: Int): Int {
        return 0xFF
    }

    override fun set(address: Int, value: Int): Boolean {
        return false
    }

}