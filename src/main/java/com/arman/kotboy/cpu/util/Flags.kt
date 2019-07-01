package com.arman.kotboy.cpu.util

interface Flags {

    val bit: Int

    fun toInt(): Int = this.bit

}

infix fun Flags.and(other: Int): Int = this.bit and other

infix fun <T : Flags> Flags.and(other: T): Int = this.bit and other.bit

infix fun Flags.or(other: Int): Int = this.bit or other

infix fun <T : Flags> Flags.or(other: T): Int = this.bit or other.bit

infix operator fun Flags.plus(other: Flags): Int = this.bit or other.bit

infix operator fun Flags.minus(other: Flags): Int = this.bit and other.bit.inv()

infix operator fun Flags.times(other: Flags): Int = this.bit xor other.bit

inline fun <reified T> enabledValues(arg: Int): List<T> where T : Enum<T>, T : Flags {
    return enumValues<T>().filter {
        arg hasFlag it
    }
}

infix fun <T : Flags> Int.hasFlag(which: T): Boolean {
    if (this == 0 || (this > 0 && which.bit == 0)) return false
    return this and which.bit == which.bit
}

infix fun Int.and(other: Flags): Int = this and other.bit

infix fun Int.or(other: Flags): Int = this or other.bit

infix fun Int.set(other: Flags): Int = this or other.bit

infix fun Int.reset(other: Flags): Int = this and other.bit.inv()

infix operator fun <T : Flags> Int.plus(other: T): Int = this or other.bit

infix operator fun <T : Flags> Int.minus(other: T): Int = this and other.bit.inv()

infix operator fun <T : Flags> Int.times(other: T): Int = this xor other.bit