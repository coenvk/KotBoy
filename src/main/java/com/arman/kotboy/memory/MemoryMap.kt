package com.arman.kotboy.memory

enum class MemoryMap(val startAddress: Address, val endAddress: Address = startAddress) {

    /*
    Interrupt Enable Register
    --------------------------- FFFF
    Internal RAM
    --------------------------- FF80
    Empty but unusable for I/O
    --------------------------- FF4C
    I/O ports
    --------------------------- FF00
    Empty but unusable for I/O
    --------------------------- FEA0
    Sprite Attrib MemoryMap (OAM)
    --------------------------- FE00
    Echo of 8kB Internal RAM
    --------------------------- E000
    8kB Internal RAM
    --------------------------- C000
    8kB switchable RAM bank
    --------------------------- A000
    8kB Video RAM
    --------------------------- 8000 --
    16kB switchable ROM bank          |
    --------------------------- 4000  |= 32kB Cartridge
    16kB ROM bank #0                  |
    --------------------------- 0000 --
    */

    RESTART_00(0x0000),
    RESTART_08(0x0008),
    RESTART_10(0x0010),
    RESTART_18(0x0018),
    RESTART_20(0x0020),
    RESTART_28(0x0028),
    RESTART_30(0x0030),
    RESTART_38(0x0038),
    V_BLANK_INTERRUPT(0x0040),
    LCDC_STATUS_INTERRUPT(0x0048),
    TIMER_OVERFLOW_INTERRUPT(0x0050),
    SERIAL_TRANSFER_COMPLETION_INTERRUPT(0x0058),
    HI_LO_P10_P13_INTERRUPT(0x0060),
    BEGIN_CODE(0x0100, 0x0103),
    NINTENDO_LOGO(0x0104, 0x0133),
    GAME_TITLE(0x0134, 0x0142),
    CGB(0x0143),
    HIGH_LICENSEE_CODE(0x0144),
    LOW_LICENSEE_CODE(0x0145),
    SGB(0x0146),
    CARTRIDGE_TYPE(0x0147),
    ROM_SIZE(0x0148),
    RAM_SIZE(0x0149),
    DESTINATION_CODE(0x014A),
    LICENSEE_CODE(0x014B),
    MASK_ROM_VERSION_NUMBER(0x014C),
    COMPLEMENT_CHECK(0x014D),
    CHECKSUM(0x014E, 0x014F);

    companion object {

        @JvmField
        val NINTENDO_LOGO = intArrayOf(
            0xCE, 0xED, 0x66, 0x66, 0xCC, 0x0D, 0x00, 0x0B, 0x03, 0x73, 0x00, 0x83, 0x00, 0x0C, 0x00, 0x0D,
            0x00, 0x08, 0x11, 0x1F, 0x88, 0x89, 0x00, 0x0E, 0xDC, 0xCC, 0x6E, 0xE6, 0xDD, 0xDD, 0xD9, 0x99,
            0xBB, 0xBB, 0x67, 0x63, 0x6E, 0x0E, 0xEC, 0xCC, 0xDD, 0xDC, 0x99, 0x9F, 0xBB, 0xB9, 0x33, 0x3E
        )

    }

}