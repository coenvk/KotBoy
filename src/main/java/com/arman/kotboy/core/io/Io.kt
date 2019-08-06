package com.arman.kotboy.core.io

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.memory.Memory
import com.arman.kotboy.core.sound.Apu

class Io(private val gb: GameBoy) : Memory {

    var cycles = 0

    private var addressSpaces: MutableList<Memory> = ArrayList()

    init {
        put(Joypad(gb)) // 0xFF00
        put(SerialPort()) // 0xFF01 - 0xFF02
        put(Div()) // 0xFF04
        put(Timer()) // 0xFF05 - 0xFF07
        put(If()) // 0xFF0F
        put(Apu()) // 0xFF24 - 0xFF26 (0xFF10 - 0xFF23)
        put(Ppu(gb)) // 0xFF40 - 0xFF4B (CBB: 0xFF40 - 0xFF6B)
        put(Ie()) // 0xFFFF
    }

    override fun accepts(address: Int): Boolean {
        return this.addressSpaces.any { it.accepts(address) }
    }

    private fun put(addressSpace: Memory, i: Int = this.addressSpaces.size): Boolean {
        this.addressSpaces.add(i, addressSpace)
        return true
    }

    override fun reset() {
        this.cycles = 0
        this.addressSpaces.forEach { it.reset() }
    }

    fun tick(cycles: Int) {
        this.cycles += cycles
        this.addressSpaces.forEach {
            val device = it as? IoDevice
            if (device is Timer) {
                if (device.tick(cycles)) {
                    gb.cpu.interrupt(Interrupt.Timer)
                }
            } else if (device is Ppu) {
                if (device.isLcdEnabled()) {
                    var c = cycles
                    while (c-- > 0) {
                        device.tick(cycles)
                    }
                }
            } else if (device is SerialPort) {
                if (device.tick(cycles)) {
                    gb.cpu.interrupt(Interrupt.Serial)
                }
            } else if (device is Joypad) {
                if (device.tick(cycles)) {
                    gb.cpu.interrupt(Interrupt.Joypad)
                }
            } else device?.tick(cycles)
        }
    }

    override fun set(address: Int, value: Int): Boolean {
        val space = getSpace(address)
        return space?.set(address, value) ?: false
    }

    override fun get(address: Int): Int {
        val space = getSpace(address)
        return space?.get(address) ?: 0xFF
    }

    override fun range(): IntRange = IoReg.P1.address..IoReg.WX.address

    override fun fill(value: Int) = this.addressSpaces.forEach { it.fill(value) }

    override fun clear() = this.addressSpaces.forEach { it.clear() }

    private fun getSpace(address: Int): Memory? {
        this.addressSpaces.forEach {
            if (it.accepts(address)) return it
        }
        return null
    }

}