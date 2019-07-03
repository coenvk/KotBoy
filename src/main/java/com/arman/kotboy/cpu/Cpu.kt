package com.arman.kotboy.cpu

import com.arman.kotboy.KotBoy
import com.arman.kotboy.cpu.util.*
import com.arman.kotboy.io.IoReg
import com.arman.kotboy.memory.MemoryMap
import com.arman.kotboy.memory.Mmu
import kotlin.experimental.inv
import kotlin.system.exitProcess

@Suppress("FunctionName", "LocalVariableName", "PropertyName")
class Cpu(private val gb: KotBoy) {

    val mmu: Mmu
        get() {
            return this.gb.mmu
        }
    val alu: Alu = Alu()
    val processor = Processor()
    var cycle: Long = 0
        private set
    var halted: Boolean = false
        private set
    var haltBug: Boolean = false
        private set
    var ime: Boolean = false
        private set

    var SP: Int = 0
    var PC: Int = 0

    /*
    |-----------------------------|
    |      15...8         7...0   |
    |-----------------------------|
    | A (accumulator)   F (flags) |
    | B                 C         |
    | D                 E         |
    | H                 L         |
    |-----------------------------|

    |-----------------------------|
    |           15...0            |
    |-----------------------------|
    | SP (stack pointer)          |
    | PC (program counter)        |
    |-----------------------------|
    */
    var regs: ByteArray = ByteArray(8)

    fun reset() {
        this.write(Reg16.AF, 0x01B0)
        this.write(Reg16.BC, 0x0013)
        this.write(Reg16.DE, 0x00D8)
        this.write(Reg16.HL, 0x014D)
        this.SP = 0xFFFE
        this.PC = 0x0100
        this.ime = true
    }

    fun interrupt(interrupt: MemoryMap) {
        val mask = when (interrupt) {
            MemoryMap.V_BLANK_INTERRUPT -> 0x01
            MemoryMap.LCDC_STATUS_INTERRUPT -> 0x02
            MemoryMap.TIMER_OVERFLOW_INTERRUPT -> 0x04
            MemoryMap.SERIAL_TRANSFER_COMPLETION_INTERRUPT -> 0x08
            MemoryMap.HI_LO_P10_P13_INTERRUPT -> 0x10
            else -> return
        }
        this.mmu[IoReg.IF.address] = this.mmu[IoReg.IF.address] or mask

        this.halted = false
        this.gb.stopped = false
    }

    fun read(reg: Reg8): Byte {
        return regs[reg.ordinal]
    }

    fun read(reg: Reg16): Int {
        val r1 = read(reg.r1).toUnsignedInt()
        val r2 = read(reg.r2).toUnsignedInt()
        return ((r1 shl 8) or r2)
    }

    fun write(reg: Reg8, arg: Byte) {
        var value = arg
        if (reg == Reg8.F) {
            value = (arg.toUnsignedInt() and 0xF0).toByte()
        }
        regs[reg.ordinal] = value
    }

    fun write(reg: Reg8, arg: Int) {
        var value = arg.toUnsignedInt()
        if (reg == Reg8.F) value = arg.toUnsignedInt() and 0xF0
        regs[reg.ordinal] = value.toByte()
    }

    fun write(reg: Reg16, arg: Int) {
        write(reg.r1, arg.msb().toByte())
        write(reg.r2, arg.lsb().toByte())
    }

    fun setFlag(flag: Flag, set: Boolean = true) {
        var f = read(Reg8.F).toUnsignedInt()
        f = if (set) (f set flag) else (f reset flag)
        write(Reg8.F, f.toByte())
    }

    fun hasFlag(flag: Flag): Boolean {
        val f = read(Reg8.F).toUnsignedInt()
        return f hasFlag flag
    }

    fun hasNext(): Boolean {
        return this.mmu[PC] >= 0
    }

    fun next(): Int {
        if (this.halted) {
            return OpCode.HALT_76.cycles
        }

        val line = PC.hexString()

        var opcode = this.mmu[PC++]

        val op: Op
        if (opcode == OpCode.PREFIX_CB.opcode) {
            opcode = this.mmu[PC++]
            op = Op(ExtOpCode[opcode])
        } else {
            op = Op(OpCode[opcode])
        }

//        if (!haltBug) PC++
//        else haltBug = false

//        if (op.argsSize() < 0) return 0

        val args = IntArray(op.argsSize())
        for (i in 0 until args.size) {
            args[i] = this.mmu[PC++]
        }

//        var out = "$line - $op\t"
//        for (arg in args) {
//            out += "${arg.hexString()} "
//        }
//        println(out)

        processor.run(op.opCode, this, mmu, *args)

        handleInterrupt()

        return op.opCode.cycles()
    }

    private fun handleInterrupt(): Boolean {
        if (!ime) {
            return false
        }

        var _if = this.mmu[IoReg.IF.address]
        val ie = this.mmu[IoReg.IE.address]

        val ieif = ie and _if

        if (ieif and 0x1F != 0) {
            this.ime = false
            this.push_n(PC)

            val handler = when {
                (ieif and 0x01) != 0 -> {
                    _if = _if and 0x01.inv()
                    MemoryMap.V_BLANK_INTERRUPT.startAddress
                }
                (ieif and 0x02) != 0 -> {
                    _if = _if and 0x02.inv()
                    MemoryMap.LCDC_STATUS_INTERRUPT.startAddress
                }
                (ieif and 0x04) != 0 -> {
                    _if = _if and 0x04.inv()
                    MemoryMap.TIMER_OVERFLOW_INTERRUPT.startAddress
                }
                (ieif and 0x08) != 0 -> {
                    _if = _if and 0x08.inv()
                    MemoryMap.SERIAL_TRANSFER_COMPLETION_INTERRUPT.startAddress
                }
                else -> {
                    _if = _if and 0x10.inv()
                    MemoryMap.HI_LO_P10_P13_INTERRUPT.startAddress
                }
            }
            this.mmu[IoReg.IF.address] = _if
            this.PC = handler
            return true
        }

        return false
    }

    fun tick(): Int {
        val cycles = next()
        this.cycle += cycles
        return cycles
    }

    fun swap_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, swap_n(arg))
    }

    fun swap_n(arg: Byte) = swap_n(arg.toUnsignedInt())

    fun swap_n(arg: Int): Byte {
        val upper = arg and 0xF0
        val lower = arg and 0x0F
        val res = (lower shl 4) or (upper shr 4)
        setFlag(Flag.Z, res.lsb() == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, false)
        return res.toByte()
    }

    fun daa() {
        val A = read(Reg8.A)
        var res = A.toUnsignedInt()
        if (!hasFlag(Flag.N)) {
            if (hasFlag(Flag.H) || (res and 0x0F) > 9) res += 0x06
            if (hasFlag(Flag.C) || res > 0x9F) res += 0x60
        } else {
            if (hasFlag(Flag.H)) res = (res - 6).toUnsignedInt()
            if (hasFlag(Flag.C)) res -= 0x60
        }
        setFlag(Flag.H, false)
        if ((res and 0x100) != 0) {
            setFlag(Flag.C)
        }
        if (res.toUnsignedInt() == 0) setFlag(Flag.Z)
        print(" " + res.hexString())
        write(Reg8.A, res.toByte())
    }

    fun cpl() {
        val A = read(Reg8.A)
        val res = A.inv()
        setFlag(Flag.N)
        setFlag(Flag.H)
        write(Reg8.A, res)
    }

    fun ccf() {
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, !hasFlag(Flag.C))
    }

    fun scf() {
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C)
    }

    fun nop() = Unit

    fun halt() {
        val _if = this.mmu[IoReg.IF.address]
        val ie = this.mmu[IoReg.IE.address]

        this.halted = this.ime || (ie and _if and 0x1F) == 0
        this.haltBug = !this.halted
    }

    fun stop() {
        this.gb.stopped = true
    }

    fun di() {
        this.ime = false
    }

    fun ei() {
        this.ime = true
    }

    fun rlca() {
        val A = read(Reg8.A)
        write(Reg8.A, rlc_n(A))
    }

    fun rla() {
        val A = read(Reg8.A)
        write(Reg8.A, rl_n(A))
    }

    fun rrca() {
        val A = read(Reg8.A)
        write(Reg8.A, rrc_n(A))
    }

    fun rra() {
        val A = read(Reg8.A)
        write(Reg8.A, rr_n(A))
    }

    fun rlc_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, rlc_n(arg))
    }

    fun rlc_n(arg: Byte): Byte {
        val res = arg.rl().toUnsignedInt()
        setFlag(Flag.Z, res.lsb() == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(7))
        return res.toByte()
    }

    fun rl_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, rl_n(arg))
    }

    fun rl_n(arg: Byte): Byte {
        val res = arg.rl().set(0, hasFlag(Flag.C)).toUnsignedInt()
        setFlag(Flag.Z, res and 0xFF == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(7))
        return res.toByte()
    }

    fun rrc_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, rrc_n(arg))
    }

    fun rrc_n(arg: Byte): Byte {
        val res = arg.rr().toUnsignedInt()
        setFlag(Flag.Z, res and 0xFF == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(0))
        return res.toByte()
    }

    fun rr_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, rr_n(arg))
    }

    fun rr_n(arg: Byte): Byte {
        val res = arg.rr().set(7, hasFlag(Flag.C)).toUnsignedInt()
        setFlag(Flag.Z, res and 0xFF == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(0))
        return res.toByte()
    }

    fun sla_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, sla_n(arg))
    }

    fun sla_n(arg: Byte): Byte {
        val res = (arg shl 1).toUnsignedInt()
        setFlag(Flag.Z, res and 0xFF == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(7))
        return res.toByte()
    }

    fun sra_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, sra_n(arg))
    }

    fun sra_n(arg: Byte): Byte {
        val res = arg.ushr(1).toByte().set(7, arg.at(7)).toUnsignedInt()
        setFlag(Flag.Z, res and 0xFF == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(0))
        return res.toByte()
    }

    fun srl_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, srl_n(arg))
    }

    fun srl_n(arg: Byte): Byte {
        val res = (arg shr 1).toUnsignedInt()
        setFlag(Flag.Z, res and 0xFF == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(0))
        return res.toByte()
    }

    fun bit_b_r(arg1: Byte, arg2: Byte) {
        val bit = arg2.at(arg1.toUnsignedInt())
        setFlag(Flag.Z, !bit)
        setFlag(Flag.N, false)
        setFlag(Flag.H)
    }

    fun set_b_r(arg: Byte, reg: Reg8) {
        write(reg, set_b_r(arg, read(reg)))
    }

    fun set_b_r(arg1: Byte, arg2: Byte): Byte {
        return arg2.set(arg1.toUnsignedInt())
    }

    fun res_b_r(arg: Byte, reg: Reg8) {
        write(reg, res_b_r(arg, read(reg)))
    }

    fun res_b_r(arg1: Byte, arg2: Byte): Byte {
        return arg2.set(arg1.toUnsignedInt(), false)
    }

    fun jp_n(arg: Int) {
        PC = arg
    }

    fun jp_c_n(arg1: Condition, arg2: Int) {
        when (arg1) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) PC = arg2
            Condition.Z -> if (hasFlag(Flag.Z)) PC = arg2
            Condition.NC -> if (!hasFlag(Flag.C)) PC = arg2
            Condition.C -> if (hasFlag(Flag.C)) PC = arg2
        }
    }

    fun jp__hl_() {
        PC = read(Reg16.HL)
    }

    fun jr_n(arg: Byte) {
        PC += arg
    }

    fun jr_c_n(arg1: Condition, arg2: Byte) {
        when (arg1) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) PC += arg2
            Condition.Z -> if (hasFlag(Flag.Z)) PC += arg2
            Condition.NC -> if (!hasFlag(Flag.C)) PC += arg2
            Condition.C -> if (hasFlag(Flag.C)) PC += arg2
        }
    }

    fun call_n(arg: Int) {
        push_n(PC.toWord())
        PC = arg
    }

    fun call_c_n(arg1: Condition, arg2: Int) {
        when (arg1) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) call_n(arg2)
            Condition.Z -> if (hasFlag(Flag.Z)) call_n(arg2)
            Condition.NC -> if (!hasFlag(Flag.C)) call_n(arg2)
            Condition.C -> if (hasFlag(Flag.C)) call_n(arg2)
        }
    }

    fun push_n(arg: Int) {
        SP -= 1
        this.mmu.set(SP, arg.msb())
        SP -= 1
        this.mmu.set(SP, arg.lsb())
    }

    fun pop_n(): Int {
        val lsb = this.mmu.get(SP)
        SP += 1
        val msb = this.mmu.get(SP)
        SP += 1
        return toWord(msb, lsb)
    }

    fun rst_n(arg: Byte) {
        push_n(PC)
        PC = arg.toUnsignedInt()
    }

    fun ret() {
        PC = pop_n()
    }

    fun ret_c(arg: Condition) {
        when (arg) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) ret()
            Condition.Z -> if (hasFlag(Flag.Z)) ret()
            Condition.NC -> if (!hasFlag(Flag.C)) ret()
            Condition.C -> if (hasFlag(Flag.C)) ret()
        }
    }

    fun reti() {
        ret()
        this.ime = true
    }

    inner class Alu {

        fun and(reg: Reg8) = and(read(reg).toUnsignedInt())

        fun and(arg: Byte) = and(arg.toUnsignedInt())

        fun and(arg: Int) {
            val A = read(Reg8.A)
            val res = A and arg.toUnsignedInt()
            setFlag(Flag.Z, res == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, true)
            setFlag(Flag.C, false)
            write(Reg8.A, res.toByte())
        }

        fun or(reg: Reg8) = or(read(reg).toUnsignedInt())

        fun or(arg: Byte) = or(arg.toUnsignedInt())

        fun or(arg: Int) {
            val A = read(Reg8.A)
            val res = A or arg.toUnsignedInt()
            setFlag(Flag.Z, res == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, false)
            setFlag(Flag.C, false)
            write(Reg8.A, res.toByte())
        }

        fun xor(reg: Reg8) = xor(read(reg).toUnsignedInt())

        fun xor(arg: Byte) = xor(arg.toUnsignedInt())

        fun xor(arg: Int) {
            val A = read(Reg8.A)
            val res = A xor arg.toUnsignedInt()
            setFlag(Flag.Z, res == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, false)
            setFlag(Flag.C, false)
            write(Reg8.A, res.toByte())
        }

        fun inc_n(reg: Reg8) {
            val arg = read(reg)
            val uarg = arg.toUnsignedInt()
            val res = uarg + 1
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, (uarg and 0x0F) == 0x0F)
            write(reg, res.toByte())
        }

        fun inc_n(reg: Reg16) {
            val arg = read(reg)
            val res = arg + 1
            write(reg, res.toWord())
        }

        fun inc_n(arg: Byte): Byte {
            val res = arg + 1
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, (arg and 0x0F) == 0)
            return res.toByte()
        }

        fun inc_n(arg: Int): Int {
            return arg + 1
        }

        fun dec_n(reg: Reg8) {
            val arg = read(reg)
            val uarg = arg.toUnsignedInt()
            val res = uarg - 1
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, (uarg and 0x0F) == 0)
            write(reg, res.toByte())
        }

        fun dec_n(reg: Reg16) {
            val arg = read(reg)
            val res = arg - 1
            write(reg, res.toWord())
        }

        fun dec_n(arg: Byte): Byte {
            val res = arg - 1
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, (arg and 0x0F) == 0)
            return res.toByte()
        }

        fun dec_n(arg: Int): Int {
            return arg - 1
        }

        fun add_A_n(reg: Reg8) = add_A_n(read(reg).toUnsignedInt())

        fun add_A_n(arg: Byte) {
            this.add_A_n(arg.toUnsignedInt())
        }

        fun add_A_n(arg: Int) {
            val A = read(Reg8.A)
            val res = A + arg.toUnsignedInt()
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, ((A and 0x0F) + (arg and 0x0F)) and 0x10 != 0)
            setFlag(Flag.H, ((A and 0xFF) + (arg and 0xFF)) and 0x100 != 0)
            write(Reg8.A, res.toByte())
        }

        fun adc_A_n(reg: Reg8) = adc_A_n(read(reg).toUnsignedInt())

        fun adc_A_n(arg: Byte) {
            this.adc_A_n(arg.toUnsignedInt())
        }

        fun adc_A_n(arg: Int) {
            val carry = hasFlag(Flag.C).toInt()
            val A = read(Reg8.A)
            val res = A + arg.toUnsignedInt() + carry
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, ((A and 0x0F) + (arg and 0x0F) + carry) and 0x10 != 0)
            setFlag(Flag.C, ((A and 0xFF) + (arg and 0xFF) + carry) and 0x100 != 0)
            write(Reg8.A, res.toByte())
        }

        fun sub_n(reg: Reg8) = sub_n(read(reg).toUnsignedInt())

        fun sub_n(arg: Byte) {
            this.sub_n(arg.toUnsignedInt())
        }

        fun sub_n(arg: Int) {
            write(Reg8.A, cp_n(arg).toByte())
        }

        fun sbc_A_n(reg: Reg8) = sbc_A_n(read(reg).toUnsignedInt())

        fun sbc_A_n(arg: Byte) {
            this.sbc_A_n(arg.toUnsignedInt())
        }

        fun sbc_A_n(arg: Int) {
            val carry = hasFlag(Flag.C).toInt()
            val A = read(Reg8.A)
            val res = A - arg.toUnsignedInt() - carry
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, ((A and 0x0F) - (arg and 0x0F) - carry) and 0x10 != 0)
            setFlag(Flag.C, ((A and 0xFF) - (arg and 0xFF) - carry) and 0x100 != 0)
            return write(Reg8.A, res.toByte())
        }

        fun cp_n(reg: Reg8): Int = cp_n(read(reg).toUnsignedInt())

        fun cp_n(arg: Byte): Int = cp_n(arg.toUnsignedInt())

        fun cp_n(arg: Int): Int {
            val A = read(Reg8.A)
            val res = A - arg.toUnsignedInt()
            setFlag(Flag.Z, res and 0xFF == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, ((A and 0x0F) - (arg and 0x0F)) and 0x10 != 0)
            setFlag(Flag.C, ((A and 0xFF) - (arg and 0xFF)) and 0x100 != 0)
            return res
        }

        fun add_HL_n(reg: Reg16) = add_HL_n(read(reg))

        fun add_HL_n(arg: Int) {
            val HL = read(Reg16.HL)
            val res = HL + arg
            setFlag(Flag.N, false)
            setFlag(Flag.H, ((HL and 0x0FFF) + (arg and 0x0FFF)) and 0x0100 != 0)
            setFlag(Flag.C, ((HL and 0xFFFF) + (arg and 0xFFFF)) and 0x1000 != 0)
            write(Reg16.HL, res.toWord())
        }

        fun add_SP_n(arg: Int): Int {
            val res = SP + arg.toUnsignedInt()
            setFlag(Flag.Z, false)
            setFlag(Flag.N, false)
            setFlag(Flag.H, ((SP and 0x0F) + (arg and 0x0F)) and 0x10 != 0)
            setFlag(Flag.C, ((SP and 0xFF) + (arg and 0xFF)) and 0x100 != 0)
            return res.toWord()
        }

    }

}