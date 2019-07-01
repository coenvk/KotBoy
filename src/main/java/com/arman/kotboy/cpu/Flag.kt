package com.arman.kotboy.cpu

import com.arman.kotboy.cpu.util.Flags

enum class Flag(override val bit: Int) : Flags {

    /*
    |-------------------------------|
    | 7 | 6 | 5 | 4 | 3 | 2 | 1 | 0 |
    |-------------------------------|
    | Z | N | H | C | 0 | 0 | 0 | 0 |
    |-------------------------------|

    Z | Zero Flag
    N | Subtract Flag
    H | Half Carry Flag
    C | Carry Flag
    0 | Not used, always zero
     */

    Z(1 shl Flag.Z_INDEX),
    N(1 shl Flag.N_INDEX),
    H(1 shl Flag.H_INDEX),
    C(1 shl Flag.C_INDEX),
    UNUSED_3(1 shl 3),
    UNUSED_2(1 shl 2),
    UNUSED_1(1 shl 1),
    UNUSED_0(1 shl 0),
    UNDEFINED(0);

    companion object {

        const val Z_INDEX = 7
        const val N_INDEX = 6
        const val H_INDEX = 5
        const val C_INDEX = 4

    }

}