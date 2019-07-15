package com.arman.kotboy.memory

import com.arman.kotboy.io.IoDevice
import com.arman.kotboy.io.IoReg

class Hdma : IoDevice(IoReg.HDMA1.address, IoReg.HDMA5.address) {

    private var transferring: Boolean = false

    override fun set(address: Int, value: Int): Boolean {
        if (address == IoReg.HDMA5.address) {
            if (transferring) {
                if (value and (1 shl 7) == 0) {
                    transferring = false
                    super.set(address, 0xFF)
                } else {
                    super.set(address, value and 0x7f)
                }
            } else {
                if (value and (1 shl 7) == 0) {

                } else {
                    transferring = true
                    super.set(address, value and 0x7f)
                }
            }
            val transferLen = value and 0x7f
        }
        return super.set(address, value)
    }

}