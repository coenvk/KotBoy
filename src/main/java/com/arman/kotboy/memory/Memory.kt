package com.arman.kotboy.memory

import com.arman.kotboy.cpu.util.hexString

typealias Address = Int

open class Memory(protected val startAddress: Address, protected val endAddress: Address = startAddress) {

    constructor() : this(0x0, 0xFFFF)

    protected var addressSpaces: MutableList<AddressSpace> = ArrayList()

    fun put(addressSpace: AddressSpace): Boolean {
        return this.addressSpaces.add(addressSpace)
    }

    fun size(): Int {
        return this.endAddress - this.startAddress + 1
    }

    open fun accepts(address: Address): Boolean {
        return address >= this.startAddress && address <= this.endAddress
    }

    open fun reset() {
        this.addressSpaces.forEach { it.reset() }
    }

    open operator fun set(address: Address, value: Int): Boolean {
        val space = getSpace(address)
        return space?.set(address, value) ?: false
    }

    open operator fun get(address: Address): Int {
        val space = getSpace(address)
        return space?.get(address) ?: 0
    }

    protected fun getSpace(address: Address): AddressSpace? {
        this.addressSpaces.forEach {
            if (it.accepts(address)) return it
        }
        return null
    }

    override fun toString(): String {
        var res = ""
        for (i in this.startAddress..this.endAddress step 16) {
            for (j in 0 until 16) {
                if (i + j >= this.size() + this.startAddress) return res
                res += "${(i + j).hexString()} = ${this.get(i + j).hexString()}\t"
            }
            res += "\n"
        }
        return res
    }

}