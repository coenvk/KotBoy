package com.arman.kotboy.memory

abstract class AddressSpace(startAddress: Address, protected val values: IntArray) :
    Memory(startAddress, startAddress + values.size - 1) {

    var enabled: Boolean = true

    constructor(startAddress: Address, endAddress: Address = startAddress) : this(
        startAddress,
        IntArray(endAddress + 1 - startAddress)
    )

    override fun reset() {
        this.values.fill(0)
    }

    fun fill(value: Int) {
        this.values.fill(value)
    }

    override fun set(address: Address, value: Int): Boolean {
        return if (accepts(address) && enabled) {
            this.values[address - this.startAddress] = value
            true
        } else false
    }

    override fun get(address: Address): Int {
        return if (accepts(address) && enabled) {
            this.values[address - this.startAddress]
        } else 0xFF
    }

    open fun get(startAddress: Address, endAddress: Address): IntArray? {
        return if (accepts(startAddress) && accepts(endAddress)) {
            this.values.sliceArray(startAddress..endAddress)
        } else null
    }

}