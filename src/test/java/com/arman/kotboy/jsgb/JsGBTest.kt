package com.arman.kotboy.jsgb

import com.arman.kotboy.core.GameBoy
import org.junit.jupiter.api.Test
import java.io.File

class JsGBTest {

    private lateinit var gb: GameBoy

    private fun setup(rom: File) {
        gb = GameBoy(rom)
        gb.reset()
    }

    fun run(rom: File) {
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
        val url = javaClass.classLoader.getResource("roms/jsgb/$rom")
        if (url != null) run(File(url.toURI()))
        else {
            val dir = "${System.getProperty("user.dir")}/src/test/resources/roms/jsgb/$rom"
            run(File(dir))
        }
    }

}