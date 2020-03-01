package com.arman.kotboy.core.cpu

class Instr(val opCode: OpCode, private val command: ((Cpu, IntArray) -> Int)) : Iterable<Instr> {

    var comment: String? = null

    fun argsSize(): Int = this.opCode.argsSize()

    override fun iterator(): Iterator<Instr> = setOf(this).iterator()

    override fun toString(): String {
        return prettryPrint()
    }

    fun prettryPrint(): String {
        var res = "$opCode\t"
        this.comment?.let { res += "\t\t// $comment" }
        return res
    }

    fun execute(cpu: Cpu, vararg args: Int): Int {
        return command(cpu, args)
    }

}