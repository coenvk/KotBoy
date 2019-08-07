package com.arman.kotboy.core.io

import com.arman.kotboy.consoles.cgb.gpu.CgbPalette
import com.arman.kotboy.consoles.cgb.memory.CgbVram
import com.arman.kotboy.consoles.gb.gpu.DmgPalette
import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.cpu.util.at
import com.arman.kotboy.core.cpu.util.toInt
import com.arman.kotboy.core.cpu.util.toUnsignedInt
import com.arman.kotboy.core.gpu.Gpu
import com.arman.kotboy.core.gpu.Palette
import com.arman.kotboy.core.gui.Display
import com.arman.kotboy.core.memory.Oam
import com.arman.kotboy.core.memory.Ram
import java.io.Serializable

class Ppu(private val gb: GameBoy) :
        IoDevice(IoReg.LCDC.address, IoReg.WX.address) {

    companion object {

        private const val SPRITES_PER_LINE = 10
        private const val MAX_SPRITES = 40

    }

    enum class Mode(val cycles: Int) {

        HBlank(204),
        VBlank(456),
        OamSearch(80),
        LcdTransfer(172);

    }

    private val buffer: IntArray = IntArray(Display.WIDTH * Display.HEIGHT)

    private val sprites: Array<out Sprite> by lazy {
        if (gb.cart.isCgb()) Array(SPRITES_PER_LINE) { CgbSprite() }
        else Array(SPRITES_PER_LINE) { Sprite() }
    }

    private val colorPalette: Palette by lazy {
        if (gb.cart.isCgb()) CgbPalette()
        else DmgPalette()
    }

    private val oam: Oam
        get() = gb.gpu.oam

    private val vram: Ram
        get() = gb.gpu.vram

    var lcdc: Int
        get() {
            return super.get(IoReg.LCDC.address)
        }
        private set(value) {
            super.set(IoReg.LCDC.address, value)
        }
    var ly: Int
        get() {
            return super.get(IoReg.LY.address)
        }
        private set(value) {
            super.set(IoReg.LY.address, value)
        }
    var lyc: Int
        get() {
            return super.get(IoReg.LYC.address)
        }
        private set(value) {
            super.set(IoReg.LYC.address, value)
        }
    var scx: Int
        get() {
            return super.get(IoReg.SCX.address)
        }
        private set(value) {
            super.set(IoReg.SCX.address, value)
        }
    var scy: Int
        get() {
            return super.get(IoReg.SCY.address)
        }
        private set(value) {
            super.set(IoReg.SCY.address, value)
        }
    var wx: Int
        get() {
            return super.get(IoReg.WX.address)
        }
        private set(value) {
            super.set(IoReg.WX.address, value)
        }
    var wy: Int
        get() {
            return super.get(IoReg.WY.address)
        }
        private set(value) {
            super.set(IoReg.WY.address, value)
        }
    var stat: Int
        get() {
            return super.get(IoReg.STAT.address)
        }
        private set(value) {
            super.set(IoReg.STAT.address, value)
        }
    var dma: Int
        get() {
            return super.get(IoReg.DMA.address)
        }
        private set(value) {
            super.set(IoReg.DMA.address, value)
        }

    override fun reset() {
        super.reset()
        this.lcdc = 0x91
        this.stat = 0x85
        this.ly = 0x00
        this.lyc = 0x00
        this.scx = 0x00
        this.scy = 0x00
        this.wx = 0x00
        this.wy = 0x00

        this.colorPalette.reset()

        setScreenMode(Mode.OamSearch)
        this.cycles = this.getScreenMode().cycles
    }

    override fun set(address: Int, value: Int): Boolean {
        if (this.colorPalette.accepts(address)) {
            return this.colorPalette.set(address, value)
        }
        return when (address) {
            IoReg.DMA.address -> {
                val src = value shl 8
                for (i in 0xFE00..0xFE9F) {
                    oam[i] = this.gb.mmu[src + i - 0xFE00].toUnsignedInt()
                }
                super.set(address, value)
            }
            IoReg.LCDC.address -> {
                if (value and 0x80 == 0) {
                    oam.enabled = true
                    vram.enabled = true
                    setScreenMode(Mode.HBlank)
                    this.ly = 0
                    buffer.fill(colorPalette.bgp(0))
                    gb.display.frameReady(buffer)
                } else if (!isLcdEnabled()) {
                    setScreenMode(Mode.OamSearch)
                    this.cycles = this.getScreenMode().cycles - 4
                }
                super.set(address, value)
            }
            else -> super.set(address, value)
        }
    }

    override fun get(address: Int): Int {
        if (this.colorPalette.accepts(address)) return this.colorPalette[address]
        if (address == IoReg.DMA.address) return 0xFF
        return super.get(address)
    }

    override fun accepts(address: Int): Boolean {
        return super.accepts(address) || this.colorPalette.accepts(address)
    }

    private fun discoverSprites() {
        val h = getSpriteHeight()
        var oamPos = 0xFE00
        var num = 0
        while (num < this.sprites.size) {
            if (oamPos - 0xFE00 < oam.size()) {
                val sy = oam[oamPos++] - 16
                val y = this.ly.toUnsignedInt()
                if (sy <= y && sy + h > y) {
                    sprites[num++].setTo(
                            sy,
                            oam[oamPos++] - 8,
                            oam[oamPos++],
                            oam[oamPos++]
                    )
                } else {
                    oamPos += 3
                }
            } else {
                sprites[num++].setTo(-16, -8, 0, 0)
            }
        }
    }

    private fun setScreenMode(mode: Mode) {
//        oam.enabled = mode != Mode.OamSearch
//        vram.enabled = mode != Mode.LcdTransfer
//        colorPalette.enabled = mode != Mode.LcdTransfer
        val i = Mode.values().indexOf(mode) and 0b11
        this.stat = (this.stat and 0xFC) or i

        if (mode == Mode.VBlank) {
            gb.cpu.interrupt(Interrupt.VBlank)
            if (isVBlankCheckEnabled()) {
                gb.cpu.interrupt(Interrupt.Lcdc)
            }
        }

        if ((isHBlankCheckEnabled() && mode == Mode.HBlank) || (isOamSearchCheckEnabled() && mode == Mode.OamSearch)) {
            gb.cpu.interrupt(Interrupt.Lcdc)
        }
    }

    override fun tick(cycles: Int): Boolean {
        if (isLcdEnabled()) {
            if (--this.cycles <= 0) {
                val mode = getScreenMode()
                when (mode) {
                    Mode.OamSearch -> {
                        this.discoverSprites()
                        setScreenMode(Mode.LcdTransfer)
                    }
                    Mode.LcdTransfer -> {
                        this.renderLine()
                        setScreenMode(Mode.HBlank)
                    }
                    Mode.HBlank, Mode.VBlank -> {
                        this.ly = (this.ly.toUnsignedInt() + 1).rem(154).toUnsignedInt()

                        if (isScanlineCheckEnabled() && isScanlineEqual()) gb.cpu.interrupt(Interrupt.Lcdc)

                        if (this.ly.toUnsignedInt() == 144) {
                            setScreenMode(Mode.VBlank)
                            this.gb.display.frameReady(buffer)
                        } else if (this.ly.toUnsignedInt() < 144) {
                            setScreenMode(Mode.OamSearch)
                        }
                    }
                }
                this.cycles = mode.cycles
            }
            return true
        }
        return false
    }

    private fun renderLine() {
        var tileData = 0
        var tileAttributes = 0
        val bgy = (this.ly + this.scy).toUnsignedInt()
        val winy = (this.ly - this.wy).toUnsignedInt()

        for (px in 0 until Display.WIDTH) {
            var color = colorPalette.bgp(0)
            var pi = 0
            var bgx = 0
            var y = 0
            var bgVisible = false
            var mapAddr = 0

            if (isWindowDisplayEnabled() && this.ly.toUnsignedInt() >= wy.toUnsignedInt() && px >= (wx - 0x07).toUnsignedInt()) {
                bgx = (px - wx + 0x07).toUnsignedInt()
                y = winy
                bgVisible = true
                mapAddr = getWindowTileTableAddr() + ((winy / 0x08) * 0x20)
            } else if (isBgEnabled()) {
                bgx = (px + scx).toUnsignedInt()
                y = bgy
                bgVisible = true
                mapAddr = getBgTileTableAddr() + ((bgy / 0x08) * 0x20)
            }

            if (bgVisible) {
                val tx = bgx.rem(0x08)
                if (px == 0 || tx == 0) {
                    val addr = mapAddr + bgx / 0x08
                    var tileId = vram[addr, 0].toUnsignedInt()
                    if (getTilePatternTableAddr() == Gpu.TILE_PATTERN_TABLE_0) {
                        tileId = tileId.toByte() + 128
                    }
                    this.vram.let {
                        if (it is CgbVram) tileAttributes = it[addr, 1]
                    }
                    tileData = getTileData(tileId, y.rem(0x08), getTilePatternTableAddr(), tileAttributes)
                }
                pi = ((tileData ushr (15 - tx)) and 1) or (((tileData ushr (7 - tx)) shl 1) and 2)
                color = colorPalette.bgp(pi, tileAttributes and 0x7)
            }

            buffer[this.ly.toUnsignedInt() * Display.WIDTH + px] = color

            if (isSpriteDisplayEnabled()) { // TODO: sprite priority -> left before right, etc.
                this.sprites.forEach loop@{
                    if (px >= it.x && px < it.x + 8) {
                        it.render(px, pi)
                    }
                }
            }
        }
    }

    private fun getTileData(
            tileId: Int,
            line: Int,
            tileDataAddr: Int,
            tileAttributes: Int = 0,
            tileHeight: Int = 8
    ): Int {
        val yFlip = tileAttributes.toByte().at(6)
        val vramBank = tileAttributes.toByte().at(3).toInt()
        var newLine = line.rem(0x08)
        val newTileId = tileId.toUnsignedInt()
        if (tileHeight == 16) {
            if (yFlip) {
                newLine = 15 - newLine
            }
            newLine = newLine and 0x7
        } else if (yFlip) {
            newLine = 7 - newLine
        }
        val tileAddr = newTileId.toUnsignedInt() * 0x10 + tileDataAddr
        val addr = tileAddr + newLine * 2
        return (vram[addr, vramBank].toUnsignedInt() shl 8) or (vram[addr + 1, vramBank].toUnsignedInt())
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
    + ------------------- Ppu enabled
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
    | | | | | | + + ----- Screen mode (0: HBlank, 1: VBlank, 2: Oam Search, 3: Ppu Transfer)
    | | | | | + --------- LY == LYC comparison
    | | | | + ----------- HBlank check enabled
    | | | + ------------- VBlank check enabled
    | | + --------------- Oam Search check enabled
    | + ----------------- Scanline check enabled
    + ------------------- X
    */

    fun getScreenMode(): Mode {
        val i = ((this.stat.toByte().at(1).toInt() shl 1) + this.stat.toByte().at(0).toInt()) and 3
        return Mode.values()[i]
    }

    fun isScanlineEqual(): Boolean {
        return this.ly == this.lyc
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

    private open inner class Sprite : Serializable {

        var x: Int = 0
        var y: Int = 0

        var tileData: Int = 0
        var tileId: Int = 0

        var flags: Int = 0

        fun setTo(y: Int, x: Int, tileId: Int, flags: Int) {
            this.x = x
            this.y = y
            this.tileData = getSpriteLine(tileId, y)
            this.tileId = tileId
            this.flags = flags
        }

        fun priority(): Boolean {
            return ((this.flags and (1 shl 7)) == 0)
        }

        fun yFlip(): Boolean {
            return ((this.flags and (1 shl 6)) != 0)
        }

        fun xFlip(): Boolean {
            return ((this.flags and (1 shl 5)) != 0)
        }

        open fun paletteNumber(): Int {
            return this.flags.at(4).toInt()
        }

        open fun vramBank(): Int = 0

        private fun getSpriteLine(tileId: Int, y: Int): Int {
            var line = ly.toUnsignedInt() - y
            var newTileId = tileId
            val height = getSpriteHeight()
            if (height == 16) {
                if (y <= ly.toUnsignedInt() && ly.toUnsignedInt() < y + 8) {
                    newTileId = if (yFlip()) newTileId or 0x01 else newTileId and 0xFE
                }
                if (y + 8 <= ly.toUnsignedInt() && ly.toUnsignedInt() < y + 16) {
                    newTileId = if (yFlip()) newTileId and 0xFE else newTileId or 0x01
                    line -= 8
                }
            }
            return getTileData(
                    newTileId,
                    line,
                    Gpu.TILE_PATTERN_TABLE_1,
                    this.flags,
                    height
            )
        }

        fun render(px: Int, currentPriority: Int) {
            if (currentPriority == 0 || priority()) {
                var x = px - this.x
                if (xFlip()) x = 7 - x // TODO: put in getTileData
                val spi = ((tileData ushr (15 - x)) and 1) or (((tileData ushr (7 - x)) shl 1) and 2)
                if (spi != 0) {
                    val color = colorPalette.obp(spi, paletteNumber())
                    buffer[ly.toUnsignedInt() * Display.WIDTH + px] = color
                }
            }
        }

    }

    private inner class CgbSprite : Sprite() {

        override fun vramBank(): Int {
            return this.flags and (1 shl 3)
        }

        override fun paletteNumber(): Int {
            return this.flags and 0x7
        }

    }
}