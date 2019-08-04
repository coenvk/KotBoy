package com.arman.kotboy.core.io

class Div : IoDevice(IoReg.DIV.address) {

    private val update: Int = 256

    override fun tick(cycles: Int): Boolean {
        super.tick(cycles)
        var div = super.get(IoReg.DIV.address)
        while (this.cycles >= update) {
            this.cycles -= update
            div++
        }
        super.set(IoReg.DIV.address, div)
        return true
    }

    override fun reset() {
        super.reset()
        super.set(IoReg.DIV.address, 0x00)
    }

    override fun set(address: Int, value: Int): Boolean {
        return super.set(address, 0x00)
    }

    override fun get(address: Int): Int {
        return super.get(address) ushr 8
    }

}