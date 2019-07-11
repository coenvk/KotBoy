package com.arman.kotboy.io.input

import java.awt.event.KeyListener

interface InputHandler : KeyListener {

    var buttonListener: ButtonListener?

}