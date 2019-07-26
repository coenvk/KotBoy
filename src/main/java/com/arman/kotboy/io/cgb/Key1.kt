package com.arman.kotboy.io.cgb

import com.arman.kotboy.cpu.util.at
import com.arman.kotboy.cpu.util.toInt
import com.arman.kotboy.memory.Address

class Key1 : Address(CgbReg.KEY1.address) {

    override fun get(address: Int): Int {
        return super.get(address) or 0x7E
    }

    override fun set(address: Int, value: Int): Boolean {
        return super.set(address, value or 0x7E)
    }

    fun switch(): Boolean {
        val value = this.get().toByte()
        return if (value.at(0)) {
            val newSpeed = (!value.at(7)).toInt()
            this.set(newSpeed shl 7)
            true
        } else false
    }

}