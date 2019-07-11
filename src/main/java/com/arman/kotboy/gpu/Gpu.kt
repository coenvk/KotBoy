package com.arman.kotboy.gpu

import com.arman.kotboy.GameBoy
import com.arman.kotboy.memory.*

class Gpu(private val gb: GameBoy) : Memory {

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

    val vram: AddressSpace = Vram()
    val oam: AddressSpace = Oam()

    override fun accepts(address: Int): Boolean {
        return this.vram.accepts(address) || this.oam.accepts(address)
    }

    override fun get(address: Int): Int {
        return if (this.vram.accepts(address)) this.vram[address]
        else this.oam[address]
    }

    override fun set(address: Int, value: Int): Boolean {
        return if (this.vram.accepts(address)) this.vram.set(address, value)
        else this.oam.set(address, value)
    }

    override fun range(): IntRange = IntRange(this.vram.range().first, this.oam.range().last)

    override fun fill(value: Int) {
        this.vram.fill(value)
        this.oam.fill(value)
    }

    override fun reset() {
        this.vram.reset()
        this.oam.reset()
    }

    override fun clear() {
        this.vram.clear()
        this.oam.clear()
    }

}