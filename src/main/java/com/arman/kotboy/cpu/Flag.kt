package com.arman.kotboy.cpu

import com.arman.kotboy.cpu.util.Flags

enum class Flag(override val bit: Long) : Flags {

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

    Z(1 shl 7),
    N(1 shl 6),
    H(1 shl 5),
    C(1 shl 4),
    UNUSED_3(1 shl 3),
    UNUSED_2(1 shl 2),
    UNUSED_1(1 shl 1),
    UNUSED_0(1 shl 0),
    UNDEFINED(0),

}