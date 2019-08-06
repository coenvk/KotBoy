package com.arman.kotboy.core.io.input

import com.arman.kotboy.core.io.Joypad
import java.awt.event.KeyEvent

class Keyboard(override var buttonListener: ButtonListener? = null) : Controller() {

    override val bindings by lazy {
        val map = ArrayList<Keybinding>()
        map.add(Keybinding(Joypad.Key.LEFT, KeyEvent.VK_LEFT))
        map.add(Keybinding(Joypad.Key.RIGHT, KeyEvent.VK_RIGHT))
        map.add(Keybinding(Joypad.Key.UP, KeyEvent.VK_UP))
        map.add(Keybinding(Joypad.Key.DOWN, KeyEvent.VK_DOWN))
        map.add(Keybinding(Joypad.Key.A, KeyEvent.VK_S))
        map.add(Keybinding(Joypad.Key.B, KeyEvent.VK_A))
        map.add(Keybinding(Joypad.Key.START, KeyEvent.VK_ENTER))
        map.add(Keybinding(Joypad.Key.SELECT, KeyEvent.VK_SHIFT))
        return@lazy map
    }

}