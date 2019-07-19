package com.arman.kotboy.gui

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JPanel

class LcdDisplay : Display, JPanel() {

    private val img: BufferedImage

    init {
        val gc = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration
        this.img = gc.createCompatibleImage(Display.WIDTH, Display.HEIGHT)

        val size = Dimension(Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE)
        this.size = size
        this.preferredSize = size

        val g = this.img.createGraphics()
        g.background = Color.WHITE
        g.clearRect(0, 0, this.img.width, this.img.height)
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