package com.arman.kotboy

import com.arman.kotboy.gui.LcdDisplay
import com.arman.kotboy.io.input.Keyboard
import javax.swing.*
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

class KotBoy {

    private var options: Options? = null
    private var gb: GameBoy? = null
    private var display: LcdDisplay = LcdDisplay()

    private val inputHandler: Keyboard = Keyboard()

    private val fileChooser: JFileChooser =
        JFileChooser("C:\\Users\\Coen\\IdeaProjects\\KotBoy\\src\\test\\resources\\roms")

    init {
        this.fileChooser.dialogTitle = "Select a ROM file"
        this.fileChooser.isAcceptAllFileFilterUsed = false
        val filter = FileNameExtensionFilter("ROM files", "gb", "gbc", "rom", "zip")
        this.fileChooser.addChoosableFileFilter(filter)
    }

    fun run() {
        if (this.options == null) {
            this.setupGui()
        } else {
            this.options?.let {
                when {
                    it.headless -> {
                        this.gb = GameBoy(it)
                        this.gb!!.run()
                    }
                    else -> setupGui()
                }
            }
        }
    }

    private fun setupGui() {
        SwingUtilities.invokeLater {
            val win = JFrame("KotBoy")
            win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            win.contentPane = this.display
            win.isResizable = false

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
            loadRomItem.addActionListener {
                this@KotBoy.gb?.paused = true
                val ret = fileChooser.showOpenDialog(null)
                if (ret == JFileChooser.APPROVE_OPTION) {
                    val file = fileChooser.selectedFile
                    this.options = Options(file)
                    this.gb = GameBoy(this.options!!, this.display, this.inputHandler)
                    win.addKeyListener(this.gb!!.inputHandler)
                    win.title = "KotBoy - ${this.gb!!.cart.title}"
                    Thread(this.gb).start()
                } else {
                    this@KotBoy.gb?.paused = false
                }
            }
            val exitItem = JMenuItem("Exit")
            exitItem.accelerator = KeyStroke.getKeyStroke("control shift Q")
            exitItem.addActionListener {
                exitProcess(0)
            }

            fileMenu.add(loadRomItem)
            fileMenu.addSeparator()
            fileMenu.add(exitItem)
            menuBar.add(fileMenu)
            win.jMenuBar = menuBar

            win.pack()
            win.setLocationRelativeTo(null)
            win.isVisible = true
        }
    }

}