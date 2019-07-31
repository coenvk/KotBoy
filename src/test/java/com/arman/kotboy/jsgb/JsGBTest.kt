package com.arman.kotboy.jsgb

import com.arman.kotboy.GameBoy
import org.junit.jupiter.api.Test

class JsGBTest {

    private lateinit var gb: GameBoy

    private fun setup(rom: String) {
        gb = GameBoy(rom)
        gb.reset()
    }

    fun run(rom: String) {
        setup(rom)

        var div = 0
        while (!gb.stopped) {
            gb.tick()
            if (++div >= 4) {
                div = 0
            }
        }
    }

    @Test
    fun testOpus5() {
        testRom("opus5.gb")
    }

    @Test
    fun testTtt() {
        testRom("ttt.gb")
    }

    private fun testRom(rom: String) {
        run("src\\test\\resources\\roms\\jsgb\\$rom")
    }

}