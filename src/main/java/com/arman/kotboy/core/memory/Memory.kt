package com.arman.kotboy.core.memory

interface Memory {

    fun range(): IntRange

    fun size(): Int {
        val r = this.range()
        return r.last - r.first + 1
    }

    fun fill(value: Int)

    fun accepts(address: Int): Boolean {
        return address in this.range()
    }

    fun reset()

    fun clear()

    operator fun set(address: Int, value: Int): Boolean

    operator fun get(address: Int): Int

}