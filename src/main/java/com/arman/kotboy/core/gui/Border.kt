package com.arman.kotboy.core.gui

import java.awt.Graphics2D

interface Border {

    fun draw(g: Graphics2D, offsetX: Int, offsetY: Int, scale: Float)

    companion object {

        const val PX = 48
        const val PY = 40

    }

}