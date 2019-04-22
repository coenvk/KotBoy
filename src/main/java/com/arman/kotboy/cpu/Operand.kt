package com.arman.kotboy.cpu

import com.arman.kotboy.AddressSpace
import com.arman.kotboy.cpu.util.signed
import com.arman.kotboy.cpu.util.toWord

enum class Operand(
    val label: String? = null,
    val bytes: Int = 0,
    val memory: Boolean = false,
    val type: Type = Type.D8
) {

    A("A") {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.A
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.A = value
        }
    },
    B("B") {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.B
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.B = value
        }
    },
    C("C") {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.C
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.C = value
        }
    },
    D("D") {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.D
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.D = value
        }
    },
    E("E") {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.E
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.E = value
        }
    },
    H("H") {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.H
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.H = value
        }
    },
    L("L") {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.L
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.L = value
        }
    },

    AF("AF", type = Type.D16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.AF
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.AF = value
        }
    },
    BC("BC", type = Type.D16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.BC
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.BC = value
        }
    },
    DE("DE", type = Type.D16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.DE
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.DE = value
        }
    },
    HL("HL", type = Type.D16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.HL
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.HL = value
        }
    },

    SP("SP", type = Type.D16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.SP
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.SP = value
        }
    },
    PC("PC", type = Type.D16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return Regs.PC
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            Regs.PC = value
        }
    },

    d8("d8", 1) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return args[0]
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            throw UnsupportedOperationException()
        }
    },
    d16("d16", 2, type = Type.D16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return toWord(args[1], args[0])
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            throw UnsupportedOperationException()
        }
    },
    r8("r8", 1, type = Type.R8) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return args[0].signed()
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            throw UnsupportedOperationException()
        }
    },
    a8("a8", 1, type = Type.A8) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return args[0]
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            throw UnsupportedOperationException()
        }
    },
    a16("a16", 2, type = Type.A16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return toWord(args[1], args[0])
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            throw UnsupportedOperationException()
        }
    },

    _BC_("(BC)", memory = true) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return addressSpace.get(Regs.BC)
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            addressSpace.set(Regs.BC, value)
        }
    },
    _DE_("(DE)", memory = true) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return addressSpace.get(Regs.DE)
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            addressSpace.set(Regs.DE, value)
        }
    },
    _HL_("(HL)", memory = true) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return addressSpace.get(Regs.HL)
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            addressSpace.set(Regs.HL, value)
        }
    },

    _a8_("(a8)", 1, memory = true, type = Type.A8) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return addressSpace.get(0xFF00 or args[0])
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            addressSpace.set(0xFF00 or args[0], value)
        }
    },
    _a16_("(a16)", 2, memory = true, type = Type.A16) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return addressSpace.get(toWord(args[1], args[0]))
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            addressSpace.set(toWord(args[1], args[0]), value)
        }
    },

    _C_("(C)", memory = true) {
        override fun read(addressSpace: AddressSpace, vararg args: Int): Int {
            return addressSpace.get(0xFF00 or Regs.C)
        }

        override fun write(addressSpace: AddressSpace, value: Int, vararg args: Int) {
            addressSpace.set(0xFF00 or Regs.C, value)
        }
    };

    abstract fun read(addressSpace: AddressSpace, vararg args: Int): Int
    abstract fun write(addressSpace: AddressSpace, value: Int, vararg args: Int)

    enum class Type {

        D8,
        D16,
        A8,
        A16,
        R8,

        UNDEFINED,

    }

}