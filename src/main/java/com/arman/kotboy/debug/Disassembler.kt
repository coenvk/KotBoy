package com.arman.kotboy.debug

import com.arman.kotboy.RomReader
import com.arman.kotboy.cpu.ExtOpCode
import com.arman.kotboy.cpu.Op
import com.arman.kotboy.cpu.OpCode
import com.arman.kotboy.cpu.util.hexString
import com.arman.kotboy.memory.cartridge.Cartridge

class Disassembler {

    private val cart: Cartridge = Cartridge(*RomReader("dr-mario.gb").read())

    private var PC: Int = 0

    fun disassemble(): String {
        var out = ""
        while (PC < this.cart.size()) {
            val line = PC.hexString()

            var opcode = this.cart[PC++]

            val op: Op
            if (opcode == OpCode.PREFIX_CB.opcode) {
                opcode = this.cart[PC++]
                op = Op(ExtOpCode[opcode])
            } else {
                op = Op(OpCode[opcode])
            }

            if (op.argsSize() < 0) continue

            val args = IntArray(op.argsSize())
            for (i in 0 until args.size) {
                args[i] = this.cart[PC++]
            }
            out += "$line - $op\t"
            for (arg in args) out += "${arg.hexString()} "
            out += "\n"
        }
        PC = 0
        return out
    }

}

fun main() {
    val disassembler = Disassembler()
    println(disassembler.disassemble())
}