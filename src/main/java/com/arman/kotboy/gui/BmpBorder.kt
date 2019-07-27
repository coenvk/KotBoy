package com.arman.kotboy.gui

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class BmpBorder(bmpFile: String) : Border {

    private val bmp: BufferedImage = ImageIO.read(javaClass.classLoader.getResourceAsStream(bmpFile))

    override fun draw(g: Graphics2D) {
        g.drawImage(this.bmp, 0, 0, bmp.width * Display.SCALE, bmp.height * Display.SCALE, null)
    }

}