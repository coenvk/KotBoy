package com.arman.kotboy.core

import com.arman.kotboy.core.cpu.util.toUnsignedInt
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipInputStream
import javax.activation.UnsupportedDataTypeException

class RomReader(private val file: File) {

    constructor(rom: String) : this(File(rom))

    private fun isExtension(name: String, vararg exts: String): Boolean {
        return if (name.contains('.')) {
            val i = name.lastIndexOf('.')
            name.substring(i + 1) in exts
        } else false
    }

    private fun read(input: InputStream, size: Int): IntArray {
        val bytes = input.readBytes()
        val ints = IntArray(size)
        for (i in ints.indices) {
            ints[i] = bytes[i].toUnsignedInt()
        }
        return ints
    }

    fun read(): IntArray {
        val input = FileInputStream(file)
        if (isExtension(file.name, "zip")) {
            val zis = ZipInputStream(input)
            try {
                var entry: ZipEntry
                while (true) {
                    entry = zis.nextEntry
                    if (entry == null) break
                    val name = entry.name
                    if (isExtension(name, "gb", "gbc", "rom")) {
                        return read(zis, entry.size.toInt())
                    }
                    zis.closeEntry()
                }
            } catch (e: ZipException) {
                throw IllegalArgumentException()
            } finally {
                zis.close()
            }
            throw IllegalArgumentException()
        } else if (isExtension(file.name, "gb", "gbc", "rom")) {
            return read(input, file.length().toInt())
        } else {
            throw UnsupportedDataTypeException("The extension of this file is not a supported rom extension: ${file.name}")
        }
    }

}