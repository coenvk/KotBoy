package com.arman.kotboy.memory.cartridge

enum class CartridgeType(val id: Int, val type: Type, val ram: Boolean = false, val battery: Boolean = false) {

    ROM(0x00, Type.ROM),
    MBC1(0x01, Type.MBC1),
    MBC1_RAM(0x02, Type.MBC1, ram = true),
    MBC1_RAM_BATTERY(0x03, Type.MBC1, ram = true, battery = true),
    MBC2(0x05, Type.MBC2),
    MBC2_BATTERY(0x06, Type.MBC2, battery = true),
    ROM_RAM(0x08, Type.ROM, ram = true),
    ROM_RAM_BATTERY(0x09, Type.ROM, ram = true, battery = true),
    MMM01(0x0B, Type.MMM01),
    MMM01_RAM(0x0C, Type.MMM01, ram = true),
    MMM01_RAM_BATTERY(0x0D, Type.MMM01, ram = true, battery = true),
    MBC3_TIMER_BATTERY(0x0F, Type.MBC3, battery = true),
    MBC3_TIMER_RAM_BATTERY(0x10, Type.MBC3, ram = true, battery = true),
    MBC3(0x11, Type.MBC3),
    MBC3_RAM(0x12, Type.MBC3, ram = true),
    MBC3_RAM_BATTERY(0x13, Type.MBC3, ram = true, battery = true),
    MBC4(0x15, Type.MBC4),
    MBC4_RAM(0x16, Type.MBC4, ram = true),
    MBC4_RAM_BATTERY(0x17, Type.MBC4, ram = true, battery = true),
    MBC5(0x19, Type.MBC5),
    MBC5_RAM(0x1A, Type.MBC5, ram = true),
    MBC5_RAM_BATTERY(0x1B, Type.MBC5, ram = true, battery = true),
    MBC5_RUMBLE(0x1C, Type.MBC5),
    MBC5_RUMBLE_RAM(0x1D, Type.MBC5, ram = true),
    MBC5_RUMBLE_RAM_BATTERY(0x1E, Type.MBC5, ram = true, battery = true),
    POCKET_CAMERA(0xFC, Type.POCKET_CAMERA),
    BANDAI_TAMA5(0xFD, Type.BANDAI_TAMA5),
    HuC3(0xFE, Type.HuC3),
    HuC1_RAM_BATTERY(0xFF, Type.HuC1, ram = true, battery = true);

    enum class Type {
        ROM, MBC1, MMM01, MBC2, MBC3, MBC4, MBC5, POCKET_CAMERA, BANDAI_TAMA5, HuC1, HuC3
    }

}