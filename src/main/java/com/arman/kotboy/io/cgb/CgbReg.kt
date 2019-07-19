package com.arman.kotboy.io.cgb

enum class CgbReg(val address: Int) {

    KEY1(0xFF4D),
    VBK(0xFF4F),
    HDMA1(0xFF51),
    HDMA2(0xFF52),
    HDMA3(0xFF53),
    HDMA4(0xFF54),
    HDMA5(0xFF55),
    RP(0xFF56),
    FF6C(0xFF6C),
    SVBK(0xFF70),
    FF72(0xFF72),
    FF73(0xFF73),
    FF74(0xFF74),
    FF75(0xFF75),
    FF76(0xFF76),
    FF77(0xFF77);

}