package com.arman.kotboy.memory

import kotlin.math.pow

enum class RamSize(val index: Int, val banks: Int) {

    NONE(0x00, 0),
    KB_2(0x01, 1),
    KB_8(0x02, 1),
    KB_32(0x03, 4),
    KB_128(0x04, 16);

    fun size(): Int {
        return (4.0.pow(index) / 2).toInt() * 1024
    }

    companion object {

        operator fun get(i: Int): RamSize {
            for (size: RamSize in values()) {
                if (size.index == i) {
                    return size
                }
            }
            return NONE
        }

    }

}