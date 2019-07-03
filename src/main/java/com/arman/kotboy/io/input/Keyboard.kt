package com.arman.kotboy.io.input

import com.arman.kotboy.io.Joypad
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class Keyboard(override var buttonListener: ButtonListener? = null) : InputHandler, KeyAdapter() {

    private val mapping by lazy {
        val map = HashMap<Int, Joypad.Key>()
        map[KeyEvent.VK_LEFT] = Joypad.Key.LEFT
        map[KeyEvent.VK_RIGHT] = Joypad.Key.RIGHT
        map[KeyEvent.VK_UP] = Joypad.Key.UP
        map[KeyEvent.VK_DOWN] = Joypad.Key.DOWN
        map[KeyEvent.VK_S] = Joypad.Key.A
        map[KeyEvent.VK_A] = Joypad.Key.B
        map[KeyEvent.VK_ENTER] = Joypad.Key.START
        map[KeyEvent.VK_SHIFT] = Joypad.Key.SELECT
        return@lazy map
    }

    override fun keyPressed(e: KeyEvent) {
        val key = getKey(e)
        key?.let { buttonListener?.onPress(it) }
    }

    override fun keyReleased(e: KeyEvent) {
        val key = getKey(e)
        key?.let { buttonListener?.onRelease(it) }
    }

    private fun getKey(e: KeyEvent): Joypad.Key? {
        return mapping[e.keyCode]
    }

}