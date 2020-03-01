package com.arman.kotboy.core.cpu

object InstrSet {

    private val instrs: Array<Instr> = setup()
    private val externalInstrs: Array<Instr> = setupExternal()

    private fun setupInstr(opCode: OpCode, command: ((Cpu, IntArray) -> Unit)): Instr {
        return Instr(opCode) { cpu, args ->
            command(cpu, args)
            opCode.cycles
        }
    }

    private fun setupJumpInstr(opCode: OpCode, command: ((Cpu, IntArray) -> Boolean)): Instr {
        return Instr(opCode) { cpu, args ->
            if (!command(cpu, args)) {
                opCode.cyclesNotTaken
            } else {
                opCode.cycles
            }
        }
    }

    private fun setup(): Array<Instr> {
        return Array(0x100) {
            when (val opCode = OpCode[it]) {
                // opcodes
                OpCode.NOP_00 -> setupInstr(opCode) { cpu, args -> cpu.nop_00(*args) }
                OpCode.LD_01 -> setupInstr(opCode) { cpu, args -> cpu.ld_01(*args) }
                OpCode.LD_02 -> setupInstr(opCode) { cpu, args -> cpu.ld_02(*args) }
                OpCode.INC_03 -> setupInstr(opCode) { cpu, args -> cpu.inc_03(*args) }
                OpCode.INC_04 -> setupInstr(opCode) { cpu, args -> cpu.inc_04(*args) }
                OpCode.DEC_05 -> setupInstr(opCode) { cpu, args -> cpu.dec_05(*args) }
                OpCode.LD_06 -> setupInstr(opCode) { cpu, args -> cpu.ld_06(*args) }
                OpCode.RLCA_07 -> setupInstr(opCode) { cpu, args -> cpu.rlca_07(*args) }
                OpCode.LD_08 -> setupInstr(opCode) { cpu, args -> cpu.ld_08(*args) }
                OpCode.ADD_09 -> setupInstr(opCode) { cpu, args -> cpu.add_09(*args) }
                OpCode.LD_0A -> setupInstr(opCode) { cpu, args -> cpu.ld_0a(*args) }
                OpCode.DEC_0B -> setupInstr(opCode) { cpu, args -> cpu.dec_0b(*args) }
                OpCode.INC_0C -> setupInstr(opCode) { cpu, args -> cpu.inc_0c(*args) }
                OpCode.DEC_0D -> setupInstr(opCode) { cpu, args -> cpu.dec_0d(*args) }
                OpCode.LD_0E -> setupInstr(opCode) { cpu, args -> cpu.ld_0e(*args) }
                OpCode.RRCA_0F -> setupInstr(opCode) { cpu, args -> cpu.rrca_0f(*args) }

                OpCode.STOP_10 -> setupInstr(opCode) { cpu, args -> cpu.stop_10(*args) }
                OpCode.LD_11 -> setupInstr(opCode) { cpu, args -> cpu.ld_11(*args) }
                OpCode.LD_12 -> setupInstr(opCode) { cpu, args -> cpu.ld_12(*args) }
                OpCode.INC_13 -> setupInstr(opCode) { cpu, args -> cpu.inc_13(*args) }
                OpCode.INC_14 -> setupInstr(opCode) { cpu, args -> cpu.inc_14(*args) }
                OpCode.DEC_15 -> setupInstr(opCode) { cpu, args -> cpu.dec_15(*args) }
                OpCode.LD_16 -> setupInstr(opCode) { cpu, args -> cpu.ld_16(*args) }
                OpCode.RLA_17 -> setupInstr(opCode) { cpu, args -> cpu.rla_17(*args) }
                OpCode.JR_18 -> setupInstr(opCode) { cpu, args -> cpu.jr_18(*args) }
                OpCode.ADD_19 -> setupInstr(opCode) { cpu, args -> cpu.add_19(*args) }
                OpCode.LD_1A -> setupInstr(opCode) { cpu, args -> cpu.ld_1a(*args) }
                OpCode.DEC_1B -> setupInstr(opCode) { cpu, args -> cpu.dec_1b(*args) }
                OpCode.INC_1C -> setupInstr(opCode) { cpu, args -> cpu.inc_1c(*args) }
                OpCode.DEC_1D -> setupInstr(opCode) { cpu, args -> cpu.dec_1d(*args) }
                OpCode.LD_1E -> setupInstr(opCode) { cpu, args -> cpu.ld_1e(*args) }
                OpCode.RRA_1F -> setupInstr(opCode) { cpu, args -> cpu.rra_1f(*args) }

                OpCode.JR_20 -> setupJumpInstr(opCode) { cpu, args -> cpu.jr_20(*args) }
                OpCode.LD_21 -> setupInstr(opCode) { cpu, args -> cpu.ld_21(*args) }
                OpCode.LD_22 -> setupInstr(opCode) { cpu, args -> cpu.ld_22(*args) }
                OpCode.INC_23 -> setupInstr(opCode) { cpu, args -> cpu.inc_23(*args) }
                OpCode.INC_24 -> setupInstr(opCode) { cpu, args -> cpu.inc_24(*args) }
                OpCode.DEC_25 -> setupInstr(opCode) { cpu, args -> cpu.dec_25(*args) }
                OpCode.LD_26 -> setupInstr(opCode) { cpu, args -> cpu.ld_26(*args) }
                OpCode.DAA_27 -> setupInstr(opCode) { cpu, args -> cpu.daa_27(*args) }
                OpCode.JR_28 -> setupJumpInstr(opCode) { cpu, args -> cpu.jr_28(*args) }
                OpCode.ADD_29 -> setupInstr(opCode) { cpu, args -> cpu.add_29(*args) }
                OpCode.LD_2A -> setupInstr(opCode) { cpu, args -> cpu.ld_2a(*args) }
                OpCode.DEC_2B -> setupInstr(opCode) { cpu, args -> cpu.dec_2b(*args) }
                OpCode.INC_2C -> setupInstr(opCode) { cpu, args -> cpu.inc_2c(*args) }
                OpCode.DEC_2D -> setupInstr(opCode) { cpu, args -> cpu.dec_2d(*args) }
                OpCode.LD_2E -> setupInstr(opCode) { cpu, args -> cpu.ld_2e(*args) }
                OpCode.CPL_2F -> setupInstr(opCode) { cpu, args -> cpu.cpl_2f(*args) }

                OpCode.JR_30 -> setupJumpInstr(opCode) { cpu, args -> cpu.jr_30(*args) }
                OpCode.LD_31 -> setupInstr(opCode) { cpu, args -> cpu.ld_31(*args) }
                OpCode.LD_32 -> setupInstr(opCode) { cpu, args -> cpu.ld_32(*args) }
                OpCode.INC_33 -> setupInstr(opCode) { cpu, args -> cpu.inc_33(*args) }
                OpCode.INC_34 -> setupInstr(opCode) { cpu, args -> cpu.inc_34(*args) }
                OpCode.DEC_35 -> setupInstr(opCode) { cpu, args -> cpu.dec_35(*args) }
                OpCode.LD_36 -> setupInstr(opCode) { cpu, args -> cpu.ld_36(*args) }
                OpCode.SCF_37 -> setupInstr(opCode) { cpu, args -> cpu.scf_37(*args) }
                OpCode.JR_38 -> setupJumpInstr(opCode) { cpu, args -> cpu.jr_38(*args) }
                OpCode.ADD_39 -> setupInstr(opCode) { cpu, args -> cpu.add_39(*args) }
                OpCode.LD_3A -> setupInstr(opCode) { cpu, args -> cpu.ld_3a(*args) }
                OpCode.DEC_3B -> setupInstr(opCode) { cpu, args -> cpu.dec_3b(*args) }
                OpCode.INC_3C -> setupInstr(opCode) { cpu, args -> cpu.inc_3c(*args) }
                OpCode.DEC_3D -> setupInstr(opCode) { cpu, args -> cpu.dec_3d(*args) }
                OpCode.LD_3E -> setupInstr(opCode) { cpu, args -> cpu.ld_3e(*args) }
                OpCode.CCF_3F -> setupInstr(opCode) { cpu, args -> cpu.ccf_3f(*args) }

                OpCode.LD_40 -> setupInstr(opCode) { cpu, args -> cpu.ld_40(*args) }
                OpCode.LD_41 -> setupInstr(opCode) { cpu, args -> cpu.ld_41(*args) }
                OpCode.LD_42 -> setupInstr(opCode) { cpu, args -> cpu.ld_42(*args) }
                OpCode.LD_43 -> setupInstr(opCode) { cpu, args -> cpu.ld_43(*args) }
                OpCode.LD_44 -> setupInstr(opCode) { cpu, args -> cpu.ld_44(*args) }
                OpCode.LD_45 -> setupInstr(opCode) { cpu, args -> cpu.ld_45(*args) }
                OpCode.LD_46 -> setupInstr(opCode) { cpu, args -> cpu.ld_46(*args) }
                OpCode.LD_47 -> setupInstr(opCode) { cpu, args -> cpu.ld_47(*args) }
                OpCode.LD_48 -> setupInstr(opCode) { cpu, args -> cpu.ld_48(*args) }
                OpCode.LD_49 -> setupInstr(opCode) { cpu, args -> cpu.ld_49(*args) }
                OpCode.LD_4A -> setupInstr(opCode) { cpu, args -> cpu.ld_4a(*args) }
                OpCode.LD_4B -> setupInstr(opCode) { cpu, args -> cpu.ld_4b(*args) }
                OpCode.LD_4C -> setupInstr(opCode) { cpu, args -> cpu.ld_4c(*args) }
                OpCode.LD_4D -> setupInstr(opCode) { cpu, args -> cpu.ld_4d(*args) }
                OpCode.LD_4E -> setupInstr(opCode) { cpu, args -> cpu.ld_4e(*args) }
                OpCode.LD_4F -> setupInstr(opCode) { cpu, args -> cpu.ld_4f(*args) }

                OpCode.LD_50 -> setupInstr(opCode) { cpu, args -> cpu.ld_50(*args) }
                OpCode.LD_51 -> setupInstr(opCode) { cpu, args -> cpu.ld_51(*args) }
                OpCode.LD_52 -> setupInstr(opCode) { cpu, args -> cpu.ld_52(*args) }
                OpCode.LD_53 -> setupInstr(opCode) { cpu, args -> cpu.ld_53(*args) }
                OpCode.LD_54 -> setupInstr(opCode) { cpu, args -> cpu.ld_54(*args) }
                OpCode.LD_55 -> setupInstr(opCode) { cpu, args -> cpu.ld_55(*args) }
                OpCode.LD_56 -> setupInstr(opCode) { cpu, args -> cpu.ld_56(*args) }
                OpCode.LD_57 -> setupInstr(opCode) { cpu, args -> cpu.ld_57(*args) }
                OpCode.LD_58 -> setupInstr(opCode) { cpu, args -> cpu.ld_58(*args) }
                OpCode.LD_59 -> setupInstr(opCode) { cpu, args -> cpu.ld_59(*args) }
                OpCode.LD_5A -> setupInstr(opCode) { cpu, args -> cpu.ld_5a(*args) }
                OpCode.LD_5B -> setupInstr(opCode) { cpu, args -> cpu.ld_5b(*args) }
                OpCode.LD_5C -> setupInstr(opCode) { cpu, args -> cpu.ld_5c(*args) }
                OpCode.LD_5D -> setupInstr(opCode) { cpu, args -> cpu.ld_5d(*args) }
                OpCode.LD_5E -> setupInstr(opCode) { cpu, args -> cpu.ld_5e(*args) }
                OpCode.LD_5F -> setupInstr(opCode) { cpu, args -> cpu.ld_5f(*args) }

                OpCode.LD_60 -> setupInstr(opCode) { cpu, args -> cpu.ld_60(*args) }
                OpCode.LD_61 -> setupInstr(opCode) { cpu, args -> cpu.ld_61(*args) }
                OpCode.LD_62 -> setupInstr(opCode) { cpu, args -> cpu.ld_62(*args) }
                OpCode.LD_63 -> setupInstr(opCode) { cpu, args -> cpu.ld_63(*args) }
                OpCode.LD_64 -> setupInstr(opCode) { cpu, args -> cpu.ld_64(*args) }
                OpCode.LD_65 -> setupInstr(opCode) { cpu, args -> cpu.ld_65(*args) }
                OpCode.LD_66 -> setupInstr(opCode) { cpu, args -> cpu.ld_66(*args) }
                OpCode.LD_67 -> setupInstr(opCode) { cpu, args -> cpu.ld_67(*args) }
                OpCode.LD_68 -> setupInstr(opCode) { cpu, args -> cpu.ld_68(*args) }
                OpCode.LD_69 -> setupInstr(opCode) { cpu, args -> cpu.ld_69(*args) }
                OpCode.LD_6A -> setupInstr(opCode) { cpu, args -> cpu.ld_6a(*args) }
                OpCode.LD_6B -> setupInstr(opCode) { cpu, args -> cpu.ld_6b(*args) }
                OpCode.LD_6C -> setupInstr(opCode) { cpu, args -> cpu.ld_6c(*args) }
                OpCode.LD_6D -> setupInstr(opCode) { cpu, args -> cpu.ld_6d(*args) }
                OpCode.LD_6E -> setupInstr(opCode) { cpu, args -> cpu.ld_6e(*args) }
                OpCode.LD_6F -> setupInstr(opCode) { cpu, args -> cpu.ld_6f(*args) }

                OpCode.LD_70 -> setupInstr(opCode) { cpu, args -> cpu.ld_70(*args) }
                OpCode.LD_71 -> setupInstr(opCode) { cpu, args -> cpu.ld_71(*args) }
                OpCode.LD_72 -> setupInstr(opCode) { cpu, args -> cpu.ld_72(*args) }
                OpCode.LD_73 -> setupInstr(opCode) { cpu, args -> cpu.ld_73(*args) }
                OpCode.LD_74 -> setupInstr(opCode) { cpu, args -> cpu.ld_74(*args) }
                OpCode.LD_75 -> setupInstr(opCode) { cpu, args -> cpu.ld_75(*args) }
                OpCode.HALT_76 -> setupInstr(opCode) { cpu, args -> cpu.halt_76(*args) }
                OpCode.LD_77 -> setupInstr(opCode) { cpu, args -> cpu.ld_77(*args) }
                OpCode.LD_78 -> setupInstr(opCode) { cpu, args -> cpu.ld_78(*args) }
                OpCode.LD_79 -> setupInstr(opCode) { cpu, args -> cpu.ld_79(*args) }
                OpCode.LD_7A -> setupInstr(opCode) { cpu, args -> cpu.ld_7a(*args) }
                OpCode.LD_7B -> setupInstr(opCode) { cpu, args -> cpu.ld_7b(*args) }
                OpCode.LD_7C -> setupInstr(opCode) { cpu, args -> cpu.ld_7c(*args) }
                OpCode.LD_7D -> setupInstr(opCode) { cpu, args -> cpu.ld_7d(*args) }
                OpCode.LD_7E -> setupInstr(opCode) { cpu, args -> cpu.ld_7e(*args) }
                OpCode.LD_7F -> setupInstr(opCode) { cpu, args -> cpu.ld_7f(*args) }

                OpCode.ADD_80 -> setupInstr(opCode) { cpu, args -> cpu.add_80(*args) }
                OpCode.ADD_81 -> setupInstr(opCode) { cpu, args -> cpu.add_81(*args) }
                OpCode.ADD_82 -> setupInstr(opCode) { cpu, args -> cpu.add_82(*args) }
                OpCode.ADD_83 -> setupInstr(opCode) { cpu, args -> cpu.add_83(*args) }
                OpCode.ADD_84 -> setupInstr(opCode) { cpu, args -> cpu.add_84(*args) }
                OpCode.ADD_85 -> setupInstr(opCode) { cpu, args -> cpu.add_85(*args) }
                OpCode.ADD_86 -> setupInstr(opCode) { cpu, args -> cpu.add_86(*args) }
                OpCode.ADD_87 -> setupInstr(opCode) { cpu, args -> cpu.add_87(*args) }
                OpCode.ADC_88 -> setupInstr(opCode) { cpu, args -> cpu.adc_88(*args) }
                OpCode.ADC_89 -> setupInstr(opCode) { cpu, args -> cpu.adc_89(*args) }
                OpCode.ADC_8A -> setupInstr(opCode) { cpu, args -> cpu.adc_8a(*args) }
                OpCode.ADC_8B -> setupInstr(opCode) { cpu, args -> cpu.adc_8b(*args) }
                OpCode.ADC_8C -> setupInstr(opCode) { cpu, args -> cpu.adc_8c(*args) }
                OpCode.ADC_8D -> setupInstr(opCode) { cpu, args -> cpu.adc_8d(*args) }
                OpCode.ADC_8E -> setupInstr(opCode) { cpu, args -> cpu.adc_8e(*args) }
                OpCode.ADC_8F -> setupInstr(opCode) { cpu, args -> cpu.adc_8f(*args) }

                OpCode.SUB_90 -> setupInstr(opCode) { cpu, args -> cpu.sub_90(*args) }
                OpCode.SUB_91 -> setupInstr(opCode) { cpu, args -> cpu.sub_91(*args) }
                OpCode.SUB_92 -> setupInstr(opCode) { cpu, args -> cpu.sub_92(*args) }
                OpCode.SUB_93 -> setupInstr(opCode) { cpu, args -> cpu.sub_93(*args) }
                OpCode.SUB_94 -> setupInstr(opCode) { cpu, args -> cpu.sub_94(*args) }
                OpCode.SUB_95 -> setupInstr(opCode) { cpu, args -> cpu.sub_95(*args) }
                OpCode.SUB_96 -> setupInstr(opCode) { cpu, args -> cpu.sub_96(*args) }
                OpCode.SUB_97 -> setupInstr(opCode) { cpu, args -> cpu.sub_97(*args) }
                OpCode.SBC_98 -> setupInstr(opCode) { cpu, args -> cpu.sbc_98(*args) }
                OpCode.SBC_99 -> setupInstr(opCode) { cpu, args -> cpu.sbc_99(*args) }
                OpCode.SBC_9A -> setupInstr(opCode) { cpu, args -> cpu.sbc_9a(*args) }
                OpCode.SBC_9B -> setupInstr(opCode) { cpu, args -> cpu.sbc_9b(*args) }
                OpCode.SBC_9C -> setupInstr(opCode) { cpu, args -> cpu.sbc_9c(*args) }
                OpCode.SBC_9D -> setupInstr(opCode) { cpu, args -> cpu.sbc_9d(*args) }
                OpCode.SBC_9E -> setupInstr(opCode) { cpu, args -> cpu.sbc_9e(*args) }
                OpCode.SBC_9F -> setupInstr(opCode) { cpu, args -> cpu.sbc_9f(*args) }

                OpCode.AND_A0 -> setupInstr(opCode) { cpu, args -> cpu.and_a0(*args) }
                OpCode.AND_A1 -> setupInstr(opCode) { cpu, args -> cpu.and_a1(*args) }
                OpCode.AND_A2 -> setupInstr(opCode) { cpu, args -> cpu.and_a2(*args) }
                OpCode.AND_A3 -> setupInstr(opCode) { cpu, args -> cpu.and_a3(*args) }
                OpCode.AND_A4 -> setupInstr(opCode) { cpu, args -> cpu.and_a4(*args) }
                OpCode.AND_A5 -> setupInstr(opCode) { cpu, args -> cpu.and_a5(*args) }
                OpCode.AND_A6 -> setupInstr(opCode) { cpu, args -> cpu.and_a6(*args) }
                OpCode.AND_A7 -> setupInstr(opCode) { cpu, args -> cpu.and_a7(*args) }
                OpCode.XOR_A8 -> setupInstr(opCode) { cpu, args -> cpu.xor_a8(*args) }
                OpCode.XOR_A9 -> setupInstr(opCode) { cpu, args -> cpu.xor_a9(*args) }
                OpCode.XOR_AA -> setupInstr(opCode) { cpu, args -> cpu.xor_aa(*args) }
                OpCode.XOR_AB -> setupInstr(opCode) { cpu, args -> cpu.xor_ab(*args) }
                OpCode.XOR_AC -> setupInstr(opCode) { cpu, args -> cpu.xor_ac(*args) }
                OpCode.XOR_AD -> setupInstr(opCode) { cpu, args -> cpu.xor_ad(*args) }
                OpCode.XOR_AE -> setupInstr(opCode) { cpu, args -> cpu.xor_ae(*args) }
                OpCode.XOR_AF -> setupInstr(opCode) { cpu, args -> cpu.xor_af(*args) }

                OpCode.OR_B0 -> setupInstr(opCode) { cpu, args -> cpu.or_b0(*args) }
                OpCode.OR_B1 -> setupInstr(opCode) { cpu, args -> cpu.or_b1(*args) }
                OpCode.OR_B2 -> setupInstr(opCode) { cpu, args -> cpu.or_b2(*args) }
                OpCode.OR_B3 -> setupInstr(opCode) { cpu, args -> cpu.or_b3(*args) }
                OpCode.OR_B4 -> setupInstr(opCode) { cpu, args -> cpu.or_b4(*args) }
                OpCode.OR_B5 -> setupInstr(opCode) { cpu, args -> cpu.or_b5(*args) }
                OpCode.OR_B6 -> setupInstr(opCode) { cpu, args -> cpu.or_b6(*args) }
                OpCode.OR_B7 -> setupInstr(opCode) { cpu, args -> cpu.or_b7(*args) }
                OpCode.CP_B8 -> setupInstr(opCode) { cpu, args -> cpu.cp_b8(*args) }
                OpCode.CP_B9 -> setupInstr(opCode) { cpu, args -> cpu.cp_b9(*args) }
                OpCode.CP_BA -> setupInstr(opCode) { cpu, args -> cpu.cp_ba(*args) }
                OpCode.CP_BB -> setupInstr(opCode) { cpu, args -> cpu.cp_bb(*args) }
                OpCode.CP_BC -> setupInstr(opCode) { cpu, args -> cpu.cp_bc(*args) }
                OpCode.CP_BD -> setupInstr(opCode) { cpu, args -> cpu.cp_bd(*args) }
                OpCode.CP_BE -> setupInstr(opCode) { cpu, args -> cpu.cp_be(*args) }
                OpCode.CP_BF -> setupInstr(opCode) { cpu, args -> cpu.cp_bf(*args) }

                OpCode.RET_C0 -> setupJumpInstr(opCode) { cpu, args -> cpu.ret_c0(*args) }
                OpCode.POP_C1 -> setupInstr(opCode) { cpu, args -> cpu.pop_c1(*args) }
                OpCode.JP_C2 -> setupJumpInstr(opCode) { cpu, args -> cpu.jp_c2(*args) }
                OpCode.JP_C3 -> setupInstr(opCode) { cpu, args -> cpu.jp_c3(*args) } // TODO: setupJumpInstr?
                OpCode.CALL_C4 -> setupJumpInstr(opCode) { cpu, args -> cpu.call_c4(*args) }
                OpCode.PUSH_C5 -> setupInstr(opCode) { cpu, args -> cpu.push_c5(*args) }
                OpCode.ADD_C6 -> setupInstr(opCode) { cpu, args -> cpu.add_c6(*args) }
                OpCode.RST_C7 -> setupInstr(opCode) { cpu, args -> cpu.rst_c7(*args) }
                OpCode.RET_C8 -> setupJumpInstr(opCode) { cpu, args -> cpu.ret_c8(*args) }
                OpCode.RET_C9 -> setupInstr(opCode) { cpu, args -> cpu.ret_c9(*args) } // TODO: setupJumpInstr?
                OpCode.JP_CA -> setupJumpInstr(opCode) { cpu, args -> cpu.jp_ca(*args) }
                OpCode.PREFIX_CB -> setupInstr(opCode) { cpu, args -> cpu.prefix_cb(*args) }
                OpCode.CALL_CC -> setupJumpInstr(opCode) { cpu, args -> cpu.call_cc(*args) }
                OpCode.CALL_CD -> setupInstr(opCode) { cpu, args -> cpu.call_cd(*args) } // TODO: setupJumpInstr?
                OpCode.ADC_CE -> setupInstr(opCode) { cpu, args -> cpu.adc_ce(*args) }
                OpCode.RST_CF -> setupInstr(opCode) { cpu, args -> cpu.rst_cf(*args) }

                OpCode.RET_D0 -> setupJumpInstr(opCode) { cpu, args -> cpu.ret_d0(*args) }
                OpCode.POP_D1 -> setupInstr(opCode) { cpu, args -> cpu.pop_d1(*args) }
                OpCode.JP_D2 -> setupJumpInstr(opCode) { cpu, args -> cpu.jp_d2(*args) }
                OpCode.CALL_D4 -> setupJumpInstr(opCode) { cpu, args -> cpu.call_d4(*args) }
                OpCode.PUSH_D5 -> setupInstr(opCode) { cpu, args -> cpu.push_d5(*args) }
                OpCode.SUB_D6 -> setupInstr(opCode) { cpu, args -> cpu.sub_d6(*args) }
                OpCode.RST_D7 -> setupInstr(opCode) { cpu, args -> cpu.rst_d7(*args) }
                OpCode.RET_D8 -> setupJumpInstr(opCode) { cpu, args -> cpu.ret_d8(*args) }
                OpCode.RETI_D9 -> setupInstr(opCode) { cpu, args -> cpu.reti_d9(*args) }
                OpCode.JP_DA -> setupJumpInstr(opCode) { cpu, args -> cpu.jp_da(*args) }
                OpCode.CALL_DC -> setupJumpInstr(opCode) { cpu, args -> cpu.call_dc(*args) }
                OpCode.SBC_DE -> setupInstr(opCode) { cpu, args -> cpu.sbc_de(*args) }
                OpCode.RST_DF -> setupInstr(opCode) { cpu, args -> cpu.rst_df(*args) }

                OpCode.LDH_E0 -> setupInstr(opCode) { cpu, args -> cpu.ldh_e0(*args) }
                OpCode.POP_E1 -> setupInstr(opCode) { cpu, args -> cpu.pop_e1(*args) }
                OpCode.LD_E2 -> setupInstr(opCode) { cpu, args -> cpu.ld_e2(*args) }
                OpCode.PUSH_E5 -> setupInstr(opCode) { cpu, args -> cpu.push_e5(*args) }
                OpCode.AND_E6 -> setupInstr(opCode) { cpu, args -> cpu.and_e6(*args) }
                OpCode.RST_E7 -> setupInstr(opCode) { cpu, args -> cpu.rst_e7(*args) }
                OpCode.ADD_E8 -> setupInstr(opCode) { cpu, args -> cpu.add_e8(*args) }
                OpCode.JP_E9 -> setupInstr(opCode) { cpu, args -> cpu.jp_e9(*args) }
                OpCode.LD_EA -> setupInstr(opCode) { cpu, args -> cpu.ld_ea(*args) }
                OpCode.XOR_EE -> setupInstr(opCode) { cpu, args -> cpu.xor_ee(*args) }
                OpCode.RST_EF -> setupInstr(opCode) { cpu, args -> cpu.rst_ef(*args) }

                OpCode.LDH_F0 -> setupInstr(opCode) { cpu, args -> cpu.ldh_f0(*args) }
                OpCode.POP_F1 -> setupInstr(opCode) { cpu, args -> cpu.pop_f1(*args) }
                OpCode.LD_F2 -> setupInstr(opCode) { cpu, args -> cpu.ld_f2(*args) }
                OpCode.DI_F3 -> setupInstr(opCode) { cpu, args -> cpu.di_f3(*args) }
                OpCode.PUSH_F5 -> setupInstr(opCode) { cpu, args -> cpu.push_f5(*args) }
                OpCode.OR_F6 -> setupInstr(opCode) { cpu, args -> cpu.or_f6(*args) }
                OpCode.RST_F7 -> setupInstr(opCode) { cpu, args -> cpu.rst_f7(*args) }
                OpCode.LD_F8 -> setupInstr(opCode) { cpu, args -> cpu.ld_f8(*args) }
                OpCode.LD_F9 -> setupInstr(opCode) { cpu, args -> cpu.ld_f9(*args) }
                OpCode.LD_FA -> setupInstr(opCode) { cpu, args -> cpu.ld_fa(*args) }
                OpCode.EI_FB -> setupInstr(opCode) { cpu, args -> cpu.ei_fb(*args) }
                OpCode.CP_FE -> setupInstr(opCode) { cpu, args -> cpu.cp_fe(*args) }
                OpCode.RST_FF -> setupInstr(opCode) { cpu, args -> cpu.rst_ff(*args) }

                else -> setupInstr(opCode) { cpu, args -> cpu.nop_00(*args) }
            }
        }
    }

    private fun setupExternal(): Array<Instr> {
        return Array(0x100) {
            when (val opCode = OpCode[it + 0x100]) {
                // external opcodes
                OpCode.RLC_00 -> setupInstr(opCode) { cpu, args -> cpu.rlc_00(*args) }
                OpCode.RLC_01 -> setupInstr(opCode) { cpu, args -> cpu.rlc_01(*args) }
                OpCode.RLC_02 -> setupInstr(opCode) { cpu, args -> cpu.rlc_02(*args) }
                OpCode.RLC_03 -> setupInstr(opCode) { cpu, args -> cpu.rlc_03(*args) }
                OpCode.RLC_04 -> setupInstr(opCode) { cpu, args -> cpu.rlc_04(*args) }
                OpCode.RLC_05 -> setupInstr(opCode) { cpu, args -> cpu.rlc_05(*args) }
                OpCode.RLC_06 -> setupInstr(opCode) { cpu, args -> cpu.rlc_06(*args) }
                OpCode.RLC_07 -> setupInstr(opCode) { cpu, args -> cpu.rlc_07(*args) }
                OpCode.RRC_08 -> setupInstr(opCode) { cpu, args -> cpu.rrc_08(*args) }
                OpCode.RRC_09 -> setupInstr(opCode) { cpu, args -> cpu.rrc_09(*args) }
                OpCode.RRC_0A -> setupInstr(opCode) { cpu, args -> cpu.rrc_0a(*args) }
                OpCode.RRC_0B -> setupInstr(opCode) { cpu, args -> cpu.rrc_0b(*args) }
                OpCode.RRC_0C -> setupInstr(opCode) { cpu, args -> cpu.rrc_0c(*args) }
                OpCode.RRC_0D -> setupInstr(opCode) { cpu, args -> cpu.rrc_0d(*args) }
                OpCode.RRC_0E -> setupInstr(opCode) { cpu, args -> cpu.rrc_0e(*args) }
                OpCode.RRC_0F -> setupInstr(opCode) { cpu, args -> cpu.rrc_0f(*args) }

                OpCode.RL_10 -> setupInstr(opCode) { cpu, args -> cpu.rl_10(*args) }
                OpCode.RL_11 -> setupInstr(opCode) { cpu, args -> cpu.rl_11(*args) }
                OpCode.RL_12 -> setupInstr(opCode) { cpu, args -> cpu.rl_12(*args) }
                OpCode.RL_13 -> setupInstr(opCode) { cpu, args -> cpu.rl_13(*args) }
                OpCode.RL_14 -> setupInstr(opCode) { cpu, args -> cpu.rl_14(*args) }
                OpCode.RL_15 -> setupInstr(opCode) { cpu, args -> cpu.rl_15(*args) }
                OpCode.RL_16 -> setupInstr(opCode) { cpu, args -> cpu.rl_16(*args) }
                OpCode.RL_17 -> setupInstr(opCode) { cpu, args -> cpu.rl_17(*args) }
                OpCode.RR_18 -> setupInstr(opCode) { cpu, args -> cpu.rr_18(*args) }
                OpCode.RR_19 -> setupInstr(opCode) { cpu, args -> cpu.rr_19(*args) }
                OpCode.RR_1A -> setupInstr(opCode) { cpu, args -> cpu.rr_1a(*args) }
                OpCode.RR_1B -> setupInstr(opCode) { cpu, args -> cpu.rr_1b(*args) }
                OpCode.RR_1C -> setupInstr(opCode) { cpu, args -> cpu.rr_1c(*args) }
                OpCode.RR_1D -> setupInstr(opCode) { cpu, args -> cpu.rr_1d(*args) }
                OpCode.RR_1E -> setupInstr(opCode) { cpu, args -> cpu.rr_1e(*args) }
                OpCode.RR_1F -> setupInstr(opCode) { cpu, args -> cpu.rr_1f(*args) }

                OpCode.SLA_20 -> setupInstr(opCode) { cpu, args -> cpu.sla_20(*args) }
                OpCode.SLA_21 -> setupInstr(opCode) { cpu, args -> cpu.sla_21(*args) }
                OpCode.SLA_22 -> setupInstr(opCode) { cpu, args -> cpu.sla_22(*args) }
                OpCode.SLA_23 -> setupInstr(opCode) { cpu, args -> cpu.sla_23(*args) }
                OpCode.SLA_24 -> setupInstr(opCode) { cpu, args -> cpu.sla_24(*args) }
                OpCode.SLA_25 -> setupInstr(opCode) { cpu, args -> cpu.sla_25(*args) }
                OpCode.SLA_26 -> setupInstr(opCode) { cpu, args -> cpu.sla_26(*args) }
                OpCode.SLA_27 -> setupInstr(opCode) { cpu, args -> cpu.sla_27(*args) }
                OpCode.SRA_28 -> setupInstr(opCode) { cpu, args -> cpu.sra_28(*args) }
                OpCode.SRA_29 -> setupInstr(opCode) { cpu, args -> cpu.sra_29(*args) }
                OpCode.SRA_2A -> setupInstr(opCode) { cpu, args -> cpu.sra_2a(*args) }
                OpCode.SRA_2B -> setupInstr(opCode) { cpu, args -> cpu.sra_2b(*args) }
                OpCode.SRA_2C -> setupInstr(opCode) { cpu, args -> cpu.sra_2c(*args) }
                OpCode.SRA_2D -> setupInstr(opCode) { cpu, args -> cpu.sra_2d(*args) }
                OpCode.SRA_2E -> setupInstr(opCode) { cpu, args -> cpu.sra_2e(*args) }
                OpCode.SRA_2F -> setupInstr(opCode) { cpu, args -> cpu.sra_2f(*args) }

                OpCode.SWAP_30 -> setupInstr(opCode) { cpu, args -> cpu.swap_30(*args) }
                OpCode.SWAP_31 -> setupInstr(opCode) { cpu, args -> cpu.swap_31(*args) }
                OpCode.SWAP_32 -> setupInstr(opCode) { cpu, args -> cpu.swap_32(*args) }
                OpCode.SWAP_33 -> setupInstr(opCode) { cpu, args -> cpu.swap_33(*args) }
                OpCode.SWAP_34 -> setupInstr(opCode) { cpu, args -> cpu.swap_34(*args) }
                OpCode.SWAP_35 -> setupInstr(opCode) { cpu, args -> cpu.swap_35(*args) }
                OpCode.SWAP_36 -> setupInstr(opCode) { cpu, args -> cpu.swap_36(*args) }
                OpCode.SWAP_37 -> setupInstr(opCode) { cpu, args -> cpu.swap_37(*args) }
                OpCode.SRL_38 -> setupInstr(opCode) { cpu, args -> cpu.srl_38(*args) }
                OpCode.SRL_39 -> setupInstr(opCode) { cpu, args -> cpu.srl_39(*args) }
                OpCode.SRL_3A -> setupInstr(opCode) { cpu, args -> cpu.srl_3a(*args) }
                OpCode.SRL_3B -> setupInstr(opCode) { cpu, args -> cpu.srl_3b(*args) }
                OpCode.SRL_3C -> setupInstr(opCode) { cpu, args -> cpu.srl_3c(*args) }
                OpCode.SRL_3D -> setupInstr(opCode) { cpu, args -> cpu.srl_3d(*args) }
                OpCode.SRL_3E -> setupInstr(opCode) { cpu, args -> cpu.srl_3e(*args) }
                OpCode.SRL_3F -> setupInstr(opCode) { cpu, args -> cpu.srl_3f(*args) }

                OpCode.BIT_40 -> setupInstr(opCode) { cpu, args -> cpu.bit_40(*args) }
                OpCode.BIT_41 -> setupInstr(opCode) { cpu, args -> cpu.bit_41(*args) }
                OpCode.BIT_42 -> setupInstr(opCode) { cpu, args -> cpu.bit_42(*args) }
                OpCode.BIT_43 -> setupInstr(opCode) { cpu, args -> cpu.bit_43(*args) }
                OpCode.BIT_44 -> setupInstr(opCode) { cpu, args -> cpu.bit_44(*args) }
                OpCode.BIT_45 -> setupInstr(opCode) { cpu, args -> cpu.bit_45(*args) }
                OpCode.BIT_46 -> setupInstr(opCode) { cpu, args -> cpu.bit_46(*args) }
                OpCode.BIT_47 -> setupInstr(opCode) { cpu, args -> cpu.bit_47(*args) }
                OpCode.BIT_48 -> setupInstr(opCode) { cpu, args -> cpu.bit_48(*args) }
                OpCode.BIT_49 -> setupInstr(opCode) { cpu, args -> cpu.bit_49(*args) }
                OpCode.BIT_4A -> setupInstr(opCode) { cpu, args -> cpu.bit_4a(*args) }
                OpCode.BIT_4B -> setupInstr(opCode) { cpu, args -> cpu.bit_4b(*args) }
                OpCode.BIT_4C -> setupInstr(opCode) { cpu, args -> cpu.bit_4c(*args) }
                OpCode.BIT_4D -> setupInstr(opCode) { cpu, args -> cpu.bit_4d(*args) }
                OpCode.BIT_4E -> setupInstr(opCode) { cpu, args -> cpu.bit_4e(*args) }
                OpCode.BIT_4F -> setupInstr(opCode) { cpu, args -> cpu.bit_4f(*args) }

                OpCode.BIT_50 -> setupInstr(opCode) { cpu, args -> cpu.bit_50(*args) }
                OpCode.BIT_51 -> setupInstr(opCode) { cpu, args -> cpu.bit_51(*args) }
                OpCode.BIT_52 -> setupInstr(opCode) { cpu, args -> cpu.bit_52(*args) }
                OpCode.BIT_53 -> setupInstr(opCode) { cpu, args -> cpu.bit_53(*args) }
                OpCode.BIT_54 -> setupInstr(opCode) { cpu, args -> cpu.bit_54(*args) }
                OpCode.BIT_55 -> setupInstr(opCode) { cpu, args -> cpu.bit_55(*args) }
                OpCode.BIT_56 -> setupInstr(opCode) { cpu, args -> cpu.bit_56(*args) }
                OpCode.BIT_57 -> setupInstr(opCode) { cpu, args -> cpu.bit_57(*args) }
                OpCode.BIT_58 -> setupInstr(opCode) { cpu, args -> cpu.bit_58(*args) }
                OpCode.BIT_59 -> setupInstr(opCode) { cpu, args -> cpu.bit_59(*args) }
                OpCode.BIT_5A -> setupInstr(opCode) { cpu, args -> cpu.bit_5a(*args) }
                OpCode.BIT_5B -> setupInstr(opCode) { cpu, args -> cpu.bit_5b(*args) }
                OpCode.BIT_5C -> setupInstr(opCode) { cpu, args -> cpu.bit_5c(*args) }
                OpCode.BIT_5D -> setupInstr(opCode) { cpu, args -> cpu.bit_5d(*args) }
                OpCode.BIT_5E -> setupInstr(opCode) { cpu, args -> cpu.bit_5e(*args) }
                OpCode.BIT_5F -> setupInstr(opCode) { cpu, args -> cpu.bit_5f(*args) }

                OpCode.BIT_60 -> setupInstr(opCode) { cpu, args -> cpu.bit_60(*args) }
                OpCode.BIT_61 -> setupInstr(opCode) { cpu, args -> cpu.bit_61(*args) }
                OpCode.BIT_62 -> setupInstr(opCode) { cpu, args -> cpu.bit_62(*args) }
                OpCode.BIT_63 -> setupInstr(opCode) { cpu, args -> cpu.bit_63(*args) }
                OpCode.BIT_64 -> setupInstr(opCode) { cpu, args -> cpu.bit_64(*args) }
                OpCode.BIT_65 -> setupInstr(opCode) { cpu, args -> cpu.bit_65(*args) }
                OpCode.BIT_66 -> setupInstr(opCode) { cpu, args -> cpu.bit_66(*args) }
                OpCode.BIT_67 -> setupInstr(opCode) { cpu, args -> cpu.bit_67(*args) }
                OpCode.BIT_68 -> setupInstr(opCode) { cpu, args -> cpu.bit_68(*args) }
                OpCode.BIT_69 -> setupInstr(opCode) { cpu, args -> cpu.bit_69(*args) }
                OpCode.BIT_6A -> setupInstr(opCode) { cpu, args -> cpu.bit_6a(*args) }
                OpCode.BIT_6B -> setupInstr(opCode) { cpu, args -> cpu.bit_6b(*args) }
                OpCode.BIT_6C -> setupInstr(opCode) { cpu, args -> cpu.bit_6c(*args) }
                OpCode.BIT_6D -> setupInstr(opCode) { cpu, args -> cpu.bit_6d(*args) }
                OpCode.BIT_6E -> setupInstr(opCode) { cpu, args -> cpu.bit_6e(*args) }
                OpCode.BIT_6F -> setupInstr(opCode) { cpu, args -> cpu.bit_6f(*args) }

                OpCode.BIT_70 -> setupInstr(opCode) { cpu, args -> cpu.bit_70(*args) }
                OpCode.BIT_71 -> setupInstr(opCode) { cpu, args -> cpu.bit_71(*args) }
                OpCode.BIT_72 -> setupInstr(opCode) { cpu, args -> cpu.bit_72(*args) }
                OpCode.BIT_73 -> setupInstr(opCode) { cpu, args -> cpu.bit_73(*args) }
                OpCode.BIT_74 -> setupInstr(opCode) { cpu, args -> cpu.bit_74(*args) }
                OpCode.BIT_75 -> setupInstr(opCode) { cpu, args -> cpu.bit_75(*args) }
                OpCode.BIT_76 -> setupInstr(opCode) { cpu, args -> cpu.bit_76(*args) }
                OpCode.BIT_77 -> setupInstr(opCode) { cpu, args -> cpu.bit_77(*args) }
                OpCode.BIT_78 -> setupInstr(opCode) { cpu, args -> cpu.bit_78(*args) }
                OpCode.BIT_79 -> setupInstr(opCode) { cpu, args -> cpu.bit_79(*args) }
                OpCode.BIT_7A -> setupInstr(opCode) { cpu, args -> cpu.bit_7a(*args) }
                OpCode.BIT_7B -> setupInstr(opCode) { cpu, args -> cpu.bit_7b(*args) }
                OpCode.BIT_7C -> setupInstr(opCode) { cpu, args -> cpu.bit_7c(*args) }
                OpCode.BIT_7D -> setupInstr(opCode) { cpu, args -> cpu.bit_7d(*args) }
                OpCode.BIT_7E -> setupInstr(opCode) { cpu, args -> cpu.bit_7e(*args) }
                OpCode.BIT_7F -> setupInstr(opCode) { cpu, args -> cpu.bit_7f(*args) }

                OpCode.RES_80 -> setupInstr(opCode) { cpu, args -> cpu.res_80(*args) }
                OpCode.RES_81 -> setupInstr(opCode) { cpu, args -> cpu.res_81(*args) }
                OpCode.RES_82 -> setupInstr(opCode) { cpu, args -> cpu.res_82(*args) }
                OpCode.RES_83 -> setupInstr(opCode) { cpu, args -> cpu.res_83(*args) }
                OpCode.RES_84 -> setupInstr(opCode) { cpu, args -> cpu.res_84(*args) }
                OpCode.RES_85 -> setupInstr(opCode) { cpu, args -> cpu.res_85(*args) }
                OpCode.RES_86 -> setupInstr(opCode) { cpu, args -> cpu.res_86(*args) }
                OpCode.RES_87 -> setupInstr(opCode) { cpu, args -> cpu.res_87(*args) }
                OpCode.RES_88 -> setupInstr(opCode) { cpu, args -> cpu.res_88(*args) }
                OpCode.RES_89 -> setupInstr(opCode) { cpu, args -> cpu.res_89(*args) }
                OpCode.RES_8A -> setupInstr(opCode) { cpu, args -> cpu.res_8a(*args) }
                OpCode.RES_8B -> setupInstr(opCode) { cpu, args -> cpu.res_8b(*args) }
                OpCode.RES_8C -> setupInstr(opCode) { cpu, args -> cpu.res_8c(*args) }
                OpCode.RES_8D -> setupInstr(opCode) { cpu, args -> cpu.res_8d(*args) }
                OpCode.RES_8E -> setupInstr(opCode) { cpu, args -> cpu.res_8e(*args) }
                OpCode.RES_8F -> setupInstr(opCode) { cpu, args -> cpu.res_8f(*args) }

                OpCode.RES_90 -> setupInstr(opCode) { cpu, args -> cpu.res_90(*args) }
                OpCode.RES_91 -> setupInstr(opCode) { cpu, args -> cpu.res_91(*args) }
                OpCode.RES_92 -> setupInstr(opCode) { cpu, args -> cpu.res_92(*args) }
                OpCode.RES_93 -> setupInstr(opCode) { cpu, args -> cpu.res_93(*args) }
                OpCode.RES_94 -> setupInstr(opCode) { cpu, args -> cpu.res_94(*args) }
                OpCode.RES_95 -> setupInstr(opCode) { cpu, args -> cpu.res_95(*args) }
                OpCode.RES_96 -> setupInstr(opCode) { cpu, args -> cpu.res_96(*args) }
                OpCode.RES_97 -> setupInstr(opCode) { cpu, args -> cpu.res_97(*args) }
                OpCode.RES_98 -> setupInstr(opCode) { cpu, args -> cpu.res_98(*args) }
                OpCode.RES_99 -> setupInstr(opCode) { cpu, args -> cpu.res_99(*args) }
                OpCode.RES_9A -> setupInstr(opCode) { cpu, args -> cpu.res_9a(*args) }
                OpCode.RES_9B -> setupInstr(opCode) { cpu, args -> cpu.res_9b(*args) }
                OpCode.RES_9C -> setupInstr(opCode) { cpu, args -> cpu.res_9c(*args) }
                OpCode.RES_9D -> setupInstr(opCode) { cpu, args -> cpu.res_9d(*args) }
                OpCode.RES_9E -> setupInstr(opCode) { cpu, args -> cpu.res_9e(*args) }
                OpCode.RES_9F -> setupInstr(opCode) { cpu, args -> cpu.res_9f(*args) }

                OpCode.RES_A0 -> setupInstr(opCode) { cpu, args -> cpu.res_a0(*args) }
                OpCode.RES_A1 -> setupInstr(opCode) { cpu, args -> cpu.res_a1(*args) }
                OpCode.RES_A2 -> setupInstr(opCode) { cpu, args -> cpu.res_a2(*args) }
                OpCode.RES_A3 -> setupInstr(opCode) { cpu, args -> cpu.res_a3(*args) }
                OpCode.RES_A4 -> setupInstr(opCode) { cpu, args -> cpu.res_a4(*args) }
                OpCode.RES_A5 -> setupInstr(opCode) { cpu, args -> cpu.res_a5(*args) }
                OpCode.RES_A6 -> setupInstr(opCode) { cpu, args -> cpu.res_a6(*args) }
                OpCode.RES_A7 -> setupInstr(opCode) { cpu, args -> cpu.res_a7(*args) }
                OpCode.RES_A8 -> setupInstr(opCode) { cpu, args -> cpu.res_a8(*args) }
                OpCode.RES_A9 -> setupInstr(opCode) { cpu, args -> cpu.res_a9(*args) }
                OpCode.RES_AA -> setupInstr(opCode) { cpu, args -> cpu.res_aa(*args) }
                OpCode.RES_AB -> setupInstr(opCode) { cpu, args -> cpu.res_ab(*args) }
                OpCode.RES_AC -> setupInstr(opCode) { cpu, args -> cpu.res_ac(*args) }
                OpCode.RES_AD -> setupInstr(opCode) { cpu, args -> cpu.res_ad(*args) }
                OpCode.RES_AE -> setupInstr(opCode) { cpu, args -> cpu.res_ae(*args) }
                OpCode.RES_AF -> setupInstr(opCode) { cpu, args -> cpu.res_af(*args) }

                OpCode.RES_B0 -> setupInstr(opCode) { cpu, args -> cpu.res_b0(*args) }
                OpCode.RES_B1 -> setupInstr(opCode) { cpu, args -> cpu.res_b1(*args) }
                OpCode.RES_B2 -> setupInstr(opCode) { cpu, args -> cpu.res_b2(*args) }
                OpCode.RES_B3 -> setupInstr(opCode) { cpu, args -> cpu.res_b3(*args) }
                OpCode.RES_B4 -> setupInstr(opCode) { cpu, args -> cpu.res_b4(*args) }
                OpCode.RES_B5 -> setupInstr(opCode) { cpu, args -> cpu.res_b5(*args) }
                OpCode.RES_B6 -> setupInstr(opCode) { cpu, args -> cpu.res_b6(*args) }
                OpCode.RES_B7 -> setupInstr(opCode) { cpu, args -> cpu.res_b7(*args) }
                OpCode.RES_B8 -> setupInstr(opCode) { cpu, args -> cpu.res_b8(*args) }
                OpCode.RES_B9 -> setupInstr(opCode) { cpu, args -> cpu.res_b9(*args) }
                OpCode.RES_BA -> setupInstr(opCode) { cpu, args -> cpu.res_ba(*args) }
                OpCode.RES_BB -> setupInstr(opCode) { cpu, args -> cpu.res_bb(*args) }
                OpCode.RES_BC -> setupInstr(opCode) { cpu, args -> cpu.res_bc(*args) }
                OpCode.RES_BD -> setupInstr(opCode) { cpu, args -> cpu.res_bd(*args) }
                OpCode.RES_BE -> setupInstr(opCode) { cpu, args -> cpu.res_be(*args) }
                OpCode.RES_BF -> setupInstr(opCode) { cpu, args -> cpu.res_bf(*args) }

                OpCode.SET_C0 -> setupInstr(opCode) { cpu, args -> cpu.set_c0(*args) }
                OpCode.SET_C1 -> setupInstr(opCode) { cpu, args -> cpu.set_c1(*args) }
                OpCode.SET_C2 -> setupInstr(opCode) { cpu, args -> cpu.set_c2(*args) }
                OpCode.SET_C3 -> setupInstr(opCode) { cpu, args -> cpu.set_c3(*args) }
                OpCode.SET_C4 -> setupInstr(opCode) { cpu, args -> cpu.set_c4(*args) }
                OpCode.SET_C5 -> setupInstr(opCode) { cpu, args -> cpu.set_c5(*args) }
                OpCode.SET_C6 -> setupInstr(opCode) { cpu, args -> cpu.set_c6(*args) }
                OpCode.SET_C7 -> setupInstr(opCode) { cpu, args -> cpu.set_c7(*args) }
                OpCode.SET_C8 -> setupInstr(opCode) { cpu, args -> cpu.set_c8(*args) }
                OpCode.SET_C9 -> setupInstr(opCode) { cpu, args -> cpu.set_c9(*args) }
                OpCode.SET_CA -> setupInstr(opCode) { cpu, args -> cpu.set_ca(*args) }
                OpCode.SET_CB -> setupInstr(opCode) { cpu, args -> cpu.set_cb(*args) }
                OpCode.SET_CC -> setupInstr(opCode) { cpu, args -> cpu.set_cc(*args) }
                OpCode.SET_CD -> setupInstr(opCode) { cpu, args -> cpu.set_cd(*args) }
                OpCode.SET_CE -> setupInstr(opCode) { cpu, args -> cpu.set_ce(*args) }
                OpCode.SET_CF -> setupInstr(opCode) { cpu, args -> cpu.set_cf(*args) }

                OpCode.SET_D0 -> setupInstr(opCode) { cpu, args -> cpu.set_d0(*args) }
                OpCode.SET_D1 -> setupInstr(opCode) { cpu, args -> cpu.set_d1(*args) }
                OpCode.SET_D2 -> setupInstr(opCode) { cpu, args -> cpu.set_d2(*args) }
                OpCode.SET_D3 -> setupInstr(opCode) { cpu, args -> cpu.set_d3(*args) }
                OpCode.SET_D4 -> setupInstr(opCode) { cpu, args -> cpu.set_d4(*args) }
                OpCode.SET_D5 -> setupInstr(opCode) { cpu, args -> cpu.set_d5(*args) }
                OpCode.SET_D6 -> setupInstr(opCode) { cpu, args -> cpu.set_d6(*args) }
                OpCode.SET_D7 -> setupInstr(opCode) { cpu, args -> cpu.set_d7(*args) }
                OpCode.SET_D8 -> setupInstr(opCode) { cpu, args -> cpu.set_d8(*args) }
                OpCode.SET_D9 -> setupInstr(opCode) { cpu, args -> cpu.set_d9(*args) }
                OpCode.SET_DA -> setupInstr(opCode) { cpu, args -> cpu.set_da(*args) }
                OpCode.SET_DB -> setupInstr(opCode) { cpu, args -> cpu.set_db(*args) }
                OpCode.SET_DC -> setupInstr(opCode) { cpu, args -> cpu.set_dc(*args) }
                OpCode.SET_DD -> setupInstr(opCode) { cpu, args -> cpu.set_dd(*args) }
                OpCode.SET_DE -> setupInstr(opCode) { cpu, args -> cpu.set_de(*args) }
                OpCode.SET_DF -> setupInstr(opCode) { cpu, args -> cpu.set_df(*args) }

                OpCode.SET_E0 -> setupInstr(opCode) { cpu, args -> cpu.set_e0(*args) }
                OpCode.SET_E1 -> setupInstr(opCode) { cpu, args -> cpu.set_e1(*args) }
                OpCode.SET_E2 -> setupInstr(opCode) { cpu, args -> cpu.set_e2(*args) }
                OpCode.SET_E3 -> setupInstr(opCode) { cpu, args -> cpu.set_e3(*args) }
                OpCode.SET_E4 -> setupInstr(opCode) { cpu, args -> cpu.set_e4(*args) }
                OpCode.SET_E5 -> setupInstr(opCode) { cpu, args -> cpu.set_e5(*args) }
                OpCode.SET_E6 -> setupInstr(opCode) { cpu, args -> cpu.set_e6(*args) }
                OpCode.SET_E7 -> setupInstr(opCode) { cpu, args -> cpu.set_e7(*args) }
                OpCode.SET_E8 -> setupInstr(opCode) { cpu, args -> cpu.set_e8(*args) }
                OpCode.SET_E9 -> setupInstr(opCode) { cpu, args -> cpu.set_e9(*args) }
                OpCode.SET_EA -> setupInstr(opCode) { cpu, args -> cpu.set_ea(*args) }
                OpCode.SET_EB -> setupInstr(opCode) { cpu, args -> cpu.set_eb(*args) }
                OpCode.SET_EC -> setupInstr(opCode) { cpu, args -> cpu.set_ec(*args) }
                OpCode.SET_ED -> setupInstr(opCode) { cpu, args -> cpu.set_ed(*args) }
                OpCode.SET_EE -> setupInstr(opCode) { cpu, args -> cpu.set_ee(*args) }
                OpCode.SET_EF -> setupInstr(opCode) { cpu, args -> cpu.set_ef(*args) }

                OpCode.SET_F0 -> setupInstr(opCode) { cpu, args -> cpu.set_f0(*args) }
                OpCode.SET_F1 -> setupInstr(opCode) { cpu, args -> cpu.set_f1(*args) }
                OpCode.SET_F2 -> setupInstr(opCode) { cpu, args -> cpu.set_f2(*args) }
                OpCode.SET_F3 -> setupInstr(opCode) { cpu, args -> cpu.set_f3(*args) }
                OpCode.SET_F4 -> setupInstr(opCode) { cpu, args -> cpu.set_f4(*args) }
                OpCode.SET_F5 -> setupInstr(opCode) { cpu, args -> cpu.set_f5(*args) }
                OpCode.SET_F6 -> setupInstr(opCode) { cpu, args -> cpu.set_f6(*args) }
                OpCode.SET_F7 -> setupInstr(opCode) { cpu, args -> cpu.set_f7(*args) }
                OpCode.SET_F8 -> setupInstr(opCode) { cpu, args -> cpu.set_f8(*args) }
                OpCode.SET_F9 -> setupInstr(opCode) { cpu, args -> cpu.set_f9(*args) }
                OpCode.SET_FA -> setupInstr(opCode) { cpu, args -> cpu.set_fa(*args) }
                OpCode.SET_FB -> setupInstr(opCode) { cpu, args -> cpu.set_fb(*args) }
                OpCode.SET_FC -> setupInstr(opCode) { cpu, args -> cpu.set_fc(*args) }
                OpCode.SET_FD -> setupInstr(opCode) { cpu, args -> cpu.set_fd(*args) }
                OpCode.SET_FE -> setupInstr(opCode) { cpu, args -> cpu.set_fe(*args) }
                OpCode.SET_FF -> setupInstr(opCode) { cpu, args -> cpu.set_ff(*args) }

                else -> setupInstr(opCode) { cpu, args -> cpu.nop_00(*args) }
            }
        }
    }

    fun execute(cpu: Cpu, opcode: Int, args: IntArray): Int {
        return if (opcode < 0x100) instrs[opcode].execute(cpu, *args)
        else externalInstrs[opcode - 0x100].execute(cpu, *args)
    }

}