/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu;

import mkrane.cpu.types.Types;

public class Register {

	// Carry Zero flag
	private boolean[] status;

	public boolean getCarry() {
		return status[0];
	}

	public void setCarry(boolean carry) {
		this.status[0] = carry;

	}

	public boolean getZero() {
		return status[1];
	}

	public void setZero(boolean zero) {
		this.status[1] = zero;
	}

	// Akkumulator
	private short acc;

	public short getAcc() {
		return acc;
	}

	public void setAcc(short acc) {
		this.acc = acc;
	}

	// Programcounter
	private byte pc;

	public byte getPC() {
		return pc;
	}

	public void setPC(byte pc) {
		this.pc = pc;

	}

	public void incPC() {
		pc++;
	}

	// Stackpointer
	private byte sp;

	public int getSP() {
		return sp;
	}

	public void incSP() {
		sp++;
	}

	public void decSP() {
		if (sp >= 0)
			sp--;
	}

	// OpCode
	private short inst;

	public short getInst() {
		return inst;
	}

	public void setInst(short inst) {
		this.inst = inst;
	}

	// READ_MEM value
	private short mdr;

	public short getMdr() {
		return mdr;
	}

	public void setMdr(short mdr) {
		this.mdr = mdr;
	}

	// READ_MEM adr
	private byte mar;

	public byte getMar() {
		return mar;
	}

	public void setMar(byte mar) {
		this.mar = mar;
	}

	// Phase FETCH --> DECODE --> READ_MEM --> OPERATE --> WRITE_MEM --> FETCH
	private byte phase;

	public byte getPhase() {
		return phase;
	}

	public void nextPhase() {
		phase = (byte) ((phase + 1) % 5);

	}

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
		sp = -1;
		inst = 0;
		mdr = 0;
		mar = 0;
		phase = Types.FETCH;
	}
}
