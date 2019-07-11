package com.arman.kotboy.cpu

import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.cpu.util.toWord

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

class Immediate8(override val value: Int) : Operand {

    override fun get(): Int {
        return super.get().toUnsignedInt()
    }

}

class Immediate16(msb: Int, lsb: Int) : Operand {

    override val value: Int = toWord(msb, lsb)

    override fun get(): Int {
        return super.get().toWord()
    }

}

class Signed8(override val value: Int) : Operand {

    override fun get(): Int {
        return super.get().toByte().toInt()
    }

}