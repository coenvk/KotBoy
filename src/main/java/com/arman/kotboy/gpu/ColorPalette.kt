package com.arman.kotboy.gpu

import com.arman.kotboy.memory.AddressSpace

open class ColorPalette(offset: Int) : AddressSpace(offset, offset + 1) {

    override fun reset() {
        super.reset()
        for (i in 0 until 8) {
            for (j in 0 until 4) {

            }
        }
    }

}