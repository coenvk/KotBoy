package com.arman.kotboy

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.Options
import com.arman.kotboy.core.gui.Display
import com.arman.kotboy.core.gui.JDisplay
import com.arman.kotboy.core.io.input.InputHandler
import com.arman.kotboy.core.io.input.Keyboard
import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

class KotBoy {

    private var gb: GameBoy? = null
    private var display: JDisplay = JDisplay()

    private val inputHandler: InputHandler = Keyboard()
    private val systemKeyListener: KeyListener

    private val fileChooser: JFileChooser =
            JFileChooser(System.getProperty("user.dir"))

    init {
        this.fileChooser.dialogTitle = "Select a ROM file"
        this.fileChooser.isAcceptAllFileFilterUsed = false
        val filter = FileNameExtensionFilter("ROM files", "gb", "gbc", "rom", "zip")
        this.fileChooser.addChoosableFileFilter(filter)

        this.systemKeyListener = object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ESCAPE) {
                    gb?.run { paused = !paused }
                }
            }
        }
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

        val sizeMenu = JMenu("Size")

        val size1Item = JMenuItem("1x1")
        size1Item.addActionListener { setSize(win, 1) }
        val size2Item = JMenuItem("2x2")
        size2Item.addActionListener { setSize(win, 2) }
        val size3Item = JMenuItem("3x3")
        size3Item.addActionListener { setSize(win, 3) }
        val size4Item = JMenuItem("4x4")
        size4Item.addActionListener { setSize(win, 4) }
        val size5Item = JMenuItem("5x5")
        size5Item.addActionListener { setSize(win, 5) }
        val size6Item = JMenuItem("6x6")
        size6Item.addActionListener { setSize(win, 6) }

        val fullscreenItem = JMenuItem("Fullscreen")
        fullscreenItem.accelerator = KeyStroke.getKeyStroke("F11")
        fullscreenItem.addActionListener { toggleFullscreen(win, !Options.windowed) }

        sizeMenu.add(size1Item)
        sizeMenu.add(size2Item)
        sizeMenu.add(size3Item)
        sizeMenu.add(size4Item)
        sizeMenu.add(size5Item)
        sizeMenu.add(size6Item)
        sizeMenu.add(fullscreenItem)

        val exitItem = JMenuItem("Exit")
        exitItem.accelerator = KeyStroke.getKeyStroke("control shift Q")
        exitItem.addActionListener { exitProcess(0) }

        val otherMenu = JMenu("Other")

        val infoItem = JMenuItem("Cartridge info")
        infoItem.addActionListener {
            gb?.run {
                val msg = "file: ${cart.file.name}\n" +
                        "name: ${cart.title}\n" +
                        "cart type: ${cart.type}\n" +
                        "ROM size: ${cart.romSize.size()} bytes\n" +
                        "RAM size: ${cart.ramSize.size()} bytes\n" +
                        "battery: ${cart.type.battery}\n" +
                        "GBC: ${cart.isCgb()}\n" +
                        "SGB: ${cart.isSgb()}"
                JOptionPane.showMessageDialog(win, msg, infoItem.text, JOptionPane.INFORMATION_MESSAGE)
            }
        }

        otherMenu.add(infoItem)

        fileMenu.add(loadRomItem)
        fileMenu.add(sizeMenu)
        fileMenu.addSeparator()
        fileMenu.add(exitItem)
        menuBar.add(fileMenu)
        menuBar.add(otherMenu)
        win.jMenuBar = menuBar
    }

    private fun loadRom(win: JFrame) {
        this.gb?.paused = true
        val ret = fileChooser.showOpenDialog(this.display.parent)
        if (ret == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            this.gb = GameBoy(file, this.display, this.inputHandler)
            win.addKeyListener(this.gb!!.inputHandler)
            win.addKeyListener(this.systemKeyListener)
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