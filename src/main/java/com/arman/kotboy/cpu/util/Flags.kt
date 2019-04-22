package com.arman.kotboy.cpu.util

class BitMask(val value: Int)

interface Flags {

    val bit: Int

    fun toBitMask(): BitMask = BitMask(bit)

}

infix fun Flags.and(other: Int): BitMask =
    BitMask(bit and other)

infix fun <T : Flags> Flags.and(other: T): BitMask =
    BitMask(bit and other.bit)

infix fun Flags.or(other: Int): BitMask =
    BitMask(bit or other)

infix fun <T : Flags> Flags.or(other: T): BitMask =
    BitMask(bit or other.bit)

infix operator fun Flags.plus(other: Flags): BitMask =
    BitMask(bit or other.bit)

inline fun <reified T> enabledValues(mask: BitMask): List<T> where T : Enum<T>, T : Flags {
    return enumValues<T>().filter {
        mask hasFlag it
    }
}

infix fun BitMask.or(other: Flags): BitMask = BitMask(value or other.bit)

infix fun BitMask.or(other: Int): BitMask = BitMask(value or other)

infix fun BitMask.and(other: Flags): BitMask = BitMask(value and other.bit)

infix fun BitMask.and(other: Int): BitMask = BitMask(value and other)

infix operator fun BitMask.plus(other: BitMask): BitMask =
    BitMask(value or other.value)

infix operator fun BitMask.plus(other: Flags): BitMask =
    BitMask(value or other.bit)

infix fun <T : Flags> BitMask.hasFlag(which: T): Boolean {
    if (value == 0 || (value > 0 && which.bit == 0)) return false
    return value and which.bit == which.bit
}

infix fun <T : Flags> BitMask.set(which: T): BitMask =
    BitMask(value or which.bit)

infix fun <T : Flags> BitMask.unset(which: T): BitMask =
    BitMask(value xor which.bit)

infix fun Int.or(other: BitMask): Int = this or other.value