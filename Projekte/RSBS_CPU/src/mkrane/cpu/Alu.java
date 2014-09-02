/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu;

import mkrane.cpu.types.AluMode;

public class Alu {

	// Input from Ctrl
	private byte mode;
	private int op1;
	private int op2;
	private boolean carry_in;

	// Output to Ctrl

	private int result;
	private boolean carry_out;

	public void setMode(byte mode) {
		this.mode = mode;
	}

	public void operate(short op1, short op2,/*short acc, */boolean carry_in) {
		if (mode == AluMode.BYPASS)
			return;
		this.op1 = op1;
		this.op2 = op2;
		this.carry_in = carry_in;
		result = 0;//;acc;
		carry_out = false;
		calcResult();//acc);
	}

	private void calcResult() {// short acc) {
		int res;
		switch (mode) {
		case AluMode.ADD:
			res = op1 + op2;
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case AluMode.AND:
			result = op1 & op2;
			break;
		case AluMode.MUL:
			res = op1 * op2;
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case AluMode.ADC:
			res = op1 + op2 + (carry_in ? 1 : 0);
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case AluMode.SUB:
			res = op1 - op2;
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case AluMode.NEG:
			result = -op1;
			break;
		case AluMode.OR:
			result = (op1 | op2);
			break;
		case AluMode.NOR:
			result = ~op1 & ~op2;
			break;
		case AluMode.SHR:
			carry_out = (op1 & 0x1) == 1;
			result = op1 >> 1;
			break;
		case AluMode.SHL:
			carry_out = (op1 & 0x80) == 1;
			result = op1 << 1;
			break;
		case AluMode.RCR:
			carry_out = (op1 & 0x1) == 1;
			result = op1 >>> 1;
			if (carry_in)
				result |= 0x1;
			break;
		case AluMode.RCL:
			carry_out = (op1 & 0x80) == 1;
			result = op1 << 1;
			if (carry_in)
				result |= 0x80;
			break;
		case AluMode.BYPASS:
		default:
			break;
		}
	}

	public short getResult() {
		return (short) result;
	}

	public boolean getCarry_out() {
		return carry_out;
	}
}
