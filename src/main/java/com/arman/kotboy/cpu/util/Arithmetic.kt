package com.arman.kotboy.cpu.util

import com.arman.kotboy.cpu.Operand

fun Byte.hexString(): String = "$${this.toString(16).toUpperCase()}"

fun Byte.bitString(): String = "$${this.toString(2)}"

fun IntRange.hexString(): String {
    val u = this.first
    val v = this.last
    return "${u.hexString()}..${v.hexString()}"
}

fun Int.hexString(): String = "$${this.toString(16).toUpperCase()}"

fun Int.hexString(length: Int): String = "$${this.toString(16).toUpperCase().padStart(length, '0')}"

fun Int.bitString(): String = "$${this.toString(2)}"

fun IntArray.contentToHexString(): String {
    if (this.isEmpty()) return "[]"
    var out = "["
    for (i in 0 until this.size - 1) {
        out += "${this[i].hexString()}, "
    }
    out += "${this[this.size - 1].hexString()}]"
    return out
}

fun Byte.toUnsignedInt(): Int = this.toInt().lsb()

fun Int.toUnsignedInt(): Int = this.lsb()

fun Int.toWord(): Int = this and 0xFFFF

fun ByteArray.toWord(): Int = (this[1] shl 8) or this[0]

fun ByteArray.toByte(): Byte = this[0]

fun IntArray.toWord(): Int = (this[1] shl 8) or this[0]

fun IntArray.toByte(): Byte = this[0].toByte()

fun IntArray.toUnsignedInt(): Int = this[0].toUnsignedInt()

fun Int.toBoolean(): Boolean = this != 0

fun Byte.at(bit: Int): Boolean {
    val b = 1 shl bit
    return (this.toUnsignedInt() and b) == b
}

fun Byte.set(bit: Int, value: Boolean = true): Byte =
    if (value) (this or (1 shl bit)).toByte() else (this and (1 shl bit).inv()).toByte()

fun Byte.toggle(bit: Int): Byte = (this xor (1 shl bit)).toByte()

fun Byte.rr(bitCount: Int = 1): Byte {
    val b = (this shr bitCount)
    if (this.at(0)) return (b or (1 shl (Byte.SIZE_BITS - bitCount))).toByte()
    return b.toByte()
}

fun Byte.rl(bitCount: Int = 1): Byte = ((this shl bitCount) or this.at(7).toInt()).toByte()

infix fun Int.or(other: Byte): Int = this or other.toUnsignedInt()

infix fun Byte.or(other: Int): Int = this.toUnsignedInt() or other

infix fun Byte.or(other: Byte): Int = this.toUnsignedInt() or other.toUnsignedInt()

infix fun Int.and(other: Byte): Int = this and other.toUnsignedInt()

infix fun Byte.and(other: Int): Int = this.toUnsignedInt() and other

infix fun Byte.and(other: Byte): Int = this.toUnsignedInt() and other.toUnsignedInt()

infix fun Int.xor(other: Byte): Int = this xor other.toUnsignedInt()

infix fun Byte.xor(other: Int): Int = this.toUnsignedInt() xor other

infix fun Byte.xor(other: Byte): Int = this.toUnsignedInt() xor other.toUnsignedInt()

infix fun Int.shl(other: Byte): Int = this shl other.toUnsignedInt()

infix fun Byte.shl(other: Int): Int = this.toUnsignedInt() shl other

infix fun Byte.shl(other: Byte): Int = this.toUnsignedInt() shl other.toUnsignedInt()

infix fun Int.shr(other: Byte): Int = this shr other.toUnsignedInt()

infix fun Byte.shr(other: Int): Int = this.toUnsignedInt() shr other

infix fun Byte.shr(other: Byte): Int = this.toUnsignedInt() shr other.toUnsignedInt()

infix fun Int.ushr(other: Byte): Int = this ushr other.toUnsignedInt()

infix fun Byte.ushr(other: Int): Int = this.toUnsignedInt() ushr other

infix fun Byte.ushr(other: Byte): Int = this.toUnsignedInt() ushr other.toUnsignedInt()

fun Boolean.toInt(): Int = if (this) 1 else 0

fun Int.msb(): Int {
    return (this ushr 8)
}

fun Int.lsb(): Int {
    return this and 0xFF
}

fun toWord(msb: Int, lsb: Int): Int {
    return (msb shl 8) or lsb
}

fun Int.at(pos: Int): Boolean {
    return (this and (1 shl pos)) != 0
}

fun Int.set(pos: Int, bit: Boolean): Int {
    return if (bit) ((this or (1 shl pos)).lsb()) else ((1 shl pos).inv() and this.lsb())
}

fun Int.signed(): Int {
    return if ((this and (1 shl 7)) == 0) this
    else this - 0x100
}