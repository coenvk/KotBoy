package com.arman.kotboy.core.memory

import com.arman.kotboy.core.cpu.util.hexString

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

    fun toBytes(): ByteArray {
        val buffer = ByteArray(values.size)
        for (i in 0 until buffer.size) {
            buffer[i] = values[i].toByte()
        }
        return buffer
    }

    override fun toString(): String {
        var res = ""
        for (i in this.startAddress..this.endAddress step 16) {
            res += "${i.hexString(4)}\t"
            for (j in 0 until 16) {
                if (i + j >= this.size() + this.startAddress) return res
                res += "${this[i + j].hexString(2)} "
            }
            res += "\n"
        }
        return res
    }

}