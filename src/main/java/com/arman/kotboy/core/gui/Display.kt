package com.arman.kotboy.core.gui

interface Display {

    fun frameReady(buffer: IntArray)

    companion object {

        const val WIDTH = 160
        const val HEIGHT = 144
        const val SCALE = 3

    }

}