package com.arman.kotboy

import com.arman.kotboy.debug.gui.InstructionPanel
import com.arman.kotboy.debug.gui.MemoryPanel
import com.arman.kotboy.debug.gui.RegisterPanel
import com.arman.kotboy.debug.gui.addAll
import com.arman.kotboy.gui.Display
import com.arman.kotboy.gui.LcdDisplay
import com.arman.kotboy.io.input.Keyboard
import java.awt.Dimension
import java.awt.FlowLayout
import java.io.File
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

class KotBoy {

    private val options: Options
    private val gb: GameBoy
    private val display: LcdDisplay?

    constructor(vararg args: String) {
        this.options = parseArgs(args)
        this.gb = if (options.headless) {
            this.display = null
            GameBoy(options)
        } else {
            this.display = LcdDisplay()
            val keyboard = Keyboard()
            GameBoy(options, display, keyboard)
        }
    }

    fun run() {
        when {
            options.headless -> this.gb.run()
            options.debug -> setupDebugGui()
            else -> setupGui()
        }
    }

    private fun setupGui() {
        SwingUtilities.invokeLater {
            val win = JFrame("GameBoy - ${gb.cart.title}")
            win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            val size = Dimension(Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE)
            this.display!!.size = size
            this.display.preferredSize = size

            win.contentPane = this.display
            win.isResizable = false
            win.pack()
            win.setLocationRelativeTo(null)
            win.isVisible = true

            win.addKeyListener(this.gb.inputHandler)

            Thread(this.gb).start()
        }
    }

    private fun setupDebugGui() {
        SwingUtilities.invokeLater {
            val win = JFrame("GameBoy - ${gb.cart.title}")
            win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            val size = Dimension(Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE)
            this.display!!.size = size
            this.display.preferredSize = size

            win.layout = FlowLayout()

            val vbox = JPanel()
            vbox.layout = BoxLayout(vbox, BoxLayout.Y_AXIS)
            val hbox = JPanel()
            hbox.layout = BoxLayout(hbox, BoxLayout.X_AXIS)
            hbox.addAll(this.display, RegisterPanel())
            vbox.addAll(hbox, MemoryPanel())

            win.add(InstructionPanel())
            win.add(vbox)

            win.isResizable = false
            win.pack()
            win.setLocationRelativeTo(null)
            win.isVisible = true

            win.addKeyListener(this.gb.inputHandler)

            Thread(this.gb).start()
        }
    }

    private fun parseArgs(args: Array<out String>): Options {
        if (args.isEmpty()) {
            exitProcess(0)
        }
        val params: MutableSet<String> = HashSet()
        var romPath: String? = null
        for (arg in args) {
            if (arg.startsWith("--")) {
                params.add(arg.substring(2))
            } else if (!arg.startsWith("-")) {
                romPath = arg
            }
        }
        if (romPath == null) throw IllegalArgumentException()
        val file = File(romPath)
        if (!file.exists()) throw IllegalArgumentException()
        return Options(file, *params.toTypedArray())
    }

}