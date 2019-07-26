package com.arman.kotboy.io

class SerialPort : IoDevice(IoReg.SB.address, IoReg.SC.address) { // TODO: cgb

    private val update: Int = 512

    override fun reset() {
        super.reset()
        super.set(IoReg.SB.address, 0x00)
        super.set(IoReg.SC.address, 0x7E)
    }

    override fun tick(cycles: Int): Boolean {
        super.tick(cycles)
        if (this.cycles >= update) {
            this.cycles -= update
            val sc = super.get(IoReg.SC.address)
            if (sc == 0x81) {
                print(get(IoReg.SB.address).rem(0xFF).toChar())
                super.set(IoReg.SB.address, 0xFF)
                super.set(IoReg.SC.address, 0x01)
                return true
            }
        }
        return false
    }

    override fun get(address: Int): Int {
        if (address == IoReg.SC.address) {
            return super.get(address) or 0b01111110
        }
        return super.get(address)
    }

}