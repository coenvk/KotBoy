package com.arman.kotboy.core.debug

object Log {

    private const val LOG = false

    fun i(s: String) {
        if (LOG) println("INFO: $s")
    }

    fun e(s: String) {
        if (LOG) println("ERROR: $s")
    }

    fun d(s: String) {
        if (LOG) println("DEBUG: $s")
    }

    fun w(s: String) {
        if (LOG) println("WARNING: $s")
    }

}