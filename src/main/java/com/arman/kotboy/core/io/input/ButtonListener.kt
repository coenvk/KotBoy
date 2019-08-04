package com.arman.kotboy.core.io.input

import com.arman.kotboy.core.io.Joypad

interface ButtonListener {

    fun onPress(key: Joypad.Key)
    fun onRelease(key: Joypad.Key)

}