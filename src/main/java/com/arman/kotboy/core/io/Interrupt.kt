package com.arman.kotboy.core.io

import com.arman.kotboy.core.memory.Address

enum class Interrupt(val address: Int, val mask: Int) {

    VBlank(0x0040, 0x01),
    Lcdc(0x0048, 0x02),
    Timer(0x0050, 0x04),
    Serial(0x0058, 0x08),
    Joypad(0x0060, 0x10);

}

class If : Address(IoReg.IF.address) {

    override fun reset() {
        super.set(0xE1)
    }

    override fun set(address: Int, value: Int): Boolean {
        return super.set(address, value or 0xE0)
    }

}

class Ie : Address(IoReg.IE.address)