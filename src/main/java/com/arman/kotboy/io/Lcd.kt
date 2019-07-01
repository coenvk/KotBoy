package com.arman.kotboy.io

import com.arman.kotboy.AddressSpace
import com.arman.kotboy.KotBoy
import com.arman.kotboy.cpu.util.and
import com.arman.kotboy.cpu.util.at
import com.arman.kotboy.cpu.util.toInt
import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.gpu.Gpu
import com.arman.kotboy.gpu.GrayPalette
import com.arman.kotboy.gui.Display
import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.MemoryMap
import java.io.Serializable

class Lcd(private val gb: KotBoy) :
    IoDevice(IoReg.LCDC.address, IoReg.WX.address) {

    enum class Mode(val cycles: Int) {

        HBlank(204),
        VBlank(456),
        OamSearch(80),
        LcdTransfer(172);

    }

    private val buffer: IntArray = IntArray(Display.WIDTH * Display.HEIGHT)
    private var sprites: Array<Sprite?> = Array(10) { Sprite() }
    private var mode: Mode = Mode.OamSearch

    val oam: AddressSpace
        get() = gb.gpu.oam

    val vram: AddressSpace
        get() = gb.gpu.vram

    var lcdc: Int
        get() {
            return this[IoReg.LCDC.address]
        }
        private set(value) {
            this[IoReg.LCDC.address] = value
        }
    var ly: Int
        get() {
            return this[IoReg.LY.address]
        }
        private set(value) {
            this[IoReg.LY.address] = value
        }
    var lyc: Int
        get() {
            return this[IoReg.LYC.address]
        }
        private set(value) {
            this[IoReg.LYC.address] = value
        }
    var scx: Int
        get() {
            return this[IoReg.SCX.address]
        }
        private set(value) {
            this[IoReg.SCX.address] = value
        }
    var scy: Int
        get() {
            return this[IoReg.SCY.address]
        }
        private set(value) {
            this[IoReg.SCY.address] = value
        }
    var wx: Int
        get() {
            return this[IoReg.WX.address]
        }
        private set(value) {
            this[IoReg.WX.address] = value
        }
    var wy: Int
        get() {
            return this[IoReg.WY.address]
        }
        private set(value) {
            this[IoReg.WY.address] = value
        }
    var stat: Int
        get() {
            return this[IoReg.STAT.address]
        }
        private set(value) {
            this[IoReg.STAT.address] = value
        }
    var bgp: Int
        get() {
            return this[IoReg.BGP.address]
        }
        private set(value) {
            this[IoReg.BGP.address] = value
        }
    var obp0: Int
        get() {
            return this[IoReg.OBP0.address]
        }
        private set(value) {
            this[IoReg.OBP0.address] = value
        }
    var obp1: Int
        get() {
            return this[IoReg.OBP1.address]
        }
        private set(value) {
            this[IoReg.OBP1.address] = value
        }
    var dma: Int
        get() {
            return this[IoReg.DMA.address]
        }
        private set(value) {
            this[IoReg.DMA.address] = value
        }

    override fun reset() {
        super.reset()
        this.lcdc = 0b10000001
        this.stat = 0b00000011
        this.scx = 0
        this.scy = 0
        this.wx = 0
        this.wy = 0
        this.bgp = 0xFC
        this.obp0 = 0xFF
        this.obp1 = 0xFF

        this.mode = Mode.OamSearch
        this.cycles = this.mode.cycles
    }

    override fun set(address: Address, value: Int): Boolean {
        return when (address) {
            IoReg.DMA.address -> {
                val src = (value shl 8).toUnsignedInt()
                for (i in 0 until 0xA0) {
                    oam[i] = this.gb.cpu.mmu[(src + i).toUnsignedInt()]
                }
                super.set(address, value)
            }
            IoReg.LCDC.address -> {
                if (value and 0x80 == 0) {
                    oam.enabled = true
                    vram.enabled = true
                    mode = Mode.HBlank
                    this.ly = 0
                    buffer.fill(GrayPalette[0])
                    gb.display.frameReady(buffer)
                } else if (!isLcdEnabled()) {
                    setMode(Mode.OamSearch)
                    this.cycles = this.mode.cycles - 4
                }
                super.set(address, value)
            }
            else -> super.set(address, value)
        }
    }

    private fun discoverSprites() {
        val h = getSpriteHeight()
        var oamPos = 0
        var num = 0
        while (num < this.sprites.size) {
            if (oamPos < oam.size()) {
                val sy = oam[oamPos++].toUnsignedInt() - 16
                val y = this.ly.toUnsignedInt()
                if (sy <= y && sy + h > y) {
                    sprites[num++]?.update(
                        sy,
                        oam[oamPos++].toUnsignedInt() - 8,
                        oam[oamPos++].toByte(),
                        oam[oamPos++].toByte()
                    )
                } else {
                    oamPos += 3
                }
            } else {
                sprites[num++]?.update(-16, -8, 0, 0)
            }
        }
    }

    private fun setMode(mode: Mode) {
//        oam.enabled = mode != Mode.OamSearch
//        vram.enabled = mode != Mode.LcdTransfer TODO: not working
        this.mode = mode

        if (mode == Mode.VBlank) {
            gb.cpu.interrupt(MemoryMap.V_BLANK_INTERRUPT)
            if (isVBlankCheckEnabled()) {
                gb.cpu.interrupt(MemoryMap.LCDC_STATUS_INTERRUPT)
            }
        }

        if ((isHBlankCheckEnabled() && mode == Mode.HBlank) || (isOamSearchCheckEnabled() && mode == Mode.OamSearch)) {
            gb.cpu.interrupt(MemoryMap.LCDC_STATUS_INTERRUPT)
        }
    }

    override fun tick(cycles: Int) {
        if (isLcdEnabled()) {
            if (--this.cycles <= 0) {
                when (mode) {
                    Mode.OamSearch -> {
                        this.discoverSprites()
                        setMode(Mode.LcdTransfer)
                    }
                    Mode.LcdTransfer -> {
                        this.renderLine()
                        setMode(Mode.HBlank)
                    }
                    Mode.HBlank, Mode.VBlank -> {
                        this.ly = (this.ly.toUnsignedInt() + 1).rem(154)

                        if (isScanlineCheckEnabled() && ly == lyc) {
                            gb.cpu.interrupt(MemoryMap.LCDC_STATUS_INTERRUPT)
                        }

                        if (this.ly.toUnsignedInt() < 144) {
                            setMode(Mode.OamSearch)
                        } else if (this.ly == 144) {
                            setMode(Mode.VBlank)
                            this.gb.display.frameReady(buffer)
                        }
                    }
                }
                this.cycles = mode.cycles
            }
        }
    }

    private fun getBitmapSliver(tileId: Int, scanline: Int, b: Int): Int {
        val address = (tileId * 0x10) + (2 * scanline) + b
        return (vram[address].toUnsignedInt() shl 8) or (vram.get(address + 1).toUnsignedInt())
    }

    private fun renderLine() {
        var bgRow = 0
        val bgy = (this.ly + this.scy).toUnsignedInt()
        val winy = (this.ly - this.wy).toUnsignedInt()

        for (px in 0 until Display.WIDTH) {
            var color = GrayPalette[0]
            var bgIdx = 0
            var bgx = 0
            var y = 0
            var ofs = 0
            var bgVisible: Boolean

            if (isWindowDisplayEnabled() && this.ly.toUnsignedInt() >= wy.toUnsignedInt() && px >= (wx - 0x07).toUnsignedInt()) {
                bgx = (px - wx + 0x07).toUnsignedInt()
                y = winy
                ofs = getWindowTileTableAddr()
                bgVisible = true
            } else if (isBgEnabled()) {
                bgx = (px + scx).toUnsignedInt()
                y = bgy
                ofs = getBgTileTableAddr()
                bgVisible = true
            } else {
                color = GrayPalette[0]
                bgIdx = 0
                bgVisible = false
            }

            if (bgVisible) {
                val tx = bgx.rem(0x08)
                if (px == 0 || tx == 0) {
                    var tileId = vram[ofs + (bgx / 0x08) + (y / 0x08 * 0x20)].toUnsignedInt()
                    if (getTilePatternTableAddr() == Gpu.TILE_PATTERN_TABLE_0) {
                        tileId = tileId.toByte() + 128
                    }
                    bgRow = getBitmapSliver(tileId, y.rem(0x08), getTilePatternTableAddr())
                }
                bgIdx = ((bgRow ushr (15 - tx)) and 1) or (((bgRow ushr (7 - tx)) shl 1) and 2)
                color = GrayPalette[(this.bgp ushr (bgIdx * 2)) and 3]
            }

            if (isSpriteDisplayEnabled()) {
                this.sprites.forEach loop@{ that ->
                    that?.let {
                        if (px >= it.x && px < it.x + 8) {
                            val x = px - it.x
                            val pi = it.y ushr (15 - x) and 1 or ((it.y ushr (7 - x)) shl 1) and 2
                            if (pi != 0 && (bgIdx == 0 || it.priority)) {
                                color = GrayPalette[(it.palette ushr (pi * 2)) and 3]
                                return@loop
                            }
                        }
                    }
                }
            }

            buffer[this.ly.toUnsignedInt() * Display.WIDTH + px] = color
        }
    }

    /*
    LCDC register
    7 6 5 4 3 2 1 0
    | | | | | | | + ----- BG enabled
    | | | | | | + ------- Sprites enabled
    | | | | | + --------- Sprite height (0: 8x8, 1: 8x16)
    | | | | + ----------- BG tile table address (0: 0x9800-0x9bff, 1: 0x9c00-0x9fff)
    | | | + ------------- Tile pattern table address (0: 0x8800-0x97ff, 1: 0x8000-0x8fff)
    | | + --------------- Window enabled
    | + ----------------- Window tile table address (0: 0x9800-0x9bff, 1: 0x9c00-0x9fff)
    + ------------------- Lcd enabled
    */

    fun isBgEnabled(): Boolean {
        return this.lcdc.toByte().at(0)
    }

    fun isSpriteDisplayEnabled(): Boolean {
        return this.lcdc.toByte().at(1)
    }

    fun getSpriteHeight(): Int {
        return if (this.lcdc.toByte().at(2)) 16 else 8
    }

    fun getBgTileTableAddr(): Int {
        return if (this.lcdc.toByte().at(3)) Gpu.BG_TILE_TABLE_1 else Gpu.BG_TILE_TABLE_0
    }

    fun getTilePatternTableAddr(): Int {
        return if (this.lcdc.toByte().at(4)) Gpu.TILE_PATTERN_TABLE_1 else Gpu.TILE_PATTERN_TABLE_0
    }

    fun isWindowDisplayEnabled(): Boolean {
        return this.lcdc.toByte().at(5)
    }

    fun getWindowTileTableAddr(): Int {
        return if (this.lcdc.toByte().at(6)) Gpu.WINDOW_TILE_TABLE_1 else Gpu.WINDOW_TILE_TABLE_0
    }

    fun isLcdEnabled(): Boolean {
        return this.lcdc.toByte().at(7)
    }

    /*
    Status register
    7 6 5 4 3 2 1 0
    | | | | | | + + ----- Screen mode (0: HBlank, 1: VBlank, 2: Oam Search, 3: Lcd Transfer)
    | | | | | + --------- LY = LYC comparison
    | | | | + ----------- HBlank check enabled
    | | | + ------------- VBlank check enabled
    | | + --------------- Oam Search check enabled
    | + ----------------- Scanline check enabled
    + ------------------- X
    */

    fun getScreenMode(): Mode {
        val i = (this.stat.toByte().at(1).toInt() * 2 + this.stat.toByte().at(0).toInt()) and 3
        return Mode.values()[i]
    }

    fun isScanlineEqual(): Boolean {
        return this.stat.toByte().at(2) || this.ly == this.lyc // TODO: either one
    }

    fun isHBlankCheckEnabled(): Boolean {
        return this.stat.toByte().at(3)
    }

    fun isVBlankCheckEnabled(): Boolean {
        return this.stat.toByte().at(4)
    }

    fun isOamSearchCheckEnabled(): Boolean {
        return this.stat.toByte().at(5)
    }

    fun isScanlineCheckEnabled(): Boolean {
        return this.stat.toByte().at(6)
    }

    private inner class Sprite : Serializable {

        var x: Int = 0
        var priority: Boolean = false
        var y: Int = 0
        var palette: Int = 0
        var tileId: Byte = 0

        fun update(y: Int, x: Int, tileId: Byte, flags: Byte) {
            this.tileId = tileId
            this.x = x
            this.priority = ((flags and 0x80) == 0)
            this.palette = if ((flags and 0x10) == 0) obp0 else obp1
            this.y = getRow(tileId, y, ((flags and 0x40) != 0), ((flags and 0x20) != 0))
        }

        private fun getRow(tileId: Byte, y: Int, yflip: Boolean, xflip: Boolean): Int {
            var row = ly.toUnsignedInt() - y
            var tid = tileId
            var bmp: Int
            if (getSpriteHeight() == 16) {
                if (yflip) {
                    row = 15 - row
                }
                tid = ((tid and 0xFE) + row / 8).toByte()
                row = row and 7
            } else if (yflip) {
                row = 7 - row
            }
            bmp = getBitmapSliver(tid.toUnsignedInt(), row, 0)
            if (xflip) {
                return flip((bmp ushr 8).toUnsignedInt()) shl 8 or flip(bmp.toUnsignedInt())
            }
            return bmp
        }

        private fun flip(b: Int): Int {
            var a = (b and 0xAA) ushr 1 or (b and 0x55) shl 1
            a = (a and 0xCC) ushr 2 or (a and 0x33) shl 2
            return (a ushr 4 or a shl 4).toUnsignedInt()
        }

    }

}