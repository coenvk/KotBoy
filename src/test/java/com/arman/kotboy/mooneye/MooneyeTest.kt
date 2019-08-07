package com.arman.kotboy.mooneye

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.cpu.Reg8
import com.arman.kotboy.core.cpu.util.toUnsignedInt
import org.junit.jupiter.api.Assertions.assertTimeoutPreemptively
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.io.File
import java.time.Duration


class MooneyeTest {

    companion object {
        const val TIMEOUT = 30L
    }

    private lateinit var gb: GameBoy

    private fun setup(rom: File) {
        gb = GameBoy(rom)
        gb.reset()
    }

    fun run(rom: File): Boolean {
        setup(rom)

        var div = 0
        while (!isInfiniteLoop() && !gb.stopped) {
            gb.tick()
            if (++div >= 4) {
                div = 0
            }
        }

        return gb.cpu.read(Reg8.A).toUnsignedInt() == 0x00 && gb.cpu.read(Reg8.B).toUnsignedInt() == 0x03 && gb.cpu.read(
                Reg8.C
        ).toUnsignedInt() == 0x05 && gb.cpu.read(Reg8.D).toUnsignedInt() == 0x08 && gb.cpu.read(Reg8.E).toUnsignedInt() == 0x0D && gb.cpu.read(
                Reg8.H
        ).toUnsignedInt() == 0x15 && gb.cpu.read(Reg8.L).toUnsignedInt() == 0x22
    }

    private fun isInfiniteLoop(): Boolean {
        val seq = intArrayOf(0x00, 0x18, 0xFD)
        var i = gb.cpu.PC
        var found = true
        for (v in seq) {
            if (gb.mmu[i++] != v) {
                found = false
                break
            }
        }
        return found
    }

    @Nested
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class AcceptanceTest {

        private fun testRom(rom: String) {
            this@MooneyeTest.testRom("acceptance/$rom")
        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class BitsTest {

            private fun testRom(rom: String) {
                this@AcceptanceTest.testRom("bits/$rom")
            }

            @Test
            fun test01MemOam() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("mem_oam.gb") }
            }

            @Test
            fun test02RegF() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("reg_f.gb") }
            }

            @Test
            fun test03UnusedHwioGS() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("unused_hwio-GS.gb") }
            }

        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class InstrTest {

            private fun testRom(rom: String) {
                this@AcceptanceTest.testRom("instr/$rom")
            }

            @Test
            fun test01Daa() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("daa.gb") }
            }

        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class InterruptsTest {

            private fun testRom(rom: String) {
                this@AcceptanceTest.testRom("interrupts/$rom")
            }

            @Test
            fun test01IePush() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ie_push.gb") }
            }

        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class OamDmaTest {

            private fun testRom(rom: String) {
                this@AcceptanceTest.testRom("oam_dma/$rom")
            }

            @Test
            fun test01Basic() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("basic.gb") }
            }

            @Test
            fun test02RegRead() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("reg_read.gb") }
            }

            @Test
            fun test03SourcesDmgABCmgbS() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("sources-dmgABCmgbS.gb") }
            }

        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class PpuTest {

            private fun testRom(rom: String) {
                this@AcceptanceTest.testRom("ppu/$rom")
            }

            @Test
            fun test01HBlankLyScxTimingGS() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("hblank_ly_scx_timing-GS.gb") }
            }

            @Test
            fun test02Intr12TimingGS() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("intr_1_2_timing-GS.gb") }
            }

            @Test
            fun test03Intr20Timing() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("intr_2_0_timing.gb") }
            }

            @Test
            fun test04Intr2Mode0Timing() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("intr_2_mode0_timing.gb") }
            }

            @Test
            fun test05Intr2Mode0TimingSprites() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("intr_2_mode0_timing_sprites.gb") }
            }

            @Test
            fun test06Intr2Mode3Timing() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("intr_2_mode3_timing.gb") }
            }

            @Test
            fun test07Intr2OamOkTiming() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("intr_2_oam_ok_timing.gb") }
            }

            @Test
            fun test08LcdonTimingDmgABCmgbS() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("lcdon_timing-dmgABCmgbS.gb") }
            }

            @Test
            fun test09LcdonWriteTimingGS() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("lcdon_write_timing-GS.gb") }
            }

            @Test
            fun test10StatIrqBlocking() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("stat_irq_blocking.gb") }
            }

            @Test
            fun test11StatLycOnOff() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("stat_lyc_onoff.gb") }
            }

            @Test
            fun test12VBlankStatIntrGS() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("vblank_stat_intr-GS.gb") }
            }

        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class SerialTest {

            private fun testRom(rom: String) {
                this@AcceptanceTest.testRom("serial/$rom")
            }

            @Test
            fun test01BootSclkAlignDmgABCmgb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_sclk_align-dmgABCmgb.gb") }
            }

        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class TimerTest {

            private fun testRom(rom: String) {
                this@AcceptanceTest.testRom("timer/$rom")
            }

            @Test
            fun test01DivWrite() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("div_write.gb") }
            }

            @Test
            fun test02RapidToggle() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rapid_toggle.gb") }
            }

            @Test
            fun test03Tim00() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim00.gb") }
            }

            @Test
            fun test04Tim00DivTrigger() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim00_div_trigger.gb") }
            }

            @Test
            fun test05Tim01() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim01.gb") }
            }

            @Test
            fun test06Tim01DivTrigger() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim01_div_trigger.gb") }
            }

            @Test
            fun test07Tim10() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim10.gb") }
            }

            @Test
            fun test08Tim10DivTrigger() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim10_div_trigger.gb") }
            }

            @Test
            fun test09Tim11() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim11.gb") }
            }

            @Test
            fun test10Tim11DivTrigger() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tim11_div_trigger.gb") }
            }

            @Test
            fun test11TimaReload() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tima_reload.gb") }
            }

            @Test
            fun test12TimaWriteReloading() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tima_write_reloading.gb") }
            }

            @Test
            fun test13TmaWriteReloading() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("tma_write_reloading.gb") }
            }

        }

        @Test
        fun test01AddSpETiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("add_sp_e_timing.gb") }
        }

        @Test
        fun test02BootDiv2S() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_div2-S.gb") }
        }

        @Test
        fun test03BootDivDmg0() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_div-dmg0.gb") }
        }

        @Test
        fun test04BootDivDmgABCmgb() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_div-dmgABCmgb.gb") }
        }

        @Test
        fun test05BootDivS() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_div-S.gb") }
        }

        @Test
        fun test06BootHwioDmg0() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_hwio-dmg0.gb") }
        }

        @Test
        fun test07BootHwioDmgABCmgb() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_hwio-dmgABCmgb.gb") }
        }

        @Test
        fun test08BootHwioS() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_hwio-S.gb") }
        }

        @Test
        fun test09BootRegsDmg0() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_regs-dmg0.gb") }
        }

        @Test
        fun test10BootRegsDmgABC() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_regs-dmgABC.gb") }
        }

        @Test
        fun test11BootRegsMgb() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_regs-mgb.gb") }
        }

        @Test
        fun test12BootRegsSgb() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_regs-sgb.gb") }
        }

        @Test
        fun test13BootRegsSgb2() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_regs-sgb2.gb") }
        }

        @Test
        fun test14CallCcTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("call_cc_timing.gb") }
        }

        @Test
        fun test15CallCcTiming2() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("call_cc_timing2.gb") }
        }

        @Test
        fun test16CallTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("call_timing.gb") }
        }

        @Test
        fun test17CallTiming2() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("call_timing2.gb") }
        }

        @Test
        fun test18DiTimingGS() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("di_timing-GS.gb") }
        }

        @Test
        fun test19DivTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("div_timing.gb") }
        }

        @Test
        fun test20EiSequence() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ei_sequence.gb") }
        }

        @Test
        fun test21EiTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ei_timing.gb") }
        }

        @Test
        fun test22HaltIme0Ei() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("halt_ime0_ei.gb") }
        }

        @Test
        fun test23HaltIme0NoIntrTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("halt_ime0_nointr_timing.gb") }
        }

        @Test
        fun test24HaltIme1Timing() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("halt_ime1_timing.gb") }
        }

        @Test
        fun test25HaltIme1Timing2GS() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("halt_ime1_timing2-GS.gb") }
        }

        @Test
        fun test26IfIeRegisters() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("if_ie_registers.gb") }
        }

        @Test
        fun test27IntrTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("intr_timing.gb") }
        }

        @Test
        fun test28JpCcTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("jp_cc_timing.gb") }
        }

        @Test
        fun test29JpTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("jp_timing.gb") }
        }

        @Test
        fun test30LdHlSpETiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ld_hl_sp_e_timing.gb") }
        }

        @Test
        fun test31OamDmaRestart() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("oam_dma_restart.gb") }
        }

        @Test
        fun test32OamDmaStart() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("oam_dma_start.gb") }
        }

        @Test
        fun test33OamDmaTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("oam_dma_timing.gb") }
        }

        @Test
        fun test34PopTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("pop_timing.gb") }
        }

        @Test
        fun test35PushTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("push_timing.gb") }
        }

        @Test
        fun test36RapidDiEi() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rapid_di_ei.gb") }
        }

        @Test
        fun test37RetCcTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ret_cc_timing.gb") }
        }

        @Test
        fun test38RetTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ret_timing.gb") }
        }

        @Test
        fun test39RetiIntrTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("reti_intr_timing.gb") }
        }

        @Test
        fun test40RetiTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("reti_timing.gb") }
        }

        @Test
        fun test41RstTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rst_timing.gb") }
        }

    }

    @Nested
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class EmulatorOnlyTest {

        private fun testRom(rom: String) {
            this@MooneyeTest.testRom("emulator-only/$rom")
        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class Mbc1Test {

            private fun testRom(rom: String) {
                this@EmulatorOnlyTest.testRom("mbc1/$rom")
            }

            @Test
            fun test01BitsRamEn() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("bits_ram_en.gb") }
            }

            @Test
            fun test02MulticartRom8Mb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("multicart_rom_8Mb.gb") }
            }

            @Test
            fun test03Ram64Kb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ram_64Kb.gb") }
            }

            @Test
            fun test04Ram256Kb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("ram_256Kb.gb") }
            }

            @Test
            fun test05Rom1Mb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rom_1Mb.gb") }
            }

            @Test
            fun test06Rom2Mb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rom_2Mb.gb") }
            }

            @Test
            fun test07Rom4Mb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rom_4Mb.gb") }
            }

            @Test
            fun test08Rom8Mb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rom_8Mb.gb") }
            }

            @Test
            fun test09Rom16Mb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rom_16Mb.gb") }
            }

            @Test
            fun test10Rom512Kb() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("rom_512Kb.gb") }
            }

        }

    }

    @Nested
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class ManualOnlyTest {

        private fun testRom(rom: String) {
            this@MooneyeTest.testRom("manual-only/$rom")
        }

        @Test
        fun test01SpritePriority() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("sprite_priority.gb") }
        }

    }

    @Nested
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class MiscTest {

        private fun testRom(rom: String) {
            this@MooneyeTest.testRom("misc/$rom")
        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class BitsTest {

            private fun testRom(rom: String) {
                this@MiscTest.testRom("bits/$rom")
            }

            @Test
            fun test01UnusedHwioC() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("unused_hwio-C.gb") }
            }

        }

        @Nested
        @TestMethodOrder(MethodOrderer.Alphanumeric::class)
        inner class PpuTest {

            private fun testRom(rom: String) {
                this@MiscTest.testRom("ppu/$rom")
            }

            @Test
            fun test01VBlankStatIntrC() {
                assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("vblank_stat_intr-C.gb") }
            }

        }

        @Test
        fun test01BootDivA() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_div-A.gb") }
        }

        @Test
        fun test02BootDivCgb0() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_div-cgb0.gb") }
        }

        @Test
        fun test03BootDivCgbABCDE() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_div-cgbABCDE.gb") }
        }

        @Test
        fun test04BootHwioC() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_hwio-C.gb") }
        }

        @Test
        fun test05BootRegsA() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_regs-A.gb") }
        }

        @Test
        fun test06BootRegsCgb() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("boot_regs-cgb.gb") }
        }

    }

    @Nested
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class UtilsTest {

        private fun testRom(rom: String) {
            this@MooneyeTest.testRom("utils/$rom")
        }

        @Test
        fun test01BootromDumper() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("bootrom_dumper.gb") }
        }

        @Test
        fun test02DumpBootHwio() {
            assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("dump_boot_hwio.gb") }
        }

    }

    private fun testRom(rom: String) {
        val url = javaClass.classLoader.getResource("roms/mooneye/$rom")
        if (url != null) run(File(url.toURI()))
        else {
            val dir = "${System.getProperty("user.dir")}/src/test/resources/roms/mooneye/$rom"
            run(File(dir))
        }
    }

}