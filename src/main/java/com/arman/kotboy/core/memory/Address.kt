package com.arman.kotboy.core.memory

open class Address(address: Int) : AddressSpace(address) {

    fun set(value: Int): Boolean {
        return set(startAddress, value)
    }

    fun get(): Int {
        return get(startAddress)
    }

}