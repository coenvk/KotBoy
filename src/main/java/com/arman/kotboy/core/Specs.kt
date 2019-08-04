package com.arman.kotboy.core

import java.awt.Dimension

object Specs {

    const val MAIN_RAM = 8 // K Byte
    const val VIDEO_RAM = 8 // K Byte

    const val SCREEN_SIZE = 2.6 // inch
    @JvmField
    val RESOLUTION = Dimension(160, 144)

    const val MAX_SPRITES = 40
    const val MAX_SPRITES_PER_LINE = 10

    @JvmField
    val MAX_SPRITE_SIZE = Dimension(8, 16)

    @JvmField
    val MIN_SPRITE_SIZE = Dimension(8, 8)

    const val MACHINE_SPEED = 1.05 // MHz
    const val CLOCK_SPEED = 4.194304 // MHz

    const val HORIZ_SYNC = 9198 // KHz
    const val VERT_SYNC = 59.73 // Hz

    const val SOUND_CHANNELS = 4 // stereo

    const val POWER = 0.7 // W

}