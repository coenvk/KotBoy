package com.arman.kotboy.gui

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GraphicsEnvironment
import java.awt.image.BufferedImage
import javax.swing.JPanel


class LcdDisplay : Display, JPanel() {

    private val img: BufferedImage

    init {
        val gc = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration
        this.img = gc.createCompatibleImage(Display.WIDTH, Display.HEIGHT)
    }

    override fun frameReady(buffer: IntArray) {
        this.img.setRGB(0, 0, Display.WIDTH, Display.HEIGHT, buffer, 0, Display.WIDTH)
        this.validate()
        this.repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        val g2d = g.create() as Graphics2D
        g2d.drawImage(img, 0, 0, Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE, null)
        g2d.dispose()
    }

}