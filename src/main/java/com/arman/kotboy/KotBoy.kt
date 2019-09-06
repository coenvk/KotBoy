package com.arman.kotboy

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.gui.Display
import com.arman.kotboy.core.gui.JDisplay
import com.arman.kotboy.core.gui.options.ColorPaletteDialog
import com.arman.kotboy.core.gui.options.EmulatedSystem
import com.arman.kotboy.core.gui.options.JoypadDialog
import com.arman.kotboy.core.gui.options.Options
import com.arman.kotboy.core.io.input.InputHandler
import com.arman.kotboy.core.io.input.Keyboard
import java.awt.Dimension
import java.awt.event.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

class KotBoy {

    private var gb: GameBoy? = null
    private var display: JDisplay = JDisplay()

    private val inputHandler: InputHandler = Keyboard()
    private val systemKeyListener: KeyListener

    private val fileChooser: JFileChooser =
            JFileChooser(System.getProperty("user.dir"))

    private lateinit var gbThread: Thread

    init {
        this.fileChooser.dialogTitle = "Select a ROM file"
        this.fileChooser.isAcceptAllFileFilterUsed = false
        val filter = FileNameExtensionFilter("ROM files", "gb", "gbc", "rom", "bin", "zip")
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
                toggleFullscreen(win)
            } else {
                win.pack()
                win.setLocationRelativeTo(null)
                win.isVisible = true
            }
        }
    }

    private fun setupMenuBar(win: JFrame) {
        val menuBar = JMenuBar()

        menuBar.add(createFileMenu(win))
        menuBar.add(createOptionsMenu(win))
        menuBar.add(createOtherMenu(win))

        win.jMenuBar = menuBar
    }

    private fun createFileMenu(win: JFrame): JMenu {
        val fileMenu = JMenu("File")
        val loadRomItem = JMenuItem("Load ROM...")
        loadRomItem.accelerator = KeyStroke.getKeyStroke("control O")
        loadRomItem.addActionListener { loadRom(win) }

        val restartRomItem = JMenuItem("Restart ROM")
        restartRomItem.addActionListener { restart(win) }

        val exitItem = JMenuItem("Exit")
        exitItem.accelerator = KeyStroke.getKeyStroke("control shift Q")
        exitItem.addActionListener { exitProcess(0) }

        fileMenu.add(loadRomItem)
        fileMenu.add(restartRomItem)
        fileMenu.addSeparator()

        fileMenu.add(exitItem)
        return fileMenu
    }

    private fun createOptionsMenu(win: JFrame): JMenu {
        val optionsMenu = JMenu("Options")

        val enableBootromItem = JCheckBoxMenuItem("Enable bootrom")
        enableBootromItem.addActionListener { Options.enableBootstrap = enableBootromItem.state }
        enableBootromItem.state = Options.enableBootstrap

        val enableBorderItem = JCheckBoxMenuItem("Enable border")
        enableBorderItem.addActionListener {
            Options.enableBorder = enableBorderItem.state
            val scale = Options.scale
            if (enableBorderItem.state) {
                setSize(win, scale * Display.WIDTH + 1, scale * Display.HEIGHT + 1)
            } else {
                setSize(win, scale * Display.WIDTH - 1, scale * Display.HEIGHT - 1)
            }
        }
        enableBorderItem.state = Options.enableBorder

        optionsMenu.add(enableBootromItem)
        optionsMenu.add(enableBorderItem)
        optionsMenu.add(createEmulatedSystemOptionsMenu(win))
        optionsMenu.add(createSizeOptionsMenu(win))
        optionsMenu.add(createColorPaletteOptionsMenuItem(win))
//        optionsMenu.add(createJoypadOptionsMenuItem(win))

        return optionsMenu
    }

    private fun createEmulatedSystemOptionsMenu(win: JFrame): JMenu {
        val emulatedSystemMenu = JMenu("Emulated system")

        val emulatedSystemDmgItem = JRadioButtonMenuItem(EmulatedSystem.DMG.mnemonic)
        emulatedSystemDmgItem.addActionListener {
            Options.emulatedSystem = EmulatedSystem.DMG
            restart(win)
        }
        val emulatedSystemCgbItem = JRadioButtonMenuItem(EmulatedSystem.CGB.mnemonic)
        emulatedSystemCgbItem.addActionListener {
            Options.emulatedSystem = EmulatedSystem.CGB
            restart(win)
        }
        val emulatedSystemSgbItem = JRadioButtonMenuItem(EmulatedSystem.SGB.mnemonic)
        emulatedSystemSgbItem.addActionListener {
            Options.emulatedSystem = EmulatedSystem.SGB
            restart(win)
        }

        val buttonGroup = ButtonGroup()
        buttonGroup.add(emulatedSystemDmgItem)
        buttonGroup.add(emulatedSystemCgbItem)
        buttonGroup.add(emulatedSystemSgbItem)

        emulatedSystemMenu.add(emulatedSystemDmgItem)
        emulatedSystemMenu.add(emulatedSystemCgbItem)
        emulatedSystemMenu.add(emulatedSystemSgbItem)

        when (Options.emulatedSystem) {
            EmulatedSystem.DMG -> emulatedSystemDmgItem.isSelected = true
            EmulatedSystem.CGB -> emulatedSystemCgbItem.isSelected = true
            EmulatedSystem.SGB -> emulatedSystemSgbItem.isSelected = true
        }

        return emulatedSystemMenu
    }

    private fun createSizeOptionsMenu(win: JFrame): JMenu {
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
        fullscreenItem.addActionListener { toggleFullscreen(win) }

        sizeMenu.add(size1Item)
        sizeMenu.add(size2Item)
        sizeMenu.add(size3Item)
        sizeMenu.add(size4Item)
        sizeMenu.add(size5Item)
        sizeMenu.add(size6Item)
        sizeMenu.add(fullscreenItem)

        return sizeMenu
    }

    private fun createColorPaletteOptionsMenuItem(win: JFrame): JMenuItem {
        val colorPaletteMenuItem = JMenuItem("Change DMG color palette...")
        colorPaletteMenuItem.addActionListener {
            val dialog = ColorPaletteDialog(win)
            dialog.addWindowListener(object : WindowAdapter() {
                override fun windowClosed(e: WindowEvent?) {
                    restart(win)
                }
            })
            dialog.isVisible = true
        }
        return colorPaletteMenuItem
    }

    private fun createJoypadOptionsMenuItem(win: JFrame): JMenuItem {
        val joypadMenuItem = JMenuItem("Configure joypad...")
        joypadMenuItem.addActionListener { JoypadDialog(win).isVisible = true }
        return joypadMenuItem
    }

    private fun createOtherMenu(win: JFrame): JMenu {
        val otherMenu = JMenu("Other")

        val infoItem = JMenuItem("Cartridge info")
        infoItem.addActionListener {
            this.gb?.run {
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
        return otherMenu
    }

    private fun loadRom(win: JFrame) {
        this.gb?.paused = true
        val ret = fileChooser.showOpenDialog(this.display.parent)
        if (ret == JFileChooser.APPROVE_OPTION) {
            restart(win)
        } else {
            this.gb?.paused = false
        }
    }

    private fun restart(win: JFrame) {
        val file = fileChooser.selectedFile ?: return

        this.gb?.let {
            it.running = false
            it.paused = false
            this.gbThread.join()
        }

        this.gb = GameBoy(file, this.display, this.inputHandler)
        win.addKeyListener(this.gb!!.inputHandler)
        win.addKeyListener(this.systemKeyListener)
        win.title = "KotBoy - ${this.gb!!.cart.title}"
        this.gbThread = Thread(this.gb)
        this.gbThread.start()
    }

    private fun setSize(win: JFrame, width: Int, height: Int) {
        val size = Dimension(width, height)
        this.display.size = size
        this.display.preferredSize = size
        win.pack()
        win.setLocationRelativeTo(null)
    }

    private fun setSize(win: JFrame, scale: Int) {
        Options.fullscreen = false
        Options.scale = scale

        setSize(win, scale * Display.WIDTH, scale * Display.HEIGHT)
    }

    private fun toggleFullscreen(win: JFrame) {
        Options.fullscreen = true

        this.display.toggleFullscreen(win)
    }

}