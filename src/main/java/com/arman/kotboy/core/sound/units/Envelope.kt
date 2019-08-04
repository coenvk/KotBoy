package com.arman.kotboy.core.sound.units

class Envelope : ModulationUnit() {

    override val rate: Int = 64

    override fun start() {
        throw UnsupportedOperationException("Operation, start, has not been implemented yet!")
    }

    override fun stop() {
        throw UnsupportedOperationException("Operation, stop, has not been implemented yet!")
    }

    override fun trigger() {
        throw UnsupportedOperationException("Operation, trigger, has not been implemented yet!")
    }

}