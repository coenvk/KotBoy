package com.arman.kotboy.blargg

import com.arman.kotboy.core.GameBoy
import com.arman.kotboy.core.cpu.util.lsb
import com.arman.kotboy.core.cpu.util.msb
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTimeoutPreemptively
import java.io.File
import java.time.Duration

class BlarggTest {

    companion object {
        const val TIMEOUT = 30L
        const val INDIVIDUAL_TIMEOUT = 20L
    }

    private lateinit var gb: GameBoy

    private fun setup(rom: File) {
        gb = GameBoy(rom)
        gb.reset()
    }

    fun run(rom: File) {
        setup(rom)

        var div = 0
        while (!isInfiniteLoop() && !gb.stopped) {
            gb.tick()
            if (++div >= 4) {
                div = 0
            }
        }
    }

    private fun isInfiniteLoop(): Boolean {
        var pc = gb.cpu.PC
        var found = false
        for (v in arrayOf(0x18, 0xfe)) {
            if (gb.mmu[pc++] != v) {
                found = true
                break
            }
        }
        if (!found) {
            return true
        }

        pc = gb.cpu.PC
        for (v in arrayOf(0xc3, pc.lsb(), pc.msb())) {
            if (gb.mmu[pc++] != v) {
                return false
            }
        }
        return true
    }

    @Nested
    @Tag("Individual")
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class CgbSoundTest {

        private fun testRom(rom: String) {
            this@BlarggTest.testRom("cgb_sound\\individual\\$rom")
        }

        @Test
        fun test01Registers() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("01-registers.gb") }
        }

        @Test
        fun test02LenCtr() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("02-len ctr.gb") }
        }

        @Test
        fun test03Trigger() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("03-trigger.gb") }
        }

        @Test
        fun test04Sweep() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("04-sweep.gb") }
        }

        @Test
        fun test05SweepDetails() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("05-sweep details.gb") }
        }

        @Test
        fun test06OverflowOnTrigger() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("06-overflow on trigger.gb") }
        }

        @Test
        fun test07LenSweepPeriodSync() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("07-len sweep period sync.gb") }
        }

        @Test
        fun test08LenCtrDuringPower() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("08-len ctr during power.gb") }
        }

        @Test
        fun test09WaveReadWhileOn() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("09-wave read while on.gb") }
        }

        @Test
        fun test10WaveTriggerWhileOn() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("10-wave trigger while on.gb") }
        }

        @Test
        fun test11RegsAfterPower() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("11-regs after power.gb") }
        }

        @Test
        fun test12Wave() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("12-wave.gb") }
        }

    }

    @Nested
    @Tag("Individual")
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class CpuInstrsTest {

        private fun testRom(rom: String) {
            this@BlarggTest.testRom("cpu_instrs\\individual\\$rom")
        }

        @Test
        fun test01Special() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("01-special.gb") }
        }

        @Test
        fun test02Interrupts() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("02-interrupts.gb") }
        }

        @Test
        fun test03OpSPHL() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("03-op sp,hl.gb") }
        }

        @Test
        fun test04OpRImm() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("04-op r,imm.gb") }
        }

        @Test
        fun test05OpRp() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("05-op rp.gb") }
        }

        @Test
        fun test06LdRR() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("06-ld r,r.gb") }
        }

        @Test
        fun test07JrJpCallRetRst() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("07-jr,jp,call,ret,rst.gb") }
        }

        @Test
        fun test08MiscInstrs() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("08-misc instrs.gb") }
        }

        @Test
        fun test09OpRR() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("09-op r,r.gb") }
        }

        @Test
        fun test10BitOps() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("10-bit ops.gb") }
        }

        @Test
        fun test11OpAHL() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("11-op a,(hl).gb") }
        }

    }

    @Nested
    @Tag("Individual")
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class DmgSoundTest {

        private fun testRom(rom: String) {
            this@BlarggTest.testRom("dmg_sound\\individual\\$rom")
        }

        @Test
        fun test01Registers() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("01-registers.gb") }
        }

        @Test
        fun test02LenCtr() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("02-len ctr.gb") }
        }

        @Test
        fun test03Trigger() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("03-trigger.gb") }
        }

        @Test
        fun test04Sweep() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("04-sweep.gb") }
        }

        @Test
        fun test05SweepDetails() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("05-sweep details.gb") }
        }

        @Test
        fun test06OverflowOnTrigger() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("06-overflow on trigger.gb") }
        }

        @Test
        fun test07LenSweepPeriodSync() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("07-len sweep period sync.gb") }
        }

        @Test
        fun test08LenCtrDuringPower() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("08-len ctr during power.gb") }
        }

        @Test
        fun test09WaveReadWhileOn() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("09-wave read while on.gb") }
        }

        @Test
        fun test10WaveTriggerWhileOn() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("10-wave trigger while on.gb") }
        }

        @Test
        fun test11RegsAfterPower() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("11-regs after power.gb") }
        }

        @Test
        fun test12WaveWriteWhileOn() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("12-wave write while on.gb") }
        }

    }

    @Nested
    @Tag("Individual")
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class MemTimingTest {

        private fun testRom(rom: String) {
            this@BlarggTest.testRom("mem_timing\\individual\\$rom")
        }

        @Test
        fun test01ReadTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("01-read_timing.gb") }
        }

        @Test
        fun test02WriteTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("02-write_timing.gb") }
        }

        @Test
        fun test03ModifyTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("03-modify_timing.gb") }
        }

    }

    @Nested
    @Tag("Individual")
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class MemTiming2Test {

        private fun testRom(rom: String) {
            this@BlarggTest.testRom("mem_timing-2\\individual\\$rom")
        }

        @Test
        fun test01ReadTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("01-read_timing.gb") }
        }

        @Test
        fun test02WriteTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("02-write_timing.gb") }
        }

        @Test
        fun test03ModifyTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("03-modify_timing.gb") }
        }

    }

    @Nested
    @Tag("Individual")
    @TestMethodOrder(MethodOrderer.Alphanumeric::class)
    inner class OamBugTest {

        private fun testRom(rom: String) {
            this@BlarggTest.testRom("oam_bug\\individual\\$rom")
        }

        @Test
        fun test01LcdSync() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("1-lcd_sync.gb") }
        }

        @Test
        fun test02Causes() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("2-causes.gb") }
        }

        @Test
        fun test03NonCauses() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("3-non_causes.gb") }
        }

        @Test
        fun test04ScanlineTiming() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("4-scanline_timing.gb") }
        }

        @Test
        fun test05TimingBug() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("5-timing_bug.gb") }
        }

        @Test
        fun test06TimingNoBug() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("6-timing_no_bug.gb") }
        }

        @Test
        fun test07TimingEffect() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("7-timing_effect.gb") }
        }

        @Test
        fun test08InstrEffect() {
            assertTimeoutPreemptively(Duration.ofSeconds(INDIVIDUAL_TIMEOUT)) { testRom("8-instr_effect.gb") }
        }

    }

    @Test
    @Tag("Main")
    fun testCgbSound() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("cgb_sound\\cgb_sound.zip") }
    }

    @Test
    @Tag("Main")
    fun testCpuInstrs() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("cpu_instrs\\cpu_instrs.zip") }
    }

    @Test
    @Tag("Main")
    fun testDmgSound() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("dmg_sound\\dmg_sound.zip") }
    }

    @Test
    @Tag("Main")
    fun testHaltBug() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("halt_bug\\halt_bug.zip") }
    }

    @Test
    @Tag("Main")
    fun testInstrTiming() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("instr_timing\\instr_timing.zip") }
    }

    @Test
    @Tag("Main")
    fun testInterruptTime() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("interrupt_time\\interrupt_time.zip") }
    }

    @Test
    @Tag("Main")
    fun testMemTiming() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("mem_timing\\mem_timing.zip") }
    }

    @Test
    @Tag("Main")
    fun testMemTiming2() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("mem_timing-2\\mem_timing-2.zip") }
    }

    @Test
    @Tag("Main")
    fun testOamBug() {
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT)) { testRom("oam_bug\\oam_bug.zip") }
    }

    private fun testRom(rom: String) {
        val url = javaClass.classLoader.getResource("roms\\blargg\\$rom")
        if (url != null) run(File(url.toURI()))
        else {
            val dir = "${System.getProperty("user.dir")}\\src\\test\\resources\\roms\\blargg\\$rom"
            run(File(dir))
        }
    }

}