package com.arman.kotboy.memory

import com.arman.kotboy.KotBoy

class Mmu(private val gb: KotBoy) {

    private val bootRom: Rom = Rom(0x0, BootRom.DMG)

    val memory: Memory = Memory()

    init {
        this.memory.put(gb.cart)
        this.memory.put(gb.gpu)
        this.memory.put(Ram(0xC000, 0xFDFF))
        this.memory.put(gb.io)
        this.memory.put(Ram(0xFF80, 0xFFFE))
        // TODO: 0xFFFF - IE interrupt enable register
    }

    fun reset() {
        this.memory.reset()
    }

    operator fun set(address: Address, value: Int): Boolean {
        return this.memory.set(address, value)
    }

    operator fun get(address: Address): Int {
        return this.memory[address]
    }

    override fun toString(): String {
        return this.memory.toString()
    }

}