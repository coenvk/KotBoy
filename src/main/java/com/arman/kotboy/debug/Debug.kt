package com.arman.kotboy.debug

val DEBUG = false

fun debug(s: String) {
    if (DEBUG) println(s)
}