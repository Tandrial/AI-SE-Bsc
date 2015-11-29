/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu.types;

public class AluMode {
	public static final byte ADD = 0;
	public static final byte AND = 1;
	public static final byte MUL = 2;
	public static final byte ADC = 3;
	public static final byte SUB = 4;
	public static final byte NEG = 5;
	public static final byte OR = 6;
	public static final byte NOR = 7;
	public static final byte SHR = 8;
	public static final byte SHL = 9;
	public static final byte RCR = 10;
	public static final byte RCL = 11;
	public static final byte BYPASS = 15;
}
