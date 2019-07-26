package com.arman.kotboy.sound

import com.arman.kotboy.io.IoDevice

abstract class SoundChannel(startAddress: Int, endAddress: Int) : IoDevice(startAddress, endAddress) {

}