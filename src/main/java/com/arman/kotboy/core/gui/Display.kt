package com.arman.kotboy.core.gui

interface Display : Runnable {

    val border: Border
        get() = BmpBorder("gameboy_border.bmp")

    fun frameReady(buffer: IntArray)

    companion object {

        const val WIDTH = 160
        const val HEIGHT = 144

    }

}