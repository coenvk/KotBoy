package com.arman.kotboy.core.memory.cartridge

import com.arman.kotboy.core.RomReader
import com.arman.kotboy.core.cpu.util.toUnsignedInt
import com.arman.kotboy.core.gui.options.EmulatedSystem
import com.arman.kotboy.core.gui.options.Options
import com.arman.kotboy.core.memory.*
import com.arman.kotboy.core.memory.cartridge.mbc.*
import com.arman.kotboy.core.memory.cartridge.mbc.battery.Battery
import java.io.File

class Cartridge(val file: File) : Memory {

    constructor(filename: String) : this(File(filename))

    val type: CartridgeType
    val colorMode: ColorMode
    val sgbIndicator: Boolean
    val romSize: RomSize
    val ramSize: RamSize
    val destinationCode: DestinationCode
    val manufacturerCode: String
    val licenseeCode: String
    val versionNumber: Int
    val headerChecksum: Int
    val globalChecksum: Int

    val title: String

    private val mbc: Mbc

    private var bootstrap: Boolean = Options.enableBootstrap

    init {
        val values = RomReader(file).read()
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

        val ram =
                if (ramSize.size() > 0) {
                    Ram(0xA000, 0xA000 + ramSize.size() - 1)
                } else null
        val rom = Rom(0x0, values.slice(IntRange(0x0, romSize.size() - 1)).toIntArray())

        val battery = Battery(file)

        this.mbc = when (type.kind) {
            CartridgeType.Kind.ROM -> RomOnly(rom)
            CartridgeType.Kind.MBC1 -> Mbc1(rom, ram, battery)
            CartridgeType.Kind.MBC2 -> Mbc2(rom, ram, battery)
            CartridgeType.Kind.MBC3 -> Mbc3(rom, ram, battery)
            CartridgeType.Kind.MBC5 -> Mbc5(rom, ram, battery)
            CartridgeType.Kind.MMM01 -> Mmm01(rom, ram, battery)
            CartridgeType.Kind.BANDAI_TAMA5,
            CartridgeType.Kind.HUC1,
            CartridgeType.Kind.HUC3,
            CartridgeType.Kind.MBC6,
            CartridgeType.Kind.MBC7,
            CartridgeType.Kind.POCKET_CAMERA -> RomOnly(rom) // FIXME: Not implemented
        }

        this.mbc.load()
    }

    fun verify(): Boolean {
        val values = RomReader(file).read()
        return verifyChecksum(values) && verifyNintentoLogo(values)
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
        if (address == 0xFF50 && value != 0x00) {
            bootstrap = false
        }
        return this.mbc.set(address, value)
    }

    override fun get(address: Int): Int {
        val bootstrap = this.bootstrap
        return if (bootstrap && !isCgb() && (address in 0x0 until 0x100)) {
            BootRom.DMG[address]
        } else if (bootstrap && isCgb() && (address in 0x0 until 0x100)) {
            BootRom.CGB[address]
        } else if (bootstrap && isSgb() && (address in 0x0 until 0x100)) {
            BootRom.SGB[address]
        } else if (bootstrap && isCgb() && (address in 0x200 until 0x900)) {
            BootRom.CGB[address - 0x100]
        } else {
            return mbc[address]
        }
    }

    override fun accepts(address: Int): Boolean {
        return this.mbc.accepts(address) || address == 0xFF50
    }

    override fun fill(value: Int) = this.mbc.fill(value)
    override fun range(): IntRange = this.mbc.range()

    override fun reset() {
        this.mbc.reset()
        this.bootstrap = Options.enableBootstrap
    }

    override fun clear() = this.mbc.clear()

    fun isCgb(): Boolean {
        return this.colorMode == ColorMode.CGB && Options.emulatedSystem == EmulatedSystem.CGB
    }

    fun isSgb(): Boolean {
        return this.sgbIndicator && Options.emulatedSystem == EmulatedSystem.SGB
    }

    override fun toString(): String = this.mbc.toString()

}