package com.arman.kotboy.io

import com.arman.kotboy.memory.Address

class Div : IoDevice(IoReg.DIV.address) {

    private val update: Int = 256

    override fun tick(cycles: Int): Boolean {
        super.tick(cycles)
        var div = get(IoReg.DIV.address)
        while (this.cycles >= update) {
            this.cycles -= update
            div += 1
            if (div > 0xFF) {
                div = 0
            }
        }
        super.set(IoReg.DIV.address, div)
        return true
    }

    override fun set(address: Address, value: Int): Boolean {
        return super.set(address, 0)
    }

    override fun get(address: Address): Int {
        return super.get(address) ushr 8
    }

}