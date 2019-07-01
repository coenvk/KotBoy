package com.arman.kotboy.gui

interface Display {

    fun frameReady(buffer: IntArray)

    companion object {

        const val WIDTH = 160
        const val HEIGHT = 144
        const val SCALE = 6

    }

}