package com.arman.kotboy.cpu

import com.arman.kotboy.GameBoy
import com.arman.kotboy.cpu.util.*
import com.arman.kotboy.debug.Log
import com.arman.kotboy.io.Interrupt
import com.arman.kotboy.io.IoReg
import com.arman.kotboy.memory.Mmu
import kotlin.experimental.inv

@Suppress("FunctionName", "LocalVariableName", "PropertyName")
class Cpu(private val gb: GameBoy) {

    companion object {
        const val FRAME_RATE = 60
        const val DMG_CLOCK_SPEED = 4194304
        const val SGB_CLOCK_SPEED = 4295454
        const val CGB_DOUBLE_CLOCK_SPEED = 8400000
    }

    val mmu: Mmu
        get() = this.gb.mmu

    val alu: Alu = Alu()
    var cycle: Long = 0
        private set
    var halted: Boolean = false
        private set
    var haltBug: Boolean = false
        private set
    var ime: Boolean = false
        private set
    var eiExecuted: Boolean = false
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

    fun interrupt(interrupt: Interrupt) {
        this.mmu[IoReg.IF.address] = this.mmu[IoReg.IF.address] or interrupt.mask
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
        handleInterrupt()

        if (this.halted) {
            return OpCode.HALT_76.cycles
        }

        val line = PC.hexString(4)

        var opcode = this.mmu[PC++]

        val op: OpCode
        if (opcode == OpCode.PREFIX_CB.opcode) {
            opcode = this.mmu[PC++]
            op = OpCode[opcode, true]
        } else {
            op = OpCode[opcode]
        }

        if (op.argsSize() < 0) return 0

        val args = IntArray(op.argsSize())
        for (i in 0 until args.size) {
            args[i] = this.mmu[PC++]
        }
        val instr = Instr(op, *args)

        Log.d("$line - $instr")

        val cycles = instr.execute(this)

        if (op != OpCode.EI_FB && eiExecuted) {
            this.ime = true
            this.eiExecuted = false
        }

        return cycles
    }

    private fun handleInterrupt(): Boolean {
        var _if = this.mmu[IoReg.IF.address]
        val ie = this.mmu[IoReg.IE.address]

        val ieif = ie and _if

        if (ieif and 0x1F != 0) {
            this.halted = false
            this.gb.stopped = false

            if (!ime) {
                return false
            }

            this.ime = false

            this.push_n(PC)
            this.cycle += 16

            val handler = when {
                (ieif and 0x01) != 0 -> {
                    _if = _if and 0x01.inv()
                    Interrupt.VBlank.address
                }
                (ieif and 0x02) != 0 -> {
                    _if = _if and 0x02.inv()
                    Interrupt.Lcdc.address
                }
                (ieif and 0x04) != 0 -> {
                    _if = _if and 0x04.inv()
                    Interrupt.Timer.address
                }
                (ieif and 0x08) != 0 -> {
                    _if = _if and 0x08.inv()
                    Interrupt.Serial.address
                }
                else -> {
                    _if = _if and 0x10.inv()
                    Interrupt.Joypad.address
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
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, false)
        return res.toByte()
    }

    fun daa() {
        val A = read(Reg8.A)
        var res = A.toUnsignedInt()

        if (hasFlag(Flag.N)) {
            var cor = 0
            if (hasFlag(Flag.H)) cor = cor or 0x06
            if (hasFlag(Flag.C)) cor = cor or 0x60
            res -= cor
        } else {
            var cor = 0
            if (hasFlag(Flag.H) || (res and 0x0F) > 0x09) cor = cor or 0x06
            if (hasFlag(Flag.C) || (res and 0xFF) > 0x99) cor = cor or 0x60
            res += cor
        }

        if (res > 0xFF) {
            setFlag(Flag.C)
        }

        setFlag(Flag.H, false)
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
        write(Reg8.A, res.toByte())
    }

    fun cpl() {
        val A = read(Reg8.A)
        val res = A.inv()
        setFlag(Flag.N)
        setFlag(Flag.H)
        write(Reg8.A, res and 0xFF)
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
        this.eiExecuted = false
    }

    fun ei() {
        this.eiExecuted = true
    }

    fun rlca() {
        rlc_n(Reg8.A)
        setFlag(Flag.Z, false)
    }

    fun rla() {
        rl_n(Reg8.A)
        setFlag(Flag.Z, false)
    }

    fun rrca() {
        rrc_n(Reg8.A)
        setFlag(Flag.Z, false)
    }

    fun rra() {
        rr_n(Reg8.A)
        setFlag(Flag.Z, false)
    }

    fun rlc_n(reg: Reg8) {
        val arg = read(reg)
        write(reg, rlc_n(arg))
    }

    fun rlc_n(arg: Byte): Byte {
        val res = arg.rl().toUnsignedInt()
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
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
        var res = arg.shl(1).toUnsignedInt()
        res = res or hasFlag(Flag.C).toInt()
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
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
        var res = arg.shr(1).toUnsignedInt()
        res = res or (if (hasFlag(Flag.C)) (1 shl 7) else 0)
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
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
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
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
        val res = arg.shr(1) or (arg and (1 shl 7))
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
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
        setFlag(Flag.Z, res.toUnsignedInt() == 0)
        setFlag(Flag.N, false)
        setFlag(Flag.H, false)
        setFlag(Flag.C, arg.at(0))
        return res.toByte()
    }

    fun bit_b_r(arg1: Int, arg2: Byte) {
        val bit = arg2.at(arg1)
        setFlag(Flag.N, false)
        setFlag(Flag.H)
        if (arg1 < 8) {
            setFlag(Flag.Z, !bit)
        }
    }

    fun set_b_r(arg: Int, reg: Reg8) {
        write(reg, set_b_r(arg, read(reg)))
    }

    fun set_b_r(arg1: Int, arg2: Byte): Byte {
        return arg2.set(arg1)
    }

    fun res_b_r(arg: Int, reg: Reg8) {
        write(reg, res_b_r(arg, read(reg)))
    }

    fun res_b_r(arg1: Int, arg2: Byte): Byte {
        return arg2.set(arg1, false)
    }

    fun jp_n(arg: Int) {
        PC = arg
    }

    fun jp_c_n(arg1: Condition, arg2: Int): Boolean {
        when (arg1) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) {
                jp_n(arg2)
                return true
            }
            Condition.Z -> if (hasFlag(Flag.Z)) {
                jp_n(arg2)
                return true
            }
            Condition.NC -> if (!hasFlag(Flag.C)) {
                jp_n(arg2)
                return true
            }
            Condition.C -> if (hasFlag(Flag.C)) {
                jp_n(arg2)
                return true
            }
        }
        return false
    }

    fun jr_n(arg: Byte) {
        PC += arg
    }

    fun jr_c_n(arg1: Condition, arg2: Byte): Boolean {
        when (arg1) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) {
                jr_n(arg2)
                return true
            }
            Condition.Z -> if (hasFlag(Flag.Z)) {
                jr_n(arg2)
                return true
            }
            Condition.NC -> if (!hasFlag(Flag.C)) {
                jr_n(arg2)
                return true
            }
            Condition.C -> if (hasFlag(Flag.C)) {
                jr_n(arg2)
                return true
            }
        }
        return false
    }

    fun call_n(arg: Int) {
        push_n(PC.toWord())
        PC = arg
    }

    fun call_c_n(arg1: Condition, arg2: Int): Boolean {
        when (arg1) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) {
                call_n(arg2)
                return true
            }
            Condition.Z -> if (hasFlag(Flag.Z)) {
                call_n(arg2)
                return true
            }
            Condition.NC -> if (!hasFlag(Flag.C)) {
                call_n(arg2)
                return true
            }
            Condition.C -> if (hasFlag(Flag.C)) {
                call_n(arg2)
                return true
            }
        }
        return false
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

    fun rst_n(arg: Int) {
        push_n(PC)
        PC = arg
    }

    fun ret() {
        PC = pop_n()
    }

    fun ret_c(arg: Condition): Boolean {
        when (arg) {
            Condition.NZ -> if (!hasFlag(Flag.Z)) {
                ret()
                return true
            }
            Condition.Z -> if (hasFlag(Flag.Z)) {
                ret()
                return true
            }
            Condition.NC -> if (!hasFlag(Flag.C)) {
                ret()
                return true
            }
            Condition.C -> if (hasFlag(Flag.C)) {
                ret()
                return true
            }
        }
        return false
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
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
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
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
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
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, false)
            setFlag(Flag.C, false)
            write(Reg8.A, res.toByte())
        }

        fun inc_n(reg: Reg8) {
            val arg = read(reg)
            write(reg, inc_n(arg.toUnsignedInt()).toByte())
        }

        fun inc_n(reg: Reg16) {
            val arg = read(reg)
            val res = arg + 1
            write(reg, res.toWord())
        }

        fun inc_n(arg: Int): Int {
            val res = (arg + 1).toUnsignedInt()
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, (0x0F and res) < (0x0F and arg))
            return res
        }

        fun dec_n(reg: Reg8) {
            val arg = read(reg)
            write(reg, dec_n(arg.toUnsignedInt()).toByte())
        }

        fun dec_n(reg: Reg16) {
            val arg = read(reg)
            val res = arg - 1
            write(reg, res.toWord())
        }

        fun dec_n(arg: Int): Int {
            val res = (arg - 1).toUnsignedInt()
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, (0x0F and arg) == 0)
            return res
        }

        fun add_A_n(reg: Reg8) = add_A_n(read(reg).toUnsignedInt())

        fun add_A_n(arg: Byte) {
            this.add_A_n(arg.toUnsignedInt())
        }

        fun add_A_n(arg: Int) {
            val A = read(Reg8.A)
            val res = (A + arg).toUnsignedInt()
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, ((A and 0x0F) + (arg and 0x0F)) > 0x0F)
            setFlag(Flag.C, ((A and 0xFF) + (arg and 0xFF)) > 0xFF)
            write(Reg8.A, res.toByte())
        }

        fun adc_A_n(reg: Reg8) = adc_A_n(read(reg).toUnsignedInt())

        fun adc_A_n(arg: Byte) {
            this.adc_A_n(arg.toUnsignedInt())
        }

        fun adc_A_n(arg: Int) {
            val carry = hasFlag(Flag.C).toInt()
            val A = read(Reg8.A)
            val res = (A + arg + carry).toUnsignedInt()
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
            setFlag(Flag.N, false)
            setFlag(Flag.H, ((A and 0x0F) + (arg and 0x0F) + carry) > 0x0F)
            setFlag(Flag.C, ((A and 0xFF) + (arg and 0xFF) + carry) > 0xFF)
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
            val res = (A - arg - carry).toUnsignedInt()
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, ((A and 0x0F) < ((arg and 0x0F) + carry)))
            setFlag(Flag.C, ((A and 0xFF) < ((arg and 0xFF) + carry)))
            return write(Reg8.A, res.toByte())
        }

        fun cp_n(reg: Reg8): Int = cp_n(read(reg).toUnsignedInt())

        fun cp_n(arg: Byte): Int = cp_n(arg.toUnsignedInt())

        fun cp_n(arg: Int): Int {
            val A = read(Reg8.A)
            val res = (A - arg).toUnsignedInt()
            setFlag(Flag.Z, res.toUnsignedInt() == 0)
            setFlag(Flag.N, true)
            setFlag(Flag.H, ((A and 0x0F) < (arg and 0x0F)))
            setFlag(Flag.C, ((A and 0xFF) < (arg and 0xFF)))
            return res
        }

        fun add_HL_n(reg: Reg16) = add_HL_n(read(reg))

        fun add_HL_n(arg: Int) {
            val HL = read(Reg16.HL)
            val res = HL + arg
            setFlag(Flag.N, false)
            setFlag(Flag.H, ((HL and 0x0FFF) + (arg and 0x0FFF)) > 0x0FFF)
            setFlag(Flag.C, ((HL and 0xFFFF) + (arg and 0xFFFF)) > 0xFFFF)
            write(Reg16.HL, res.toWord())
        }

        fun add_SP_n(arg: Int): Int {
            setFlag(Flag.Z, false)
            setFlag(Flag.N, false)
            val sarg = arg.toByte()
            setFlag(Flag.H, ((SP and 0x0F) + (sarg and 0x0F)) > 0x0F)
            setFlag(Flag.C, ((SP and 0xFF) + (sarg and 0xFF)) > 0xFF)
            return (SP + sarg).toWord()
        }

    }

    // opcode functions
    fun nop_00(vararg args: Int) = nop()

    fun prefix_cb(vararg args: Int) = Unit

    fun ld_06(vararg args: Int) = write(Reg8.B, args[0])
    fun ld_0e(vararg args: Int) = write(Reg8.C, args[0])
    fun ld_16(vararg args: Int) = write(Reg8.D, args[0])
    fun ld_1e(vararg args: Int) = write(Reg8.E, args[0])
    fun ld_26(vararg args: Int) = write(Reg8.H, args[0])
    fun ld_2e(vararg args: Int) = write(Reg8.L, args[0])

    fun ld_40(vararg args: Int) = write(Reg8.B, read(Reg8.B))
    fun ld_41(vararg args: Int) = write(Reg8.B, read(Reg8.C))
    fun ld_42(vararg args: Int) = write(Reg8.B, read(Reg8.D))
    fun ld_43(vararg args: Int) = write(Reg8.B, read(Reg8.E))
    fun ld_44(vararg args: Int) = write(Reg8.B, read(Reg8.H))
    fun ld_45(vararg args: Int) = write(Reg8.B, read(Reg8.L))
    fun ld_46(vararg args: Int) =
        write(Reg8.B, mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun ld_48(vararg args: Int) = write(Reg8.C, read(Reg8.B))
    fun ld_49(vararg args: Int) = write(Reg8.C, read(Reg8.C))
    fun ld_4a(vararg args: Int) = write(Reg8.C, read(Reg8.D))
    fun ld_4b(vararg args: Int) = write(Reg8.C, read(Reg8.E))
    fun ld_4c(vararg args: Int) = write(Reg8.C, read(Reg8.H))
    fun ld_4d(vararg args: Int) = write(Reg8.C, read(Reg8.L))
    fun ld_4e(vararg args: Int) =
        write(Reg8.C, mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun ld_50(vararg args: Int) = write(Reg8.D, read(Reg8.B))
    fun ld_51(vararg args: Int) = write(Reg8.D, read(Reg8.C))
    fun ld_52(vararg args: Int) = write(Reg8.D, read(Reg8.D))
    fun ld_53(vararg args: Int) = write(Reg8.D, read(Reg8.E))
    fun ld_54(vararg args: Int) = write(Reg8.D, read(Reg8.H))
    fun ld_55(vararg args: Int) = write(Reg8.D, read(Reg8.L))
    fun ld_56(vararg args: Int) =
        write(Reg8.D, mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun ld_58(vararg args: Int) = write(Reg8.E, read(Reg8.B))
    fun ld_59(vararg args: Int) = write(Reg8.E, read(Reg8.C))
    fun ld_5a(vararg args: Int) = write(Reg8.E, read(Reg8.D))
    fun ld_5b(vararg args: Int) = write(Reg8.E, read(Reg8.E))
    fun ld_5c(vararg args: Int) = write(Reg8.E, read(Reg8.H))
    fun ld_5d(vararg args: Int) = write(Reg8.E, read(Reg8.L))
    fun ld_5e(vararg args: Int) =
        write(Reg8.E, mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun ld_60(vararg args: Int) = write(Reg8.H, read(Reg8.B))
    fun ld_61(vararg args: Int) = write(Reg8.H, read(Reg8.C))
    fun ld_62(vararg args: Int) = write(Reg8.H, read(Reg8.D))
    fun ld_63(vararg args: Int) = write(Reg8.H, read(Reg8.E))
    fun ld_64(vararg args: Int) = write(Reg8.H, read(Reg8.H))
    fun ld_65(vararg args: Int) = write(Reg8.H, read(Reg8.L))
    fun ld_66(vararg args: Int) =
        write(Reg8.H, mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun ld_68(vararg args: Int) = write(Reg8.L, read(Reg8.B))
    fun ld_69(vararg args: Int) = write(Reg8.L, read(Reg8.C))
    fun ld_6a(vararg args: Int) = write(Reg8.L, read(Reg8.D))
    fun ld_6b(vararg args: Int) = write(Reg8.L, read(Reg8.E))
    fun ld_6c(vararg args: Int) = write(Reg8.L, read(Reg8.H))
    fun ld_6d(vararg args: Int) = write(Reg8.L, read(Reg8.L))
    fun ld_6e(vararg args: Int) =
        write(Reg8.L, mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun ld_70(vararg args: Int) =
        mmu.set(read(Reg16.HL), read(Reg8.B).toUnsignedInt())

    fun ld_71(vararg args: Int) =
        mmu.set(read(Reg16.HL), read(Reg8.C).toUnsignedInt())

    fun ld_72(vararg args: Int) =
        mmu.set(read(Reg16.HL), read(Reg8.D).toUnsignedInt())

    fun ld_73(vararg args: Int) =
        mmu.set(read(Reg16.HL), read(Reg8.E).toUnsignedInt())

    fun ld_74(vararg args: Int) =
        mmu.set(read(Reg16.HL), read(Reg8.H).toUnsignedInt())

    fun ld_75(vararg args: Int) =
        mmu.set(read(Reg16.HL), read(Reg8.L).toUnsignedInt())

    fun ld_36(vararg args: Int) {
        val addr = args[0]
        mmu.set(read(Reg16.HL), addr)
    }

    fun ld_7f(vararg args: Int) = write(Reg8.A, read(Reg8.A))
    fun ld_78(vararg args: Int) = write(Reg8.A, read(Reg8.B))
    fun ld_79(vararg args: Int) = write(Reg8.A, read(Reg8.C))
    fun ld_7a(vararg args: Int) = write(Reg8.A, read(Reg8.D))
    fun ld_7b(vararg args: Int) = write(Reg8.A, read(Reg8.E))
    fun ld_7c(vararg args: Int) = write(Reg8.A, read(Reg8.H))
    fun ld_7d(vararg args: Int) = write(Reg8.A, read(Reg8.L))

    fun ld_0a(vararg args: Int) =
        write(Reg8.A, mmu.get(read(Reg16.BC)).toUnsignedInt())

    fun ld_1a(vararg args: Int) =
        write(Reg8.A, mmu.get(read(Reg16.DE)).toUnsignedInt())

    fun ld_7e(vararg args: Int) =
        write(Reg8.A, mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun ld_fa(vararg args: Int) {
        val addr = args.toWord()
        write(Reg8.A, mmu.get(addr).toUnsignedInt())
    }

    fun ld_3e(vararg args: Int) = write(Reg8.A, args[0])

    fun ld_47(vararg args: Int) = write(Reg8.B, read(Reg8.A))
    fun ld_4f(vararg args: Int) = write(Reg8.C, read(Reg8.A))
    fun ld_57(vararg args: Int) = write(Reg8.D, read(Reg8.A))
    fun ld_5f(vararg args: Int) = write(Reg8.E, read(Reg8.A))
    fun ld_67(vararg args: Int) = write(Reg8.H, read(Reg8.A))
    fun ld_6f(vararg args: Int) = write(Reg8.L, read(Reg8.A))

    fun ld_02(vararg args: Int) =
        mmu.set(read(Reg16.BC), read(Reg8.A).toUnsignedInt())

    fun ld_12(vararg args: Int) =
        mmu.set(read(Reg16.DE), read(Reg8.A).toUnsignedInt())

    fun ld_77(vararg args: Int) =
        mmu.set(read(Reg16.HL), read(Reg8.A).toUnsignedInt())

    fun ld_ea(vararg args: Int) {
        val addr = args.toWord()
        mmu.set(addr, read(Reg8.A).toUnsignedInt())
    }

    fun ld_f2(vararg args: Int) =
        write(Reg8.A, mmu.get(0xFF00 + read(Reg8.C).toUnsignedInt()).toUnsignedInt())

    fun ld_e2(vararg args: Int) =
        mmu.set(0xFF00 + read(Reg8.C).toUnsignedInt(), read(Reg8.A).toUnsignedInt())

    fun ld_3a(vararg args: Int) {
        val hl = read(Reg16.HL)
        write(Reg8.A, mmu.get(hl).toUnsignedInt())
        write(Reg16.HL, hl - 1)
    }

    fun ld_32(vararg args: Int) {
        val hl = read(Reg16.HL)
        mmu.set(hl, read(Reg8.A).toUnsignedInt())
        write(Reg16.HL, hl - 1)
    }

    fun ld_2a(vararg args: Int) {
        val hl = read(Reg16.HL)
        write(Reg8.A, mmu.get(hl).toUnsignedInt())
        write(Reg16.HL, hl + 1)
    }

    fun ld_22(vararg args: Int) {
        val hl = read(Reg16.HL)
        mmu[hl] = read(Reg8.A).toUnsignedInt()
        write(Reg16.HL, hl + 1)
    }

    fun ldh_e0(vararg args: Int) {
        val addr = 0xFF00 + args[0].toUnsignedInt()
        mmu.set(addr, read(Reg8.A).toUnsignedInt())
    }

    fun ldh_f0(vararg args: Int) {
        val addr = 0xFF00 + args[0].toUnsignedInt()
        write(Reg8.A, mmu[addr].toUnsignedInt())
    }

    fun ld_01(vararg args: Int) = write(Reg16.BC, args.toWord())
    fun ld_11(vararg args: Int) = write(Reg16.DE, args.toWord())
    fun ld_21(vararg args: Int) = write(Reg16.HL, args.toWord())

    fun ld_31(vararg args: Int) {
        SP = args.toWord()
    }

    fun ld_f9(vararg args: Int) {
        SP = read(Reg16.HL).toWord()
    }

    fun ld_f8(vararg args: Int) =
        write(Reg16.HL, alu.add_SP_n(args[0]))

    fun ld_08(vararg args: Int) {
        mmu.set(args.toWord(), SP.lsb())
        mmu.set((args.toWord() + 1).toWord(), SP.msb())
    }

    fun push_f5(vararg args: Int) = push_n(read(Reg16.AF))
    fun push_c5(vararg args: Int) = push_n(read(Reg16.BC))
    fun push_d5(vararg args: Int) = push_n(read(Reg16.DE))
    fun push_e5(vararg args: Int) = push_n(read(Reg16.HL))

    fun pop_f1(vararg args: Int) = write(Reg16.AF, pop_n())
    fun pop_c1(vararg args: Int) = write(Reg16.BC, pop_n())
    fun pop_d1(vararg args: Int) = write(Reg16.DE, pop_n())
    fun pop_e1(vararg args: Int) = write(Reg16.HL, pop_n())

    fun add_87(vararg args: Int) = alu.add_A_n(read(Reg8.A))
    fun add_80(vararg args: Int) = alu.add_A_n(read(Reg8.B))
    fun add_81(vararg args: Int) = alu.add_A_n(read(Reg8.C))
    fun add_82(vararg args: Int) = alu.add_A_n(read(Reg8.D))
    fun add_83(vararg args: Int) = alu.add_A_n(read(Reg8.E))
    fun add_84(vararg args: Int) = alu.add_A_n(read(Reg8.H))
    fun add_85(vararg args: Int) = alu.add_A_n(read(Reg8.L))
    fun add_86(vararg args: Int) =
        alu.add_A_n(mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun add_c6(vararg args: Int) = alu.add_A_n(args[0].toUnsignedInt())

    fun adc_8f(vararg args: Int) = alu.adc_A_n(read(Reg8.A))
    fun adc_88(vararg args: Int) = alu.adc_A_n(read(Reg8.B))
    fun adc_89(vararg args: Int) = alu.adc_A_n(read(Reg8.C))
    fun adc_8a(vararg args: Int) = alu.adc_A_n(read(Reg8.D))
    fun adc_8b(vararg args: Int) = alu.adc_A_n(read(Reg8.E))
    fun adc_8c(vararg args: Int) = alu.adc_A_n(read(Reg8.H))
    fun adc_8d(vararg args: Int) = alu.adc_A_n(read(Reg8.L))
    fun adc_8e(vararg args: Int) =
        alu.adc_A_n(mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun adc_ce(vararg args: Int) = alu.adc_A_n(args[0].toUnsignedInt())

    fun sub_97(vararg args: Int) = alu.sub_n(read(Reg8.A))
    fun sub_90(vararg args: Int) = alu.sub_n(read(Reg8.B))
    fun sub_91(vararg args: Int) = alu.sub_n(read(Reg8.C))
    fun sub_92(vararg args: Int) = alu.sub_n(read(Reg8.D))
    fun sub_93(vararg args: Int) = alu.sub_n(read(Reg8.E))
    fun sub_94(vararg args: Int) = alu.sub_n(read(Reg8.H))
    fun sub_95(vararg args: Int) = alu.sub_n(read(Reg8.L))
    fun sub_96(vararg args: Int) =
        alu.sub_n(mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun sub_d6(vararg args: Int) = alu.sub_n(args[0].toUnsignedInt())

    fun sbc_9f(vararg args: Int) = alu.sbc_A_n(read(Reg8.A))
    fun sbc_98(vararg args: Int) = alu.sbc_A_n(read(Reg8.B))
    fun sbc_99(vararg args: Int) = alu.sbc_A_n(read(Reg8.C))
    fun sbc_9a(vararg args: Int) = alu.sbc_A_n(read(Reg8.D))
    fun sbc_9b(vararg args: Int) = alu.sbc_A_n(read(Reg8.E))
    fun sbc_9c(vararg args: Int) = alu.sbc_A_n(read(Reg8.H))
    fun sbc_9d(vararg args: Int) = alu.sbc_A_n(read(Reg8.L))
    fun sbc_9e(vararg args: Int) =
        alu.sbc_A_n(mmu.get(read(Reg16.HL)).toUnsignedInt())

    fun sbc_de(vararg args: Int) = alu.sbc_A_n(args[0].toUnsignedInt())

    fun and_a7(vararg args: Int) = alu.and(read(Reg8.A))
    fun and_a0(vararg args: Int) = alu.and(read(Reg8.B))
    fun and_a1(vararg args: Int) = alu.and(read(Reg8.C))
    fun and_a2(vararg args: Int) = alu.and(read(Reg8.D))
    fun and_a3(vararg args: Int) = alu.and(read(Reg8.E))
    fun and_a4(vararg args: Int) = alu.and(read(Reg8.H))
    fun and_a5(vararg args: Int) = alu.and(read(Reg8.L))
    fun and_a6(vararg args: Int) = alu.and(mmu.get(read(Reg16.HL)).toUnsignedInt())
    fun and_e6(vararg args: Int) = alu.and(args[0].toUnsignedInt())

    fun or_b7(vararg args: Int) = alu.or(read(Reg8.A))
    fun or_b0(vararg args: Int) = alu.or(read(Reg8.B))
    fun or_b1(vararg args: Int) = alu.or(read(Reg8.C))
    fun or_b2(vararg args: Int) = alu.or(read(Reg8.D))
    fun or_b3(vararg args: Int) = alu.or(read(Reg8.E))
    fun or_b4(vararg args: Int) = alu.or(read(Reg8.H))
    fun or_b5(vararg args: Int) = alu.or(read(Reg8.L))
    fun or_b6(vararg args: Int) = alu.or(mmu.get(read(Reg16.HL)).toUnsignedInt())
    fun or_f6(vararg args: Int) = alu.or(args[0].toUnsignedInt())

    fun xor_af(vararg args: Int) = alu.xor(read(Reg8.A))
    fun xor_a8(vararg args: Int) = alu.xor(read(Reg8.B))
    fun xor_a9(vararg args: Int) = alu.xor(read(Reg8.C))
    fun xor_aa(vararg args: Int) = alu.xor(read(Reg8.D))
    fun xor_ab(vararg args: Int) = alu.xor(read(Reg8.E))
    fun xor_ac(vararg args: Int) = alu.xor(read(Reg8.H))
    fun xor_ad(vararg args: Int) = alu.xor(read(Reg8.L))
    fun xor_ae(vararg args: Int) = alu.xor(mmu.get(read(Reg16.HL)).toUnsignedInt())
    fun xor_ee(vararg args: Int) = alu.xor(args[0].toUnsignedInt())

    fun cp_bf(vararg args: Int) = alu.cp_n(read(Reg8.A))
    fun cp_b8(vararg args: Int) = alu.cp_n(read(Reg8.B))
    fun cp_b9(vararg args: Int) = alu.cp_n(read(Reg8.C))
    fun cp_ba(vararg args: Int) = alu.cp_n(read(Reg8.D))
    fun cp_bb(vararg args: Int) = alu.cp_n(read(Reg8.E))
    fun cp_bc(vararg args: Int) = alu.cp_n(read(Reg8.H))
    fun cp_bd(vararg args: Int) = alu.cp_n(read(Reg8.L))
    fun cp_be(vararg args: Int) = alu.cp_n(mmu[read(Reg16.HL)].toUnsignedInt())
    fun cp_fe(vararg args: Int) = alu.cp_n(args[0].toUnsignedInt())

    fun inc_3c(vararg args: Int) = alu.inc_n(Reg8.A)
    fun inc_04(vararg args: Int) = alu.inc_n(Reg8.B)
    fun inc_0c(vararg args: Int) = alu.inc_n(Reg8.C)
    fun inc_14(vararg args: Int) = alu.inc_n(Reg8.D)
    fun inc_1c(vararg args: Int) = alu.inc_n(Reg8.E)
    fun inc_24(vararg args: Int) = alu.inc_n(Reg8.H)
    fun inc_2c(vararg args: Int) = alu.inc_n(Reg8.L)
    fun inc_34(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr)
        mmu.set(addr, alu.inc_n(value))
    }

    fun dec_3d(vararg args: Int) = alu.dec_n(Reg8.A)
    fun dec_05(vararg args: Int) = alu.dec_n(Reg8.B)

    fun dec_0d(vararg args: Int) = alu.dec_n(Reg8.C)
    fun dec_15(vararg args: Int) = alu.dec_n(Reg8.D)
    fun dec_1d(vararg args: Int) = alu.dec_n(Reg8.E)
    fun dec_25(vararg args: Int) = alu.dec_n(Reg8.H)
    fun dec_2d(vararg args: Int) = alu.dec_n(Reg8.L)
    fun dec_35(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr)
        mmu.set(addr, alu.dec_n(value))
    }

    fun add_09(vararg args: Int) = alu.add_HL_n(read(Reg16.BC))
    fun add_19(vararg args: Int) = alu.add_HL_n(read(Reg16.DE))
    fun add_29(vararg args: Int) = alu.add_HL_n(read(Reg16.HL))
    fun add_39(vararg args: Int) = alu.add_HL_n(SP)

    fun add_e8(vararg args: Int) {
        SP = alu.add_SP_n(args[0])
    }

    fun inc_03(vararg args: Int) = alu.inc_n(Reg16.BC)
    fun inc_13(vararg args: Int) = alu.inc_n(Reg16.DE)
    fun inc_23(vararg args: Int) = alu.inc_n(Reg16.HL)

    fun inc_33(vararg args: Int) {
        SP = (SP + 1).toWord()
    }

    fun dec_0b(vararg args: Int) = alu.dec_n(Reg16.BC)
    fun dec_1b(vararg args: Int) = alu.dec_n(Reg16.DE)
    fun dec_2b(vararg args: Int) = alu.dec_n(Reg16.HL)

    fun dec_3b(vararg args: Int) {
        SP = (SP - 1).toWord()
    }

    fun swap_37(vararg args: Int) = swap_n(Reg8.A)
    fun swap_30(vararg args: Int) = swap_n(Reg8.B)
    fun swap_31(vararg args: Int) = swap_n(Reg8.C)
    fun swap_32(vararg args: Int) = swap_n(Reg8.D)
    fun swap_33(vararg args: Int) = swap_n(Reg8.E)
    fun swap_34(vararg args: Int) = swap_n(Reg8.H)
    fun swap_35(vararg args: Int) = swap_n(Reg8.L)
    fun swap_36(vararg args: Int) =
        mmu.set(read(Reg16.HL), swap_n(mmu.get(read(Reg16.HL))).toUnsignedInt())

    fun daa_27(vararg args: Int) = daa()

    fun cpl_2f(vararg args: Int) = cpl()

    fun ccf_3f(vararg args: Int) = ccf()

    fun scf_37(vararg args: Int) = scf()

    fun halt_76(vararg args: Int) = halt()
    fun stop_10(vararg args: Int) = stop()
    fun di_f3(vararg args: Int) = di()
    fun ei_fb(vararg args: Int) = ei()

    fun rlca_07(vararg args: Int) = rlca()
    fun rla_17(vararg args: Int) = rla()

    fun rrca_0f(vararg args: Int) = rrca()
    fun rra_1f(vararg args: Int) = rra()

    fun rlc_07(vararg args: Int) = rlc_n(Reg8.A)
    fun rlc_00(vararg args: Int) = rlc_n(Reg8.B)
    fun rlc_01(vararg args: Int) = rlc_n(Reg8.C)
    fun rlc_02(vararg args: Int) = rlc_n(Reg8.D)
    fun rlc_03(vararg args: Int) = rlc_n(Reg8.E)
    fun rlc_04(vararg args: Int) = rlc_n(Reg8.H)
    fun rlc_05(vararg args: Int) = rlc_n(Reg8.L)
    fun rlc_06(vararg args: Int) =
        mmu.set(read(Reg16.HL), rlc_n(mmu.get(read(Reg16.HL)).toByte()).toUnsignedInt())

    fun rl_17(vararg args: Int) = rl_n(Reg8.A)
    fun rl_10(vararg args: Int) = rl_n(Reg8.B)
    fun rl_11(vararg args: Int) = rl_n(Reg8.C)
    fun rl_12(vararg args: Int) = rl_n(Reg8.D)
    fun rl_13(vararg args: Int) = rl_n(Reg8.E)
    fun rl_14(vararg args: Int) = rl_n(Reg8.H)
    fun rl_15(vararg args: Int) = rl_n(Reg8.L)
    fun rl_16(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, rl_n(value).toUnsignedInt())
    }

    fun rrc_0f(vararg args: Int) = rrc_n(Reg8.A)
    fun rrc_08(vararg args: Int) = rrc_n(Reg8.B)
    fun rrc_09(vararg args: Int) = rrc_n(Reg8.C)
    fun rrc_0a(vararg args: Int) = rrc_n(Reg8.D)
    fun rrc_0b(vararg args: Int) = rrc_n(Reg8.E)
    fun rrc_0c(vararg args: Int) = rrc_n(Reg8.H)
    fun rrc_0d(vararg args: Int) = rrc_n(Reg8.L)
    fun rrc_0e(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, rrc_n(value).toUnsignedInt())
    }

    fun rr_1f(vararg args: Int) = rr_n(Reg8.A)
    fun rr_18(vararg args: Int) = rr_n(Reg8.B)
    fun rr_19(vararg args: Int) = rr_n(Reg8.C)
    fun rr_1a(vararg args: Int) = rr_n(Reg8.D)
    fun rr_1b(vararg args: Int) = rr_n(Reg8.E)
    fun rr_1c(vararg args: Int) = rr_n(Reg8.H)
    fun rr_1d(vararg args: Int) = rr_n(Reg8.L)
    fun rr_1e(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, rr_n(value).toUnsignedInt())
    }

    fun sla_27(vararg args: Int) = sla_n(Reg8.A)
    fun sla_20(vararg args: Int) = sla_n(Reg8.B)
    fun sla_21(vararg args: Int) = sla_n(Reg8.C)
    fun sla_22(vararg args: Int) = sla_n(Reg8.D)
    fun sla_23(vararg args: Int) = sla_n(Reg8.E)
    fun sla_24(vararg args: Int) = sla_n(Reg8.H)
    fun sla_25(vararg args: Int) = sla_n(Reg8.L)
    fun sla_26(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, sla_n(value).toUnsignedInt())
    }

    fun sra_2f(vararg args: Int) = sra_n(Reg8.A)
    fun sra_28(vararg args: Int) = sra_n(Reg8.B)
    fun sra_29(vararg args: Int) = sra_n(Reg8.C)
    fun sra_2a(vararg args: Int) = sra_n(Reg8.D)
    fun sra_2b(vararg args: Int) = sra_n(Reg8.E)
    fun sra_2c(vararg args: Int) = sra_n(Reg8.H)
    fun sra_2d(vararg args: Int) = sra_n(Reg8.L)
    fun sra_2e(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, sra_n(value).toUnsignedInt())
    }

    fun srl_3f(vararg args: Int) = srl_n(Reg8.A)
    fun srl_38(vararg args: Int) = srl_n(Reg8.B)
    fun srl_39(vararg args: Int) = srl_n(Reg8.C)
    fun srl_3a(vararg args: Int) = srl_n(Reg8.D)
    fun srl_3b(vararg args: Int) = srl_n(Reg8.E)
    fun srl_3c(vararg args: Int) = srl_n(Reg8.H)
    fun srl_3d(vararg args: Int) = srl_n(Reg8.L)
    fun srl_3e(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, srl_n(value).toUnsignedInt())
    }

    fun bit_47(vararg args: Int) = bit_b_r(0, read(Reg8.A))
    fun bit_40(vararg args: Int) = bit_b_r(0, read(Reg8.B))
    fun bit_41(vararg args: Int) = bit_b_r(0, read(Reg8.C))
    fun bit_42(vararg args: Int) = bit_b_r(0, read(Reg8.D))
    fun bit_43(vararg args: Int) = bit_b_r(0, read(Reg8.E))
    fun bit_44(vararg args: Int) = bit_b_r(0, read(Reg8.H))
    fun bit_45(vararg args: Int) = bit_b_r(0, read(Reg8.L))
    fun bit_46(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(0, mmu.get(addr).toByte())
    }

    fun bit_4f(vararg args: Int) = bit_b_r(1, read(Reg8.A))
    fun bit_48(vararg args: Int) = bit_b_r(1, read(Reg8.B))
    fun bit_49(vararg args: Int) = bit_b_r(1, read(Reg8.C))
    fun bit_4a(vararg args: Int) = bit_b_r(1, read(Reg8.D))
    fun bit_4b(vararg args: Int) = bit_b_r(1, read(Reg8.E))
    fun bit_4c(vararg args: Int) = bit_b_r(1, read(Reg8.H))
    fun bit_4d(vararg args: Int) = bit_b_r(1, read(Reg8.L))
    fun bit_4e(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(1, mmu.get(addr).toByte())
    }

    fun bit_57(vararg args: Int) = bit_b_r(2, read(Reg8.A))
    fun bit_50(vararg args: Int) = bit_b_r(2, read(Reg8.B))
    fun bit_51(vararg args: Int) = bit_b_r(2, read(Reg8.C))
    fun bit_52(vararg args: Int) = bit_b_r(2, read(Reg8.D))
    fun bit_53(vararg args: Int) = bit_b_r(2, read(Reg8.E))
    fun bit_54(vararg args: Int) = bit_b_r(2, read(Reg8.H))
    fun bit_55(vararg args: Int) = bit_b_r(2, read(Reg8.L))
    fun bit_56(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(2, mmu.get(addr).toByte())
    }

    fun bit_5f(vararg args: Int) = bit_b_r(3, read(Reg8.A))
    fun bit_58(vararg args: Int) = bit_b_r(3, read(Reg8.B))
    fun bit_59(vararg args: Int) = bit_b_r(3, read(Reg8.C))
    fun bit_5a(vararg args: Int) = bit_b_r(3, read(Reg8.D))
    fun bit_5b(vararg args: Int) = bit_b_r(3, read(Reg8.E))
    fun bit_5c(vararg args: Int) = bit_b_r(3, read(Reg8.H))
    fun bit_5d(vararg args: Int) = bit_b_r(3, read(Reg8.L))
    fun bit_5e(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(3, mmu.get(addr).toByte())
    }

    fun bit_67(vararg args: Int) = bit_b_r(4, read(Reg8.A))
    fun bit_60(vararg args: Int) = bit_b_r(4, read(Reg8.B))
    fun bit_61(vararg args: Int) = bit_b_r(4, read(Reg8.C))
    fun bit_62(vararg args: Int) = bit_b_r(4, read(Reg8.D))
    fun bit_63(vararg args: Int) = bit_b_r(4, read(Reg8.E))
    fun bit_64(vararg args: Int) = bit_b_r(4, read(Reg8.H))
    fun bit_65(vararg args: Int) = bit_b_r(4, read(Reg8.L))
    fun bit_66(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(4, mmu.get(addr).toByte())
    }

    fun bit_6f(vararg args: Int) = bit_b_r(5, read(Reg8.A))
    fun bit_68(vararg args: Int) = bit_b_r(5, read(Reg8.B))
    fun bit_69(vararg args: Int) = bit_b_r(5, read(Reg8.C))
    fun bit_6a(vararg args: Int) = bit_b_r(5, read(Reg8.D))
    fun bit_6b(vararg args: Int) = bit_b_r(5, read(Reg8.E))
    fun bit_6c(vararg args: Int) = bit_b_r(5, read(Reg8.H))
    fun bit_6d(vararg args: Int) = bit_b_r(5, read(Reg8.L))
    fun bit_6e(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(5, mmu.get(addr).toByte())
    }

    fun bit_77(vararg args: Int) = bit_b_r(6, read(Reg8.A))
    fun bit_70(vararg args: Int) = bit_b_r(6, read(Reg8.B))
    fun bit_71(vararg args: Int) = bit_b_r(6, read(Reg8.C))
    fun bit_72(vararg args: Int) = bit_b_r(6, read(Reg8.D))
    fun bit_73(vararg args: Int) = bit_b_r(6, read(Reg8.E))
    fun bit_74(vararg args: Int) = bit_b_r(6, read(Reg8.H))
    fun bit_75(vararg args: Int) = bit_b_r(6, read(Reg8.L))
    fun bit_76(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(6, mmu.get(addr).toByte())
    }

    fun bit_7f(vararg args: Int) = bit_b_r(7, read(Reg8.A))
    fun bit_78(vararg args: Int) = bit_b_r(7, read(Reg8.B))
    fun bit_79(vararg args: Int) = bit_b_r(7, read(Reg8.C))
    fun bit_7a(vararg args: Int) = bit_b_r(7, read(Reg8.D))
    fun bit_7b(vararg args: Int) = bit_b_r(7, read(Reg8.E))
    fun bit_7c(vararg args: Int) = bit_b_r(7, read(Reg8.H))
    fun bit_7d(vararg args: Int) = bit_b_r(7, read(Reg8.L))
    fun bit_7e(vararg args: Int) {
        val addr = read(Reg16.HL)
        bit_b_r(7, mmu.get(addr).toByte())
    }

    fun set_c7(vararg args: Int) = set_b_r(0, Reg8.A)
    fun set_c0(vararg args: Int) = set_b_r(0, Reg8.B)
    fun set_c1(vararg args: Int) = set_b_r(0, Reg8.C)
    fun set_c2(vararg args: Int) = set_b_r(0, Reg8.D)
    fun set_c3(vararg args: Int) = set_b_r(0, Reg8.E)
    fun set_c4(vararg args: Int) = set_b_r(0, Reg8.H)
    fun set_c5(vararg args: Int) = set_b_r(0, Reg8.L)
    fun set_c6(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(0, value).toUnsignedInt())
    }

    fun set_cf(vararg args: Int) = set_b_r(1, Reg8.A)
    fun set_c8(vararg args: Int) = set_b_r(1, Reg8.B)
    fun set_c9(vararg args: Int) = set_b_r(1, Reg8.C)
    fun set_ca(vararg args: Int) = set_b_r(1, Reg8.D)
    fun set_cb(vararg args: Int) = set_b_r(1, Reg8.E)
    fun set_cc(vararg args: Int) = set_b_r(1, Reg8.H)
    fun set_cd(vararg args: Int) = set_b_r(1, Reg8.L)
    fun set_ce(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(1, value).toUnsignedInt())
    }

    fun set_d7(vararg args: Int) = set_b_r(2, Reg8.A)
    fun set_d0(vararg args: Int) = set_b_r(2, Reg8.B)
    fun set_d1(vararg args: Int) = set_b_r(2, Reg8.C)
    fun set_d2(vararg args: Int) = set_b_r(2, Reg8.D)
    fun set_d3(vararg args: Int) = set_b_r(2, Reg8.E)
    fun set_d4(vararg args: Int) = set_b_r(2, Reg8.H)
    fun set_d5(vararg args: Int) = set_b_r(2, Reg8.L)
    fun set_d6(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(2, value).toUnsignedInt())
    }

    fun set_df(vararg args: Int) = set_b_r(3, Reg8.A)
    fun set_d8(vararg args: Int) = set_b_r(3, Reg8.B)
    fun set_d9(vararg args: Int) = set_b_r(3, Reg8.C)
    fun set_da(vararg args: Int) = set_b_r(3, Reg8.D)
    fun set_db(vararg args: Int) = set_b_r(3, Reg8.E)
    fun set_dc(vararg args: Int) = set_b_r(3, Reg8.H)
    fun set_dd(vararg args: Int) = set_b_r(3, Reg8.L)
    fun set_de(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(3, value).toUnsignedInt())
    }

    fun set_e7(vararg args: Int) = set_b_r(4, Reg8.A)
    fun set_e0(vararg args: Int) = set_b_r(4, Reg8.B)
    fun set_e1(vararg args: Int) = set_b_r(4, Reg8.C)
    fun set_e2(vararg args: Int) = set_b_r(4, Reg8.D)
    fun set_e3(vararg args: Int) = set_b_r(4, Reg8.E)
    fun set_e4(vararg args: Int) = set_b_r(4, Reg8.H)
    fun set_e5(vararg args: Int) = set_b_r(4, Reg8.L)
    fun set_e6(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(4, value).toUnsignedInt())
    }

    fun set_ef(vararg args: Int) = set_b_r(5, Reg8.A)
    fun set_e8(vararg args: Int) = set_b_r(5, Reg8.B)
    fun set_e9(vararg args: Int) = set_b_r(5, Reg8.C)
    fun set_ea(vararg args: Int) = set_b_r(5, Reg8.D)
    fun set_eb(vararg args: Int) = set_b_r(5, Reg8.E)
    fun set_ec(vararg args: Int) = set_b_r(5, Reg8.H)
    fun set_ed(vararg args: Int) = set_b_r(5, Reg8.L)
    fun set_ee(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(5, value).toUnsignedInt())
    }

    fun set_f7(vararg args: Int) = set_b_r(6, Reg8.A)
    fun set_f0(vararg args: Int) = set_b_r(6, Reg8.B)
    fun set_f1(vararg args: Int) = set_b_r(6, Reg8.C)
    fun set_f2(vararg args: Int) = set_b_r(6, Reg8.D)
    fun set_f3(vararg args: Int) = set_b_r(6, Reg8.E)
    fun set_f4(vararg args: Int) = set_b_r(6, Reg8.H)
    fun set_f5(vararg args: Int) = set_b_r(6, Reg8.L)
    fun set_f6(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(6, value).toUnsignedInt())
    }

    fun set_ff(vararg args: Int) = set_b_r(7, Reg8.A)
    fun set_f8(vararg args: Int) = set_b_r(7, Reg8.B)
    fun set_f9(vararg args: Int) = set_b_r(7, Reg8.C)
    fun set_fa(vararg args: Int) = set_b_r(7, Reg8.D)
    fun set_fb(vararg args: Int) = set_b_r(7, Reg8.E)
    fun set_fc(vararg args: Int) = set_b_r(7, Reg8.H)
    fun set_fd(vararg args: Int) = set_b_r(7, Reg8.L)
    fun set_fe(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, set_b_r(7, value).toUnsignedInt())
    }

    fun res_87(vararg args: Int) = res_b_r(0, Reg8.A)
    fun res_80(vararg args: Int) = res_b_r(0, Reg8.B)
    fun res_81(vararg args: Int) = res_b_r(0, Reg8.C)
    fun res_82(vararg args: Int) = res_b_r(0, Reg8.D)
    fun res_83(vararg args: Int) = res_b_r(0, Reg8.E)
    fun res_84(vararg args: Int) = res_b_r(0, Reg8.H)
    fun res_85(vararg args: Int) = res_b_r(0, Reg8.L)
    fun res_86(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(0, value).toUnsignedInt())
    }

    fun res_8f(vararg args: Int) = res_b_r(1, Reg8.A)
    fun res_88(vararg args: Int) = res_b_r(1, Reg8.B)
    fun res_89(vararg args: Int) = res_b_r(1, Reg8.C)
    fun res_8a(vararg args: Int) = res_b_r(1, Reg8.D)
    fun res_8b(vararg args: Int) = res_b_r(1, Reg8.E)
    fun res_8c(vararg args: Int) = res_b_r(1, Reg8.H)
    fun res_8d(vararg args: Int) = res_b_r(1, Reg8.L)
    fun res_8e(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(1, value).toUnsignedInt())
    }

    fun res_97(vararg args: Int) = res_b_r(2, Reg8.A)
    fun res_90(vararg args: Int) = res_b_r(2, Reg8.B)
    fun res_91(vararg args: Int) = res_b_r(2, Reg8.C)
    fun res_92(vararg args: Int) = res_b_r(2, Reg8.D)
    fun res_93(vararg args: Int) = res_b_r(2, Reg8.E)
    fun res_94(vararg args: Int) = res_b_r(2, Reg8.H)
    fun res_95(vararg args: Int) = res_b_r(2, Reg8.L)
    fun res_96(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(2, value).toUnsignedInt())
    }

    fun res_9f(vararg args: Int) = res_b_r(3, Reg8.A)
    fun res_98(vararg args: Int) = res_b_r(3, Reg8.B)
    fun res_99(vararg args: Int) = res_b_r(3, Reg8.C)
    fun res_9a(vararg args: Int) = res_b_r(3, Reg8.D)
    fun res_9b(vararg args: Int) = res_b_r(3, Reg8.E)
    fun res_9c(vararg args: Int) = res_b_r(3, Reg8.H)
    fun res_9d(vararg args: Int) = res_b_r(3, Reg8.L)
    fun res_9e(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(3, value).toUnsignedInt())
    }

    fun res_a7(vararg args: Int) = res_b_r(4, Reg8.A)
    fun res_a0(vararg args: Int) = res_b_r(4, Reg8.B)
    fun res_a1(vararg args: Int) = res_b_r(4, Reg8.C)
    fun res_a2(vararg args: Int) = res_b_r(4, Reg8.D)
    fun res_a3(vararg args: Int) = res_b_r(4, Reg8.E)
    fun res_a4(vararg args: Int) = res_b_r(4, Reg8.H)
    fun res_a5(vararg args: Int) = res_b_r(4, Reg8.L)
    fun res_a6(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(4, value).toUnsignedInt())
    }

    fun res_af(vararg args: Int) = res_b_r(5, Reg8.A)
    fun res_a8(vararg args: Int) = res_b_r(5, Reg8.B)
    fun res_a9(vararg args: Int) = res_b_r(5, Reg8.C)
    fun res_aa(vararg args: Int) = res_b_r(5, Reg8.D)
    fun res_ab(vararg args: Int) = res_b_r(5, Reg8.E)
    fun res_ac(vararg args: Int) = res_b_r(5, Reg8.H)
    fun res_ad(vararg args: Int) = res_b_r(5, Reg8.L)
    fun res_ae(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(5, value).toUnsignedInt())
    }

    fun res_b7(vararg args: Int) = res_b_r(6, Reg8.A)
    fun res_b0(vararg args: Int) = res_b_r(6, Reg8.B)
    fun res_b1(vararg args: Int) = res_b_r(6, Reg8.C)
    fun res_b2(vararg args: Int) = res_b_r(6, Reg8.D)
    fun res_b3(vararg args: Int) = res_b_r(6, Reg8.E)
    fun res_b4(vararg args: Int) = res_b_r(6, Reg8.H)
    fun res_b5(vararg args: Int) = res_b_r(6, Reg8.L)
    fun res_b6(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(6, value).toUnsignedInt())
    }

    fun res_bf(vararg args: Int) = res_b_r(7, Reg8.A)
    fun res_b8(vararg args: Int) = res_b_r(7, Reg8.B)
    fun res_b9(vararg args: Int) = res_b_r(7, Reg8.C)
    fun res_ba(vararg args: Int) = res_b_r(7, Reg8.D)
    fun res_bb(vararg args: Int) = res_b_r(7, Reg8.E)
    fun res_bc(vararg args: Int) = res_b_r(7, Reg8.H)
    fun res_bd(vararg args: Int) = res_b_r(7, Reg8.L)
    fun res_be(vararg args: Int) {
        val addr = read(Reg16.HL)
        val value = mmu.get(addr).toByte()
        mmu.set(addr, res_b_r(7, value).toUnsignedInt())
    }

    fun jp_c3(vararg args: Int) = jp_n(args.toWord())

    fun jp_c2(vararg args: Int): Boolean = jp_c_n(Condition.NZ, args.toWord())
    fun jp_ca(vararg args: Int): Boolean = jp_c_n(Condition.Z, args.toWord())
    fun jp_d2(vararg args: Int): Boolean = jp_c_n(Condition.NC, args.toWord())
    fun jp_da(vararg args: Int): Boolean = jp_c_n(Condition.C, args.toWord())

    fun jp_e9(vararg args: Int) = jp_n(read(Reg16.HL))

    fun jr_18(vararg args: Int) = jr_n(args[0].toByte())

    fun jr_20(vararg args: Int): Boolean = jr_c_n(Condition.NZ, args[0].toByte())
    fun jr_28(vararg args: Int): Boolean = jr_c_n(Condition.Z, args[0].toByte())
    fun jr_30(vararg args: Int): Boolean = jr_c_n(Condition.NC, args[0].toByte())
    fun jr_38(vararg args: Int): Boolean = jr_c_n(Condition.C, args[0].toByte())

    fun call_cd(vararg args: Int) = call_n(args.toWord())

    fun call_c4(vararg args: Int): Boolean = call_c_n(Condition.NZ, args.toWord())
    fun call_cc(vararg args: Int): Boolean = call_c_n(Condition.Z, args.toWord())
    fun call_d4(vararg args: Int): Boolean = call_c_n(Condition.NC, args.toWord())
    fun call_dc(vararg args: Int): Boolean = call_c_n(Condition.C, args.toWord())

    fun rst_c7(vararg args: Int) = rst_n(0x00)
    fun rst_cf(vararg args: Int) = rst_n(0x08)
    fun rst_d7(vararg args: Int) = rst_n(0x10)
    fun rst_df(vararg args: Int) = rst_n(0x18)
    fun rst_e7(vararg args: Int) = rst_n(0x20)
    fun rst_ef(vararg args: Int) = rst_n(0x28)
    fun rst_f7(vararg args: Int) = rst_n(0x30)
    fun rst_ff(vararg args: Int) = rst_n(0x38)

    fun ret_c9(vararg args: Int) = ret()

    fun ret_c0(vararg args: Int): Boolean = ret_c(Condition.NZ)
    fun ret_c8(vararg args: Int): Boolean = ret_c(Condition.Z)
    fun ret_d0(vararg args: Int): Boolean = ret_c(Condition.NC)
    fun ret_d8(vararg args: Int): Boolean = ret_c(Condition.C)

    fun reti_d9(vararg args: Int) = reti()

}