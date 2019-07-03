package com.arman.kotboy.io

import com.arman.kotboy.cpu.util.hexString
import com.arman.kotboy.memory.Address
import com.arman.kotboy.memory.AddressSpace

class Serial : AddressSpace(IoReg.SB.address, IoReg.SC.address) {

    /*
     Bit 7 - Transfer Start Flag
     0: Non transfer
     1: Start transfer

     Bit 0 - Shift Clock
     0: External Clock (500KHz Max.)
     1: Internal Clock (8192Hz)

     Transfer is initiated by setting the Transfer Start Flag.
     This bit may be read and is automatically set to 0 at the end of Transfer.

     Transmitting and receiving serial data is done simultaneously.
     The received data is automatically stored in SB.
     */

    override fun set(address: Address, value: Int): Boolean {
        if (address == IoReg.SB.address) {
            return super.set(address, value)
        }
        if (address == IoReg.SC.address && value == 0x81) {
            print(get(IoReg.SB.address).rem(0xFF).toChar())
        }
        return super.set(address, value)
    }

    override fun get(address: Address): Int {
        if (address == IoReg.SC.address) {
            return super.get(address) or 0b01111110
        }
        return super.get(address)
    }

}