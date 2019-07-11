package com.arman.kotboy.cpu

import com.arman.kotboy.cpu.util.lsb
import com.arman.kotboy.cpu.util.msb
import com.arman.kotboy.cpu.util.toUnsignedInt
import com.arman.kotboy.cpu.util.toWord
import com.arman.kotboy.memory.Mmu

@Suppress("FunctionName", "UNUSED_PARAMETER")
class Processor {

    fun run(opCode: OpCode, cpu: Cpu, mmu: Mmu, vararg args: Int): Int {
        when (opCode) {
            // opcodes
            OpCode.NOP_00 -> this.nop_00(cpu, mmu, *args)
            OpCode.LD_01 -> this.ld_01(cpu, mmu, *args)
            OpCode.LD_02 -> this.ld_02(cpu, mmu, *args)
            OpCode.INC_03 -> this.inc_03(cpu, mmu, *args)
            OpCode.INC_04 -> this.inc_04(cpu, mmu, *args)
            OpCode.DEC_05 -> this.dec_05(cpu, mmu, *args)
            OpCode.LD_06 -> this.ld_06(cpu, mmu, *args)
            OpCode.RLCA_07 -> this.rlca_07(cpu, mmu, *args)
            OpCode.LD_08 -> this.ld_08(cpu, mmu, *args)
            OpCode.ADD_09 -> this.add_09(cpu, mmu, *args)
            OpCode.LD_0A -> this.ld_0a(cpu, mmu, *args)
            OpCode.DEC_0B -> this.dec_0b(cpu, mmu, *args)
            OpCode.INC_0C -> this.inc_0c(cpu, mmu, *args)
            OpCode.DEC_0D -> this.dec_0d(cpu, mmu, *args)
            OpCode.LD_0E -> this.ld_0e(cpu, mmu, *args)
            OpCode.RRCA_0F -> this.rrca_0f(cpu, mmu, *args)

            OpCode.STOP_10 -> this.stop_10(cpu, mmu, *args)
            OpCode.LD_11 -> this.ld_11(cpu, mmu, *args)
            OpCode.LD_12 -> this.ld_12(cpu, mmu, *args)
            OpCode.INC_13 -> this.inc_13(cpu, mmu, *args)
            OpCode.INC_14 -> this.inc_14(cpu, mmu, *args)
            OpCode.DEC_15 -> this.dec_15(cpu, mmu, *args)
            OpCode.LD_16 -> this.ld_16(cpu, mmu, *args)
            OpCode.RLA_17 -> this.rla_17(cpu, mmu, *args)
            OpCode.JR_18 -> this.jr_18(cpu, mmu, *args)
            OpCode.ADD_19 -> this.add_19(cpu, mmu, *args)
            OpCode.LD_1A -> this.ld_1a(cpu, mmu, *args)
            OpCode.DEC_1B -> this.dec_1b(cpu, mmu, *args)
            OpCode.INC_1C -> this.inc_1c(cpu, mmu, *args)
            OpCode.DEC_1D -> this.dec_1d(cpu, mmu, *args)
            OpCode.LD_1E -> this.ld_1e(cpu, mmu, *args)
            OpCode.RRA_1F -> this.rra_1f(cpu, mmu, *args)

            OpCode.JR_20 -> if (!this.jr_20(cpu, mmu, *args)) {
                return OpCode.JR_20.cyclesNotTaken
            }
            OpCode.LD_21 -> this.ld_21(cpu, mmu, *args)
            OpCode.LD_22 -> this.ld_22(cpu, mmu, *args)
            OpCode.INC_23 -> this.inc_23(cpu, mmu, *args)
            OpCode.INC_24 -> this.inc_24(cpu, mmu, *args)
            OpCode.DEC_25 -> this.dec_25(cpu, mmu, *args)
            OpCode.LD_26 -> this.ld_26(cpu, mmu, *args)
            OpCode.DAA_27 -> this.daa_27(cpu, mmu, *args)
            OpCode.JR_28 -> if (!this.jr_28(cpu, mmu, *args)) {
                return OpCode.JR_28.cyclesNotTaken
            }
            OpCode.ADD_29 -> this.add_29(cpu, mmu, *args)
            OpCode.LD_2A -> this.ld_2a(cpu, mmu, *args)
            OpCode.DEC_2B -> this.dec_2b(cpu, mmu, *args)
            OpCode.INC_2C -> this.inc_2c(cpu, mmu, *args)
            OpCode.DEC_2D -> this.dec_2d(cpu, mmu, *args)
            OpCode.LD_2E -> this.ld_2e(cpu, mmu, *args)
            OpCode.CPL_2F -> this.cpl_2f(cpu, mmu, *args)

            OpCode.JR_30 -> if (!this.jr_30(cpu, mmu, *args)) {
                return OpCode.JR_30.cyclesNotTaken
            }
            OpCode.LD_31 -> this.ld_31(cpu, mmu, *args)
            OpCode.LD_32 -> this.ld_32(cpu, mmu, *args)
            OpCode.INC_33 -> this.inc_33(cpu, mmu, *args)
            OpCode.INC_34 -> this.inc_34(cpu, mmu, *args)
            OpCode.DEC_35 -> this.dec_35(cpu, mmu, *args)
            OpCode.LD_36 -> this.ld_36(cpu, mmu, *args)
            OpCode.SCF_37 -> this.scf_37(cpu, mmu, *args)
            OpCode.JR_38 -> if (!this.jr_38(cpu, mmu, *args)) {
                return OpCode.JR_38.cyclesNotTaken
            }
            OpCode.ADD_39 -> this.add_39(cpu, mmu, *args)
            OpCode.LD_3A -> this.ld_3a(cpu, mmu, *args)
            OpCode.DEC_3B -> this.dec_3b(cpu, mmu, *args)
            OpCode.INC_3C -> this.inc_3c(cpu, mmu, *args)
            OpCode.DEC_3D -> this.dec_3d(cpu, mmu, *args)
            OpCode.LD_3E -> this.ld_3e(cpu, mmu, *args)
            OpCode.CCF_3F -> this.ccf_3f(cpu, mmu, *args)

            OpCode.LD_40 -> this.ld_40(cpu, mmu, *args)
            OpCode.LD_41 -> this.ld_41(cpu, mmu, *args)
            OpCode.LD_42 -> this.ld_42(cpu, mmu, *args)
            OpCode.LD_43 -> this.ld_43(cpu, mmu, *args)
            OpCode.LD_44 -> this.ld_44(cpu, mmu, *args)
            OpCode.LD_45 -> this.ld_45(cpu, mmu, *args)
            OpCode.LD_46 -> this.ld_46(cpu, mmu, *args)
            OpCode.LD_47 -> this.ld_47(cpu, mmu, *args)
            OpCode.LD_48 -> this.ld_48(cpu, mmu, *args)
            OpCode.LD_49 -> this.ld_49(cpu, mmu, *args)
            OpCode.LD_4A -> this.ld_4a(cpu, mmu, *args)
            OpCode.LD_4B -> this.ld_4b(cpu, mmu, *args)
            OpCode.LD_4C -> this.ld_4c(cpu, mmu, *args)
            OpCode.LD_4D -> this.ld_4d(cpu, mmu, *args)
            OpCode.LD_4E -> this.ld_4e(cpu, mmu, *args)
            OpCode.LD_4F -> this.ld_4f(cpu, mmu, *args)

            OpCode.LD_50 -> this.ld_50(cpu, mmu, *args)
            OpCode.LD_51 -> this.ld_51(cpu, mmu, *args)
            OpCode.LD_52 -> this.ld_52(cpu, mmu, *args)
            OpCode.LD_53 -> this.ld_53(cpu, mmu, *args)
            OpCode.LD_54 -> this.ld_54(cpu, mmu, *args)
            OpCode.LD_55 -> this.ld_55(cpu, mmu, *args)
            OpCode.LD_56 -> this.ld_56(cpu, mmu, *args)
            OpCode.LD_57 -> this.ld_57(cpu, mmu, *args)
            OpCode.LD_58 -> this.ld_58(cpu, mmu, *args)
            OpCode.LD_59 -> this.ld_59(cpu, mmu, *args)
            OpCode.LD_5A -> this.ld_5a(cpu, mmu, *args)
            OpCode.LD_5B -> this.ld_5b(cpu, mmu, *args)
            OpCode.LD_5C -> this.ld_5c(cpu, mmu, *args)
            OpCode.LD_5D -> this.ld_5d(cpu, mmu, *args)
            OpCode.LD_5E -> this.ld_5e(cpu, mmu, *args)
            OpCode.LD_5F -> this.ld_5f(cpu, mmu, *args)

            OpCode.LD_60 -> this.ld_60(cpu, mmu, *args)
            OpCode.LD_61 -> this.ld_61(cpu, mmu, *args)
            OpCode.LD_62 -> this.ld_62(cpu, mmu, *args)
            OpCode.LD_63 -> this.ld_63(cpu, mmu, *args)
            OpCode.LD_64 -> this.ld_64(cpu, mmu, *args)
            OpCode.LD_65 -> this.ld_65(cpu, mmu, *args)
            OpCode.LD_66 -> this.ld_66(cpu, mmu, *args)
            OpCode.LD_67 -> this.ld_67(cpu, mmu, *args)
            OpCode.LD_68 -> this.ld_68(cpu, mmu, *args)
            OpCode.LD_69 -> this.ld_69(cpu, mmu, *args)
            OpCode.LD_6A -> this.ld_6a(cpu, mmu, *args)
            OpCode.LD_6B -> this.ld_6b(cpu, mmu, *args)
            OpCode.LD_6C -> this.ld_6c(cpu, mmu, *args)
            OpCode.LD_6D -> this.ld_6d(cpu, mmu, *args)
            OpCode.LD_6E -> this.ld_6e(cpu, mmu, *args)
            OpCode.LD_6F -> this.ld_6f(cpu, mmu, *args)

            OpCode.LD_70 -> this.ld_70(cpu, mmu, *args)
            OpCode.LD_71 -> this.ld_71(cpu, mmu, *args)
            OpCode.LD_72 -> this.ld_72(cpu, mmu, *args)
            OpCode.LD_73 -> this.ld_73(cpu, mmu, *args)
            OpCode.LD_74 -> this.ld_74(cpu, mmu, *args)
            OpCode.LD_75 -> this.ld_75(cpu, mmu, *args)
            OpCode.HALT_76 -> this.halt_76(cpu, mmu, *args)
            OpCode.LD_77 -> this.ld_77(cpu, mmu, *args)
            OpCode.LD_78 -> this.ld_78(cpu, mmu, *args)
            OpCode.LD_79 -> this.ld_79(cpu, mmu, *args)
            OpCode.LD_7A -> this.ld_7a(cpu, mmu, *args)
            OpCode.LD_7B -> this.ld_7b(cpu, mmu, *args)
            OpCode.LD_7C -> this.ld_7c(cpu, mmu, *args)
            OpCode.LD_7D -> this.ld_7d(cpu, mmu, *args)
            OpCode.LD_7E -> this.ld_7e(cpu, mmu, *args)
            OpCode.LD_7F -> this.ld_7f(cpu, mmu, *args)

            OpCode.ADD_80 -> this.add_80(cpu, mmu, *args)
            OpCode.ADD_81 -> this.add_81(cpu, mmu, *args)
            OpCode.ADD_82 -> this.add_82(cpu, mmu, *args)
            OpCode.ADD_83 -> this.add_83(cpu, mmu, *args)
            OpCode.ADD_84 -> this.add_84(cpu, mmu, *args)
            OpCode.ADD_85 -> this.add_85(cpu, mmu, *args)
            OpCode.ADD_86 -> this.add_86(cpu, mmu, *args)
            OpCode.ADD_87 -> this.add_87(cpu, mmu, *args)
            OpCode.ADC_88 -> this.adc_88(cpu, mmu, *args)
            OpCode.ADC_89 -> this.adc_89(cpu, mmu, *args)
            OpCode.ADC_8A -> this.adc_8a(cpu, mmu, *args)
            OpCode.ADC_8B -> this.adc_8b(cpu, mmu, *args)
            OpCode.ADC_8C -> this.adc_8c(cpu, mmu, *args)
            OpCode.ADC_8D -> this.adc_8d(cpu, mmu, *args)
            OpCode.ADC_8E -> this.adc_8e(cpu, mmu, *args)
            OpCode.ADC_8F -> this.adc_8f(cpu, mmu, *args)

            OpCode.SUB_90 -> this.sub_90(cpu, mmu, *args)
            OpCode.SUB_91 -> this.sub_91(cpu, mmu, *args)
            OpCode.SUB_92 -> this.sub_92(cpu, mmu, *args)
            OpCode.SUB_93 -> this.sub_93(cpu, mmu, *args)
            OpCode.SUB_94 -> this.sub_94(cpu, mmu, *args)
            OpCode.SUB_95 -> this.sub_95(cpu, mmu, *args)
            OpCode.SUB_96 -> this.sub_96(cpu, mmu, *args)
            OpCode.SUB_97 -> this.sub_97(cpu, mmu, *args)
            OpCode.SBC_98 -> this.sbc_98(cpu, mmu, *args)
            OpCode.SBC_99 -> this.sbc_99(cpu, mmu, *args)
            OpCode.SBC_9A -> this.sbc_9a(cpu, mmu, *args)
            OpCode.SBC_9B -> this.sbc_9b(cpu, mmu, *args)
            OpCode.SBC_9C -> this.sbc_9c(cpu, mmu, *args)
            OpCode.SBC_9D -> this.sbc_9d(cpu, mmu, *args)
            OpCode.SBC_9E -> this.sbc_9e(cpu, mmu, *args)
            OpCode.SBC_9F -> this.sbc_9f(cpu, mmu, *args)

            OpCode.AND_A0 -> this.and_a0(cpu, mmu, *args)
            OpCode.AND_A1 -> this.and_a1(cpu, mmu, *args)
            OpCode.AND_A2 -> this.and_a2(cpu, mmu, *args)
            OpCode.AND_A3 -> this.and_a3(cpu, mmu, *args)
            OpCode.AND_A4 -> this.and_a4(cpu, mmu, *args)
            OpCode.AND_A5 -> this.and_a5(cpu, mmu, *args)
            OpCode.AND_A6 -> this.and_a6(cpu, mmu, *args)
            OpCode.AND_A7 -> this.and_a7(cpu, mmu, *args)
            OpCode.XOR_A8 -> this.xor_a8(cpu, mmu, *args)
            OpCode.XOR_A9 -> this.xor_a9(cpu, mmu, *args)
            OpCode.XOR_AA -> this.xor_aa(cpu, mmu, *args)
            OpCode.XOR_AB -> this.xor_ab(cpu, mmu, *args)
            OpCode.XOR_AC -> this.xor_ac(cpu, mmu, *args)
            OpCode.XOR_AD -> this.xor_ad(cpu, mmu, *args)
            OpCode.XOR_AE -> this.xor_ae(cpu, mmu, *args)
            OpCode.XOR_AF -> this.xor_af(cpu, mmu, *args)

            OpCode.OR_B0 -> this.or_b0(cpu, mmu, *args)
            OpCode.OR_B1 -> this.or_b1(cpu, mmu, *args)
            OpCode.OR_B2 -> this.or_b2(cpu, mmu, *args)
            OpCode.OR_B3 -> this.or_b3(cpu, mmu, *args)
            OpCode.OR_B4 -> this.or_b4(cpu, mmu, *args)
            OpCode.OR_B5 -> this.or_b5(cpu, mmu, *args)
            OpCode.OR_B6 -> this.or_b6(cpu, mmu, *args)
            OpCode.OR_B7 -> this.or_b7(cpu, mmu, *args)
            OpCode.CP_B8 -> this.cp_b8(cpu, mmu, *args)
            OpCode.CP_B9 -> this.cp_b9(cpu, mmu, *args)
            OpCode.CP_BA -> this.cp_ba(cpu, mmu, *args)
            OpCode.CP_BB -> this.cp_bb(cpu, mmu, *args)
            OpCode.CP_BC -> this.cp_bc(cpu, mmu, *args)
            OpCode.CP_BD -> this.cp_bd(cpu, mmu, *args)
            OpCode.CP_BE -> this.cp_be(cpu, mmu, *args)
            OpCode.CP_BF -> this.cp_bf(cpu, mmu, *args)

            OpCode.RET_C0 -> if (!this.ret_c0(cpu, mmu, *args)) {
                return OpCode.RET_C0.cyclesNotTaken
            }
            OpCode.POP_C1 -> this.pop_c1(cpu, mmu, *args)
            OpCode.JP_C2 -> if (!this.jp_c2(cpu, mmu, *args)) {
                return OpCode.JP_C2.cyclesNotTaken
            }
            OpCode.JP_C3 -> this.jp_c3(cpu, mmu, *args)
            OpCode.CALL_C4 -> if (!this.call_c4(cpu, mmu, *args)) {
                return OpCode.CALL_C4.cyclesNotTaken
            }
            OpCode.PUSH_C5 -> this.push_c5(cpu, mmu, *args)
            OpCode.ADD_C6 -> this.add_c6(cpu, mmu, *args)
            OpCode.RST_C7 -> this.rst_c7(cpu, mmu, *args)
            OpCode.RET_C8 -> if (!this.ret_c8(cpu, mmu, *args)) {
                return OpCode.RET_C8.cyclesNotTaken
            }
            OpCode.RET_C9 -> this.ret_c9(cpu, mmu, *args)
            OpCode.JP_CA -> if (!this.jp_ca(cpu, mmu, *args)) {
                return OpCode.JP_CA.cyclesNotTaken
            }
            OpCode.PREFIX_CB -> this.prefix_cb(cpu, mmu, *args)
            OpCode.CALL_CC -> if (!this.call_cc(cpu, mmu, *args)) {
                return OpCode.CALL_CC.cyclesNotTaken
            }
            OpCode.CALL_CD -> this.call_cd(cpu, mmu, *args)
            OpCode.ADC_CE -> this.adc_ce(cpu, mmu, *args)
            OpCode.RST_CF -> this.rst_cf(cpu, mmu, *args)

            OpCode.RET_D0 -> if (!this.ret_d0(cpu, mmu, *args)) {
                return OpCode.RET_D0.cyclesNotTaken
            }
            OpCode.POP_D1 -> this.pop_d1(cpu, mmu, *args)
            OpCode.JP_D2 -> if (!this.jp_d2(cpu, mmu, *args)) {
                return OpCode.JP_D2.cyclesNotTaken
            }
            OpCode.CALL_D4 -> if (!this.call_d4(cpu, mmu, *args)) {
                return OpCode.CALL_D4.cyclesNotTaken
            }
            OpCode.PUSH_D5 -> this.push_d5(cpu, mmu, *args)
            OpCode.SUB_D6 -> this.sub_d6(cpu, mmu, *args)
            OpCode.RST_D7 -> this.rst_d7(cpu, mmu, *args)
            OpCode.RET_D8 -> if (!this.ret_d8(cpu, mmu, *args)) {
                return OpCode.RET_D8.cyclesNotTaken
            }
            OpCode.RETI_D9 -> this.reti_d9(cpu, mmu, *args)
            OpCode.JP_DA -> if (!this.jp_da(cpu, mmu, *args)) {
                return OpCode.JP_DA.cyclesNotTaken
            }
            OpCode.CALL_DC -> if (!this.call_dc(cpu, mmu, *args)) {
                return OpCode.CALL_DC.cyclesNotTaken
            }
            OpCode.SBC_DE -> this.sbc_de(cpu, mmu, *args)
            OpCode.RST_DF -> this.rst_df(cpu, mmu, *args)

            OpCode.LDH_E0 -> this.ldh_e0(cpu, mmu, *args)
            OpCode.POP_E1 -> this.pop_e1(cpu, mmu, *args)
            OpCode.LD_E2 -> this.ld_e2(cpu, mmu, *args)
            OpCode.PUSH_E5 -> this.push_e5(cpu, mmu, *args)
            OpCode.AND_E6 -> this.and_e6(cpu, mmu, *args)
            OpCode.RST_E7 -> this.rst_e7(cpu, mmu, *args)
            OpCode.ADD_E8 -> this.add_e8(cpu, mmu, *args)
            OpCode.JP_E9 -> this.jp_e9(cpu, mmu, *args)
            OpCode.LD_EA -> this.ld_ea(cpu, mmu, *args)
            OpCode.XOR_EE -> this.xor_ee(cpu, mmu, *args)
            OpCode.RST_EF -> this.rst_ef(cpu, mmu, *args)

            OpCode.LDH_F0 -> this.ldh_f0(cpu, mmu, *args)
            OpCode.POP_F1 -> this.pop_f1(cpu, mmu, *args)
            OpCode.LD_F2 -> this.ld_f2(cpu, mmu, *args)
            OpCode.DI_F3 -> this.di_f3(cpu, mmu, *args)
            OpCode.PUSH_F5 -> this.push_f5(cpu, mmu, *args)
            OpCode.OR_F6 -> this.or_f6(cpu, mmu, *args)
            OpCode.RST_F7 -> this.rst_f7(cpu, mmu, *args)
            OpCode.LD_F8 -> this.ld_f8(cpu, mmu, *args)
            OpCode.LD_F9 -> this.ld_f9(cpu, mmu, *args)
            OpCode.LD_FA -> this.ld_fa(cpu, mmu, *args)
            OpCode.EI_FB -> this.ei_fb(cpu, mmu, *args)
            OpCode.CP_FE -> this.cp_fe(cpu, mmu, *args)
            OpCode.RST_FF -> this.rst_ff(cpu, mmu, *args)

            // external opcodes
            OpCode.RLC_00 -> this.rlc_00(cpu, mmu, *args)
            OpCode.RLC_01 -> this.rlc_01(cpu, mmu, *args)
            OpCode.RLC_02 -> this.rlc_02(cpu, mmu, *args)
            OpCode.RLC_03 -> this.rlc_03(cpu, mmu, *args)
            OpCode.RLC_04 -> this.rlc_04(cpu, mmu, *args)
            OpCode.RLC_05 -> this.rlc_05(cpu, mmu, *args)
            OpCode.RLC_06 -> this.rlc_06(cpu, mmu, *args)
            OpCode.RLC_07 -> this.rlc_07(cpu, mmu, *args)
            OpCode.RRC_08 -> this.rrc_08(cpu, mmu, *args)
            OpCode.RRC_09 -> this.rrc_09(cpu, mmu, *args)
            OpCode.RRC_0A -> this.rrc_0a(cpu, mmu, *args)
            OpCode.RRC_0B -> this.rrc_0b(cpu, mmu, *args)
            OpCode.RRC_0C -> this.rrc_0c(cpu, mmu, *args)
            OpCode.RRC_0D -> this.rrc_0d(cpu, mmu, *args)
            OpCode.RRC_0E -> this.rrc_0e(cpu, mmu, *args)
            OpCode.RRC_0F -> this.rrc_0f(cpu, mmu, *args)

            OpCode.RL_10 -> this.rl_10(cpu, mmu, *args)
            OpCode.RL_11 -> this.rl_11(cpu, mmu, *args)
            OpCode.RL_12 -> this.rl_12(cpu, mmu, *args)
            OpCode.RL_13 -> this.rl_13(cpu, mmu, *args)
            OpCode.RL_14 -> this.rl_14(cpu, mmu, *args)
            OpCode.RL_15 -> this.rl_15(cpu, mmu, *args)
            OpCode.RL_16 -> this.rl_16(cpu, mmu, *args)
            OpCode.RL_17 -> this.rl_17(cpu, mmu, *args)
            OpCode.RR_18 -> this.rr_18(cpu, mmu, *args)
            OpCode.RR_19 -> this.rr_19(cpu, mmu, *args)
            OpCode.RR_1A -> this.rr_1a(cpu, mmu, *args)
            OpCode.RR_1B -> this.rr_1b(cpu, mmu, *args)
            OpCode.RR_1C -> this.rr_1c(cpu, mmu, *args)
            OpCode.RR_1D -> this.rr_1d(cpu, mmu, *args)
            OpCode.RR_1E -> this.rr_1e(cpu, mmu, *args)
            OpCode.RR_1F -> this.rr_1f(cpu, mmu, *args)

            OpCode.SLA_20 -> this.sla_20(cpu, mmu, *args)
            OpCode.SLA_21 -> this.sla_21(cpu, mmu, *args)
            OpCode.SLA_22 -> this.sla_22(cpu, mmu, *args)
            OpCode.SLA_23 -> this.sla_23(cpu, mmu, *args)
            OpCode.SLA_24 -> this.sla_24(cpu, mmu, *args)
            OpCode.SLA_25 -> this.sla_25(cpu, mmu, *args)
            OpCode.SLA_26 -> this.sla_26(cpu, mmu, *args)
            OpCode.SLA_27 -> this.sla_27(cpu, mmu, *args)
            OpCode.SRA_28 -> this.sra_28(cpu, mmu, *args)
            OpCode.SRA_29 -> this.sra_29(cpu, mmu, *args)
            OpCode.SRA_2A -> this.sra_2a(cpu, mmu, *args)
            OpCode.SRA_2B -> this.sra_2b(cpu, mmu, *args)
            OpCode.SRA_2C -> this.sra_2c(cpu, mmu, *args)
            OpCode.SRA_2D -> this.sra_2d(cpu, mmu, *args)
            OpCode.SRA_2E -> this.sra_2e(cpu, mmu, *args)
            OpCode.SRA_2F -> this.sra_2f(cpu, mmu, *args)

            OpCode.SWAP_30 -> this.swap_30(cpu, mmu, *args)
            OpCode.SWAP_31 -> this.swap_31(cpu, mmu, *args)
            OpCode.SWAP_32 -> this.swap_32(cpu, mmu, *args)
            OpCode.SWAP_33 -> this.swap_33(cpu, mmu, *args)
            OpCode.SWAP_34 -> this.swap_34(cpu, mmu, *args)
            OpCode.SWAP_35 -> this.swap_35(cpu, mmu, *args)
            OpCode.SWAP_36 -> this.swap_36(cpu, mmu, *args)
            OpCode.SWAP_37 -> this.swap_37(cpu, mmu, *args)
            OpCode.SRL_38 -> this.srl_38(cpu, mmu, *args)
            OpCode.SRL_39 -> this.srl_39(cpu, mmu, *args)
            OpCode.SRL_3A -> this.srl_3a(cpu, mmu, *args)
            OpCode.SRL_3B -> this.srl_3b(cpu, mmu, *args)
            OpCode.SRL_3C -> this.srl_3c(cpu, mmu, *args)
            OpCode.SRL_3D -> this.srl_3d(cpu, mmu, *args)
            OpCode.SRL_3E -> this.srl_3e(cpu, mmu, *args)
            OpCode.SRL_3F -> this.srl_3f(cpu, mmu, *args)

            OpCode.BIT_40 -> this.bit_40(cpu, mmu, *args)
            OpCode.BIT_41 -> this.bit_41(cpu, mmu, *args)
            OpCode.BIT_42 -> this.bit_42(cpu, mmu, *args)
            OpCode.BIT_43 -> this.bit_43(cpu, mmu, *args)
            OpCode.BIT_44 -> this.bit_44(cpu, mmu, *args)
            OpCode.BIT_45 -> this.bit_45(cpu, mmu, *args)
            OpCode.BIT_46 -> this.bit_46(cpu, mmu, *args)
            OpCode.BIT_47 -> this.bit_47(cpu, mmu, *args)
            OpCode.BIT_48 -> this.bit_48(cpu, mmu, *args)
            OpCode.BIT_49 -> this.bit_49(cpu, mmu, *args)
            OpCode.BIT_4A -> this.bit_4a(cpu, mmu, *args)
            OpCode.BIT_4B -> this.bit_4b(cpu, mmu, *args)
            OpCode.BIT_4C -> this.bit_4c(cpu, mmu, *args)
            OpCode.BIT_4D -> this.bit_4d(cpu, mmu, *args)
            OpCode.BIT_4E -> this.bit_4e(cpu, mmu, *args)
            OpCode.BIT_4F -> this.bit_4f(cpu, mmu, *args)

            OpCode.BIT_50 -> this.bit_50(cpu, mmu, *args)
            OpCode.BIT_51 -> this.bit_51(cpu, mmu, *args)
            OpCode.BIT_52 -> this.bit_52(cpu, mmu, *args)
            OpCode.BIT_53 -> this.bit_53(cpu, mmu, *args)
            OpCode.BIT_54 -> this.bit_54(cpu, mmu, *args)
            OpCode.BIT_55 -> this.bit_55(cpu, mmu, *args)
            OpCode.BIT_56 -> this.bit_56(cpu, mmu, *args)
            OpCode.BIT_57 -> this.bit_57(cpu, mmu, *args)
            OpCode.BIT_58 -> this.bit_58(cpu, mmu, *args)
            OpCode.BIT_59 -> this.bit_59(cpu, mmu, *args)
            OpCode.BIT_5A -> this.bit_5a(cpu, mmu, *args)
            OpCode.BIT_5B -> this.bit_5b(cpu, mmu, *args)
            OpCode.BIT_5C -> this.bit_5c(cpu, mmu, *args)
            OpCode.BIT_5D -> this.bit_5d(cpu, mmu, *args)
            OpCode.BIT_5E -> this.bit_5e(cpu, mmu, *args)
            OpCode.BIT_5F -> this.bit_5f(cpu, mmu, *args)

            OpCode.BIT_60 -> this.bit_60(cpu, mmu, *args)
            OpCode.BIT_61 -> this.bit_61(cpu, mmu, *args)
            OpCode.BIT_62 -> this.bit_62(cpu, mmu, *args)
            OpCode.BIT_63 -> this.bit_63(cpu, mmu, *args)
            OpCode.BIT_64 -> this.bit_64(cpu, mmu, *args)
            OpCode.BIT_65 -> this.bit_65(cpu, mmu, *args)
            OpCode.BIT_66 -> this.bit_66(cpu, mmu, *args)
            OpCode.BIT_67 -> this.bit_67(cpu, mmu, *args)
            OpCode.BIT_68 -> this.bit_68(cpu, mmu, *args)
            OpCode.BIT_69 -> this.bit_69(cpu, mmu, *args)
            OpCode.BIT_6A -> this.bit_6a(cpu, mmu, *args)
            OpCode.BIT_6B -> this.bit_6b(cpu, mmu, *args)
            OpCode.BIT_6C -> this.bit_6c(cpu, mmu, *args)
            OpCode.BIT_6D -> this.bit_6d(cpu, mmu, *args)
            OpCode.BIT_6E -> this.bit_6e(cpu, mmu, *args)
            OpCode.BIT_6F -> this.bit_6f(cpu, mmu, *args)

            OpCode.BIT_70 -> this.bit_70(cpu, mmu, *args)
            OpCode.BIT_71 -> this.bit_71(cpu, mmu, *args)
            OpCode.BIT_72 -> this.bit_72(cpu, mmu, *args)
            OpCode.BIT_73 -> this.bit_73(cpu, mmu, *args)
            OpCode.BIT_74 -> this.bit_74(cpu, mmu, *args)
            OpCode.BIT_75 -> this.bit_75(cpu, mmu, *args)
            OpCode.BIT_76 -> this.bit_76(cpu, mmu, *args)
            OpCode.BIT_77 -> this.bit_77(cpu, mmu, *args)
            OpCode.BIT_78 -> this.bit_78(cpu, mmu, *args)
            OpCode.BIT_79 -> this.bit_79(cpu, mmu, *args)
            OpCode.BIT_7A -> this.bit_7a(cpu, mmu, *args)
            OpCode.BIT_7B -> this.bit_7b(cpu, mmu, *args)
            OpCode.BIT_7C -> this.bit_7c(cpu, mmu, *args)
            OpCode.BIT_7D -> this.bit_7d(cpu, mmu, *args)
            OpCode.BIT_7E -> this.bit_7e(cpu, mmu, *args)
            OpCode.BIT_7F -> this.bit_7f(cpu, mmu, *args)

            OpCode.RES_80 -> this.res_80(cpu, mmu, *args)
            OpCode.RES_81 -> this.res_81(cpu, mmu, *args)
            OpCode.RES_82 -> this.res_82(cpu, mmu, *args)
            OpCode.RES_83 -> this.res_83(cpu, mmu, *args)
            OpCode.RES_84 -> this.res_84(cpu, mmu, *args)
            OpCode.RES_85 -> this.res_85(cpu, mmu, *args)
            OpCode.RES_86 -> this.res_86(cpu, mmu, *args)
            OpCode.RES_87 -> this.res_87(cpu, mmu, *args)
            OpCode.RES_88 -> this.res_88(cpu, mmu, *args)
            OpCode.RES_89 -> this.res_89(cpu, mmu, *args)
            OpCode.RES_8A -> this.res_8a(cpu, mmu, *args)
            OpCode.RES_8B -> this.res_8b(cpu, mmu, *args)
            OpCode.RES_8C -> this.res_8c(cpu, mmu, *args)
            OpCode.RES_8D -> this.res_8d(cpu, mmu, *args)
            OpCode.RES_8E -> this.res_8e(cpu, mmu, *args)
            OpCode.RES_8F -> this.res_8f(cpu, mmu, *args)

            OpCode.RES_90 -> this.res_90(cpu, mmu, *args)
            OpCode.RES_91 -> this.res_91(cpu, mmu, *args)
            OpCode.RES_92 -> this.res_92(cpu, mmu, *args)
            OpCode.RES_93 -> this.res_93(cpu, mmu, *args)
            OpCode.RES_94 -> this.res_94(cpu, mmu, *args)
            OpCode.RES_95 -> this.res_95(cpu, mmu, *args)
            OpCode.RES_96 -> this.res_96(cpu, mmu, *args)
            OpCode.RES_97 -> this.res_97(cpu, mmu, *args)
            OpCode.RES_98 -> this.res_98(cpu, mmu, *args)
            OpCode.RES_99 -> this.res_99(cpu, mmu, *args)
            OpCode.RES_9A -> this.res_9a(cpu, mmu, *args)
            OpCode.RES_9B -> this.res_9b(cpu, mmu, *args)
            OpCode.RES_9C -> this.res_9c(cpu, mmu, *args)
            OpCode.RES_9D -> this.res_9d(cpu, mmu, *args)
            OpCode.RES_9E -> this.res_9e(cpu, mmu, *args)
            OpCode.RES_9F -> this.res_9f(cpu, mmu, *args)

            OpCode.RES_A0 -> this.res_a0(cpu, mmu, *args)
            OpCode.RES_A1 -> this.res_a1(cpu, mmu, *args)
            OpCode.RES_A2 -> this.res_a2(cpu, mmu, *args)
            OpCode.RES_A3 -> this.res_a3(cpu, mmu, *args)
            OpCode.RES_A4 -> this.res_a4(cpu, mmu, *args)
            OpCode.RES_A5 -> this.res_a5(cpu, mmu, *args)
            OpCode.RES_A6 -> this.res_a6(cpu, mmu, *args)
            OpCode.RES_A7 -> this.res_a7(cpu, mmu, *args)
            OpCode.RES_A8 -> this.res_a8(cpu, mmu, *args)
            OpCode.RES_A9 -> this.res_a9(cpu, mmu, *args)
            OpCode.RES_AA -> this.res_aa(cpu, mmu, *args)
            OpCode.RES_AB -> this.res_ab(cpu, mmu, *args)
            OpCode.RES_AC -> this.res_ac(cpu, mmu, *args)
            OpCode.RES_AD -> this.res_ad(cpu, mmu, *args)
            OpCode.RES_AE -> this.res_ae(cpu, mmu, *args)
            OpCode.RES_AF -> this.res_af(cpu, mmu, *args)

            OpCode.RES_B0 -> this.res_b0(cpu, mmu, *args)
            OpCode.RES_B1 -> this.res_b1(cpu, mmu, *args)
            OpCode.RES_B2 -> this.res_b2(cpu, mmu, *args)
            OpCode.RES_B3 -> this.res_b3(cpu, mmu, *args)
            OpCode.RES_B4 -> this.res_b4(cpu, mmu, *args)
            OpCode.RES_B5 -> this.res_b5(cpu, mmu, *args)
            OpCode.RES_B6 -> this.res_b6(cpu, mmu, *args)
            OpCode.RES_B7 -> this.res_b7(cpu, mmu, *args)
            OpCode.RES_B8 -> this.res_b8(cpu, mmu, *args)
            OpCode.RES_B9 -> this.res_b9(cpu, mmu, *args)
            OpCode.RES_BA -> this.res_ba(cpu, mmu, *args)
            OpCode.RES_BB -> this.res_bb(cpu, mmu, *args)
            OpCode.RES_BC -> this.res_bc(cpu, mmu, *args)
            OpCode.RES_BD -> this.res_bd(cpu, mmu, *args)
            OpCode.RES_BE -> this.res_be(cpu, mmu, *args)
            OpCode.RES_BF -> this.res_bf(cpu, mmu, *args)

            OpCode.SET_C0 -> this.set_c0(cpu, mmu, *args)
            OpCode.SET_C1 -> this.set_c1(cpu, mmu, *args)
            OpCode.SET_C2 -> this.set_c2(cpu, mmu, *args)
            OpCode.SET_C3 -> this.set_c3(cpu, mmu, *args)
            OpCode.SET_C4 -> this.set_c4(cpu, mmu, *args)
            OpCode.SET_C5 -> this.set_c5(cpu, mmu, *args)
            OpCode.SET_C6 -> this.set_c6(cpu, mmu, *args)
            OpCode.SET_C7 -> this.set_c7(cpu, mmu, *args)
            OpCode.SET_C8 -> this.set_c8(cpu, mmu, *args)
            OpCode.SET_C9 -> this.set_c9(cpu, mmu, *args)
            OpCode.SET_CA -> this.set_ca(cpu, mmu, *args)
            OpCode.SET_CB -> this.set_cb(cpu, mmu, *args)
            OpCode.SET_CC -> this.set_cc(cpu, mmu, *args)
            OpCode.SET_CD -> this.set_cd(cpu, mmu, *args)
            OpCode.SET_CE -> this.set_ce(cpu, mmu, *args)
            OpCode.SET_CF -> this.set_cf(cpu, mmu, *args)

            OpCode.SET_D0 -> this.set_d0(cpu, mmu, *args)
            OpCode.SET_D1 -> this.set_d1(cpu, mmu, *args)
            OpCode.SET_D2 -> this.set_d2(cpu, mmu, *args)
            OpCode.SET_D3 -> this.set_d3(cpu, mmu, *args)
            OpCode.SET_D4 -> this.set_d4(cpu, mmu, *args)
            OpCode.SET_D5 -> this.set_d5(cpu, mmu, *args)
            OpCode.SET_D6 -> this.set_d6(cpu, mmu, *args)
            OpCode.SET_D7 -> this.set_d7(cpu, mmu, *args)
            OpCode.SET_D8 -> this.set_d8(cpu, mmu, *args)
            OpCode.SET_D9 -> this.set_d9(cpu, mmu, *args)
            OpCode.SET_DA -> this.set_da(cpu, mmu, *args)
            OpCode.SET_DB -> this.set_db(cpu, mmu, *args)
            OpCode.SET_DC -> this.set_dc(cpu, mmu, *args)
            OpCode.SET_DD -> this.set_dd(cpu, mmu, *args)
            OpCode.SET_DE -> this.set_de(cpu, mmu, *args)
            OpCode.SET_DF -> this.set_df(cpu, mmu, *args)

            OpCode.SET_E0 -> this.set_e0(cpu, mmu, *args)
            OpCode.SET_E1 -> this.set_e1(cpu, mmu, *args)
            OpCode.SET_E2 -> this.set_e2(cpu, mmu, *args)
            OpCode.SET_E3 -> this.set_e3(cpu, mmu, *args)
            OpCode.SET_E4 -> this.set_e4(cpu, mmu, *args)
            OpCode.SET_E5 -> this.set_e5(cpu, mmu, *args)
            OpCode.SET_E6 -> this.set_e6(cpu, mmu, *args)
            OpCode.SET_E7 -> this.set_e7(cpu, mmu, *args)
            OpCode.SET_E8 -> this.set_e8(cpu, mmu, *args)
            OpCode.SET_E9 -> this.set_e9(cpu, mmu, *args)
            OpCode.SET_EA -> this.set_ea(cpu, mmu, *args)
            OpCode.SET_EB -> this.set_eb(cpu, mmu, *args)
            OpCode.SET_EC -> this.set_ec(cpu, mmu, *args)
            OpCode.SET_ED -> this.set_ed(cpu, mmu, *args)
            OpCode.SET_EE -> this.set_ee(cpu, mmu, *args)
            OpCode.SET_EF -> this.set_ef(cpu, mmu, *args)

            OpCode.SET_F0 -> this.set_f0(cpu, mmu, *args)
            OpCode.SET_F1 -> this.set_f1(cpu, mmu, *args)
            OpCode.SET_F2 -> this.set_f2(cpu, mmu, *args)
            OpCode.SET_F3 -> this.set_f3(cpu, mmu, *args)
            OpCode.SET_F4 -> this.set_f4(cpu, mmu, *args)
            OpCode.SET_F5 -> this.set_f5(cpu, mmu, *args)
            OpCode.SET_F6 -> this.set_f6(cpu, mmu, *args)
            OpCode.SET_F7 -> this.set_f7(cpu, mmu, *args)
            OpCode.SET_F8 -> this.set_f8(cpu, mmu, *args)
            OpCode.SET_F9 -> this.set_f9(cpu, mmu, *args)
            OpCode.SET_FA -> this.set_fa(cpu, mmu, *args)
            OpCode.SET_FB -> this.set_fb(cpu, mmu, *args)
            OpCode.SET_FC -> this.set_fc(cpu, mmu, *args)
            OpCode.SET_FD -> this.set_fd(cpu, mmu, *args)
            OpCode.SET_FE -> this.set_fe(cpu, mmu, *args)
            OpCode.SET_FF -> this.set_ff(cpu, mmu, *args)
        }
        return opCode.cycles
    }

    private fun nop_00(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.nop()
    private fun prefix_cb(cpu: Cpu, mmu: Mmu, vararg args: Int) = Unit

    private fun ld_06(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, args[0])
    private fun ld_0e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, args[0])
    private fun ld_16(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, args[0])
    private fun ld_1e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, args[0])
    private fun ld_26(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, args[0])
    private fun ld_2e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, args[0])

    private fun ld_40(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, cpu.read(Reg8.B))
    private fun ld_41(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, cpu.read(Reg8.C))
    private fun ld_42(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, cpu.read(Reg8.D))
    private fun ld_43(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, cpu.read(Reg8.E))
    private fun ld_44(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, cpu.read(Reg8.H))
    private fun ld_45(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, cpu.read(Reg8.L))
    private fun ld_46(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.B, mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun ld_48(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, cpu.read(Reg8.B))
    private fun ld_49(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, cpu.read(Reg8.C))
    private fun ld_4a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, cpu.read(Reg8.D))
    private fun ld_4b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, cpu.read(Reg8.E))
    private fun ld_4c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, cpu.read(Reg8.H))
    private fun ld_4d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, cpu.read(Reg8.L))
    private fun ld_4e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.C, mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun ld_50(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, cpu.read(Reg8.B))
    private fun ld_51(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, cpu.read(Reg8.C))
    private fun ld_52(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, cpu.read(Reg8.D))
    private fun ld_53(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, cpu.read(Reg8.E))
    private fun ld_54(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, cpu.read(Reg8.H))
    private fun ld_55(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, cpu.read(Reg8.L))
    private fun ld_56(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.D, mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun ld_58(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, cpu.read(Reg8.B))
    private fun ld_59(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, cpu.read(Reg8.C))
    private fun ld_5a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, cpu.read(Reg8.D))
    private fun ld_5b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, cpu.read(Reg8.E))
    private fun ld_5c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, cpu.read(Reg8.H))
    private fun ld_5d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, cpu.read(Reg8.L))
    private fun ld_5e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.E, mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun ld_60(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, cpu.read(Reg8.B))
    private fun ld_61(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, cpu.read(Reg8.C))
    private fun ld_62(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, cpu.read(Reg8.D))
    private fun ld_63(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, cpu.read(Reg8.E))
    private fun ld_64(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, cpu.read(Reg8.H))
    private fun ld_65(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, cpu.read(Reg8.L))
    private fun ld_66(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.H, mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun ld_68(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, cpu.read(Reg8.B))
    private fun ld_69(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, cpu.read(Reg8.C))
    private fun ld_6a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, cpu.read(Reg8.D))
    private fun ld_6b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, cpu.read(Reg8.E))
    private fun ld_6c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, cpu.read(Reg8.H))
    private fun ld_6d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, cpu.read(Reg8.L))
    private fun ld_6e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.L, mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun ld_70(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.read(Reg8.B).toUnsignedInt())

    private fun ld_71(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.read(Reg8.C).toUnsignedInt())

    private fun ld_72(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.read(Reg8.D).toUnsignedInt())

    private fun ld_73(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.read(Reg8.E).toUnsignedInt())

    private fun ld_74(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.read(Reg8.H).toUnsignedInt())

    private fun ld_75(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.read(Reg8.L).toUnsignedInt())

    private fun ld_36(cpu: Cpu, mmu: Mmu, vararg args: Int) = mmu.set(cpu.read(Reg16.HL), args[0])

    private fun ld_7f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, cpu.read(Reg8.A))
    private fun ld_78(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, cpu.read(Reg8.B))
    private fun ld_79(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, cpu.read(Reg8.C))
    private fun ld_7a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, cpu.read(Reg8.D))
    private fun ld_7b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, cpu.read(Reg8.E))
    private fun ld_7c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, cpu.read(Reg8.H))
    private fun ld_7d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, cpu.read(Reg8.L))

    private fun ld_0a(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.A, mmu.get(cpu.read(Reg16.BC)).toUnsignedInt())

    private fun ld_1a(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.A, mmu.get(cpu.read(Reg16.DE)).toUnsignedInt())

    private fun ld_7e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.A, mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun ld_fa(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, mmu.get(args.toWord()).toUnsignedInt())
    private fun ld_3e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.A, args[0])

    private fun ld_47(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.B, cpu.read(Reg8.A))
    private fun ld_4f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.C, cpu.read(Reg8.A))
    private fun ld_57(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.D, cpu.read(Reg8.A))
    private fun ld_5f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.E, cpu.read(Reg8.A))
    private fun ld_67(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.H, cpu.read(Reg8.A))
    private fun ld_6f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg8.L, cpu.read(Reg8.A))

    private fun ld_02(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.BC), cpu.read(Reg8.A).toUnsignedInt())

    private fun ld_12(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.DE), cpu.read(Reg8.A).toUnsignedInt())

    private fun ld_77(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.read(Reg8.A).toUnsignedInt())

    private fun ld_ea(cpu: Cpu, mmu: Mmu, vararg args: Int) = mmu.set(args.toWord(), cpu.read(Reg8.A).toUnsignedInt())

    private fun ld_f2(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.A, mmu.get(0xFF00 + cpu.read(Reg8.C).toUnsignedInt()).toUnsignedInt())

    private fun ld_e2(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(0xFF00 + cpu.read(Reg8.C).toUnsignedInt(), cpu.read(Reg8.A).toUnsignedInt())

    private fun ld_3a(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        val hl = cpu.read(Reg16.HL)
        cpu.write(Reg8.A, mmu.get(hl).toUnsignedInt())
        cpu.write(Reg16.HL, hl - 1)
    }

    private fun ld_32(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        val hl = cpu.read(Reg16.HL)
        mmu.set(hl, cpu.read(Reg8.A).toUnsignedInt())
        cpu.write(Reg16.HL, hl - 1)
    }

    private fun ld_2a(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        val hl = cpu.read(Reg16.HL)
        cpu.write(Reg8.A, mmu.get(hl).toUnsignedInt())
        cpu.write(Reg16.HL, hl + 1)
    }

    private fun ld_22(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        val hl = cpu.read(Reg16.HL)
        mmu[hl] = cpu.read(Reg8.A).toUnsignedInt()
        cpu.write(Reg16.HL, hl + 1)
    }

    private fun ldh_e0(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(0xFF00 + args[0], cpu.read(Reg8.A).toUnsignedInt())

    private fun ldh_f0(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg8.A, mmu[0xFF00 + args[0]].toUnsignedInt())

    private fun ld_01(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg16.BC, args.toWord())
    private fun ld_11(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg16.DE, args.toWord())
    private fun ld_21(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg16.HL, args.toWord())

    private fun ld_31(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        cpu.SP = args.toWord()
    }

    private fun ld_f9(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        cpu.SP = cpu.read(Reg16.HL)
    }

    private fun ld_f8(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.write(Reg16.HL, cpu.alu.add_SP_n(args[0]))

    private fun ld_08(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        mmu.set(args.toWord(), cpu.SP.lsb())
        mmu.set((args.toWord() + 1).toWord(), cpu.SP.msb())
    }

    private fun push_f5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.push_n(cpu.read(Reg16.AF))
    private fun push_c5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.push_n(cpu.read(Reg16.BC))
    private fun push_d5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.push_n(cpu.read(Reg16.DE))
    private fun push_e5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.push_n(cpu.read(Reg16.HL))

    private fun pop_f1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg16.AF, cpu.pop_n())
    private fun pop_c1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg16.BC, cpu.pop_n())
    private fun pop_d1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg16.DE, cpu.pop_n())
    private fun pop_e1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.write(Reg16.HL, cpu.pop_n())

    private fun add_87(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(cpu.read(Reg8.A))
    private fun add_80(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(cpu.read(Reg8.B))
    private fun add_81(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(cpu.read(Reg8.C))
    private fun add_82(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(cpu.read(Reg8.D))
    private fun add_83(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(cpu.read(Reg8.E))
    private fun add_84(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(cpu.read(Reg8.H))
    private fun add_85(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(cpu.read(Reg8.L))
    private fun add_86(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.alu.add_A_n(mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun add_c6(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_A_n(args[0].toUnsignedInt())

    private fun adc_8f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(cpu.read(Reg8.A))
    private fun adc_88(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(cpu.read(Reg8.B))
    private fun adc_89(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(cpu.read(Reg8.C))
    private fun adc_8a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(cpu.read(Reg8.D))
    private fun adc_8b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(cpu.read(Reg8.E))
    private fun adc_8c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(cpu.read(Reg8.H))
    private fun adc_8d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(cpu.read(Reg8.L))
    private fun adc_8e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.alu.adc_A_n(mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun adc_ce(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.adc_A_n(args[0].toUnsignedInt())

    private fun sub_97(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(cpu.read(Reg8.A))
    private fun sub_90(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(cpu.read(Reg8.B))
    private fun sub_91(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(cpu.read(Reg8.C))
    private fun sub_92(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(cpu.read(Reg8.D))
    private fun sub_93(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(cpu.read(Reg8.E))
    private fun sub_94(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(cpu.read(Reg8.H))
    private fun sub_95(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(cpu.read(Reg8.L))
    private fun sub_96(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.alu.sub_n(mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun sub_d6(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sub_n(args[0].toUnsignedInt())

    private fun sbc_9f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(cpu.read(Reg8.A))
    private fun sbc_98(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(cpu.read(Reg8.B))
    private fun sbc_99(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(cpu.read(Reg8.C))
    private fun sbc_9a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(cpu.read(Reg8.D))
    private fun sbc_9b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(cpu.read(Reg8.E))
    private fun sbc_9c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(cpu.read(Reg8.H))
    private fun sbc_9d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(cpu.read(Reg8.L))
    private fun sbc_9e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        cpu.alu.sbc_A_n(mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())

    private fun sbc_de(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.sbc_A_n(args[0].toUnsignedInt())

    private fun and_a7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(cpu.read(Reg8.A))
    private fun and_a0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(cpu.read(Reg8.B))
    private fun and_a1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(cpu.read(Reg8.C))
    private fun and_a2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(cpu.read(Reg8.D))
    private fun and_a3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(cpu.read(Reg8.E))
    private fun and_a4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(cpu.read(Reg8.H))
    private fun and_a5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(cpu.read(Reg8.L))
    private fun and_a6(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())
    private fun and_e6(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.and(args[0].toUnsignedInt())

    private fun or_b7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(cpu.read(Reg8.A))
    private fun or_b0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(cpu.read(Reg8.B))
    private fun or_b1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(cpu.read(Reg8.C))
    private fun or_b2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(cpu.read(Reg8.D))
    private fun or_b3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(cpu.read(Reg8.E))
    private fun or_b4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(cpu.read(Reg8.H))
    private fun or_b5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(cpu.read(Reg8.L))
    private fun or_b6(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())
    private fun or_f6(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.or(args[0].toUnsignedInt())

    private fun xor_af(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(cpu.read(Reg8.A))
    private fun xor_a8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(cpu.read(Reg8.B))
    private fun xor_a9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(cpu.read(Reg8.C))
    private fun xor_aa(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(cpu.read(Reg8.D))
    private fun xor_ab(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(cpu.read(Reg8.E))
    private fun xor_ac(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(cpu.read(Reg8.H))
    private fun xor_ad(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(cpu.read(Reg8.L))
    private fun xor_ae(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(mmu.get(cpu.read(Reg16.HL)).toUnsignedInt())
    private fun xor_ee(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.xor(args[0].toUnsignedInt())

    private fun cp_bf(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(cpu.read(Reg8.A))
    private fun cp_b8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(cpu.read(Reg8.B))
    private fun cp_b9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(cpu.read(Reg8.C))
    private fun cp_ba(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(cpu.read(Reg8.D))
    private fun cp_bb(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(cpu.read(Reg8.E))
    private fun cp_bc(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(cpu.read(Reg8.H))
    private fun cp_bd(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(cpu.read(Reg8.L))
    private fun cp_be(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(mmu[cpu.read(Reg16.HL)].toUnsignedInt())
    private fun cp_fe(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.cp_n(args[0].toUnsignedInt())

    private fun inc_3c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg8.A)
    private fun inc_04(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg8.B)
    private fun inc_0c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg8.C)
    private fun inc_14(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg8.D)
    private fun inc_1c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg8.E)
    private fun inc_24(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg8.H)
    private fun inc_2c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg8.L)
    private fun inc_34(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.alu.inc_n(mmu.get(cpu.read(Reg16.HL))))

    private fun dec_3d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg8.A)
    private fun dec_05(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg8.B)

    private fun dec_0d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg8.C)
    private fun dec_15(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg8.D)
    private fun dec_1d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg8.E)
    private fun dec_25(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg8.H)
    private fun dec_2d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg8.L)
    private fun dec_35(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.alu.dec_n(mmu.get(cpu.read(Reg16.HL))))

    private fun add_09(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_HL_n(cpu.read(Reg16.BC))
    private fun add_19(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_HL_n(cpu.read(Reg16.DE))
    private fun add_29(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_HL_n(cpu.read(Reg16.HL))
    private fun add_39(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.add_HL_n(cpu.SP)

    private fun add_e8(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        cpu.SP = cpu.alu.add_SP_n(args[0])
    }

    private fun inc_03(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg16.BC)
    private fun inc_13(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg16.DE)
    private fun inc_23(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.inc_n(Reg16.HL)

    private fun inc_33(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        cpu.SP = (cpu.SP + 1).toWord()
    }

    private fun dec_0b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg16.BC)
    private fun dec_1b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg16.DE)
    private fun dec_2b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.alu.dec_n(Reg16.HL)

    private fun dec_3b(cpu: Cpu, mmu: Mmu, vararg args: Int) {
        cpu.SP = (cpu.SP - 1).toWord()
    }

    private fun swap_37(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.swap_n(Reg8.A)
    private fun swap_30(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.swap_n(Reg8.B)
    private fun swap_31(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.swap_n(Reg8.C)
    private fun swap_32(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.swap_n(Reg8.D)
    private fun swap_33(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.swap_n(Reg8.E)
    private fun swap_34(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.swap_n(Reg8.H)
    private fun swap_35(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.swap_n(Reg8.L)
    private fun swap_36(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.swap_n(mmu.get(cpu.read(Reg16.HL))).toUnsignedInt())

    private fun daa_27(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.daa()

    private fun cpl_2f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.cpl()

    private fun ccf_3f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.ccf()

    private fun scf_37(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.scf()

    private fun halt_76(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.halt()
    private fun stop_10(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.stop()
    private fun di_f3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.di()
    private fun ei_fb(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.ei()

    private fun rlca_07(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlca()
    private fun rla_17(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rla()

    private fun rrca_0f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrca()
    private fun rra_1f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rra()

    private fun rlc_07(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlc_n(Reg8.A)
    private fun rlc_00(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlc_n(Reg8.B)
    private fun rlc_01(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlc_n(Reg8.C)
    private fun rlc_02(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlc_n(Reg8.D)
    private fun rlc_03(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlc_n(Reg8.E)
    private fun rlc_04(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlc_n(Reg8.H)
    private fun rlc_05(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rlc_n(Reg8.L)
    private fun rlc_06(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.rlc_n(mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun rl_17(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rl_n(Reg8.A)
    private fun rl_10(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rl_n(Reg8.B)
    private fun rl_11(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rl_n(Reg8.C)
    private fun rl_12(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rl_n(Reg8.D)
    private fun rl_13(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rl_n(Reg8.E)
    private fun rl_14(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rl_n(Reg8.H)
    private fun rl_15(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rl_n(Reg8.L)
    private fun rl_16(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.rl_n(mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun rrc_0f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrc_n(Reg8.A)
    private fun rrc_08(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrc_n(Reg8.B)
    private fun rrc_09(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrc_n(Reg8.C)
    private fun rrc_0a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrc_n(Reg8.D)
    private fun rrc_0b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrc_n(Reg8.E)
    private fun rrc_0c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrc_n(Reg8.H)
    private fun rrc_0d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rrc_n(Reg8.L)
    private fun rrc_0e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.rrc_n(mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun rr_1f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rr_n(Reg8.A)
    private fun rr_18(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rr_n(Reg8.B)
    private fun rr_19(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rr_n(Reg8.C)
    private fun rr_1a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rr_n(Reg8.D)
    private fun rr_1b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rr_n(Reg8.E)
    private fun rr_1c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rr_n(Reg8.H)
    private fun rr_1d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rr_n(Reg8.L)
    private fun rr_1e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.rr_n(mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun sla_27(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sla_n(Reg8.A)
    private fun sla_20(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sla_n(Reg8.B)
    private fun sla_21(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sla_n(Reg8.C)
    private fun sla_22(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sla_n(Reg8.D)
    private fun sla_23(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sla_n(Reg8.E)
    private fun sla_24(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sla_n(Reg8.H)
    private fun sla_25(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sla_n(Reg8.L)
    private fun sla_26(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.sla_n(mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun sra_2f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sra_n(Reg8.A)
    private fun sra_28(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sra_n(Reg8.B)
    private fun sra_29(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sra_n(Reg8.C)
    private fun sra_2a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sra_n(Reg8.D)
    private fun sra_2b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sra_n(Reg8.E)
    private fun sra_2c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sra_n(Reg8.H)
    private fun sra_2d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.sra_n(Reg8.L)
    private fun sra_2e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.sra_n(mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun srl_3f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.srl_n(Reg8.A)
    private fun srl_38(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.srl_n(Reg8.B)
    private fun srl_39(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.srl_n(Reg8.C)
    private fun srl_3a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.srl_n(Reg8.D)
    private fun srl_3b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.srl_n(Reg8.E)
    private fun srl_3c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.srl_n(Reg8.H)
    private fun srl_3d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.srl_n(Reg8.L)
    private fun srl_3e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.srl_n(mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun bit_47(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, cpu.read(Reg8.A))
    private fun bit_40(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, cpu.read(Reg8.B))
    private fun bit_41(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, cpu.read(Reg8.C))
    private fun bit_42(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, cpu.read(Reg8.D))
    private fun bit_43(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, cpu.read(Reg8.E))
    private fun bit_44(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, cpu.read(Reg8.H))
    private fun bit_45(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, cpu.read(Reg8.L))
    private fun bit_46(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(0, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun bit_4f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, cpu.read(Reg8.A))
    private fun bit_48(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, cpu.read(Reg8.B))
    private fun bit_49(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, cpu.read(Reg8.C))
    private fun bit_4a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, cpu.read(Reg8.D))
    private fun bit_4b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, cpu.read(Reg8.E))
    private fun bit_4c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, cpu.read(Reg8.H))
    private fun bit_4d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, cpu.read(Reg8.L))
    private fun bit_4e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(1, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun bit_57(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, cpu.read(Reg8.A))
    private fun bit_50(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, cpu.read(Reg8.B))
    private fun bit_51(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, cpu.read(Reg8.C))
    private fun bit_52(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, cpu.read(Reg8.D))
    private fun bit_53(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, cpu.read(Reg8.E))
    private fun bit_54(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, cpu.read(Reg8.H))
    private fun bit_55(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, cpu.read(Reg8.L))
    private fun bit_56(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(2, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun bit_5f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, cpu.read(Reg8.A))
    private fun bit_58(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, cpu.read(Reg8.B))
    private fun bit_59(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, cpu.read(Reg8.C))
    private fun bit_5a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, cpu.read(Reg8.D))
    private fun bit_5b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, cpu.read(Reg8.E))
    private fun bit_5c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, cpu.read(Reg8.H))
    private fun bit_5d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, cpu.read(Reg8.L))
    private fun bit_5e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(3, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun bit_67(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, cpu.read(Reg8.A))
    private fun bit_60(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, cpu.read(Reg8.B))
    private fun bit_61(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, cpu.read(Reg8.C))
    private fun bit_62(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, cpu.read(Reg8.D))
    private fun bit_63(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, cpu.read(Reg8.E))
    private fun bit_64(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, cpu.read(Reg8.H))
    private fun bit_65(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, cpu.read(Reg8.L))
    private fun bit_66(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(4, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun bit_6f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, cpu.read(Reg8.A))
    private fun bit_68(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, cpu.read(Reg8.B))
    private fun bit_69(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, cpu.read(Reg8.C))
    private fun bit_6a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, cpu.read(Reg8.D))
    private fun bit_6b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, cpu.read(Reg8.E))
    private fun bit_6c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, cpu.read(Reg8.H))
    private fun bit_6d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, cpu.read(Reg8.L))
    private fun bit_6e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(5, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun bit_77(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, cpu.read(Reg8.A))
    private fun bit_70(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, cpu.read(Reg8.B))
    private fun bit_71(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, cpu.read(Reg8.C))
    private fun bit_72(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, cpu.read(Reg8.D))
    private fun bit_73(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, cpu.read(Reg8.E))
    private fun bit_74(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, cpu.read(Reg8.H))
    private fun bit_75(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, cpu.read(Reg8.L))
    private fun bit_76(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(6, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun bit_7f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, cpu.read(Reg8.A))
    private fun bit_78(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, cpu.read(Reg8.B))
    private fun bit_79(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, cpu.read(Reg8.C))
    private fun bit_7a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, cpu.read(Reg8.D))
    private fun bit_7b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, cpu.read(Reg8.E))
    private fun bit_7c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, cpu.read(Reg8.H))
    private fun bit_7d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, cpu.read(Reg8.L))
    private fun bit_7e(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.bit_b_r(7, mmu.get(cpu.read(Reg16.HL)).toByte())

    private fun set_c7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(0, Reg8.A)
    private fun set_c0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(0, Reg8.B)
    private fun set_c1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(0, Reg8.C)
    private fun set_c2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(0, Reg8.D)
    private fun set_c3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(0, Reg8.E)
    private fun set_c4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(0, Reg8.H)
    private fun set_c5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(0, Reg8.L)
    private fun set_c6(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(0, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun set_cf(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(1, Reg8.A)
    private fun set_c8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(1, Reg8.B)
    private fun set_c9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(1, Reg8.C)
    private fun set_ca(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(1, Reg8.D)
    private fun set_cb(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(1, Reg8.E)
    private fun set_cc(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(1, Reg8.H)
    private fun set_cd(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(1, Reg8.L)
    private fun set_ce(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(1, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun set_d7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(2, Reg8.A)
    private fun set_d0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(2, Reg8.B)
    private fun set_d1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(2, Reg8.C)
    private fun set_d2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(2, Reg8.D)
    private fun set_d3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(2, Reg8.E)
    private fun set_d4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(2, Reg8.H)
    private fun set_d5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(2, Reg8.L)
    private fun set_d6(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(2, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun set_df(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(3, Reg8.A)
    private fun set_d8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(3, Reg8.B)
    private fun set_d9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(3, Reg8.C)
    private fun set_da(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(3, Reg8.D)
    private fun set_db(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(3, Reg8.E)
    private fun set_dc(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(3, Reg8.H)
    private fun set_dd(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(3, Reg8.L)
    private fun set_de(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(3, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun set_e7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(4, Reg8.A)
    private fun set_e0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(4, Reg8.B)
    private fun set_e1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(4, Reg8.C)
    private fun set_e2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(4, Reg8.D)
    private fun set_e3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(4, Reg8.E)
    private fun set_e4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(4, Reg8.H)
    private fun set_e5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(4, Reg8.L)
    private fun set_e6(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(4, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun set_ef(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(5, Reg8.A)
    private fun set_e8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(5, Reg8.B)
    private fun set_e9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(5, Reg8.C)
    private fun set_ea(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(5, Reg8.D)
    private fun set_eb(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(5, Reg8.E)
    private fun set_ec(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(5, Reg8.H)
    private fun set_ed(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(5, Reg8.L)
    private fun set_ee(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(5, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun set_f7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(6, Reg8.A)
    private fun set_f0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(6, Reg8.B)
    private fun set_f1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(6, Reg8.C)
    private fun set_f2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(6, Reg8.D)
    private fun set_f3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(6, Reg8.E)
    private fun set_f4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(6, Reg8.H)
    private fun set_f5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(6, Reg8.L)
    private fun set_f6(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(6, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun set_ff(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(7, Reg8.A)
    private fun set_f8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(7, Reg8.B)
    private fun set_f9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(7, Reg8.C)
    private fun set_fa(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(7, Reg8.D)
    private fun set_fb(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(7, Reg8.E)
    private fun set_fc(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(7, Reg8.H)
    private fun set_fd(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.set_b_r(7, Reg8.L)
    private fun set_fe(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.set_b_r(7, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_87(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(0, Reg8.A)
    private fun res_80(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(0, Reg8.B)
    private fun res_81(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(0, Reg8.C)
    private fun res_82(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(0, Reg8.D)
    private fun res_83(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(0, Reg8.E)
    private fun res_84(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(0, Reg8.H)
    private fun res_85(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(0, Reg8.L)
    private fun res_86(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(0, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_8f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(1, Reg8.A)
    private fun res_88(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(1, Reg8.B)
    private fun res_89(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(1, Reg8.C)
    private fun res_8a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(1, Reg8.D)
    private fun res_8b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(1, Reg8.E)
    private fun res_8c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(1, Reg8.H)
    private fun res_8d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(1, Reg8.L)
    private fun res_8e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(1, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_97(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(2, Reg8.A)
    private fun res_90(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(2, Reg8.B)
    private fun res_91(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(2, Reg8.C)
    private fun res_92(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(2, Reg8.D)
    private fun res_93(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(2, Reg8.E)
    private fun res_94(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(2, Reg8.H)
    private fun res_95(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(2, Reg8.L)
    private fun res_96(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(2, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_9f(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(3, Reg8.A)
    private fun res_98(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(3, Reg8.B)
    private fun res_99(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(3, Reg8.C)
    private fun res_9a(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(3, Reg8.D)
    private fun res_9b(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(3, Reg8.E)
    private fun res_9c(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(3, Reg8.H)
    private fun res_9d(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(3, Reg8.L)
    private fun res_9e(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(3, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_a7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(4, Reg8.A)
    private fun res_a0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(4, Reg8.B)
    private fun res_a1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(4, Reg8.C)
    private fun res_a2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(4, Reg8.D)
    private fun res_a3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(4, Reg8.E)
    private fun res_a4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(4, Reg8.H)
    private fun res_a5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(4, Reg8.L)
    private fun res_a6(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(4, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_af(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(5, Reg8.A)
    private fun res_a8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(5, Reg8.B)
    private fun res_a9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(5, Reg8.C)
    private fun res_aa(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(5, Reg8.D)
    private fun res_ab(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(5, Reg8.E)
    private fun res_ac(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(5, Reg8.H)
    private fun res_ad(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(5, Reg8.L)
    private fun res_ae(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(5, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_b7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(6, Reg8.A)
    private fun res_b0(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(6, Reg8.B)
    private fun res_b1(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(6, Reg8.C)
    private fun res_b2(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(6, Reg8.D)
    private fun res_b3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(6, Reg8.E)
    private fun res_b4(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(6, Reg8.H)
    private fun res_b5(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(6, Reg8.L)
    private fun res_b6(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(6, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun res_bf(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(7, Reg8.A)
    private fun res_b8(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(7, Reg8.B)
    private fun res_b9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(7, Reg8.C)
    private fun res_ba(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(7, Reg8.D)
    private fun res_bb(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(7, Reg8.E)
    private fun res_bc(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(7, Reg8.H)
    private fun res_bd(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.res_b_r(7, Reg8.L)
    private fun res_be(cpu: Cpu, mmu: Mmu, vararg args: Int) =
        mmu.set(cpu.read(Reg16.HL), cpu.res_b_r(7, mmu.get(cpu.read(Reg16.HL)).toByte()).toUnsignedInt())

    private fun jp_c3(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.jp_n(args.toWord())

    private fun jp_c2(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jp_c_n(Condition.NZ, args.toWord())
    private fun jp_ca(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jp_c_n(Condition.Z, args.toWord())
    private fun jp_d2(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jp_c_n(Condition.NC, args.toWord())
    private fun jp_da(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jp_c_n(Condition.C, args.toWord())

    private fun jp_e9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.jp_n(cpu.read(Reg16.HL))

    private fun jr_18(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.jr_n(args[0].toByte())

    private fun jr_20(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jr_c_n(Condition.NZ, args[0].toByte())
    private fun jr_28(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jr_c_n(Condition.Z, args[0].toByte())
    private fun jr_30(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jr_c_n(Condition.NC, args[0].toByte())
    private fun jr_38(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.jr_c_n(Condition.C, args[0].toByte())

    private fun call_cd(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.call_n(args.toWord())

    private fun call_c4(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.call_c_n(Condition.NZ, args.toWord())
    private fun call_cc(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.call_c_n(Condition.Z, args.toWord())
    private fun call_d4(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.call_c_n(Condition.NC, args.toWord())
    private fun call_dc(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.call_c_n(Condition.C, args.toWord())

    private fun rst_c7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x00)
    private fun rst_cf(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x08)
    private fun rst_d7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x10)
    private fun rst_df(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x18)
    private fun rst_e7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x20)
    private fun rst_ef(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x28)
    private fun rst_f7(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x30)
    private fun rst_ff(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.rst_n(0x38)

    private fun ret_c9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.ret()

    private fun ret_c0(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.ret_c(Condition.NZ)
    private fun ret_c8(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.ret_c(Condition.Z)
    private fun ret_d0(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.ret_c(Condition.NC)
    private fun ret_d8(cpu: Cpu, mmu: Mmu, vararg args: Int): Boolean = cpu.ret_c(Condition.C)

    private fun reti_d9(cpu: Cpu, mmu: Mmu, vararg args: Int) = cpu.reti()

}