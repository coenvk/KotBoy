package com.arman.kotboy

import com.arman.kotboy.cpu.Cpu
import com.arman.kotboy.cpu.util.hexString
import com.arman.kotboy.gpu.Gpu
import com.arman.kotboy.gui.Display
import com.arman.kotboy.io.Io
import com.arman.kotboy.io.input.ButtonListener
import com.arman.kotboy.io.input.InputHandler
import com.arman.kotboy.memory.Mmu
import com.arman.kotboy.memory.cartridge.Cartridge

class KotBoy(val display: Display, val cart: Cartridge, val inputHandler: InputHandler) : Runnable {

    constructor(cart: Cartridge, inputHandler: InputHandler) : this(
        object : Display {
            override fun frameReady(buffer: IntArray) {
            }
        }, cart, inputHandler
    )

    constructor(cart: Cartridge) : this(
        cart,
        object : InputHandler {
            override var buttonListener: ButtonListener?
                get() = null
                set(value) {}
        }
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
        this.io.reset()
        this.gpu.reset()
    }

    override fun run() {
        this.reset()
        this.running = true
        while (this.running) {
            if (!stopped) {
                tick()
            }
        }
    }

    fun tick() {
        val cycles = cpu.tick()
        io.tick(cycles)
    }

}