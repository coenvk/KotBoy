package com.arman.kotboy.core.sound

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.io.IoDevice

abstract class SoundChannel(startAddress: Int, endAddress: Int) : IoDevice(startAddress, endAddress) {

    private var lengthCounter: Int = 0
    private var dacEnabled: Boolean = false
    private var channelEnabled: Boolean = false

    val frequency: Int
        get() = 2048 - (this.nr3 or ((this.nr4 and 0x7) shl 8))

    abstract val period: Int

    abstract var nr0: Int
    abstract var nr1: Int
    abstract var nr2: Int
    abstract var nr3: Int
    abstract var nr4: Int

    open val length: Int = 64

    protected var clocks: Int = 0

    var output: Int = 0
        private set

    fun isEnabled(): Boolean {
        return this.channelEnabled && this.dacEnabled
    }

    protected fun isLengthEnabled(): Boolean {
        return this.nr4.at(6)
    }

    open fun start() {
        this.reset()
    }

    override fun tick(cycles: Int): Boolean {
        super.tick(cycles)

        if (this.cycles >= 256) {
            this.cycles = 0
            if (this.isLengthEnabled() && this.lengthCounter > 0) {
                if (--this.lengthCounter == 0) {
                    this.channelEnabled = false
                }
            }
        }

        return true
    }

    abstract fun stop()

    open fun trigger() {
        this.channelEnabled = true
        if (this.lengthCounter == 0) this.lengthCounter = this.length
        this.clocks = this.period
        // reload volume envelope timer with period
        // reload starting volume
    }

}