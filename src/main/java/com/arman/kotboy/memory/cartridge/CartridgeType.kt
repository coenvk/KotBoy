package com.arman.kotboy.memory.cartridge

enum class CartridgeType(val id: Int, val kind: Kind, val ram: Boolean = false, val battery: Boolean = false) {

    ROM_ONLY(0x00, Kind.ROM),
    MBC1(0x01, Kind.MBC1),
    MBC1_RAM(0x02, Kind.MBC1, ram = true),
    MBC1_RAM_BATTERY(0x03, Kind.MBC1, ram = true, battery = true),
    MBC2(0x05, Kind.MBC2),
    MBC2_BATTERY(0x06, Kind.MBC2, battery = true),
    RAM(0x08, Kind.ROM, ram = true),
    RAM_BATTERY(0x09, Kind.ROM, ram = true, battery = true),
    MMM01(0x0B, Kind.MMM01),
    MMM01_SRAM(0x0C, Kind.MMM01, ram = true),
    MMM01_SRAM_BATTERY(0x0D, Kind.MMM01, ram = true, battery = true),
    MBC3_TIMER_BATTERY(0x0F, Kind.MBC3, battery = true),
    MBC3_TIMER_RAM_BATTERY(0x10, Kind.MBC3, ram = true, battery = true),
    MBC3(0x11, Kind.MBC3),
    MBC3_RAM(0x12, Kind.MBC3, ram = true),
    MBC3_RAM_BATTERY(0x13, Kind.MBC3, ram = true, battery = true),

    MBC5(0x19, Kind.MBC5),
    MBC5_RAM(0x1A, Kind.MBC5, ram = true),
    MBC5_RAM_BATTERY(0x1B, Kind.MBC5, ram = true, battery = true),
    MBC5_RUMBLE(0x1C, Kind.MBC5),
    MBC5_RUMBLE_SRAM(0x1D, Kind.MBC5, ram = true),
    MBC5_RUMBLE_SRAM_BATTERY(0x1E, Kind.MBC5, ram = true, battery = true),

    MBC6(0x20, Kind.MBC6, ram = false, battery = false),
    MBC7_SENSOR_RUMBLE_RAM_BATTERY(0x22, Kind.MBC7, ram = true, battery = true),

    POCKET_CAMERA(0xFC, Kind.POCKET_CAMERA),
    BANDAI_TAMA5(0xFD, Kind.BANDAI_TAMA5),
    HUC3(0xFE, Kind.HUC3),
    HUC1_RAM_BATTERY(0xFF, Kind.HUC1, ram = true, battery = true);

    enum class Kind {
        ROM, MBC1, MMM01, MBC2, MBC3, MBC5, MBC6, MBC7, POCKET_CAMERA, BANDAI_TAMA5, HUC1, HUC3
    }

    companion object {

        operator fun get(id: Int): CartridgeType {
            values().forEach {
                if (it.id == id) {
                    return it
                }
            }
            throw IllegalArgumentException("Unsupported cartridge kind!")
        }

    }

}