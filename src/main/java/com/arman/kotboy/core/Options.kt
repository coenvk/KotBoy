package com.arman.kotboy.core

import java.util.prefs.Preferences

object Options {

    private val store: Preferences = Preferences.userNodeForPackage(Options::class.java)

    var fullscreen: Boolean = store.getBoolean("fullscreen", false)
        set(value) {
            field = value
            store.putBoolean("fullscreen", value)
        }

    var windowed: Boolean = store.getBoolean("windowed", true)
        set(value) {
            field = value
            store.putBoolean("windowed", value)
        }

    var scale: Int = store.getInt("scale", 1)
        set(value) {
            field = value
            store.putInt("scale", value)
        }

    var bootstrap: Boolean = store.getBoolean("bootstrap", false)
        set(value) {
            field = value
            store.putBoolean("bootstrap", value)
        }

    var dmg: Boolean = store.getBoolean("dmg", true)
        set(value) {
            field = value
            store.putBoolean("dmg", value)
        }

    var cgb: Boolean = store.getBoolean("cgb", false)
        set(value) {
            field = value
            store.putBoolean("cgb", value)
        }

}