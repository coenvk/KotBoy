package com.arman.kotboy.memory.cartridge

import com.arman.kotboy.AddressSpace
import com.arman.kotboy.memory.*
import com.arman.kotboy.memory.cartridge.mbc.*

class Cartridge(startAddress: Address, vararg values: Int) : AddressSpace(startAddress, values) {

    private val type: CartridgeType = CartridgeType[this.values[0x0147]]

    val title: String

    private val colorMode: ColorMode
        get() {
            return when (this.values[0x143]) {
                0x80, 0xC0 -> {
                    ColorMode.CGB
                }
                else /* 0x00 */ -> {
                    ColorMode.DMG
                }
            }
        }

    private val sgbIndicator: Boolean
        get() {
            return this.values[0x146] == 3
        }

    private val romSize: RomSize
        get() {
            return RomSize[this.values[0x148]]
        }

    private val ramSize: RamSize
        get() {
            return RamSize[this.values[0x149]]
        }

    private val destinationCode: DestinationCode
        get() {
            return DestinationCode.values()[this.values[0x14A]]
        }

    private val mbc: Mbc

    init {
        val sb = StringBuilder()
        for (i in 0x134..0x142) {
            val c = values[i]
            if (c == 0) {
                break
            }
            sb.append(c.toChar())
        }
        this.title = sb.toString()

        val ram = Ram(0xA000, 0xA000 + ramSize.size())
        val rom = Rom(0x0, this.values)

        this.mbc = when (type.kind) {
            CartridgeType.Kind.MBC1 -> Mbc1(rom, ram)
            CartridgeType.Kind.MBC2 -> Mbc2(rom, ram)
            CartridgeType.Kind.MBC3 -> Mbc3(rom, ram)
            CartridgeType.Kind.MBC5 -> Mbc5(rom, ram)
            else -> RomOnly(rom, ram)
        }
    }

    override fun get(address: Address): Int {
        val bootstrap: Boolean = false
        return if (bootstrap && colorMode != ColorMode.CGB && (address in 0x0 until 0x100)) {
            BootRom.DMG[address]
        } else if (bootstrap && colorMode == ColorMode.CGB && (address in 0x0 until 0x100)) {
            BootRom.CGB[address]
        } else if (bootstrap && colorMode == ColorMode.CGB && (address in 0x200 until 0x900)) {
            BootRom.CGB[address - 0x100]
        } else if (address == 0xFF50) {
            0xFF
        } else {
            super.get(address)
        }
    }

}