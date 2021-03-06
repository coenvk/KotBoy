package com.arman.kotboy.core.debug

import com.arman.kotboy.core.cpu.Instr
import com.arman.kotboy.core.cpu.OpCode
import com.arman.kotboy.core.cpu.util.hexString
import com.arman.kotboy.core.memory.cartridge.Cartridge

class Disassembler {

    private val cart: Cartridge =
            Cartridge("C:\\Users\\Coen\\IdeaProjects\\KotBoy\\src\\test\\resources\\roms\\animaniacs.gb")

    private var PC: Int = 0

    fun disassemble(): String {
        var out = ""
        while (PC < this.cart.size()) {
            val line = PC.hexString()

            var opcode = this.cart[PC++]

            if (opcode == OpCode.PREFIX_CB.opcode) {
                opcode = this.cart[PC++]
            }

            val op = OpCode[opcode]

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