package com.arman.kotboy.cpu

import com.arman.kotboy.cpu.util.*

object Regs {

    var A: Int = 0
    var B: Int = 0
    var C: Int = 0
    var D: Int = 0
    var E: Int = 0
    var H: Int = 0
    var L: Int = 0

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

    private var flags: BitMask = BitMask(0)

    var AF: Int
        get() = (A shl 8) or flags
        set(value) {
            A = value.msb()
            flags = flags and 0xf0
        }

    var BC: Int
        get() = (B shl 8) or C
        set(value) {
            B = value.msb()
            C = value.lsb()
        }

    var DE: Int
        get() = (D shl 8) or E
        set(value) {
            D = value.msb()
            E = value.lsb()
        }

    var HL: Int
        get() = (H shl 8) or L
        set(value) {
            H = value.msb()
            L = value.lsb()
        }

}