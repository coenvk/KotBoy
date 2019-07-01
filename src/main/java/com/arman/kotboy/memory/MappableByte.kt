package com.arman.kotboy.memory

import java.io.Serializable

class MappableByte : Mappable, Serializable {

    private var data: Byte = 0

    override fun read(address: Address): Byte {
        return this.data
    }

    override fun write(address: Address, value: Byte) {
        this.data = value
    }

}