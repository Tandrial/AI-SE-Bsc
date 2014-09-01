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
	private int op1;
	private int op2;
	private boolean carry_in;

	// Output to Ctrl

	private int result;
	private boolean carry_out;

	public void setMode(byte mode) {
		this.mode = mode;
	}

	public void operate(short op1, short op2, short acc, boolean carry_in) {
		if (mode == 0xF) {
			result = acc;
			return;
		}
		this.op1 = op1;
		this.op2 = op2;
		this.carry_in = carry_in;
		result = 0;
		carry_out = false;
		calcResult(acc);
	}

	private void calcResult(short acc) {
		int res;
		switch (mode) {
		case 0: // ADD
			res = op1 + op2;
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case 1: // AND
			result = op1 & op2;
			break;
		case 2: // MUL
			res = op1 * op2;
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case 3: // ADC
			res = op1 + op2 + (carry_in ? 1 : 0);
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case 4: // SUB
			res = op1 - op2;
			if (res > Short.MAX_VALUE || res < Short.MIN_VALUE)
				carry_out = true;
			result = res;
			break;
		case 5: // NEG
			result = -acc;
			break;
		case 6: // OR
			result = (op1 | op2);
			break;
		case 7: // NOR
			result = ~op1 & ~op2;
			break;
		case 8: // SHR
			carry_out = (acc & 0x1) == 1;
			result = acc >> 1;
			break;
		case 9: // SHL
			carry_out = (acc & 0x80) == 1;
			result = acc << 1;
			break;
		case 10: // RCR
			carry_out = (acc & 0x1) == 1;
			result = acc >>> 1;
			if (carry_in)
				result |= 0x1;
			break;
		case 11: // RCL
			carry_out = (acc & 0x80) == 1;
			result = acc << 1;
			if (carry_in)
				result |= 0x80;
			break;
		case 12:
		case 13:
		case 14:
		default: // BYPASS
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
