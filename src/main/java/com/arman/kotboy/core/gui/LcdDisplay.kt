package com.arman.kotboy.core.gui

import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JPanel

class LcdDisplay : Display, JPanel() {

    private val img: BufferedImage
    private val border: Border? = BmpBorder("gameboy_border.bmp")

    private val offsetX: Int
    private val offsetY: Int

    init {
        val scale = Display.SCALE
        val size = if (this.border == null) {
            this.offsetX = 0
            this.offsetY = 0
            Dimension(Display.WIDTH * scale, Display.HEIGHT * scale)
        } else {
            this.offsetX = 48 * scale
            this.offsetY = 40 * scale
            Dimension((Display.WIDTH + 96) * scale, (Display.HEIGHT + 80) * scale)
        }

        val gc = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration
        this.img = gc.createCompatibleImage(Display.WIDTH, Display.HEIGHT)

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
        this.border?.draw(g2d)
        g2d.drawImage(img, offsetX, offsetY, Display.WIDTH * Display.SCALE, Display.HEIGHT * Display.SCALE, null)
        g2d.dispose()
    }

}