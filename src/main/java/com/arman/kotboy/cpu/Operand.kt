package com.arman.kotboy.cpu

interface Operand {

    enum class Type {

        d8,
        d16,
        r8,
        a8,
        a16,

        UNDEFINED;

    }

}