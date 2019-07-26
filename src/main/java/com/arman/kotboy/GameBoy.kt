package com.arman.kotboy

import com.arman.kotboy.cpu.Cpu
import com.arman.kotboy.cpu.Reg8
import com.arman.kotboy.gpu.Gpu
import com.arman.kotboy.gui.Display
import com.arman.kotboy.io.Io
import com.arman.kotboy.io.input.ButtonListener
import com.arman.kotboy.io.input.InputHandler
import com.arman.kotboy.memory.Mmu
import com.arman.kotboy.memory.cartridge.Cartridge
import java.awt.event.KeyEvent
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class GameBoy(private val options: Options, val display: Display, val inputHandler: InputHandler) : Runnable {

    val cart: Cartridge = Cartridge(options)

    private val lock = ReentrantLock()
    private val unPauseCondition = lock.newCondition()

    constructor(options: Options, inputHandler: InputHandler) : this(
        options,
        object : Display {
            override fun frameReady(buffer: IntArray) {
            }
        }, inputHandler
    )

    constructor(options: Options) : this(
        options,
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

    constructor(rom: String) : this(Options(rom))

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

//        if (cart.isCgb()) {
//            this.cpu.write(Reg8.A, 0x11)
//        }
    }

    override fun run() {
        if (!options.bootstrap) {
            this.reset()
        }

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
                val timeBetweenFrames = 1000 / Cpu.FRAME_RATE
                var cycle = 0
                while (cycle < Cpu.DMG_CLOCK_SPEED / Cpu.FRAME_RATE) {
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