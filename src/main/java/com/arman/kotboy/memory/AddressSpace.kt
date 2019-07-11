package com.arman.kotboy.memory

abstract class AddressSpace(protected val startAddress: Int, protected val values: IntArray) : Memory {

    var enabled: Boolean = true
    val endAddress: Int = this.values.size + this.startAddress - 1

    constructor(startAddress: Int, endAddress: Int = startAddress) : this(
        startAddress,
        IntArray(endAddress + 1 - startAddress)
    )

    override fun reset() {
        this.values.fill(0)
    }

    override fun fill(value: Int) {
        this.values.fill(value)
    }

    override fun clear() {
        this.values.fill(0)
    }

    override fun range(): IntRange {
        return IntRange(startAddress, endAddress)
    }

    override fun set(address: Int, value: Int): Boolean {
        return if (accepts(address) && enabled) {
            this.values[address - this.startAddress] = value
            true
        } else false
    }

    override fun get(address: Int): Int {
        return if (accepts(address) && enabled) {
            this.values[address - this.startAddress]
        } else 0xFF
    }

    open fun get(startAddress: Int, endAddress: Int): IntArray? {
        return if (accepts(startAddress) && accepts(endAddress)) {
            this.values.sliceArray(startAddress..endAddress)
        } else null
    }

    override fun toString(): String {
        var res = ""
        for (i in this.startAddress..this.endAddress step 16) {
            for (j in 0 until 15) {
                if (i + j >= this.size() + this.startAddress) return res
                res += "${(i + j)} = ${this[i + j]} | "
            }
            res += "${(i + 15)} = ${this[i + 15]}\n"
        }
        return res
    }

}