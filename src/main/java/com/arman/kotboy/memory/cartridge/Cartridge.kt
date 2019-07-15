package com.arman.kotboy.memory.cartridge

import com.arman.kotboy.Options
import com.arman.kotboy.RomReader
import com.arman.kotboy.cpu.util.hexString
import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.memory.*
import com.arman.kotboy.memory.cartridge.mbc.*

class Cartridge(vararg values: Int, private var bootstrap: Boolean = false) : Memory {

    constructor(file: String, bootstrap: Boolean) : this(*RomReader(file).read(), bootstrap = bootstrap)
    constructor(options: Options) : this(options.file.absolutePath, options.bootstrap)

    private val type: CartridgeType by lazy {
        CartridgeType[values[MemoryMap.CARTRIDGE_TYPE.startAddress]]
    }

    val title: String

    val colorMode: ColorMode by lazy {
        when (values[MemoryMap.CGB.startAddress]) {
            0x80, 0xC0 -> {
                ColorMode.CGB
            }
            else /* 0x00 */ -> {
                ColorMode.DMG
            }
        }
    }

    val sgbIndicator: Boolean by lazy {
        values[MemoryMap.SGB.startAddress] == 3
    }

    val romSize: RomSize by lazy {
        RomSize[values[MemoryMap.ROM_SIZE.startAddress].toUnsignedInt()]
    }

    val ramSize: RamSize by lazy {
        RamSize[values[MemoryMap.RAM_SIZE.startAddress]]
    }

    val destinationCode: DestinationCode by lazy {
        DestinationCode.values()[values[MemoryMap.DESTINATION_CODE.startAddress]]
    }

    private val mbc: Mbc

    init {
        val sb = StringBuilder()
        for (i in MemoryMap.GAME_TITLE.startAddress..MemoryMap.GAME_TITLE.endAddress) {
            val c = values[i]
            if (c == 0) {
                break
            }
            sb.append(c.toChar())
        }
        this.title = sb.toString()

        val ram =
            if (ramSize.size() > 0) {
                Ram(0xA000, 0xA000 + ramSize.size())
            } else null
        val rom = Rom(0x0, values.slice(IntRange(0x0, romSize.size() - 1)).toIntArray())

        this.mbc = when (type.kind) {
            CartridgeType.Kind.MBC1 -> Mbc1(rom, ram)
            CartridgeType.Kind.MBC2 -> Mbc2(rom, ram)
            CartridgeType.Kind.MBC3 -> Mbc3(rom, ram)
            CartridgeType.Kind.MBC5 -> Mbc5(rom, ram)
            else -> RomOnly(rom, ram)
        }
    }

    override fun set(address: Int, value: Int): Boolean {
        if (address == 0xFF50 && value == 0x01) {
            bootstrap = false
        }
        return this.mbc.set(address, value)
    }

    override fun get(address: Int): Int {
        return if (bootstrap && colorMode != ColorMode.CGB && (address in 0x0 until 0x100)) {
            BootRom.DMG[address]
        } else if (bootstrap && colorMode == ColorMode.CGB && (address in 0x0 until 0x100)) {
            BootRom.CGB[address]
        } else if (bootstrap && colorMode == ColorMode.CGB && (address in 0x200 until 0x900)) {
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
}