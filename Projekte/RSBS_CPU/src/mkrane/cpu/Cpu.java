/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu;

import java.io.File;

public class Cpu {

	private Ctrl steuerwerk;

	public Cpu(File f) {
		steuerwerk = new Ctrl(f);
	}

	public void startSim() {
		steuerwerk.work();
	}

	public static void main(String[] args) {
		Cpu c;
		if (args.length == 1)
			c = new Cpu(new File(args[0]));
		else
			c = new Cpu(new File("mul_test.hex"));
		
		c.startSim();
	}
}
