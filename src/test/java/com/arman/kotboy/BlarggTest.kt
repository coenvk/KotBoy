package com.arman.kotboy

import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths

class BlarggTest {

    @Test
    fun testCpu() {
        
    }

    private fun getPath(name: String): Path {
        return Paths.get("src/test/resources/roms/blargg", name)
    }

}