package com.arman.kotboy.core.gui.options

import java.io.Serializable

enum class EmulatedSystem(
        val mnemonic: String
) : Serializable {

    DMG("Game Boy"),
    CGB("Game Boy Color"),
    SGB("Super Game Boy");

}