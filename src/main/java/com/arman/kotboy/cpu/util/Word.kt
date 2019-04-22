package com.arman.kotboy.cpu.util

typealias Word = Int

fun Word.msb(): Word {
    return (this shr 8) and 0xFF
}

fun Word.lsb(): Word {
    return this and 0xFF
}

fun Word.inc(): Word {
    return (this + 1) and 0xFFFF
}

fun Word.dec(): Word {
    return (this - 1) and 0xFFFF
}

fun toWord(msb: Int, lsb: Int): Word {
    return (msb shr 8) or lsb
}

fun Word.bit(pos: Word): Boolean {
    return (this and (1 shl pos)) != 0
}

fun Word.putBit(pos: Word, bit: Boolean): Word {
    return if (bit) ((this or (1 shl pos)) and 0xFF) else ((1 shl pos).inv() and this and 0xFF)
}

fun Word.putMsb(bit: Boolean): Word {
    return this.putBit(8, bit)
}

fun Word.putLsb(bit: Boolean): Word {
    return this.putBit(0, bit)
}

fun Word.signed(): Word {
    return if ((this and (1 shl 7)) == 0) this
    else this - 0x100
}