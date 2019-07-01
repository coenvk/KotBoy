package com.arman.kotboy.gpu

import com.arman.kotboy.AddressSpace
import com.arman.kotboy.KotBoy
import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.Ram

class Gpu(private val gb: KotBoy) : AddressSpace(0xFFFFFF) {

    companion object {
        const val TILE_SIZE = 8
        const val TILE_BYTE_SIZE = 16
        const val BG_TILE_TABLE_0 = 0x9800
        const val BG_TILE_TABLE_1 = 0x9C00
        const val TILE_PATTERN_TABLE_0 = 0x8800
        const val TILE_PATTERN_TABLE_1 = 0x8000
        const val WINDOW_TILE_TABLE_0 = 0x9800
        const val WINDOW_TILE_TABLE_1 = 0x9C00
    }

    val vram: AddressSpace = Ram(0x8000, 0x9FFF)
    val oam: AddressSpace = Ram(0xFE00, 0xFE9F)

    override fun accepts(address: Address): Boolean {
        return this.vram.accepts(address) || this.oam.accepts(address)
    }

    override fun get(address: Address): Int {
        return if (this.vram.accepts(address)) this.vram[address]
        else this.oam[address]
    }

    override fun set(address: Address, value: Int): Boolean {
        return if (this.vram.accepts(address)) this.vram.set(address, value)
        else this.oam.set(address, value)
    }

}