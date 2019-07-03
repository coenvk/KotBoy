package com.arman.kotboy.memory

class MemoryByte(address: Address) : AddressSpace(address) {

    fun set(value: Int): Boolean {
        return set(startAddress, value)
    }

    fun get(): Int {
        return get(startAddress)
    }

}