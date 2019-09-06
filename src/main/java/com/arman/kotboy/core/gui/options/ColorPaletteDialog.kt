package com.arman.kotboy.core.gui.options

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder

class ColorPaletteDialog(owner: JFrame) : JDialog(owner, true) {

    companion object {

        const val WIDTH = 300
        const val HEIGHT = 260
        const val PADDING = 10
        const val MINOR_TICK_SPACING = 5
        const val MAJOR_TICK_SPACING = 255
        const val MAX_COLOR_VALUE = 255
        const val EDIT_FIELD_SIZE = 40
        const val EDIT_FIELD_BORDER_WIDTH = 2

    }

    private val selectorBorder: Border =
            CompoundBorder(
                    LineBorder(Color.BLACK, EDIT_FIELD_BORDER_WIDTH),
                    LineBorder(Color.WHITE, EDIT_FIELD_BORDER_WIDTH)
            )

    private var editPalette0: JTextField = JTextField()
    private var editPalette1: JTextField = JTextField()
    private var editPalette2: JTextField = JTextField()
    private var editPalette3: JTextField = JTextField()

    private var sliderRed: JSlider = JSlider(0, MAX_COLOR_VALUE, 0)
    private var sliderGreen: JSlider = JSlider(0, MAX_COLOR_VALUE, 0)
    private var sliderBlue: JSlider = JSlider(0, MAX_COLOR_VALUE, 0)

    private var selectedPalette: JTextField = editPalette0

    init {
        setup()
        pack()
        setLocationRelativeTo(owner)
    }

    private fun setup() {
        title = "Change DMG color palette"
        layout = BorderLayout()
        isResizable = false

        setupSliders()
        setupEditFields()
        setupDialogButtons()

        updateSliders()

        preferredSize = Dimension(WIDTH, HEIGHT)
    }

    private fun setupDialogButtons() {
        val buttonPanel = JPanel()
        add(buttonPanel, BorderLayout.SOUTH)

        val okButton = JButton("OK")
        okButton.addActionListener { handleOK() }
        buttonPanel.add(okButton)
        val applyButton = JButton("Apply")
        applyButton.addActionListener { handleApply() }
        buttonPanel.add(applyButton)
        val cancelButton = JButton("Cancel")
        cancelButton.addActionListener { handleCancel() }
        buttonPanel.add(cancelButton)

        val defaultButton = JButton("Default")
        defaultButton.addActionListener { handleDefault() }
        buttonPanel.add(defaultButton)
    }

    private fun setupSliders() {
        val sliderPanel = JPanel()
        sliderPanel.layout = BoxLayout(sliderPanel, BoxLayout.Y_AXIS)
        sliderPanel.border = EmptyBorder(PADDING, PADDING, 0, PADDING)
        add(sliderPanel, BorderLayout.NORTH)

        sliderRed.paintTicks = true
        sliderRed.paintLabels = true
        sliderRed.minorTickSpacing = MINOR_TICK_SPACING
        sliderRed.majorTickSpacing = MAJOR_TICK_SPACING
        sliderRed.addChangeListener {
            val currentColor = selectedPalette.background
            selectedPalette.background = Color(sliderRed.value, currentColor.green, currentColor.blue)
        }
        sliderPanel.add(sliderRed)

        sliderGreen.paintTicks = true
        sliderGreen.paintLabels = true
        sliderGreen.minorTickSpacing = MINOR_TICK_SPACING
        sliderGreen.majorTickSpacing = MAJOR_TICK_SPACING
        sliderGreen.addChangeListener {
            val currentColor = selectedPalette.background
            selectedPalette.background = Color(currentColor.red, sliderGreen.value, currentColor.blue)
        }
        sliderPanel.add(sliderGreen)

        sliderBlue.paintTicks = true
        sliderBlue.paintLabels = true
        sliderBlue.minorTickSpacing = MINOR_TICK_SPACING
        sliderBlue.majorTickSpacing = MAJOR_TICK_SPACING
        sliderBlue.addChangeListener {
            val currentColor = selectedPalette.background
            selectedPalette.background = Color(currentColor.red, currentColor.green, sliderBlue.value)
        }
        sliderPanel.add(sliderBlue)
    }

    private fun setupEditFields() {
        val selectorPanel = JPanel(FlowLayout(FlowLayout.CENTER, 0, 0))
        selectorPanel.border = EmptyBorder(0, PADDING, PADDING, PADDING)
        add(selectorPanel, BorderLayout.CENTER)

        val editorSize = Dimension(EDIT_FIELD_SIZE, EDIT_FIELD_SIZE)

        editPalette0.isEditable = false
        editPalette0.preferredSize = editorSize
        editPalette0.background = Color(Options.palette0)
        editPalette0.border = selectorBorder
        editPalette0.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                editPalette0.border = selectorBorder
                editPalette1.border = null
                editPalette2.border = null
                editPalette3.border = null
                selectedPalette = editPalette0
                updateSliders()
            }
        })
        selectorPanel.add(editPalette0)

        editPalette1.isEditable = false
        editPalette1.preferredSize = editorSize
        editPalette1.background = Color(Options.palette1)
        editPalette1.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                editPalette0.border = null
                editPalette1.border = selectorBorder
                editPalette2.border = null
                editPalette3.border = null
                selectedPalette = editPalette1
                updateSliders()
            }
        })
        selectorPanel.add(editPalette1)

        editPalette2.isEditable = false
        editPalette2.preferredSize = editorSize
        editPalette2.background = Color(Options.palette2)
        editPalette2.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                editPalette0.border = null
                editPalette1.border = null
                editPalette2.border = selectorBorder
                editPalette3.border = null
                selectedPalette = editPalette2
                updateSliders()
            }
        })
        selectorPanel.add(editPalette2)

        editPalette3.isEditable = false
        editPalette3.preferredSize = editorSize
        editPalette3.background = Color(Options.palette3)
        editPalette3.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                editPalette0.border = null
                editPalette1.border = null
                editPalette2.border = null
                editPalette3.border = selectorBorder
                selectedPalette = editPalette3
                updateSliders()
            }
        })
        selectorPanel.add(editPalette3)
    }

    private fun updateSliders() {
        val color = selectedPalette.background
        sliderRed.value = color.red
        sliderGreen.value = color.green
        sliderBlue.value = color.blue
    }

    private fun handleOK() {
        handleApply()
        dispose()
    }

    private fun handleApply() {
        Options.palette0 = editPalette0.background.rgb
        Options.palette1 = editPalette1.background.rgb
        Options.palette2 = editPalette2.background.rgb
        Options.palette3 = editPalette3.background.rgb
    }

    private fun handleCancel() {
        dispose()
    }

    private fun handleDefault() {
        editPalette0.background = Color(DefaultOptions.palette0)
        editPalette1.background = Color(DefaultOptions.palette1)
        editPalette2.background = Color(DefaultOptions.palette2)
        editPalette3.background = Color(DefaultOptions.palette3)
        updateSliders()
    }

}