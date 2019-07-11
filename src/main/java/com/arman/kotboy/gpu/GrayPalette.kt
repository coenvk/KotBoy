package com.arman.kotboy.gpu

class GrayPalette : ColorPalette() {

    companion object {

        private const val WHITE = 0xFFFFFF
        private const val LIGHT_GRAY = 0xAAAAAA
        private const val DARK_GRAY = 0x555555
        private const val BLACK = 0x000000

        operator fun get(i: Int): Int {
            return when (i) {
                1 -> LIGHT_GRAY
                2 -> DARK_GRAY
                3 -> BLACK
                else -> WHITE
            }
        }

    }

}