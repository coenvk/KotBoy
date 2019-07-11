package com.arman.kotboy.memory

class InvalidRegion(private val value: Int, startAddress: Int, endAddress: Int) : AddressSpace(startAddress, endAddress) {

    override fun get(address: Int): Int {
        return value
    }

    override fun set(address: Int, value: Int): Boolean {
        return false
    }

}