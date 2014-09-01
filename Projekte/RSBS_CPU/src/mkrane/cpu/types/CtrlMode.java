package mkrane.cpu.types;

public class CtrlMode {

	public static final byte[][] MODES = {
			// MODE1
			{ Types.ACC, Types.INST, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE2
			{ Types.ACC, Types.INST, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_TRUE },
			// MODE2
			{ Types.INST, Types.NONE, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE4
			{ Types.ACC, Types.NONE, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE5
			{ Types.INST, Types.PC, Types.PC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE6
			{ Types.INST, Types.PC, Types.PC, Types.UPCOND_CARRY,
					Types.UPCARRY_FALSE },
			// MODE7
			{ Types.INST, Types.PC, Types.PC, Types.UPCOND_ZERO,
					Types.UPCARRY_FALSE },
			// MODE16 TODO MISSING
			{ Types.NONE, Types.NONE, Types.NONE, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE8
			{ Types.AMEM, Types.ACC, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE9
			{ Types.ACC, Types.AMEM, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_TRUE },
			// MODE10
			{ Types.ACC, Types.NONE, Types.AMEM, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE11
			{ Types.RMEM, Types.ACC, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE12
			{ Types.ACC, Types.RMEM, Types.ACC, Types.UPCOND_ALWAYS,
					Types.UPCARRY_TRUE },
			// MODE13
			{ Types.ACC, Types.NONE, Types.RMEM, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE14
			{ Types.SMEM, Types.NONE, Types.IREG, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE },
			// MODE15
			{ Types.IREG, Types.NONE, Types.SMEM, Types.UPCOND_ALWAYS,
					Types.UPCARRY_FALSE } };
}
