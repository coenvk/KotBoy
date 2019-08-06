package com.arman.kotboy

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.Options
import com.arman.kotboy.core.gui.Display
import com.arman.kotboy.core.gui.JDisplay
import com.arman.kotboy.core.io.input.InputHandler
import com.arman.kotboy.core.io.input.Keyboard
import java.awt.Dimension
import javax.swing.*
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

class KotBoy {

    private var gb: GameBoy? = null
    private var display: JDisplay = JDisplay()

    private val inputHandler: InputHandler = Keyboard()

    private val fileChooser: JFileChooser =
            JFileChooser(System.getProperty("user.dir"))

    init {
        this.fileChooser.dialogTitle = "Select a ROM file"
        this.fileChooser.isAcceptAllFileFilterUsed = false
        val filter = FileNameExtensionFilter("ROM files", "gb", "gbc", "rom", "zip")
        this.fileChooser.addChoosableFileFilter(filter)
    }

    fun run() = setupGui()

    private fun setupGui() {
        SwingUtilities.invokeLater {
            val win = JFrame("KotBoy")
            win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            win.contentPane = this.display
            Thread(this.display).start()

            setupMenuBar(win)

            if (Options.fullscreen) {
                toggleFullscreen(win, Options.windowed)
            } else {
                win.pack()
                win.setLocationRelativeTo(null)
                win.isVisible = true
            }
        }
    }

    private fun setupMenuBar(win: JFrame) {
        val menuBar = JMenuBar()
        val fileMenu = JMenu("File")
        fileMenu.addMenuListener(object : MenuListener {
            override fun menuSelected(e: MenuEvent?) {
                this@KotBoy.gb?.paused = true
            }

            override fun menuCanceled(e: MenuEvent?) {
            }

            override fun menuDeselected(e: MenuEvent) {
                this@KotBoy.gb?.paused = false
            }
        })

        val loadRomItem = JMenuItem("Load ROM...")
        loadRomItem.accelerator = KeyStroke.getKeyStroke("control O")
        loadRomItem.addActionListener { loadRom(win) }

        val sizeItem = JMenu("Size")

        val size1Item = JMenuItem("1 x 1")
        size1Item.addActionListener { setSize(win, 1) }
        val size2Item = JMenuItem("2 x 2")
        size2Item.addActionListener { setSize(win, 2) }
        val size3Item = JMenuItem("3 x 3")
        size3Item.addActionListener { setSize(win, 3) }

        val fullscreenItem = JMenuItem("Fullscreen")
        fullscreenItem.accelerator = KeyStroke.getKeyStroke("F11")
        fullscreenItem.addActionListener { toggleFullscreen(win, !Options.windowed) }

        sizeItem.add(size1Item)
        sizeItem.add(size2Item)
        sizeItem.add(size3Item)
        sizeItem.add(fullscreenItem)

        val exitItem = JMenuItem("Exit")
        exitItem.accelerator = KeyStroke.getKeyStroke("control shift Q")
        exitItem.addActionListener { exitProcess(0) }

        fileMenu.add(loadRomItem)
        fileMenu.add(sizeItem)
        fileMenu.addSeparator()
        fileMenu.add(exitItem)
        menuBar.add(fileMenu)
        win.jMenuBar = menuBar
    }

    private fun loadRom(win: JFrame) {
        this.gb?.paused = true
        val ret = fileChooser.showOpenDialog(this.display.parent)
        if (ret == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            this.gb = GameBoy(file, this.display, this.inputHandler)
            win.addKeyListener(this.gb!!.inputHandler)
            win.title = "KotBoy - ${this.gb!!.cart.title}"
            Thread(this.gb).start()
        } else {
            this.gb?.paused = false
        }
    }

    private fun setSize(win: JFrame, scale: Int) {
        Options.windowed = true
        Options.fullscreen = false
        Options.scale = scale

        val size = Dimension(Display.WIDTH * scale, Display.HEIGHT * scale)
        this.display.size = size
        this.display.preferredSize = size
        win.pack()
        win.setLocationRelativeTo(null)
    }

    private fun toggleFullscreen(win: JFrame, windowed: Boolean) {
        Options.windowed = windowed
        Options.fullscreen = true

        this.display.toggleFullscreen(win, windowed)
    }

}