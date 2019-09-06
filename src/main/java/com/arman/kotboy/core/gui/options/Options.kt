package com.arman.kotboy.core.gui.options

import com.arman.kotboy.core.cpu.util.toEnum
import java.util.prefs.Preferences

object Options {

    private const val FULLSCREEN = "fullscreen"
    private const val SCALE = "scale"
    private const val ENABLE_BOOTSTRAP = "enableBootstrap"
    private const val ENABLE_BORDER = "enableBorder"
    private const val EMULATED_SYSTEM = "emulatedSystem"
    private const val COLOR_PALETTE = "colorPalette"
    private const val PALETTE_0 = "palette0"
    private const val PALETTE_1 = "palette1"
    private const val PALETTE_2 = "palette2"
    private const val PALETTE_3 = "palette3"
    private const val KEY_BINDINGS = "keyBindings"
    private const val KEY_LEFT = "keyLeft"
    private const val KEY_RIGHT = "keyRight"
    private const val KEY_UP = "keyUp"
    private const val KEY_DOWN = "keyDown"
    private const val KEY_A = "keyA"
    private const val KEY_B = "keyB"
    private const val KEY_START = "keyStart"
    private const val KEY_SELECT = "keySelect"

    private val store: Preferences = Preferences.userNodeForPackage(Options::class.java)
    private val colorPalette: Preferences = store.node(COLOR_PALETTE)
    private val keyBindings: Preferences = store.node(KEY_BINDINGS)

    var fullscreen: Boolean = store.getBoolean(FULLSCREEN, DefaultOptions.fullscreen)
        set(value) {
            field = value
            store.putBoolean(FULLSCREEN, value)
        }

    var scale: Int = store.getInt(SCALE, DefaultOptions.scale)
        set(value) {
            field = value
            store.putInt(SCALE, value)
        }

    var enableBootstrap: Boolean = store.getBoolean(ENABLE_BOOTSTRAP, DefaultOptions.enableBootstrap)
        set(value) {
            field = value
            store.putBoolean(ENABLE_BOOTSTRAP, value)
        }

    var enableBorder: Boolean = store.getBoolean(ENABLE_BORDER, DefaultOptions.enableBorder)
        set(value) {
            field = value
            store.putBoolean(ENABLE_BORDER, value)
        }

    var emulatedSystem: EmulatedSystem = store.getInt(EMULATED_SYSTEM, DefaultOptions.emulatedSystem).toEnum()
        set(value) {
            field = value
            store.putInt(EMULATED_SYSTEM, value.ordinal)
        }

    var palette0: Int = colorPalette.getInt(PALETTE_0, DefaultOptions.palette0)
        set(value) {
            field = value
            colorPalette.putInt(PALETTE_0, value)
        }

    var palette1: Int = colorPalette.getInt(PALETTE_1, DefaultOptions.palette1)
        set(value) {
            field = value
            colorPalette.putInt(PALETTE_1, value)
        }

    var palette2: Int = colorPalette.getInt(PALETTE_2, DefaultOptions.palette2)
        set(value) {
            field = value
            colorPalette.putInt(PALETTE_2, value)
        }

    var palette3: Int = colorPalette.getInt(PALETTE_3, DefaultOptions.palette3)
        set(value) {
            field = value
            colorPalette.putInt(PALETTE_3, value)
        }

    var keyLeft: Int = keyBindings.getInt(KEY_LEFT, DefaultOptions.keyLeft)
        set(value) {
            field = value
            keyBindings.putInt(KEY_LEFT, value)
        }

    var keyRight: Int = keyBindings.getInt(KEY_RIGHT, DefaultOptions.keyRight)
        set(value) {
            field = value
            keyBindings.putInt(KEY_RIGHT, value)
        }

    var keyUp: Int = keyBindings.getInt(KEY_UP, DefaultOptions.keyUp)
        set(value) {
            field = value
            keyBindings.putInt(KEY_UP, value)
        }

    var keyDown: Int = keyBindings.getInt(KEY_DOWN, DefaultOptions.keyDown)
        set(value) {
            field = value
            keyBindings.putInt(KEY_DOWN, value)
        }

    var keyA: Int = keyBindings.getInt(KEY_A, DefaultOptions.keyA)
        set(value) {
            field = value
            keyBindings.putInt(KEY_A, value)
        }

    var keyB: Int = keyBindings.getInt(KEY_B, DefaultOptions.keyB)
        set(value) {
            field = value
            keyBindings.putInt(KEY_B, value)
        }

    var keyStart: Int = keyBindings.getInt(KEY_START, DefaultOptions.keyStart)
        set(value) {
            field = value
            keyBindings.putInt(KEY_START, value)
        }

    var keySelect: Int = keyBindings.getInt(KEY_SELECT, DefaultOptions.keySelect)
        set(value) {
            field = value
            keyBindings.putInt(KEY_SELECT, value)
        }

}