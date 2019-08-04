package com.arman.kotboy.core.debug.gui

import java.awt.Component
import java.util.*
import javax.swing.JLabel
import javax.swing.JPanel

fun JPanel.add(s: String) = this.add(JLabel(s))

fun JPanel.addAll(vararg comps: Component) {
    for (comp in comps) this.add(comp)
}

fun JPanel.addAll(vararg ss: String) {
    for (s in ss) this.add(JLabel(s))
}

abstract class DebugPanel : Observer, JPanel() {

    override fun update(o: Observable?, arg: Any?) {
        // TODO
    }

}