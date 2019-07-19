package com.arman.kotboy.cpu

interface Operand {

    abstract val value: Int

    fun get(): Int {
        return this.value
    }

    enum class Type {

        d8,
        d16,
        r8,
        a8,
        a16,

        UNDEFINED;

    }

}