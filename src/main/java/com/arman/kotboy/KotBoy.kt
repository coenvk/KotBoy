package com.arman.kotboy

import com.arman.kotboy.cpu.Cpu
import com.arman.kotboy.gpu.Gpu
import com.arman.kotboy.gui.Display
import com.arman.kotboy.io.*
import com.arman.kotboy.memory.Mmu
import com.arman.kotboy.memory.cartridge.Cartridge

class KotBoy(val display: Display, val cart: Cartridge) : Runnable {

    constructor(cart: Cartridge) : this(
        object : Display {
            override fun frameReady(buffer: IntArray) {
            }
        }, cart
    )

    companion object {
        const val BASE_CLOCK = 4096
        const val CLOCK = BASE_CLOCK * 1000 / 60
    }

    val io: Io = Io(this)
    val gpu: Gpu = Gpu(this)
    val cpu: Cpu = Cpu(this)
    val mmu: Mmu = Mmu(this)

    var stopped: Boolean = false
    var running: Boolean = false

    fun reset() {
        this.cpu.reset()
    }

    override fun run() {
        this.running = true
        while (this.running) {
            if (!stopped) {
                val cycles = cpu.tick()
                io.tick(cycles)
            }
        }
    }

}