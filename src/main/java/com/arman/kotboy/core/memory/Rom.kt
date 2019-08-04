package com.arman.kotboy.core.memory

class Rom : AddressSpace {

    constructor(startAddress: Int, values: IntArray) : super(startAddress, values)
    constructor(startAddress: Int, endAddress: Int) : super(startAddress, endAddress)

    override fun set(address: Int, value: Int): Boolean {
        return false
    }

}