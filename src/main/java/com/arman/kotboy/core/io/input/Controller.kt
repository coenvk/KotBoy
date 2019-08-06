package com.arman.kotboy.core.io.input

import com.arman.kotboy.core.io.Joypad
import java.awt.event.KeyEvent

abstract class Controller : InputHandler {

    protected abstract val bindings: MutableList<Keybinding>

    final override fun keyPressed(e: KeyEvent) {
        val key = getKey(e)
        key?.let { buttonListener?.onPress(it) }
    }

    final override fun keyReleased(e: KeyEvent) {
        val key = getKey(e)
        key?.let { buttonListener?.onRelease(it) }
    }

    private fun getKey(e: KeyEvent): Joypad.Key? {
        bindings.forEach {
            if (it.keyCode == e.keyCode) {
                return it.button
            }
        }
        return null
    }

}