package com.arman.kotboy

import com.arman.kotboy.gui.Display
import com.arman.kotboy.gui.LcdDisplay
import com.arman.kotboy.io.input.Keyboard
import com.arman.kotboy.memory.cartridge.Cartridge
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

        val keyboard = Keyboard()
        win.addKeyListener(keyboard)

        val reader = RomReader("blargg/cpu_instrs.zip")
        val cart = Cartridge(*reader.read())
        win.title = "KotBoy - ${cart.title}"
        val gb = KotBoy(display, cart, keyboard)
        Thread(gb).start()
    }
}