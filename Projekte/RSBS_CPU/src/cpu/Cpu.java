/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package cpu;

import java.io.File;

import ram.Ram;
import ctrl.Cntrl;
import alu.Alu;

public class Cpu {

	private File file;

	private Alu alu;
	private Ram ram;
	private Cntrl steuerwerk;

	public Cpu(File f) {
		file = f;
		alu = new Alu();
		ram = new Ram(file);
		steuerwerk = new Cntrl();
	}
}
