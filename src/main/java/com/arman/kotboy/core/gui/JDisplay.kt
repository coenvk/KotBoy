package com.arman.kotboy.core.gui

import com.arman.kotboy.core.gui.options.Options
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.image.BufferedImage
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.concurrent.withLock
import kotlin.math.min
import kotlin.math.round

class JDisplay : Display, JPanel() {

    private val img: BufferedImage

    private var offsetX: Int
    private var offsetY: Int
    private var scale: Float = Options.scale.toFloat()

    private var lock: ReentrantLock = ReentrantLock()
    private val repaintCondition: Condition = lock.newCondition()

    init {
        val gc = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.defaultConfiguration
        this.img = gc.createCompatibleImage(Display.WIDTH, Display.HEIGHT)

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                onResize(e.component)
            }
        })

        this.offsetX = 0
        this.offsetY = 0

        val size = Dimension(Options.scale * Display.WIDTH, Options.scale * Display.HEIGHT)

        this.size = size
        this.preferredSize = size

        val g = this.img.createGraphics()
        g.background = Color.WHITE
        g.clearRect(0, 0, this.img.width, this.img.height)
    }

    fun toggleFullscreen(frame: JFrame) {
        frame.dispose()

        frame.isUndecorated = true
        frame.contentPane = this
        frame.extendedState = Frame.MAXIMIZED_BOTH
        frame.bounds = frame.graphicsConfiguration.bounds
        frame.graphicsConfiguration.device.fullScreenWindow = frame
        frame.isVisible = true
    }

    @Synchronized
    private fun onResize(comp: Component) {
        val width = comp.width
        val height = comp.height
        scale = if (!Options.enableBorder) {
            min(width.toFloat() / Display.WIDTH, height.toFloat() / Display.HEIGHT)
        } else {
            min(width.toFloat() / (Display.WIDTH + Border.PX * 2), height.toFloat() / (Display.HEIGHT + Border.PY * 2))
        }
        offsetX = ((width - Display.WIDTH * scale) / 2).toInt()
        offsetY = ((height - Display.HEIGHT * scale) / 2).toInt()

        lock.withLock {
            repaintCondition.signalAll()
        }
    }

    @Synchronized
    override fun frameReady(buffer: IntArray) {
        this.img.setRGB(0, 0, Display.WIDTH, Display.HEIGHT, buffer, 0, Display.WIDTH)
        lock.withLock {
            repaintCondition.signalAll()
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        val g2d = g.create() as Graphics2D

        if (Options.enableBorder) {
            val borderOffsetX = round(offsetX - Border.PX * scale)
            val borderOffsetY = round(offsetY - Border.PY * scale)
            this.border.draw(g2d, borderOffsetX.toInt(), borderOffsetY.toInt(), scale)
        }

        g2d.drawImage(img, offsetX, offsetY, (Display.WIDTH * scale).toInt(), (Display.HEIGHT * scale).toInt(), null)
        g2d.dispose()
    }

    override fun run() {
        while (true) {
            lock.withLock {
                repaintCondition.await()
            }

            this.validate()
            this.repaint()
        }
    }

}