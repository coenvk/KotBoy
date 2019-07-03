package com.arman.kotboy

import com.arman.kotboy.cpu.Reg8
import com.arman.kotboy.cpu.util.lsb
import com.arman.kotboy.cpu.util.msb
import com.arman.kotboy.memory.cartridge.Cartridge

open class MemoryTest {

    private lateinit var gb: KotBoy

    private var running: Boolean = false

    protected var text: String = ""
    protected var status: Int = 0

    private fun setup(rom: String) {
        val reader = RomReader(rom)
        val cart = Cartridge(*reader.read())
        gb = KotBoy(cart)
        gb.reset()
    }

    fun run(rom: String) {
        setup(rom)

        status = 0x80
        var div = 0
        while (status == 0x80 && !isInfiniteLoop()) {
            gb.tick()
            if (++div >= 4) {
                status = getResult()
                div = 0
            }
        }
    }

    private fun isInfiniteLoop(): Boolean {
        var pc = gb.cpu.PC
        var found = false
        for (v in arrayOf(0x18, 0xfe)) {
            if (gb.mmu[pc++] != v) {
                found = true
                break
            }
        }
        if (!found) {
            return true
        }

        pc = gb.cpu.PC
        for (v in arrayOf(0xc3, pc.lsb(), pc.msb())) {
            if (gb.mmu[pc++] != v) {
                return false
            }
        }
        return true
    }

    private fun getResult(): Int {
        if (!running) {
            var i = 0xA000
            for (v in arrayOf(0x80, 0xde, 0xb0, 0x61)) {
                if (gb.mmu[i++] != v) {
                    return 0x80
                }
            }
            running = true
        }
        val status = gb.mmu[0xA000]

        var pc = gb.cpu.PC
        for (v in arrayOf(0xe5, 0xf5, 0xfa, 0x83, 0xd8)) {
            if (gb.mmu[pc++] != v) {
                return status
            }
        }

        val a = gb.cpu.read(Reg8.A).toChar()
        text += a
        gb.cpu.PC = gb.cpu.PC + 0x19
        return status
    }

}