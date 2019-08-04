package com.arman.kotboy.core.io.input

import java.awt.event.KeyListener

interface InputHandler : KeyListener {

    var buttonListener: ButtonListener?

}