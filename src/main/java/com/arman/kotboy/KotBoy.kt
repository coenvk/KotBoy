package com.arman.kotboy

import com.arman.kotboy.debug.gui.InstructionPanel
import com.arman.kotboy.debug.gui.MemoryPanel
import com.arman.kotboy.debug.gui.RegisterPanel
import com.arman.kotboy.debug.gui.addAll
import com.arman.kotboy.gui.Display
import com.arman.kotboy.gui.LcdDisplay
import com.arman.kotboy.io.input.Keyboard
import java.awt.*
import javax.swing.*
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener
import javax.swing.filechooser.FileNameExtensionFilter

class KotBoy {

    private var options: Options? = null
    private var gb: GameBoy? = null
    private var display: LcdDisplay = LcdDisplay()

    private val inputHandler: Keyboard = Keyboard()

    private val fileChooser: JFileChooser = JFileChooser()

    init {
        this.fileChooser.dialogTitle = "Select a ROM file"
        this.fileChooser.isAcceptAllFileFilterUsed = false
        val filter = FileNameExtensionFilter("ROM files", "gb", "zip")
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
                    it.debug -> setupDebugGui()
                    else -> setupGui()
                }
            }
        }
    }

    private fun setupGui() {
        SwingUtilities.invokeLater {
            val win = JFrame("KotBoy")
            win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            val size = Dimension(Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE)
            this.display.size = size
            this.display.preferredSize = size

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
            val loadItem = JMenuItem("Load ROM...")
            loadItem.addActionListener {
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

            fileMenu.add(loadItem)
            menuBar.add(fileMenu)
            win.jMenuBar = menuBar

            win.pack()
            win.setLocationRelativeTo(null)
            win.isVisible = true
        }
    }

    private fun setupDebugGui() {
        SwingUtilities.invokeLater {
            val win = JFrame("KotBoy")
            win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

            val size = Dimension(Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE)
            this.display.size = size
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
            val loadItem = JMenuItem("Load ROM...")
            loadItem.addActionListener {
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

            fileMenu.add(loadItem)
            menuBar.add(fileMenu)
            win.jMenuBar = menuBar

            win.pack()
            win.setLocationRelativeTo(null)
            win.isVisible = true
        }
    }

}