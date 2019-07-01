package com.arman.kotboy.io

import com.arman.kotboy.KotBoy
import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.AddressSpace
import com.arman.kotboy.memory.MemoryByte
import com.arman.kotboy.memory.MemoryMap

class Io(private val gb: KotBoy) : AddressSpace(0xFFFFFF) {

    var cycles = 0

    init {
        put(Lcd(gb))
        put(Div())
        put(Serial())
        put(Joypad())
        put(Timer())
        put(MemoryByte(IoReg.IE.address))
    }

    override fun accepts(address: Address): Boolean {
        return address in IoReg.P1.address..IoReg.WX.address || address == IoReg.IE.address
    }

    override fun reset() {
        super.reset()
        this.cycles = 0
    }

    fun tick(cycles: Int) {
        this.cycles += cycles
        this.addressSpaces.forEach {
            val device = it as? IoDevice
            if (device is Timer) {
                if (device.tick(cycles)) {
                    gb.cpu.interrupt(MemoryMap.TIMER_OVERFLOW_INTERRUPT)
                }
            } else if (device is Lcd) {
                if (device.isLcdEnabled()) {
                    var c = cycles
                    while (c-- > 0) {
                        device.tick(cycles)
                    }
                }
            } else device?.tick(cycles)
        }
    }

    override fun set(address: Address, value: Int): Boolean {
        val space = getSpace(address)
        return space?.set(address, value) ?: false
    }

    override fun get(address: Address): Int {
        val space = getSpace(address)
        return space?.get(address) ?: 0
    }

}