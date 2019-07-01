package com.arman.kotboy

import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.gui.LcdDisplay
import com.arman.kotboy.gui.Display
import com.arman.kotboy.memory.cartridge.Cartridge
import java.io.File
import java.io.IOException
import javax.swing.JFrame
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val win = JFrame()
        win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val display = LcdDisplay()
        display.setSize(Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE)

        win.contentPane = display
        win.isResizable = false
        win.setSize(Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE)
        win.setLocationRelativeTo(null)
        win.isVisible = true

        val cart = Cartridge(0x0, *getRom())
        win.title = "KotBoy - ${cart.title}"
        Thread(KotBoy(display, cart)).start()
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
        r[i] = rom[i].toUnsignedInt()
    }
    return r
}