package com.arman.kotboy.cpu

import com.arman.kotboy.cpu.util.hexString

class Instr(val opCode: OpCode, vararg val args: Int) : Iterable<Instr> {

//    constructor(opCode: OpCode, vararg args: Int) : this(
//        opCode,
//        args = {
//            assert(opCode.args.size == args.size)
//            val res: Array<Operand> = emptyArray()
//            var i = 0
//            while (i < args.size) {
//                val arg = args[i]
//                res[i] = when (opCode.args[i]) {
//                    Operand.Type.d8, Operand.Type.a8 -> Immediate8(arg)
//                    Operand.Type.d16, Operand.Type.a16 -> Immediate16(args[++i], arg)
//                    else -> Signed8(arg) // Operand.Type.r8
//                }
//                i++
//            }
//            res
//        }
//    )
//
//    private fun convertArgs(args: IntArray): Array<Operand> {
//        assert(opCode.args.size == args.size)
//        val res: Array<Operand> = emptyArray()
//        var i = 0
//        while (i < args.size) {
//            val arg = args[i]
//            res[i] = when (opCode.args[i]) {
//                Operand.Type.d8, Operand.Type.a8 -> Immediate8(arg)
//                Operand.Type.d16, Operand.Type.a16 -> Immediate16(args[++i], arg)
//                else -> Signed8(arg) // Operand.Type.r8
//            }
//            i++
//        }
//        return res
//    }

    var comment: String? = null

    fun argsSize(): Int = this.opCode.argsSize()

    override fun iterator(): Iterator<Instr> = setOf(this).iterator()

    override fun toString(): String {
        return prettryPrint()
    }

    fun prettryPrint(): String {
        var res = "$opCode\t"
        for (arg in args) res += "${arg.hexString()} "
        this.comment?.let { res += "\t\t// $comment" }
        return res
    }

    fun execute(cpu: Cpu): Int {
        when (opCode) {
            // opcodes
            OpCode.NOP_00 -> cpu.nop_00(*args)
            OpCode.LD_01 -> cpu.ld_01(*args)
            OpCode.LD_02 -> cpu.ld_02(*args)
            OpCode.INC_03 -> cpu.inc_03(*args)
            OpCode.INC_04 -> cpu.inc_04(*args)
            OpCode.DEC_05 -> cpu.dec_05(*args)
            OpCode.LD_06 -> cpu.ld_06(*args)
            OpCode.RLCA_07 -> cpu.rlca_07(*args)
            OpCode.LD_08 -> cpu.ld_08(*args)
            OpCode.ADD_09 -> cpu.add_09(*args)
            OpCode.LD_0A -> cpu.ld_0a(*args)
            OpCode.DEC_0B -> cpu.dec_0b(*args)
            OpCode.INC_0C -> cpu.inc_0c(*args)
            OpCode.DEC_0D -> cpu.dec_0d(*args)
            OpCode.LD_0E -> cpu.ld_0e(*args)
            OpCode.RRCA_0F -> cpu.rrca_0f(*args)

            OpCode.STOP_10 -> cpu.stop_10(*args)
            OpCode.LD_11 -> cpu.ld_11(*args)
            OpCode.LD_12 -> cpu.ld_12(*args)
            OpCode.INC_13 -> cpu.inc_13(*args)
            OpCode.INC_14 -> cpu.inc_14(*args)
            OpCode.DEC_15 -> cpu.dec_15(*args)
            OpCode.LD_16 -> cpu.ld_16(*args)
            OpCode.RLA_17 -> cpu.rla_17(*args)
            OpCode.JR_18 -> cpu.jr_18(*args)
            OpCode.ADD_19 -> cpu.add_19(*args)
            OpCode.LD_1A -> cpu.ld_1a(*args)
            OpCode.DEC_1B -> cpu.dec_1b(*args)
            OpCode.INC_1C -> cpu.inc_1c(*args)
            OpCode.DEC_1D -> cpu.dec_1d(*args)
            OpCode.LD_1E -> cpu.ld_1e(*args)
            OpCode.RRA_1F -> cpu.rra_1f(*args)

            OpCode.JR_20 -> if (!cpu.jr_20(*args)) {
                return OpCode.JR_20.cyclesNotTaken
            }
            OpCode.LD_21 -> cpu.ld_21(*args)
            OpCode.LD_22 -> cpu.ld_22(*args)
            OpCode.INC_23 -> cpu.inc_23(*args)
            OpCode.INC_24 -> cpu.inc_24(*args)
            OpCode.DEC_25 -> cpu.dec_25(*args)
            OpCode.LD_26 -> cpu.ld_26(*args)
            OpCode.DAA_27 -> cpu.daa_27(*args)
            OpCode.JR_28 -> if (!cpu.jr_28(*args)) {
                return OpCode.JR_28.cyclesNotTaken
            }
            OpCode.ADD_29 -> cpu.add_29(*args)
            OpCode.LD_2A -> cpu.ld_2a(*args)
            OpCode.DEC_2B -> cpu.dec_2b(*args)
            OpCode.INC_2C -> cpu.inc_2c(*args)
            OpCode.DEC_2D -> cpu.dec_2d(*args)
            OpCode.LD_2E -> cpu.ld_2e(*args)
            OpCode.CPL_2F -> cpu.cpl_2f(*args)

            OpCode.JR_30 -> if (!cpu.jr_30(*args)) {
                return OpCode.JR_30.cyclesNotTaken
            }
            OpCode.LD_31 -> cpu.ld_31(*args)
            OpCode.LD_32 -> cpu.ld_32(*args)
            OpCode.INC_33 -> cpu.inc_33(*args)
            OpCode.INC_34 -> cpu.inc_34(*args)
            OpCode.DEC_35 -> cpu.dec_35(*args)
            OpCode.LD_36 -> cpu.ld_36(*args)
            OpCode.SCF_37 -> cpu.scf_37(*args)
            OpCode.JR_38 -> if (!cpu.jr_38(*args)) {
                return OpCode.JR_38.cyclesNotTaken
            }
            OpCode.ADD_39 -> cpu.add_39(*args)
            OpCode.LD_3A -> cpu.ld_3a(*args)
            OpCode.DEC_3B -> cpu.dec_3b(*args)
            OpCode.INC_3C -> cpu.inc_3c(*args)
            OpCode.DEC_3D -> cpu.dec_3d(*args)
            OpCode.LD_3E -> cpu.ld_3e(*args)
            OpCode.CCF_3F -> cpu.ccf_3f(*args)

            OpCode.LD_40 -> cpu.ld_40(*args)
            OpCode.LD_41 -> cpu.ld_41(*args)
            OpCode.LD_42 -> cpu.ld_42(*args)
            OpCode.LD_43 -> cpu.ld_43(*args)
            OpCode.LD_44 -> cpu.ld_44(*args)
            OpCode.LD_45 -> cpu.ld_45(*args)
            OpCode.LD_46 -> cpu.ld_46(*args)
            OpCode.LD_47 -> cpu.ld_47(*args)
            OpCode.LD_48 -> cpu.ld_48(*args)
            OpCode.LD_49 -> cpu.ld_49(*args)
            OpCode.LD_4A -> cpu.ld_4a(*args)
            OpCode.LD_4B -> cpu.ld_4b(*args)
            OpCode.LD_4C -> cpu.ld_4c(*args)
            OpCode.LD_4D -> cpu.ld_4d(*args)
            OpCode.LD_4E -> cpu.ld_4e(*args)
            OpCode.LD_4F -> cpu.ld_4f(*args)

            OpCode.LD_50 -> cpu.ld_50(*args)
            OpCode.LD_51 -> cpu.ld_51(*args)
            OpCode.LD_52 -> cpu.ld_52(*args)
            OpCode.LD_53 -> cpu.ld_53(*args)
            OpCode.LD_54 -> cpu.ld_54(*args)
            OpCode.LD_55 -> cpu.ld_55(*args)
            OpCode.LD_56 -> cpu.ld_56(*args)
            OpCode.LD_57 -> cpu.ld_57(*args)
            OpCode.LD_58 -> cpu.ld_58(*args)
            OpCode.LD_59 -> cpu.ld_59(*args)
            OpCode.LD_5A -> cpu.ld_5a(*args)
            OpCode.LD_5B -> cpu.ld_5b(*args)
            OpCode.LD_5C -> cpu.ld_5c(*args)
            OpCode.LD_5D -> cpu.ld_5d(*args)
            OpCode.LD_5E -> cpu.ld_5e(*args)
            OpCode.LD_5F -> cpu.ld_5f(*args)

            OpCode.LD_60 -> cpu.ld_60(*args)
            OpCode.LD_61 -> cpu.ld_61(*args)
            OpCode.LD_62 -> cpu.ld_62(*args)
            OpCode.LD_63 -> cpu.ld_63(*args)
            OpCode.LD_64 -> cpu.ld_64(*args)
            OpCode.LD_65 -> cpu.ld_65(*args)
            OpCode.LD_66 -> cpu.ld_66(*args)
            OpCode.LD_67 -> cpu.ld_67(*args)
            OpCode.LD_68 -> cpu.ld_68(*args)
            OpCode.LD_69 -> cpu.ld_69(*args)
            OpCode.LD_6A -> cpu.ld_6a(*args)
            OpCode.LD_6B -> cpu.ld_6b(*args)
            OpCode.LD_6C -> cpu.ld_6c(*args)
            OpCode.LD_6D -> cpu.ld_6d(*args)
            OpCode.LD_6E -> cpu.ld_6e(*args)
            OpCode.LD_6F -> cpu.ld_6f(*args)

            OpCode.LD_70 -> cpu.ld_70(*args)
            OpCode.LD_71 -> cpu.ld_71(*args)
            OpCode.LD_72 -> cpu.ld_72(*args)
            OpCode.LD_73 -> cpu.ld_73(*args)
            OpCode.LD_74 -> cpu.ld_74(*args)
            OpCode.LD_75 -> cpu.ld_75(*args)
            OpCode.HALT_76 -> cpu.halt_76(*args)
            OpCode.LD_77 -> cpu.ld_77(*args)
            OpCode.LD_78 -> cpu.ld_78(*args)
            OpCode.LD_79 -> cpu.ld_79(*args)
            OpCode.LD_7A -> cpu.ld_7a(*args)
            OpCode.LD_7B -> cpu.ld_7b(*args)
            OpCode.LD_7C -> cpu.ld_7c(*args)
            OpCode.LD_7D -> cpu.ld_7d(*args)
            OpCode.LD_7E -> cpu.ld_7e(*args)
            OpCode.LD_7F -> cpu.ld_7f(*args)

            OpCode.ADD_80 -> cpu.add_80(*args)
            OpCode.ADD_81 -> cpu.add_81(*args)
            OpCode.ADD_82 -> cpu.add_82(*args)
            OpCode.ADD_83 -> cpu.add_83(*args)
            OpCode.ADD_84 -> cpu.add_84(*args)
            OpCode.ADD_85 -> cpu.add_85(*args)
            OpCode.ADD_86 -> cpu.add_86(*args)
            OpCode.ADD_87 -> cpu.add_87(*args)
            OpCode.ADC_88 -> cpu.adc_88(*args)
            OpCode.ADC_89 -> cpu.adc_89(*args)
            OpCode.ADC_8A -> cpu.adc_8a(*args)
            OpCode.ADC_8B -> cpu.adc_8b(*args)
            OpCode.ADC_8C -> cpu.adc_8c(*args)
            OpCode.ADC_8D -> cpu.adc_8d(*args)
            OpCode.ADC_8E -> cpu.adc_8e(*args)
            OpCode.ADC_8F -> cpu.adc_8f(*args)

            OpCode.SUB_90 -> cpu.sub_90(*args)
            OpCode.SUB_91 -> cpu.sub_91(*args)
            OpCode.SUB_92 -> cpu.sub_92(*args)
            OpCode.SUB_93 -> cpu.sub_93(*args)
            OpCode.SUB_94 -> cpu.sub_94(*args)
            OpCode.SUB_95 -> cpu.sub_95(*args)
            OpCode.SUB_96 -> cpu.sub_96(*args)
            OpCode.SUB_97 -> cpu.sub_97(*args)
            OpCode.SBC_98 -> cpu.sbc_98(*args)
            OpCode.SBC_99 -> cpu.sbc_99(*args)
            OpCode.SBC_9A -> cpu.sbc_9a(*args)
            OpCode.SBC_9B -> cpu.sbc_9b(*args)
            OpCode.SBC_9C -> cpu.sbc_9c(*args)
            OpCode.SBC_9D -> cpu.sbc_9d(*args)
            OpCode.SBC_9E -> cpu.sbc_9e(*args)
            OpCode.SBC_9F -> cpu.sbc_9f(*args)

            OpCode.AND_A0 -> cpu.and_a0(*args)
            OpCode.AND_A1 -> cpu.and_a1(*args)
            OpCode.AND_A2 -> cpu.and_a2(*args)
            OpCode.AND_A3 -> cpu.and_a3(*args)
            OpCode.AND_A4 -> cpu.and_a4(*args)
            OpCode.AND_A5 -> cpu.and_a5(*args)
            OpCode.AND_A6 -> cpu.and_a6(*args)
            OpCode.AND_A7 -> cpu.and_a7(*args)
            OpCode.XOR_A8 -> cpu.xor_a8(*args)
            OpCode.XOR_A9 -> cpu.xor_a9(*args)
            OpCode.XOR_AA -> cpu.xor_aa(*args)
            OpCode.XOR_AB -> cpu.xor_ab(*args)
            OpCode.XOR_AC -> cpu.xor_ac(*args)
            OpCode.XOR_AD -> cpu.xor_ad(*args)
            OpCode.XOR_AE -> cpu.xor_ae(*args)
            OpCode.XOR_AF -> cpu.xor_af(*args)

            OpCode.OR_B0 -> cpu.or_b0(*args)
            OpCode.OR_B1 -> cpu.or_b1(*args)
            OpCode.OR_B2 -> cpu.or_b2(*args)
            OpCode.OR_B3 -> cpu.or_b3(*args)
            OpCode.OR_B4 -> cpu.or_b4(*args)
            OpCode.OR_B5 -> cpu.or_b5(*args)
            OpCode.OR_B6 -> cpu.or_b6(*args)
            OpCode.OR_B7 -> cpu.or_b7(*args)
            OpCode.CP_B8 -> cpu.cp_b8(*args)
            OpCode.CP_B9 -> cpu.cp_b9(*args)
            OpCode.CP_BA -> cpu.cp_ba(*args)
            OpCode.CP_BB -> cpu.cp_bb(*args)
            OpCode.CP_BC -> cpu.cp_bc(*args)
            OpCode.CP_BD -> cpu.cp_bd(*args)
            OpCode.CP_BE -> cpu.cp_be(*args)
            OpCode.CP_BF -> cpu.cp_bf(*args)

            OpCode.RET_C0 -> if (!cpu.ret_c0(*args)) {
                return OpCode.RET_C0.cyclesNotTaken
            }
            OpCode.POP_C1 -> cpu.pop_c1(*args)
            OpCode.JP_C2 -> if (!cpu.jp_c2(*args)) {
                return OpCode.JP_C2.cyclesNotTaken
            }
            OpCode.JP_C3 -> cpu.jp_c3(*args)
            OpCode.CALL_C4 -> if (!cpu.call_c4(*args)) {
                return OpCode.CALL_C4.cyclesNotTaken
            }
            OpCode.PUSH_C5 -> cpu.push_c5(*args)
            OpCode.ADD_C6 -> cpu.add_c6(*args)
            OpCode.RST_C7 -> cpu.rst_c7(*args)
            OpCode.RET_C8 -> if (!cpu.ret_c8(*args)) {
                return OpCode.RET_C8.cyclesNotTaken
            }
            OpCode.RET_C9 -> cpu.ret_c9(*args)
            OpCode.JP_CA -> if (!cpu.jp_ca(*args)) {
                return OpCode.JP_CA.cyclesNotTaken
            }
            OpCode.PREFIX_CB -> cpu.prefix_cb(*args)
            OpCode.CALL_CC -> if (!cpu.call_cc(*args)) {
                return OpCode.CALL_CC.cyclesNotTaken
            }
            OpCode.CALL_CD -> cpu.call_cd(*args)
            OpCode.ADC_CE -> cpu.adc_ce(*args)
            OpCode.RST_CF -> cpu.rst_cf(*args)

            OpCode.RET_D0 -> if (!cpu.ret_d0(*args)) {
                return OpCode.RET_D0.cyclesNotTaken
            }
            OpCode.POP_D1 -> cpu.pop_d1(*args)
            OpCode.JP_D2 -> if (!cpu.jp_d2(*args)) {
                return OpCode.JP_D2.cyclesNotTaken
            }
            OpCode.CALL_D4 -> if (!cpu.call_d4(*args)) {
                return OpCode.CALL_D4.cyclesNotTaken
            }
            OpCode.PUSH_D5 -> cpu.push_d5(*args)
            OpCode.SUB_D6 -> cpu.sub_d6(*args)
            OpCode.RST_D7 -> cpu.rst_d7(*args)
            OpCode.RET_D8 -> if (!cpu.ret_d8(*args)) {
                return OpCode.RET_D8.cyclesNotTaken
            }
            OpCode.RETI_D9 -> cpu.reti_d9(*args)
            OpCode.JP_DA -> if (!cpu.jp_da(*args)) {
                return OpCode.JP_DA.cyclesNotTaken
            }
            OpCode.CALL_DC -> if (!cpu.call_dc(*args)) {
                return OpCode.CALL_DC.cyclesNotTaken
            }
            OpCode.SBC_DE -> cpu.sbc_de(*args)
            OpCode.RST_DF -> cpu.rst_df(*args)

            OpCode.LDH_E0 -> cpu.ldh_e0(*args)
            OpCode.POP_E1 -> cpu.pop_e1(*args)
            OpCode.LD_E2 -> cpu.ld_e2(*args)
            OpCode.PUSH_E5 -> cpu.push_e5(*args)
            OpCode.AND_E6 -> cpu.and_e6(*args)
            OpCode.RST_E7 -> cpu.rst_e7(*args)
            OpCode.ADD_E8 -> cpu.add_e8(*args)
            OpCode.JP_E9 -> cpu.jp_e9(*args)
            OpCode.LD_EA -> cpu.ld_ea(*args)
            OpCode.XOR_EE -> cpu.xor_ee(*args)
            OpCode.RST_EF -> cpu.rst_ef(*args)

            OpCode.LDH_F0 -> cpu.ldh_f0(*args)
            OpCode.POP_F1 -> cpu.pop_f1(*args)
            OpCode.LD_F2 -> cpu.ld_f2(*args)
            OpCode.DI_F3 -> cpu.di_f3(*args)
            OpCode.PUSH_F5 -> cpu.push_f5(*args)
            OpCode.OR_F6 -> cpu.or_f6(*args)
            OpCode.RST_F7 -> cpu.rst_f7(*args)
            OpCode.LD_F8 -> cpu.ld_f8(*args)
            OpCode.LD_F9 -> cpu.ld_f9(*args)
            OpCode.LD_FA -> cpu.ld_fa(*args)
            OpCode.EI_FB -> cpu.ei_fb(*args)
            OpCode.CP_FE -> cpu.cp_fe(*args)
            OpCode.RST_FF -> cpu.rst_ff(*args)

            // external opcodes
            OpCode.RLC_00 -> cpu.rlc_00(*args)
            OpCode.RLC_01 -> cpu.rlc_01(*args)
            OpCode.RLC_02 -> cpu.rlc_02(*args)
            OpCode.RLC_03 -> cpu.rlc_03(*args)
            OpCode.RLC_04 -> cpu.rlc_04(*args)
            OpCode.RLC_05 -> cpu.rlc_05(*args)
            OpCode.RLC_06 -> cpu.rlc_06(*args)
            OpCode.RLC_07 -> cpu.rlc_07(*args)
            OpCode.RRC_08 -> cpu.rrc_08(*args)
            OpCode.RRC_09 -> cpu.rrc_09(*args)
            OpCode.RRC_0A -> cpu.rrc_0a(*args)
            OpCode.RRC_0B -> cpu.rrc_0b(*args)
            OpCode.RRC_0C -> cpu.rrc_0c(*args)
            OpCode.RRC_0D -> cpu.rrc_0d(*args)
            OpCode.RRC_0E -> cpu.rrc_0e(*args)
            OpCode.RRC_0F -> cpu.rrc_0f(*args)

            OpCode.RL_10 -> cpu.rl_10(*args)
            OpCode.RL_11 -> cpu.rl_11(*args)
            OpCode.RL_12 -> cpu.rl_12(*args)
            OpCode.RL_13 -> cpu.rl_13(*args)
            OpCode.RL_14 -> cpu.rl_14(*args)
            OpCode.RL_15 -> cpu.rl_15(*args)
            OpCode.RL_16 -> cpu.rl_16(*args)
            OpCode.RL_17 -> cpu.rl_17(*args)
            OpCode.RR_18 -> cpu.rr_18(*args)
            OpCode.RR_19 -> cpu.rr_19(*args)
            OpCode.RR_1A -> cpu.rr_1a(*args)
            OpCode.RR_1B -> cpu.rr_1b(*args)
            OpCode.RR_1C -> cpu.rr_1c(*args)
            OpCode.RR_1D -> cpu.rr_1d(*args)
            OpCode.RR_1E -> cpu.rr_1e(*args)
            OpCode.RR_1F -> cpu.rr_1f(*args)

            OpCode.SLA_20 -> cpu.sla_20(*args)
            OpCode.SLA_21 -> cpu.sla_21(*args)
            OpCode.SLA_22 -> cpu.sla_22(*args)
            OpCode.SLA_23 -> cpu.sla_23(*args)
            OpCode.SLA_24 -> cpu.sla_24(*args)
            OpCode.SLA_25 -> cpu.sla_25(*args)
            OpCode.SLA_26 -> cpu.sla_26(*args)
            OpCode.SLA_27 -> cpu.sla_27(*args)
            OpCode.SRA_28 -> cpu.sra_28(*args)
            OpCode.SRA_29 -> cpu.sra_29(*args)
            OpCode.SRA_2A -> cpu.sra_2a(*args)
            OpCode.SRA_2B -> cpu.sra_2b(*args)
            OpCode.SRA_2C -> cpu.sra_2c(*args)
            OpCode.SRA_2D -> cpu.sra_2d(*args)
            OpCode.SRA_2E -> cpu.sra_2e(*args)
            OpCode.SRA_2F -> cpu.sra_2f(*args)

            OpCode.SWAP_30 -> cpu.swap_30(*args)
            OpCode.SWAP_31 -> cpu.swap_31(*args)
            OpCode.SWAP_32 -> cpu.swap_32(*args)
            OpCode.SWAP_33 -> cpu.swap_33(*args)
            OpCode.SWAP_34 -> cpu.swap_34(*args)
            OpCode.SWAP_35 -> cpu.swap_35(*args)
            OpCode.SWAP_36 -> cpu.swap_36(*args)
            OpCode.SWAP_37 -> cpu.swap_37(*args)
            OpCode.SRL_38 -> cpu.srl_38(*args)
            OpCode.SRL_39 -> cpu.srl_39(*args)
            OpCode.SRL_3A -> cpu.srl_3a(*args)
            OpCode.SRL_3B -> cpu.srl_3b(*args)
            OpCode.SRL_3C -> cpu.srl_3c(*args)
            OpCode.SRL_3D -> cpu.srl_3d(*args)
            OpCode.SRL_3E -> cpu.srl_3e(*args)
            OpCode.SRL_3F -> cpu.srl_3f(*args)

            OpCode.BIT_40 -> cpu.bit_40(*args)
            OpCode.BIT_41 -> cpu.bit_41(*args)
            OpCode.BIT_42 -> cpu.bit_42(*args)
            OpCode.BIT_43 -> cpu.bit_43(*args)
            OpCode.BIT_44 -> cpu.bit_44(*args)
            OpCode.BIT_45 -> cpu.bit_45(*args)
            OpCode.BIT_46 -> cpu.bit_46(*args)
            OpCode.BIT_47 -> cpu.bit_47(*args)
            OpCode.BIT_48 -> cpu.bit_48(*args)
            OpCode.BIT_49 -> cpu.bit_49(*args)
            OpCode.BIT_4A -> cpu.bit_4a(*args)
            OpCode.BIT_4B -> cpu.bit_4b(*args)
            OpCode.BIT_4C -> cpu.bit_4c(*args)
            OpCode.BIT_4D -> cpu.bit_4d(*args)
            OpCode.BIT_4E -> cpu.bit_4e(*args)
            OpCode.BIT_4F -> cpu.bit_4f(*args)

            OpCode.BIT_50 -> cpu.bit_50(*args)
            OpCode.BIT_51 -> cpu.bit_51(*args)
            OpCode.BIT_52 -> cpu.bit_52(*args)
            OpCode.BIT_53 -> cpu.bit_53(*args)
            OpCode.BIT_54 -> cpu.bit_54(*args)
            OpCode.BIT_55 -> cpu.bit_55(*args)
            OpCode.BIT_56 -> cpu.bit_56(*args)
            OpCode.BIT_57 -> cpu.bit_57(*args)
            OpCode.BIT_58 -> cpu.bit_58(*args)
            OpCode.BIT_59 -> cpu.bit_59(*args)
            OpCode.BIT_5A -> cpu.bit_5a(*args)
            OpCode.BIT_5B -> cpu.bit_5b(*args)
            OpCode.BIT_5C -> cpu.bit_5c(*args)
            OpCode.BIT_5D -> cpu.bit_5d(*args)
            OpCode.BIT_5E -> cpu.bit_5e(*args)
            OpCode.BIT_5F -> cpu.bit_5f(*args)

            OpCode.BIT_60 -> cpu.bit_60(*args)
            OpCode.BIT_61 -> cpu.bit_61(*args)
            OpCode.BIT_62 -> cpu.bit_62(*args)
            OpCode.BIT_63 -> cpu.bit_63(*args)
            OpCode.BIT_64 -> cpu.bit_64(*args)
            OpCode.BIT_65 -> cpu.bit_65(*args)
            OpCode.BIT_66 -> cpu.bit_66(*args)
            OpCode.BIT_67 -> cpu.bit_67(*args)
            OpCode.BIT_68 -> cpu.bit_68(*args)
            OpCode.BIT_69 -> cpu.bit_69(*args)
            OpCode.BIT_6A -> cpu.bit_6a(*args)
            OpCode.BIT_6B -> cpu.bit_6b(*args)
            OpCode.BIT_6C -> cpu.bit_6c(*args)
            OpCode.BIT_6D -> cpu.bit_6d(*args)
            OpCode.BIT_6E -> cpu.bit_6e(*args)
            OpCode.BIT_6F -> cpu.bit_6f(*args)

            OpCode.BIT_70 -> cpu.bit_70(*args)
            OpCode.BIT_71 -> cpu.bit_71(*args)
            OpCode.BIT_72 -> cpu.bit_72(*args)
            OpCode.BIT_73 -> cpu.bit_73(*args)
            OpCode.BIT_74 -> cpu.bit_74(*args)
            OpCode.BIT_75 -> cpu.bit_75(*args)
            OpCode.BIT_76 -> cpu.bit_76(*args)
            OpCode.BIT_77 -> cpu.bit_77(*args)
            OpCode.BIT_78 -> cpu.bit_78(*args)
            OpCode.BIT_79 -> cpu.bit_79(*args)
            OpCode.BIT_7A -> cpu.bit_7a(*args)
            OpCode.BIT_7B -> cpu.bit_7b(*args)
            OpCode.BIT_7C -> cpu.bit_7c(*args)
            OpCode.BIT_7D -> cpu.bit_7d(*args)
            OpCode.BIT_7E -> cpu.bit_7e(*args)
            OpCode.BIT_7F -> cpu.bit_7f(*args)

            OpCode.RES_80 -> cpu.res_80(*args)
            OpCode.RES_81 -> cpu.res_81(*args)
            OpCode.RES_82 -> cpu.res_82(*args)
            OpCode.RES_83 -> cpu.res_83(*args)
            OpCode.RES_84 -> cpu.res_84(*args)
            OpCode.RES_85 -> cpu.res_85(*args)
            OpCode.RES_86 -> cpu.res_86(*args)
            OpCode.RES_87 -> cpu.res_87(*args)
            OpCode.RES_88 -> cpu.res_88(*args)
            OpCode.RES_89 -> cpu.res_89(*args)
            OpCode.RES_8A -> cpu.res_8a(*args)
            OpCode.RES_8B -> cpu.res_8b(*args)
            OpCode.RES_8C -> cpu.res_8c(*args)
            OpCode.RES_8D -> cpu.res_8d(*args)
            OpCode.RES_8E -> cpu.res_8e(*args)
            OpCode.RES_8F -> cpu.res_8f(*args)

            OpCode.RES_90 -> cpu.res_90(*args)
            OpCode.RES_91 -> cpu.res_91(*args)
            OpCode.RES_92 -> cpu.res_92(*args)
            OpCode.RES_93 -> cpu.res_93(*args)
            OpCode.RES_94 -> cpu.res_94(*args)
            OpCode.RES_95 -> cpu.res_95(*args)
            OpCode.RES_96 -> cpu.res_96(*args)
            OpCode.RES_97 -> cpu.res_97(*args)
            OpCode.RES_98 -> cpu.res_98(*args)
            OpCode.RES_99 -> cpu.res_99(*args)
            OpCode.RES_9A -> cpu.res_9a(*args)
            OpCode.RES_9B -> cpu.res_9b(*args)
            OpCode.RES_9C -> cpu.res_9c(*args)
            OpCode.RES_9D -> cpu.res_9d(*args)
            OpCode.RES_9E -> cpu.res_9e(*args)
            OpCode.RES_9F -> cpu.res_9f(*args)

            OpCode.RES_A0 -> cpu.res_a0(*args)
            OpCode.RES_A1 -> cpu.res_a1(*args)
            OpCode.RES_A2 -> cpu.res_a2(*args)
            OpCode.RES_A3 -> cpu.res_a3(*args)
            OpCode.RES_A4 -> cpu.res_a4(*args)
            OpCode.RES_A5 -> cpu.res_a5(*args)
            OpCode.RES_A6 -> cpu.res_a6(*args)
            OpCode.RES_A7 -> cpu.res_a7(*args)
            OpCode.RES_A8 -> cpu.res_a8(*args)
            OpCode.RES_A9 -> cpu.res_a9(*args)
            OpCode.RES_AA -> cpu.res_aa(*args)
            OpCode.RES_AB -> cpu.res_ab(*args)
            OpCode.RES_AC -> cpu.res_ac(*args)
            OpCode.RES_AD -> cpu.res_ad(*args)
            OpCode.RES_AE -> cpu.res_ae(*args)
            OpCode.RES_AF -> cpu.res_af(*args)

            OpCode.RES_B0 -> cpu.res_b0(*args)
            OpCode.RES_B1 -> cpu.res_b1(*args)
            OpCode.RES_B2 -> cpu.res_b2(*args)
            OpCode.RES_B3 -> cpu.res_b3(*args)
            OpCode.RES_B4 -> cpu.res_b4(*args)
            OpCode.RES_B5 -> cpu.res_b5(*args)
            OpCode.RES_B6 -> cpu.res_b6(*args)
            OpCode.RES_B7 -> cpu.res_b7(*args)
            OpCode.RES_B8 -> cpu.res_b8(*args)
            OpCode.RES_B9 -> cpu.res_b9(*args)
            OpCode.RES_BA -> cpu.res_ba(*args)
            OpCode.RES_BB -> cpu.res_bb(*args)
            OpCode.RES_BC -> cpu.res_bc(*args)
            OpCode.RES_BD -> cpu.res_bd(*args)
            OpCode.RES_BE -> cpu.res_be(*args)
            OpCode.RES_BF -> cpu.res_bf(*args)

            OpCode.SET_C0 -> cpu.set_c0(*args)
            OpCode.SET_C1 -> cpu.set_c1(*args)
            OpCode.SET_C2 -> cpu.set_c2(*args)
            OpCode.SET_C3 -> cpu.set_c3(*args)
            OpCode.SET_C4 -> cpu.set_c4(*args)
            OpCode.SET_C5 -> cpu.set_c5(*args)
            OpCode.SET_C6 -> cpu.set_c6(*args)
            OpCode.SET_C7 -> cpu.set_c7(*args)
            OpCode.SET_C8 -> cpu.set_c8(*args)
            OpCode.SET_C9 -> cpu.set_c9(*args)
            OpCode.SET_CA -> cpu.set_ca(*args)
            OpCode.SET_CB -> cpu.set_cb(*args)
            OpCode.SET_CC -> cpu.set_cc(*args)
            OpCode.SET_CD -> cpu.set_cd(*args)
            OpCode.SET_CE -> cpu.set_ce(*args)
            OpCode.SET_CF -> cpu.set_cf(*args)

            OpCode.SET_D0 -> cpu.set_d0(*args)
            OpCode.SET_D1 -> cpu.set_d1(*args)
            OpCode.SET_D2 -> cpu.set_d2(*args)
            OpCode.SET_D3 -> cpu.set_d3(*args)
            OpCode.SET_D4 -> cpu.set_d4(*args)
            OpCode.SET_D5 -> cpu.set_d5(*args)
            OpCode.SET_D6 -> cpu.set_d6(*args)
            OpCode.SET_D7 -> cpu.set_d7(*args)
            OpCode.SET_D8 -> cpu.set_d8(*args)
            OpCode.SET_D9 -> cpu.set_d9(*args)
            OpCode.SET_DA -> cpu.set_da(*args)
            OpCode.SET_DB -> cpu.set_db(*args)
            OpCode.SET_DC -> cpu.set_dc(*args)
            OpCode.SET_DD -> cpu.set_dd(*args)
            OpCode.SET_DE -> cpu.set_de(*args)
            OpCode.SET_DF -> cpu.set_df(*args)

            OpCode.SET_E0 -> cpu.set_e0(*args)
            OpCode.SET_E1 -> cpu.set_e1(*args)
            OpCode.SET_E2 -> cpu.set_e2(*args)
            OpCode.SET_E3 -> cpu.set_e3(*args)
            OpCode.SET_E4 -> cpu.set_e4(*args)
            OpCode.SET_E5 -> cpu.set_e5(*args)
            OpCode.SET_E6 -> cpu.set_e6(*args)
            OpCode.SET_E7 -> cpu.set_e7(*args)
            OpCode.SET_E8 -> cpu.set_e8(*args)
            OpCode.SET_E9 -> cpu.set_e9(*args)
            OpCode.SET_EA -> cpu.set_ea(*args)
            OpCode.SET_EB -> cpu.set_eb(*args)
            OpCode.SET_EC -> cpu.set_ec(*args)
            OpCode.SET_ED -> cpu.set_ed(*args)
            OpCode.SET_EE -> cpu.set_ee(*args)
            OpCode.SET_EF -> cpu.set_ef(*args)

            OpCode.SET_F0 -> cpu.set_f0(*args)
            OpCode.SET_F1 -> cpu.set_f1(*args)
            OpCode.SET_F2 -> cpu.set_f2(*args)
            OpCode.SET_F3 -> cpu.set_f3(*args)
            OpCode.SET_F4 -> cpu.set_f4(*args)
            OpCode.SET_F5 -> cpu.set_f5(*args)
            OpCode.SET_F6 -> cpu.set_f6(*args)
            OpCode.SET_F7 -> cpu.set_f7(*args)
            OpCode.SET_F8 -> cpu.set_f8(*args)
            OpCode.SET_F9 -> cpu.set_f9(*args)
            OpCode.SET_FA -> cpu.set_fa(*args)
            OpCode.SET_FB -> cpu.set_fb(*args)
            OpCode.SET_FC -> cpu.set_fc(*args)
            OpCode.SET_FD -> cpu.set_fd(*args)
            OpCode.SET_FE -> cpu.set_fe(*args)
            OpCode.SET_FF -> cpu.set_ff(*args)
        }
        return opCode.cycles
    }

}