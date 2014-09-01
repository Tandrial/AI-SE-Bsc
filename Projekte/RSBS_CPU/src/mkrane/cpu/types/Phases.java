/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu.types;

public class Phases {
	public static final byte FETCH = 0;
	public static final byte DECODE = 1;
	public static final byte READ_MEM = 2;
	public static final byte OPERATE = 3;
	public static final byte WRITE_MEM = 4;
}
