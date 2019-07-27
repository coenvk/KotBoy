package com.arman.kotboy.memory.cartridge

import com.arman.kotboy.Options
import com.arman.kotboy.RomReader
import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.memory.*
import com.arman.kotboy.memory.cartridge.battery.Battery
import com.arman.kotboy.memory.cartridge.mbc.*
import java.io.File
import kotlin.system.exitProcess

class Cartridge(private val options: Options) : Memory {

    constructor(file: File) : this(Options(file))
    constructor(filename: String) : this(File(filename))

    private val type: CartridgeType
    private val colorMode: ColorMode
    private val sgbIndicator: Boolean
    private val romSize: RomSize
    private val ramSize: RamSize
    private val destinationCode: DestinationCode
    private val manufacturerCode: String
    private val licenseeCode: String
    private val versionNumber: Int
    private val headerChecksum: Int
    private val globalChecksum: Int

    val title: String

    private val mbc: Mbc

    private var bootstrap: Boolean = options.bootstrap

    init {
        val values = RomReader(options.file).read()
        this.type = CartridgeType[values[MemoryMap.CARTRIDGE_TYPE.startAddress]]

        val sb = StringBuilder()
        for (i in MemoryMap.GAME_TITLE.startAddress..MemoryMap.GAME_TITLE.endAddress) {
            val c = values[i]
            if (c == 0) {
                break
            }
            sb.append(c.toChar())
        }
        this.title = sb.toString()

        sb.clear()
        for (i in MemoryMap.MANUFACTURER_CODE.startAddress..MemoryMap.MANUFACTURER_CODE.endAddress) {
            val c = values[i]
            sb.append(c.toChar())
        }
        this.manufacturerCode = sb.toString()

        val oldLicenseeCode = values[MemoryMap.OLD_LICENSEE_CODE.startAddress]
        this.licenseeCode = if (oldLicenseeCode == 0x33) {
            sb.clear()
            for (i in MemoryMap.NEW_LICENSEE_CODE.startAddress..MemoryMap.NEW_LICENSEE_CODE.endAddress) {
                val c = values[i]
                sb.append(c.toChar())
            }
            sb.toString()
        } else oldLicenseeCode.toChar().toString()

        this.colorMode = when (values[MemoryMap.CGB_FLAG.startAddress]) {
            0x80, 0xC0 -> {
                ColorMode.CGB
            }
            else /* 0x00 */ -> {
                ColorMode.DMG
            }
        }

        this.sgbIndicator = values[MemoryMap.SGB_FLAG.startAddress] == 0x03 && oldLicenseeCode == 0x33

        this.romSize = RomSize[values[MemoryMap.ROM_SIZE.startAddress].toUnsignedInt()]
        this.ramSize = RamSize[values[MemoryMap.RAM_SIZE.startAddress]]

        this.destinationCode = DestinationCode.values()[values[MemoryMap.DESTINATION_CODE.startAddress]]

        this.versionNumber = values[MemoryMap.MASK_ROM_VERSION_NUMBER.startAddress]

        this.headerChecksum = values[MemoryMap.HEADER_CHECKSUM.startAddress]
        this.globalChecksum =
            (values[MemoryMap.GLOBAL_CHECKSUM.startAddress] shl 8) or values[MemoryMap.GLOBAL_CHECKSUM.endAddress]

        if (!this.verifyChecksum(values) || !this.verifyNintentoLogo(values)) { // TODO: stop / lock gameboy
            exitProcess(-1)
        }

        val ram =
            if (ramSize.size() > 0) {
                Ram(0xA000, 0xA000 + ramSize.size())
            } else null
        val rom = Rom(0x0, values.slice(IntRange(0x0, romSize.size() - 1)).toIntArray())

        val battery = Battery(options)

        this.mbc = when (type.kind) {
            CartridgeType.Kind.MBC1 -> Mbc1(rom, ram, battery)
            CartridgeType.Kind.MBC2 -> Mbc2(rom, ram, battery)
            CartridgeType.Kind.MBC3 -> Mbc3(rom, ram, battery)
            CartridgeType.Kind.MBC5 -> Mbc5(rom, ram, battery)
            else -> RomOnly(rom, ram)
        }

        this.mbc.load()
    }

    private fun verifyChecksum(values: IntArray): Boolean {
        var x = 0
        for (i in 0x0134..0x014C) {
            x = x - values[i] - 1
        }
        return x.toUnsignedInt() == this.headerChecksum.toUnsignedInt()
    }

    private fun verifyNintentoLogo(values: IntArray): Boolean {
        for (i in 0x00 until 0x30) {
            if (MemoryMap.NINTENDO_LOGO[i] != values[MemoryMap.NINTENDO_LOGO.startAddress + i]) return false
        }
        return true
    }

    override fun set(address: Int, value: Int): Boolean {
        if (address == 0xFF50 && value == 0x01) {
            bootstrap = false
        }
        return this.mbc.set(address, value)
    }

    override fun get(address: Int): Int {
        return if (bootstrap && !isCgb() && (address in 0x0 until 0x100)) {
            BootRom.DMG[address]
        } else if (bootstrap && isCgb() && (address in 0x0 until 0x100)) {
            BootRom.CGB[address]
        } else if (bootstrap && isCgb() && (address in 0x200 until 0x900)) {
            BootRom.CGB[address - 0x100]
        } else {
            return mbc[address]
        }
    }

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || address == 0xFF50
    }

    override fun fill(value: Int) = this.mbc.fill(value)
    override fun range(): IntRange = this.mbc.range()
    override fun reset() = this.mbc.reset()
    override fun clear() = this.mbc.clear()

    fun isCgb(): Boolean {
        return this.colorMode == ColorMode.CGB
    }

    fun isSgb(): Boolean {
        return this.sgbIndicator
    }

    override fun toString(): String = this.mbc.toString()

}