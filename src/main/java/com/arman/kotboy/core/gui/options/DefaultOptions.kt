package com.arman.kotboy.core.gui.options

import com.arman.kotboy.consoles.gb.gpu.DmgScheme
import java.awt.event.KeyEvent

object DefaultOptions {

    const val fullscreen = false

    const val scale = 1

    const val enableBootstrap = false

    const val enableBorder = false

    const val emulatedSystem = 0

    @JvmField
    val palette0 = DmgScheme.GREY.colors[0]

    @JvmField
    val palette1 = DmgScheme.GREY.colors[1]

    @JvmField
    val palette2 = DmgScheme.GREY.colors[2]

    @JvmField
    val palette3 = DmgScheme.GREY.colors[3]

    const val keyLeft = KeyEvent.VK_LEFT

    const val keyRight = KeyEvent.VK_RIGHT

    const val keyUp = KeyEvent.VK_UP

    const val keyDown = KeyEvent.VK_DOWN

    const val keyA = KeyEvent.VK_S

    const val keyB = KeyEvent.VK_A

    const val keyStart = KeyEvent.VK_ENTER

    const val keySelect = KeyEvent.VK_SHIFT

}