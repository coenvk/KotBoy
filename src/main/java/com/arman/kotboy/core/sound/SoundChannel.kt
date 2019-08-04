package com.arman.kotboy.core.sound

import com.arman.kotboy.core.io.IoDevice

abstract class SoundChannel(startAddress: Int, endAddress: Int) : IoDevice(startAddress, endAddress) {

    private var lengthCounter: Int = 0
    private var dacEnabled: Boolean = false
    private var channelEnabled: Boolean = false

    abstract val frequency: Int
    abstract val period: Int

    open val length: Int = 64

    protected var clocks: Int = 0

    fun isEnabled(): Boolean {
        return this.enabled && this.dacEnabled
    }

    open fun start() {
        this.reset()
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