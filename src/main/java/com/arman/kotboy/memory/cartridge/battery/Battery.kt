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

    fun save(ram: Ram, rtc: Rtc? = null) {
        savFile.writeBytes(ram.toBytes())
        rtc?.let { saveRtc(it) }
    }

    private fun saveRtc(rtc: Rtc) {
        val data = rtc.serialize()
        val buffer = ByteArray(4 * data.size)
        var i = 0
        while (i < data.size) {
            var j = i * 4
            val value = data[i++]
            buffer[j++] = (value and 0xFF).toByte()
            buffer[j++] = ((value shr 8) and 0xFF).toByte()
            buffer[j++] = ((value shr 16) and 0xFF).toByte()
            buffer[j] = ((value shr 24) and 0xFF).toByte()
        }
        savFile.appendBytes(buffer)
    }

    fun load(ram: Ram, rtc: Rtc? = null): Boolean {
        if (!this.savFile.exists()) return false
        val enabled = ram.enabled

        ram.enabled = true
        var len = this.savFile.length()
        len -= len.rem(0x2000)
        val buffer = this.savFile.readBytes()
        val offset = min(ram.size(), len.toInt())
        for (i in 0 until offset) {
            ram[i + 0xA000] = buffer[i].toUnsignedInt()
        }
        ram.enabled = enabled

        rtc?.let {
            val rtcBuffer = buffer.sliceArray(offset until buffer.size)
            if (rtcBuffer.size >= 44) {
                loadRtc(it, rtcBuffer)
            }
        }

        return true
    }

    private fun loadRtc(rtc: Rtc, buffer: ByteArray): Boolean {
        val data = LongArray(12)
        var i = 0
        while (i < data.size) {
            var j = i * 4
            val d = buffer[j++].toUnsignedInt()
            val c = buffer[j++] shl 8
            val b = buffer[j++] shl 16
            val a = buffer[j] shl 24
            data[i++] = (a or b or c or d).toLong()
        }

        rtc.deserialize(data)
        return true
    }

}