package com.arman.kotboy.core

import java.io.File

class Options(val file: File, vararg params: String) {

    constructor(rom: String, vararg params: String) : this(File(rom), *params)

    val dmg: Boolean = params.contains("dmg")
    val cgb: Boolean = if (this.dmg) false else params.contains("cgb")
    val bootstrap: Boolean = params.contains("bootstrap")
    val disableSaves: Boolean = params.contains("disable-saves")
    val debug: Boolean = params.contains("debug")
    val headless: Boolean = params.contains("headless")

}