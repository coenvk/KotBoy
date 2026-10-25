package com.arman.kotboy.memory

import com.arman.kotboy.core.memory.cartridge.Cartridge
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class BatterySaveTest {

    @TempDir
    lateinit var dir: File

    /** A 32 KB MBC5 + RAM + battery ROM with 8 KB of RAM, plus a .sav that starts with [save]. */
    private fun cartridgeWithSave(vararg save: Int): Cartridge {
        val rom = ByteArray(0x8000)
        rom[0x147] = 0x1B
        rom[0x148] = 0x00
        rom[0x149] = 0x02
        val romFile = File(dir, "game.gb")
        romFile.writeBytes(rom)
        val sav = ByteArray(0x2000)
        save.forEachIndexed { i, b -> sav[i] = b.toByte() }
        File(dir, "game.sav").writeBytes(sav)
        return Cartridge(romFile)
    }

    @Test
    fun `battery RAM loaded from the save survives a reset`() {
        val cart = cartridgeWithSave(0x4C, 0x4B, 0x50, 0x52)
        // GameBoy.run() resets the machine when the boot ROM is off
        cart.reset()
        cart.set(0x0000, 0x0A) // enable RAM
        assertEquals(0x4C, cart.get(0xA000))
        assertEquals(0x4B, cart.get(0xA001))
        assertEquals(0x50, cart.get(0xA002))
        assertEquals(0x52, cart.get(0xA003))
    }
}
