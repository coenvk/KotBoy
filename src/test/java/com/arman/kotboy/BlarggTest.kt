package com.arman.kotboy

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BlarggTest : MemoryTest() {

    @Test
    fun testCgbSound() {
        testRom("cgb_sound.zip")
    }

    @Test
    fun testCpu() {
        testRom("cpu_instrs.zip")
    }

    @Test
    fun testDmgSound() {
        testRom("dmg_sound.zip")
    }

    @Test
    fun testHaltBug() {
        testRom("halt_bug.zip")
    }

    @Test
    fun testInstrTiming() {
        testRom("instr_timing.zip")
    }

    @Test
    fun testInterruptTime() {
        testRom("interrupt_time.zip")
    }

    @Test
    fun testMemTiming() {
        testRom("mem_timing.zip")
    }

    @Test
    fun testMemTiming2() {
        testRom("mem_timing-2.zip")
    }

    @Test
    fun testOamBug() {
        testRom("oam_bug.zip")
    }

    private fun testRom(rom: String) {
        run("blargg/$rom")
        assertEquals(0, status, "Non-zero return value")
        println(text)
    }

}