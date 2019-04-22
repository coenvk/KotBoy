package com.arman.kotboy

typealias Address = Int

interface AddressSpace {

    fun accepts(address: Address): Boolean

    fun set(address: Address, value: Int)

    fun get(address: Address): Int

}