package com.arman.kotboy.memory

interface Mappable {

    fun read(address: Address): Byte
    fun write(address: Address, value: Byte)

}