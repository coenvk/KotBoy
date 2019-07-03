package com.arman.kotboy

import com.arman.kotboy.memory.cartridge.Cartridge
import org.junit.jupiter.api.Test

class BootTest {

    @Test
    fun testBoot() {
        val reader = RomReader("dr-mario.gb")
        val cart = Cartridge(*reader.read())
        val kb = KotBoy(cart)
        kb.run()
        assert(true)
    }

}