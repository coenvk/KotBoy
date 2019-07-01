package com.arman.kotboy

import com.arman.kotboy.cpu.util.and
import com.arman.kotboy.gui.Display
import com.arman.kotboy.memory.cartridge.Cartridge
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException

class BootTest {

    @Test
    fun testBoot() {
        val cart = Cartridge(0x0, *getRom())
        val kb = KotBoy(object : Display {
            override fun frameReady(buffer: IntArray) {
            }
        }, cart)
        kb.run()
        assert(true)
    }

}

private fun getRom(): IntArray {
    val rom: ByteArray
    try {
        rom = File("src/test/resources/dr-mario.gb").readBytes()
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    val r = IntArray(rom.size)
    for (i in r.indices) {
        r[i] = rom[i] and 0xff
    }
    return r
}