package com.arman.kotboy.memory

enum class RomSize(val index: Int, val banks: Int) {

    KB_32(0x00, 2),
    KB_64(0x01, 4),
    KB_128(0x02, 8),
    KB_256(0x03, 16),
    KB_512(0x04, 32),
    MB_1(0x05, 64),
    MB_2(0x06, 128),
    MB_1_1(0x52, 72),
    MB_1_2(0x53, 80),
    MB_1_5(0x54, 96);

    fun size(): Int {
        return (32 shl index) * 1024
    }

    fun kbyte(): Int {
        return BANK_KBYTES * this.banks
    }

    fun kbit(): Int {
        return kbyte() * Byte.SIZE_BITS
    }

    fun mbyte(): Int {
        return kbyte() / KBYTE_PER_MBYTE
    }

    fun mbit(): Int {
        return mbyte() * Byte.SIZE_BITS
    }

    companion object {

        const val BANK_KBYTES = 16
        const val KBYTE_PER_MBYTE = 1024

        operator fun get(i: Int): RomSize {
            for (size: RomSize in values()) {
                if (size.index == i) {
                    return size
                }
            }
            return KB_32
        }

    }

}