package com.arman.kotboy.core.gui.options

import com.arman.kotboy.core.debug.gui.FlowGridLayout
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import javax.swing.border.EmptyBorder

class JoypadDialog(owner: JFrame? = null) : JDialog(owner, true), KeyListener {

    private val statusBar = JLabel()

    private var configuring: Boolean = false
    private var defaultConfigurationKey: Int = 0

    private val labelLeftKey = JLabel(KeyEvent.getKeyText(Options.keyLeft))
    private val labelRightKey = JLabel(KeyEvent.getKeyText(Options.keyRight))
    private val labelUpKey = JLabel(KeyEvent.getKeyText(Options.keyUp))
    private val labelDownKey = JLabel(KeyEvent.getKeyText(Options.keyDown))
    private val labelAKey = JLabel(KeyEvent.getKeyText(Options.keyA))
    private val labelBKey = JLabel(KeyEvent.getKeyText(Options.keyB))
    private val labelStartKey = JLabel(KeyEvent.getKeyText(Options.keyStart))
    private val labelSelectKey = JLabel(KeyEvent.getKeyText(Options.keySelect))

    init {
        setup()
        pack()
        setLocationRelativeTo(owner)
    }

    private fun setup() {
        title = "Configure joypad"
        isResizable = false
        layout = BorderLayout()
        val dialogSize = Dimension(200, 270)
        preferredSize = dialogSize

        addKeyListener(this)

        val pane = JPanel()
        pane.border = EmptyBorder(10, 10, 10, 10)
        add(pane, BorderLayout.CENTER)
        pane.layout = FlowGridLayout(8, 3, 1, 5)

        setupKeyButtons(pane)

        statusBar.text = "Click a button to configure a key..."
        add(statusBar, BorderLayout.PAGE_END)
    }

    private fun setupKeyButtons(pane: JPanel) {
        val ignoreSize = Dimension(-1, -1)
        val buttonSize = Dimension(80, 20)

        pane.add(JLabel("Left:"))
        pane.add(labelLeftKey)
        val buttonLeftKey = JButton("Change")
        buttonLeftKey.preferredSize = buttonSize
        buttonLeftKey.minimumSize = ignoreSize
        buttonLeftKey.maximumSize = ignoreSize
        buttonLeftKey.isFocusable = false
        buttonLeftKey.addActionListener {
            statusBar.text = "Configure Left key..."
            defaultConfigurationKey = KeyEvent.VK_LEFT
            configuring = true
        }
        pane.add(buttonLeftKey)

        pane.add(JLabel("Right:"))
        pane.add(labelRightKey)
        val buttonRightKey = JButton("Change")
        buttonRightKey.preferredSize = buttonSize
        buttonRightKey.minimumSize = ignoreSize
        buttonRightKey.maximumSize = ignoreSize
        buttonRightKey.isFocusable = false
        buttonRightKey.addActionListener {
            statusBar.text = "Configure Right key..."
            defaultConfigurationKey = KeyEvent.VK_RIGHT
            configuring = true
        }
        pane.add(buttonRightKey)

        pane.add(JLabel("Up:"))
        pane.add(labelUpKey)
        val buttonUpKey = JButton("Change")
        buttonUpKey.preferredSize = buttonSize
        buttonUpKey.minimumSize = ignoreSize
        buttonUpKey.maximumSize = ignoreSize
        buttonUpKey.isFocusable = false
        buttonUpKey.addActionListener {
            statusBar.text = "Configure Up key..."
            defaultConfigurationKey = KeyEvent.VK_UP
            configuring = true
        }
        pane.add(buttonUpKey)

        pane.add(JLabel("Down:"))
        pane.add(labelDownKey)
        val buttonDownKey = JButton("Change")
        buttonDownKey.preferredSize = buttonSize
        buttonDownKey.minimumSize = ignoreSize
        buttonDownKey.maximumSize = ignoreSize
        buttonDownKey.isFocusable = false
        buttonDownKey.addActionListener {
            statusBar.text = "Configure Down key..."
            defaultConfigurationKey = KeyEvent.VK_DOWN
            configuring = true
        }
        pane.add(buttonDownKey)

        pane.add(JLabel("A:"))
        pane.add(labelAKey)
        val buttonAKey = JButton("Change")
        buttonAKey.preferredSize = buttonSize
        buttonAKey.minimumSize = ignoreSize
        buttonAKey.maximumSize = ignoreSize
        buttonAKey.isFocusable = false
        buttonAKey.addActionListener {
            statusBar.text = "Configure A key..."
            defaultConfigurationKey = KeyEvent.VK_S
            configuring = true
        }
        pane.add(buttonAKey)

        pane.add(JLabel("B:"))
        pane.add(labelBKey)
        val buttonBKey = JButton("Change")
        buttonBKey.preferredSize = buttonSize
        buttonBKey.minimumSize = ignoreSize
        buttonBKey.maximumSize = ignoreSize
        buttonBKey.isFocusable = false
        buttonBKey.addActionListener {
            statusBar.text = "Configure B key..."
            defaultConfigurationKey = KeyEvent.VK_A
            configuring = true
        }
        pane.add(buttonBKey)

        pane.add(JLabel("Start:"))
        pane.add(labelStartKey)
        val buttonStartKey = JButton("Change")
        buttonStartKey.preferredSize = buttonSize
        buttonStartKey.minimumSize = ignoreSize
        buttonStartKey.maximumSize = ignoreSize
        buttonStartKey.isFocusable = false
        buttonStartKey.addActionListener {
            statusBar.text = "Configure Start key..."
            defaultConfigurationKey = KeyEvent.VK_ENTER
            configuring = true
        }
        pane.add(buttonStartKey)

        pane.add(JLabel("Select:"))
        pane.add(labelSelectKey)
        val buttonSelectKey = JButton("Change")
        buttonSelectKey.preferredSize = buttonSize
        buttonSelectKey.minimumSize = ignoreSize
        buttonSelectKey.maximumSize = ignoreSize
        buttonSelectKey.isFocusable = false
        buttonSelectKey.addActionListener {
            statusBar.text = "Configure Select key..."
            defaultConfigurationKey = KeyEvent.VK_SHIFT
            configuring = true
        }
        pane.add(buttonSelectKey)
    }

    override fun keyReleased(e: KeyEvent) {
        println("Key released")
        val keyCode = e.keyCode
        if (configuring) {
            when (defaultConfigurationKey) {
                0 -> Unit
                KeyEvent.VK_LEFT -> {
                    Options.keyLeft = keyCode
                    labelLeftKey.text = KeyEvent.getKeyText(keyCode)
                }
                KeyEvent.VK_RIGHT -> {
                    Options.keyRight = keyCode
                    labelRightKey.text = KeyEvent.getKeyText(keyCode)
                }
                KeyEvent.VK_UP -> {
                    Options.keyUp = keyCode
                    labelUpKey.text = KeyEvent.getKeyText(keyCode)
                }
                KeyEvent.VK_DOWN -> {
                    Options.keyDown = keyCode
                    labelDownKey.text = KeyEvent.getKeyText(keyCode)
                }
                KeyEvent.VK_S -> {
                    Options.keyA = keyCode
                    labelAKey.text = KeyEvent.getKeyText(keyCode)
                }
                KeyEvent.VK_A -> {
                    Options.keyB = keyCode
                    labelBKey.text = KeyEvent.getKeyText(keyCode)
                }
                KeyEvent.VK_ENTER -> {
                    Options.keyStart = keyCode
                    labelStartKey.text = KeyEvent.getKeyText(keyCode)
                }
                KeyEvent.VK_SHIFT -> {
                    Options.keySelect = keyCode
                    labelSelectKey.text = KeyEvent.getKeyText(keyCode)
                }
            }
            statusBar.text = "Click a button to configure a key..."
            defaultConfigurationKey = 0
            configuring = false
        }
    }

    override fun keyPressed(e: KeyEvent) = Unit

    override fun keyTyped(e: KeyEvent) = Unit

}