/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package reg;

import dataTypes.Types;

public class Register {

	// Carry Zero flag
	private boolean[] status;

	// Akkumulator
	private short acc;

	// Programcounter
	private byte pc;

	// Stackpointer
	private byte sp;

	// OpCode
	private short inst;

	// READ_MEM value
	private short mdr;

	// READ_MEM adr
	private byte mar;

	// Phase FETCH --> DECODE --> READ_MEM --> OPERATE --> WRITE_MEM --> FETCH
	private byte phase;

	public Register() {
		reset();
	}

	public void saveStatus(boolean carry, boolean zero, short acc, byte pc,
			byte sp, short inst, short mdr, byte mar, byte phase) {
		status = new boolean[] { carry, zero };
		this.acc = acc;
		this.pc = pc;
		this.sp = sp;
		this.inst = inst;
		this.mdr = mdr;
		this.mar = mar;
		this.phase = phase;
	}

	public void reset() {
		status = new boolean[] { false, true };
		acc = 0;
		pc = 0;
		sp = 0;
		inst = 0;
		mdr = 0;
		mar = 0;
		phase = Types.FETCH;
	}
}
