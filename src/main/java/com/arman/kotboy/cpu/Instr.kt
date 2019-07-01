package com.arman.kotboy.cpu

data class Instr(val type: Type, val args: Array<Argument>) {

    enum class Type {

        LD,
        LDH,
        POP,
        PUSH,
        ADD,
        ADC,
        SUB,
        SBC,
        AND,
        OR,
        XOR,
        CP,
        INC,
        DEC,
        SWAP,
        DAA,
        CPL,
        CCF,
        SCF,
        NOP,
        HALT,
        STOP,
        DI,
        EI,
        RLCA,
        RLA,
        RRCA,
        RRA,
        RLC,
        RL,
        RRC,
        RR,
        SLA,
        SRA,
        SRL,
        BIT,
        SET,
        RES,
        JP,
        JR,
        CALL,
        RST,
        RET,
        RETI,
        PREFIX;

    }

    override fun toString(): String {
        val sb = StringBuilder("${type.name} ")
        sb.append(this.args[0].label)
        for (i in 1 until this.args.size) {
            sb.append(",${this.args[i].label}")
        }
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Instr

        if (type != other.type) return false
        if (!args.contentEquals(other.args)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + args.contentHashCode()
        return result
    }

}