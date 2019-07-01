package com.arman.kotboy.cpu

interface OpCodes {

    fun cycles(): Int
    fun argsSize(): Int
    fun args(): Array<out Operand.Type>

}