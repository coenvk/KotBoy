package com.arman.kotboy.consoles.cgb.io

import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toInt
import com.arman.kotboy.core.io.IoDevice

class Key1 : IoDevice(CgbIoReg.KEY1.address) {

    var key1: Int
        get() {
            return super.get(CgbIoReg.KEY1.address)
        }
        set(value) {
            super.set(value, CgbIoReg.KEY1.address)
        }

    override fun get(address: Int): Int {
        return super.get(address) or 0x7E
    }

    override fun set(address: Int, value: Int): Boolean {
        return super.set(address, value and 0x01)
    }

    fun switch(): Boolean {
        val value = this.key1.toByte()
        return if (value.at(0)) {
            val newSpeed = (!value.at(7)).toInt()
            this.key1 = newSpeed shl 7
            true
        } else false
    }

    fun multiplier(): Int {
        return this.key1.at(7).toInt() + 1
    }

}