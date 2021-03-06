package com.arman.kotboy.core.gpu

import com.arman.kotboy.consoles.cgb.memory.CgbVram
import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.memory.Memory
import com.arman.kotboy.core.memory.Oam
import com.arman.kotboy.core.memory.Ram
import com.arman.kotboy.core.memory.Vram

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

    val vram: Ram = if (gb.cart.isCgb()) CgbVram() else Vram()
    val oam: Oam = Oam()

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