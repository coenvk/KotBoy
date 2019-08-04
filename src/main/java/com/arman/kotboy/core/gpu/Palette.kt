package com.arman.kotboy.core.gpu

import com.arman.kotboy.core.memory.AddressSpace

abstract class Palette(startAddress: Int, endAddress: Int) : AddressSpace(startAddress, endAddress) {

    abstract fun bgp(color: Int, index: Int): Int
    abstract fun obp(color: Int, index: Int): Int

    open fun bgp(color: Int): Int = bgp(color, 0)
    open fun obp(color: Int): Int = obp(color, 0)

}