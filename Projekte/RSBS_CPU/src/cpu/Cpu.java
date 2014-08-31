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

	private Cntrl steuerwerk;

	public Cpu(File f) {
		steuerwerk = new Cntrl(f);
	}

	public void startSim() {
		steuerwerk.work();

	}

	public static void main(String[] args) {
		Cpu c = new Cpu(new File("ram1b.hex"));
		c.startSim();
	}
}
