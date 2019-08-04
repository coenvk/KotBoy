package com.arman.kotboy.core.sound.units

abstract class ModulationUnit {

    abstract val rate: Int

    abstract fun start()
    abstract fun stop()
    abstract fun trigger()

}