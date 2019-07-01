package com.arman.kotboy

import com.arman.kotboy.cpu.Cpu
import com.arman.kotboy.gpu.Gpu
import com.arman.kotboy.gui.Display
import com.arman.kotboy.io.Div
import com.arman.kotboy.io.Io
import com.arman.kotboy.io.Lcd
import com.arman.kotboy.io.Timer
import com.arman.kotboy.memory.*
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

    val gpu: Gpu = Gpu()
    val cpu: Cpu
    val io: Io = Io()
    val mmu: Mmu

    var stopped: Boolean = false
    var running: Boolean = false

    init {
        this.mmu = Mmu(this)
        this.cpu = Cpu(this)
        val lcd = Lcd(this)
        io.put(lcd)
        io.put(Div())
        io.put(Timer())
    }

    fun reset() {
        this.cpu.reset()
    }

    override fun run() {
        while (cpu.PC != 0x100) {
            var cycles = cpu.tick()

            while (cycles-- > 0) {
                io.tick(cycles)
            }
        }
    }

}