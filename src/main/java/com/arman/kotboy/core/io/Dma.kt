package com.arman.kotboy.core.io

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.cpu.util.toUnsignedInt

class Dma(private val gb: GameBoy) : IoDevice(IoReg.DMA.address) {

    companion object {

        const val DURATION_SECONDS = 160 / 1000000

    }

    private var transferring: Boolean = false

    var dma: Int
        get() {
            return super.get(IoReg.DMA.address)
        }
        private set(value) {
            super.set(IoReg.DMA.address, value)
        }

    override fun tick(cycles: Int): Boolean {
        if (this.transferring) {
            super.tick(cycles)
            val durationCycles = this.gb.clockSpeed * DURATION_SECONDS // TODO: twice less in cgb double speed mode
            if (this.cycles >= durationCycles) {
                this.cycles = 0
                this.gb.gpu.oam.enabled = true
                this.transfer()
                this.transferring = false
            }
            return false
        }
        return true
    }

    private fun transfer() {
        val src = this.dma.shl(8) - 0xFE00
        for (i in 0xFE00..0xFE9F) {
            this.gb.gpu.oam[i] = this.gb.mmu[src + i].toUnsignedInt()
        }
    }

    override fun set(address: Int, value: Int): Boolean {
        return if (super.set(address, value)) {
            this.cycles = 0
            this.transferring = true
            this.gb.gpu.oam.enabled = false
            true
        } else false
    }

    override fun get(address: Int): Int {
        return 0xFF
    }

}