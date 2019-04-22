package com.arman.kotboy.cpu

class Num(val value: kotlin.Byte) : Operand(Type.D8) {

    override fun toString(): String {
        return this.value.toString()
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Num) return false
        if (this.value != other.value) return false
        return true
    }

}