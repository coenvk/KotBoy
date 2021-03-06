package com.arman.kotboy.core.memory

import com.arman.kotboy.consoles.cgb.memory.CgbWram
import com.arman.kotboy.consoles.cgb.memory.UndocumentedSpace
import com.arman.kotboy.core.GameBoy

class Mmu(private val gb: GameBoy) : Memory {

    private var addressSpaces: MutableList<Memory> = ArrayList()

    init {
        this.put(gb.gpu) // 0x8000 - 0x9FFF, 0xFE00 - 0xFE9F
        this.put(gb.io) // 0xFF00 - 0xFF4B, 0xFFFF (cgb: 0xFF00 - 0xFF6B, 0xFFFF)

        if (gb.cart.isCgb()) this.put(CgbWram())
        else this.put(Wram()) // 0xC000 - 0xDFFF (echo 0xE000 - 0xFDFF)

        this.put(Hram()) // 0xFF80 - 0xFFFE
        this.put(gb.cart) // 0x0000 - 0x7FFF, 0xA000 - 0xBFFF

        if (gb.cart.isCgb()) {
            this.put(UndocumentedSpace()) // 0xFF72 - 0xFF77
        }

        this.put(InvalidRegion(0xFEA0, 0xFEFF)) // 0xFEA0 - 0xFEFF
        this.put(InvalidRegion(0xFF4C, 0xFF7F)) // 0xFF4C - 0xFF7F
    }

    override fun get(address: Int): Int {
        val space = getSpace(address)
        return space?.get(address) ?: 0xFF
    }

    override fun reset() {
        this.addressSpaces.forEach { it.reset() }
    }

    override fun clear() {
        this.addressSpaces.clear()
    }

    override fun set(address: Int, value: Int): Boolean {
        val space = getSpace(address)
        return space?.set(address, value) ?: false
    }

    private fun put(addressSpace: Memory, i: Int = this.addressSpaces.size): Boolean {
        this.addressSpaces.add(i, addressSpace)
        return true
    }

    override fun range(): IntRange = IntRange(0x0, 0xFFFF)

    override fun accepts(address: Int): Boolean {
        return this.addressSpaces.any { it.accepts(address) }
    }

    override fun fill(value: Int) = this.addressSpaces.forEach { it.fill(value) }

    private fun getSpace(address: Int): Memory? {
        this.addressSpaces.forEach {
            if (it.accepts(address)) return it
        }
        return null
    }

}