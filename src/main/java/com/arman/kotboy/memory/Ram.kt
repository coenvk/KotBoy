package com.arman.kotboy.memory

open class Ram : AddressSpace {

    constructor(startAddress: Int, values: IntArray) : super(startAddress, values)
    constructor(startAddress: Int, endAddress: Int) : super(startAddress, endAddress)

}

class Vram : Ram(0x8000, 0x9FFF)

class Wram : Ram(0xC000, 0xFDFF) {

    override fun get(address: Int): Int = super.get(translate(address))

    override fun set(address: Int, value: Int): Boolean = super.set(translate(address), value)

    private fun translate(address: Int): Int {
        return if (address in 0xE000..0xFDFF) {
            address - 0x2000 // address - 0xE000 + 0xC000
        } else address
    }

}

class Oam : Ram(0xFE00, 0xFE9F)

class Hram : Ram(0xFF80, 0xFFFE)
