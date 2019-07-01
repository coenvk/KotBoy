package com.arman.kotboy.cpu

enum class Argument(
    val label: String,
    val bytes: Int = 0,
    val type: Type = Type.D8,
    val memory: Boolean = false
) {

    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    H("H"),
    L("L"),
    AF("AF", type = Type.D16),
    BC("BC", type = Type.D16),
    DE("DE", type = Type.D16),
    HL("HL", type = Type.D16),
    SP("SP", type = Type.D16),
    SPpr8("SP+r8", type = Type.D16),
    PC("PC", type = Type.D16),
    d8("d8", 1),
    d16("d16", 2, type = Type.D16),
    r8("r8", 1, type = Type.R8),
    a8("a8", 1, type = Type.A8),
    a16("a16", 2, type = Type.A16),
    _BC_("(BC)", memory = true),
    _DE_("(DE)", memory = true),
    _HL_("(HL)", memory = true),
    _HLi_("(HL+)", memory = true),
    _HLd_("(HL-)", memory = true),
    _a8_("(a8)", 1, type = Type.A8, memory = true),
    _a16_("(a16)", 2, type = Type.A16, memory = true),
    _C_("(C)", memory = true),

    UNDEFINED("-", type = Type.UNDEFINED);

    enum class Type {

        D8,
        D16,
        R8,
        A8,
        A16,

        UNDEFINED;

    }

    override fun toString(): String {
        return this.label
    }

}