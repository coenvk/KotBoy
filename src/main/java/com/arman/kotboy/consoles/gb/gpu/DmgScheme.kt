package com.arman.kotboy.consoles.gb.gpu

enum class DmgScheme(vararg val colors: Int) {

    /*
    Color Palettes from https://lospec.com
     */

    // https://lospec.com/kerrielake
    MIST_GB(0xC4F0C2, 0x5AB9A8, 0x1E606E, 0x2D1B00),
    NYMPH_GB(0xA1EF8C, 0x3FAC95, 0x446176, 0x2C2137),
    ICE_CREAM_GB(0xFFF6D3, 0xF9A875, 0xEB6B6F, 0x7C3F58),
    RUSTIC_GB(0xA96868, 0xEDB4A1, 0x764462, 0x2C2137),
    WISH_G(0x8BE5FF, 0x608FCF, 0x7550E8, 0x622E4C),

    // https://lospec.com/wildleoknight
    NOSTALGIA(0xD0D058, 0xA0A840, 0x708028, 0x405010),
    SPACEHAZE(0xF8E3C4, 0xCC3495, 0x6B1FB1, 0x0B0630),
    PLATINUM(0xE0F0E8, 0xA8C0B0, 0x507868, 0x183030),
    LIGHTGREEN(0xF4FBD0, 0x68CF68, 0x1E9178, 0x05241F),
    DIRTYBOY(0xC4CFA1, 0x8B956D, 0x4D533C, 0x1F1F1F),
    CRIMSON(0xEFF9D6, 0xBA5044, 0x7A1C4B, 0x1B0326),
    BLUE_SENI(0xD0F4F8, 0x70B0C0, 0x3C3468, 0x1C0820),
    PURPLEDAWN(0xEEFDED, 0x9A7BBC, 0x2D757E, 0x001B2E),
    HARSHGREENS(0xBEEB71, 0x6AB417, 0x376D03, 0x172808),


    GREY(0xFFFFFF, 0xAAAAAA, 0x555555, 0x000000),
    GB_POCKET(0xFFFFFF, 0xA9A9A9, 0x545454, 0x000000),
    GB_ORIGINAL(0x9BBC0F, 0x8BAC0F, 0x306230, 0xF380F),
    GB_LIGHT(0x00B581, 0x009A71, 0x00694A, 0x004F3B),
    GB_KIOSK(0xECEDB0, 0xBBBB18, 0x6B6E00, 0x103700),
    SGB(0xFFEFCE, 0xDE944A, 0xAD2921, 0x311852),
    YELLOW(0xF8F078, 0xB0A848, 0x686830, 0x202010),
    RED(0xFFC0C0, 0xFF6060, 0xC00000, 0x600000),
    GREEN(0xC0FFC0, 0x60FF60, 0x00C000, 0x006000),
    BLUE(0xC0C0FF, 0x6060FF, 0x0000C0, 0x000060);

}