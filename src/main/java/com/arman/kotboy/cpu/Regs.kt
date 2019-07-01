package com.arman.kotboy.cpu

import com.arman.kotboy.cpu.util.lsb
import com.arman.kotboy.cpu.util.msb
import com.arman.kotboy.cpu.util.toUnsignedInt

object Regs {

    var A: Byte = 0
    var F: Byte = 0

    var B: Byte = 0
    var C: Byte = 0

    var D: Byte = 0
    var E: Byte = 0

    var H: Byte = 0
    var L: Byte = 0

    var SP: Int = 0
    var PC: Int = 0

    fun incPC() {
        PC = (PC + 1) and 0xFFFF
    }

    fun decSP() {
        SP = (SP - 1) and 0xFFFF
    }

    fun incSP() {
        SP = (SP + 1) and 0xFFFF
    }

    private fun readReg16(r1: Byte, r2: Byte): Int {
        return (r1.toUnsignedInt() shl 8) or r2.toUnsignedInt()
    }

    var AF: Int
        get() = readReg16(A, F)
        set(value) {
            A = value.msb().toByte()
            F = (value.toUnsignedInt() and 0xF0).toByte()
        }

    var BC: Int
        get() = readReg16(B, C)
        set(value) {
            B = value.msb().toByte()
            C = value.lsb().toByte()
        }

    var DE: Int
        get() = readReg16(D, E)
        set(value) {
            D = value.msb().toByte()
            E = value.lsb().toByte()
        }

    var HL: Int
        get() = readReg16(H, L)
        set(value) {
            H = value.msb().toByte()
            L = value.lsb().toByte()
        }

}