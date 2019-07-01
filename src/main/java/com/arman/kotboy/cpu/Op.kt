package com.arman.kotboy.cpu

class Op(val opCode: OpCodes) : Iterable<Op> {

    var comment: String? = null

    fun argsSize(): Int = this.opCode.argsSize()

    override fun iterator(): Iterator<Op> = setOf(this).iterator()

    override fun toString(): String {
        return prettryPrint()
    }

    fun prettryPrint(): String {
        var res = "$opCode"
        this.comment?.let { res += " // $comment" }
        return res
    }

}