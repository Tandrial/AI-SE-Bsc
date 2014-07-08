/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package dataTypes;

public class Types {

	public static int RAM_SIZE = 1 << 8; // 256;

	public static byte FETCH = 0;
	public static byte DECODE = 1;
	public static byte READ_MEM = 2;
	public static byte OPERATE = 3;
	public static byte WRITE_MEM = 4;

}
