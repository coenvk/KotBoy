package com.arman.kotboy.memory

import com.arman.kotboy.KotBoy

class Mmu(private val gb: KotBoy) : AddressSpace(0xFFFFFF) {

    private val bootRom: Rom = Rom(0x0, BootRom.DMG)

    val memory: Memory = Memory()

    init {
        this.memory.put(gb.io)
        this.memory.put(gb.cart)
        this.memory.put(gb.gpu)
        this.memory.put(Ram(0xC000, 0xFDFF))
        this.memory.put(Ram(0xFF80, 0xFFFE))
    }

    override fun reset() {
        this.memory.reset()
    }

    override fun accepts(address: Address): Boolean {
        return this.memory.accepts(address)
    }

    override fun set(address: Address, value: Int): Boolean {
        return this.memory.set(address, value)
    }

    override fun get(address: Address): Int {
        return this.memory[address]
    }

    override fun get(startAddress: Address, endAddress: Address): IntArray {
        val vals = IntArray(endAddress - startAddress + 1)
        for (i in startAddress..endAddress) {
            vals[i - startAddress] = this[i]
        }
        return vals
    }

    override fun toString(): String {
        return this.memory.toString()
    }

}