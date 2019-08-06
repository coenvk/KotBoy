package com.arman.kotboy.core.gui

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class BmpBorder(bmpFile: String) : Border {

    private val bmp: BufferedImage = ImageIO.read(javaClass.classLoader.getResourceAsStream(bmpFile))

    override fun draw(g: Graphics2D, offsetX: Int, offsetY: Int, scale: Float) {
        g.drawImage(this.bmp, offsetX, offsetY, (bmp.width * scale).toInt(), (bmp.height * scale).toInt(), null)
    }

}