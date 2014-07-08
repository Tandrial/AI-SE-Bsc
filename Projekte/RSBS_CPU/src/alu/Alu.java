/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package alu;

public class Alu {

	// Input from Ctrl
	private byte mode;
	private short op1;
	private short op2;
	private boolean carry_in;

	// Output to Ctrl

	private short result;
	private boolean carry_out;

	public void setMode(byte mode) {
		this.mode = mode;
	}

	public void operate(short op1, short op2, boolean carry_in) {
		if (mode == 0xF)
			return;
		this.op1 = op1;
		this.op2 = op2;
		this.carry_in = carry_in;
		result = 0;
		carry_out = false;
		calcResult();
	}

	private void calcResult() {
		switch (mode) {
		case 0: // ADD
			int res = op1 + op2;
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = (short) res;
			break;
		case 1: // AND
			result = (short) (op1 & op2);
			break;
		case 2: // MUL
			res = op1 * op2;
			if (res > Short.MAX_VALUE)
				carry_out = true;
			result = (short) res;
			break;
		case 3: // ADC
			res = op1 + op2 + (carry_in ? 1 : 0);
			if (res > Short.MAX_VALUE)
				carry_out = true;
			result = (short) res;
			break;
		case 4: // SUB
			res = op1 - op2;
			if (res < Short.MIN_VALUE)
				carry_out = true;
			result = (short) res;
			break;
		case 5: // NEG
			result = (short) -op1;
			break;
		case 6: // OR
			result = (short) (op1 | op2);
			break;
		case 7: // NOR
			result = (short) (~op1 & ~op2);
			break;
		case 8: // SHR
			carry_out = (result & 0x1) == 1;
			result = (short) (result >>> 1);
			break;
		case 9: // SHL
			carry_out = (result & 0x80) == 1;
			result = (short) (result << 1);
			break;
		case 10: // RCR
			carry_out = (result & 0x1) == 1;
			result = (short) (result >>> 1);
			if (carry_in)
				result = (short) (result | 0x1);
			break;
		case 11: // RCL
			carry_out = (result & 0x80) == 1;
			result = (short) (result << 1);
			if (carry_in)
				result = (short) (result | 0x80);
			break;
		case 12:

			break;
		case 13:

			break;
		case 14:

			break;
		default: // BYPASS
			break;
		}
	}

	public short getResult() {
		return result;
	}

	public boolean getCarry_out() {
		return carry_out;
	}
}
