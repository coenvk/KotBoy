package com.arman.kotboy.io.input

import com.arman.kotboy.io.Joypad

interface ButtonListener {

    fun onPress(key: Joypad.Key)
    fun onRelease(key: Joypad.Key)

}