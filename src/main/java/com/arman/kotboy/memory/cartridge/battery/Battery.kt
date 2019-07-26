package com.arman.kotboy.memory.cartridge.battery

import com.arman.kotboy.Options
import com.arman.kotboy.cpu.util.shl
import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.memory.Ram
import com.arman.kotboy.memory.cartridge.rtc.Rtc
import java.io.File
import kotlin.math.min

class Battery(options: Options) {

    private val savFile: File by lazy {
        File(options.file.parent, "${options.file.nameWithoutExtension}.sav")
    }

    private val rtcFile: File by lazy {
        File(options.file.parent, "${options.file.nameWithoutExtension}.rtc")
    }

    fun save(ram: Ram) {
        savFile.writeBytes(ram.toBytes())
    }

    fun saveRtc(rtc: Rtc) {
        val data = rtc.serialze()
        val buffer = ByteArray(4 * data.size)
        var i = 0
        while (i < min(4 * data.size, buffer.size)) {
            val value = data[i / 4]
            buffer[i++] = (value and 0xFF).toByte()
            buffer[i++] = ((value shr 8) and 0xFF).toByte()
            buffer[i++] = ((value shr 16) and 0xFF).toByte()
            buffer[i++] = ((value shr 24) and 0xFF).toByte()
        }
        rtcFile.writeBytes(buffer)
    }

    fun load(ram: Ram): Boolean {
        if (!this.savFile.exists()) return false
        val enabled = ram.enabled

        ram.enabled = true
        var len = this.savFile.length()
        len -= len.rem(0x2000)
        val buffer = this.savFile.readBytes()
        for (i in 0 until min(ram.size(), len.toInt())) {
            ram[i + 0xA000] = buffer[i].toUnsignedInt()
        }
        ram.enabled = enabled

        return true
    }

    fun loadRtc(rtc: Rtc): Boolean {
        if (!this.rtcFile.exists()) return false

        val buffer = this.rtcFile.readBytes()
        val data = LongArray(11)
        var i = 0
        while (i < min(4 * data.size, buffer.size)) {
            val j = i
            val d = buffer[i++]
            val c = buffer[i++]
            val b = buffer[i++]
            val a = buffer[i++]
            data[j] = ((a shl 24) or (b shl 16) or (c shl 8) or d.toUnsignedInt()).toLong()
        }

        rtc.deserialize(data)
        return true
    }

}