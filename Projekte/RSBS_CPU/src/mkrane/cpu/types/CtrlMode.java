/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu.types;

public class CtrlMode {

	public static final byte NONE = 0;
	public static final byte ACC = 1;
	public static final byte PC = 2;
	public static final byte INST = 3;
	public static final byte AMEM = 4;
	public static final byte RMEM = 5;
	public static final byte SMEM = 6;
	public static final byte IREG = 7;
	public static final byte PMEM = 8;
	

	public static final byte UPCOND_ALWAYS = 0;
	public static final byte UPCOND_CARRY = 1;
	public static final byte UPCOND_ZERO = 2;

	public static final byte UPCARRY_FALSE = 0;
	public static final byte UPCARRY_TRUE = 1;

	public static final byte[][] MODES = {
			{ ACC, INST, ACC, UPCOND_ALWAYS, UPCARRY_FALSE },		// MODE1
			{ ACC, INST, ACC, UPCOND_ALWAYS, UPCARRY_TRUE },		// MODE2
			{ INST, NONE, ACC, UPCOND_ALWAYS, UPCARRY_FALSE },		// MODE3
			{ ACC, NONE, ACC, UPCOND_ALWAYS, UPCARRY_FALSE },		// MODE4
			{ INST, PC, PC, UPCOND_ALWAYS, UPCARRY_FALSE }, 		// MODE5
			{ INST, PC, PC, UPCOND_CARRY, UPCARRY_FALSE }, 			// MODE6
			{ INST, PC, PC, UPCOND_ZERO, UPCARRY_FALSE }, 			// MODE7
			{ ACC, PMEM, ACC, UPCOND_ALWAYS, UPCARRY_TRUE }, 		// MODE16
			{ AMEM, ACC, ACC, UPCOND_ALWAYS, UPCARRY_FALSE }, 		// MODE8
			{ ACC, AMEM, ACC, UPCOND_ALWAYS, UPCARRY_TRUE }, 		// MODE9
			{ ACC, NONE, AMEM, UPCOND_ALWAYS, UPCARRY_FALSE }, 		// MODE10
			{ RMEM, ACC, ACC, UPCOND_ALWAYS, UPCARRY_FALSE }, 		// MODE11
			{ ACC, RMEM, ACC, UPCOND_ALWAYS, UPCARRY_TRUE }, 		// MODE12
			{ ACC, NONE, RMEM, UPCOND_ALWAYS, UPCARRY_FALSE }, 		// MODE13
			{ SMEM, NONE, IREG, UPCOND_ALWAYS, UPCARRY_FALSE }, 	// MODE14
			{ IREG, NONE, SMEM, UPCOND_ALWAYS, UPCARRY_FALSE } }; 	// MODE15
}
