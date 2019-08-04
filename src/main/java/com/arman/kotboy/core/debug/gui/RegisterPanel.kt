package com.arman.kotboy.core.debug.gui

import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*

class RegisterPanel : DebugPanel() {

    private val af = JLabel("0080")
    private val bc = JLabel("0013")
    private val de = JLabel("00D8")
    private val hl = JLabel("014D")
    private val sp = JLabel("FFFE")
    private val pc = JLabel("028C")

    private val lcdc = JLabel("83")
    private val stat = JLabel("02")
    private val ly = JLabel("58")
    private val ime = JLabel("0")
    private val ie = JLabel("0D")
    private val _if = JLabel("E1")

    private val z = JCheckBox("Z")
    private val n = JCheckBox("N")
    private val h = JCheckBox("H")
    private val c = JCheckBox("C")

    init {
        val pane = JPanel(GridLayout(6, 4))
        pane.add("af=")
        pane.add(af)
        pane.add("lcdc=")
        pane.add(lcdc)
        pane.add("bc=")
        pane.add(bc)
        pane.add("stat=")
        pane.add(stat)
        pane.add("de=")
        pane.add(de)
        pane.add("ly=")
        pane.add(ly)
        pane.add("hl=")
        pane.add(hl)
        pane.add("ime=")
        pane.add(ime)
        pane.add("sp=")
        pane.add(sp)
        pane.add("ie=")
        pane.add(ie)
        pane.add("pc=")
        pane.add(pc)
        pane.add("if=")
        pane.add(_if)
        add(pane)

        val flagsPane = JPanel(GridLayout(4, 1))
        flagsPane.addAll(z, n, h, c)
        add(flagsPane, BorderLayout.EAST)
    }

}

fun main() {
    SwingUtilities.invokeLater {
        val win = JFrame()
        win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val panel = RegisterPanel()

        win.contentPane = panel
        win.isResizable = false
        win.setLocationRelativeTo(null)
        win.isVisible = true
        win.pack()

        win.title = "GameBoy"
    }
}