package com.arman.kotboy.core.debug.gui

import com.arman.kotboy.core.cpu.util.hexString
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingUtilities

class MemoryPanel : DebugPanel() {

    init {
        val pane = JPanel()
        val scrollPane =
            JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
        val w = 400
        val h = 200
        scrollPane.maximumSize = Dimension(w, h)
        scrollPane.preferredSize = Dimension(w, h)
        pane.layout = FlowGridLayout(0x10000 / 0x10, 2, 50)
        for (i in 0x0..0xFFFF step 0x10) {
            pane.addAll(i.hexString(4), "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")
        }
        add(scrollPane)
    }

}

fun main() {
    SwingUtilities.invokeLater {
        val win = JFrame()
        win.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val panel = MemoryPanel()

        win.contentPane = panel
        win.isResizable = false
        win.setLocationRelativeTo(null)
        win.isVisible = true
        win.pack()

        win.title = "GameBoy"
    }
}