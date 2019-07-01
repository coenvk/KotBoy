package com.arman.kotboy.memory

import com.arman.kotboy.AddressSpace

class Ram : AddressSpace {

    constructor(startAddress: Address, values: IntArray) : super(startAddress, values)
    constructor(startAddress: Address, endAddress: Address) : super(startAddress, endAddress)

}