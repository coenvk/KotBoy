package com.arman.kotboy.core.cpu

enum class Reg16(val r1: Reg8, val r2: Reg8) {

    AF(Reg8.A, Reg8.F),
    BC(Reg8.B, Reg8.C),
    DE(Reg8.D, Reg8.E),
    HL(Reg8.H, Reg8.L);

}