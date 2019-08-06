package com.arman.kotboy.core

import com.arman.kotboy.core.cpu.Cpu
import com.arman.kotboy.core.cpu.Reg8
import com.arman.kotboy.core.gpu.Gpu
import com.arman.kotboy.core.gui.Display
import com.arman.kotboy.core.io.Io
import com.arman.kotboy.core.io.input.ButtonListener
import com.arman.kotboy.core.io.input.InputHandler
import com.arman.kotboy.core.memory.Mmu
import com.arman.kotboy.core.memory.cartridge.Cartridge
import java.awt.event.KeyEvent
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class GameBoy(private val file: File, val display: Display, val inputHandler: InputHandler) : Runnable {

    val cart: Cartridge = Cartridge(file)

    private val lock = ReentrantLock()
    private val unPauseCondition = lock.newCondition()

    constructor(file: File, inputHandler: InputHandler) : this(
            file,
            object : Display {
                override fun run() {
                }

                override fun frameReady(buffer: IntArray) {
                }
            }, inputHandler
    )

    constructor(file: File) : this(
            file,
            object : InputHandler {
                override fun keyTyped(e: KeyEvent?) {
                }

                override fun keyPressed(e: KeyEvent?) {
                }

                override fun keyReleased(e: KeyEvent?) {
                }

                override var buttonListener: ButtonListener?
                    get() = null
                    set(value) {}
            }
    )

    constructor(rom: String) : this(File(rom))

    val io: Io = Io(this)
    val gpu: Gpu = Gpu(this)
    val cpu: Cpu = Cpu(this)
    val mmu: Mmu = Mmu(this)

    var stopped: Boolean = false
    var running: Boolean = false

    @Volatile
    var paused: Boolean = false
        set(value) {
            field = value
            if (!value) lock.withLock { unPauseCondition.signalAll() }
        }


    fun reset() {
        this.cpu.reset()
        this.io.reset()
        this.gpu.reset()

        if (cart.isCgb()) {
            this.cpu.write(Reg8.A, 0x11)
        }
    }

    override fun run() {
        if (!Options.bootstrap) {
            this.reset()
        }

        val clockSpeed = if (cart.isSgb()) {
            Cpu.SGB_CLOCK_SPEED
        } else Cpu.DMG_CLOCK_SPEED
        val cyclesPerFrame = clockSpeed / Cpu.FRAME_RATE
        val timeBetweenFrames = 1000 / Cpu.FRAME_RATE

        this.running = true
        while (this.running) {

            if (this.paused) {
                lock.withLock {
                    while (this.paused) {
                        unPauseCondition.await()
                    }
                }
            }

            if (!stopped) {
                val start = System.currentTimeMillis()
                var cycle = 0
                while (cycle < cyclesPerFrame) {
                    cycle += tick()
                }
                val frameTime = System.currentTimeMillis() - start
                val sleepTime = timeBetweenFrames - frameTime
                if (frameTime < timeBetweenFrames) {
                    Thread.sleep(sleepTime)
                }
            }

        }
    }

    fun tick(): Int {
        val cycles = cpu.tick()
        io.tick(cycles)
        return cycles
    }

}