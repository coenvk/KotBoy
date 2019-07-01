package com.arman.kotboy.io

import com.arman.kotboy.AddressSpace
import com.arman.kotboy.memory.Address

class Io : AddressSpace(IoReg.P1.address, IoReg.WX.address) {

    var cycles = 0

    override fun reset() {
        super.reset()
        this.cycles = 0
    }

    fun tick(cycles: Int) {
        this.cycles += cycles
        this.addressSpaces.forEach {
            val device = it as? IoDevice
            device?.tick(cycles)
        }
    }

    override fun set(address: Address, value: Int): Boolean {
        val space = getSpace(address)
        return space?.set(address, value) ?: false
    }

    override fun get(address: Address): Int {
        val space = getSpace(address)
        return space?.get(address) ?: 0
    }

}