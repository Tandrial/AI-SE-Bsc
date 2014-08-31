/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package ctrl;

import java.io.File;

import ram.Ram;
import reg.Register;
import alu.Alu;
import dataTypes.Types;

public class Cntrl {

	private Register reg;
	private Alu alu;
	private Ram ram;

	public Cntrl(File f) {
		reg = new Register();
		ram = new Ram(f);
		alu = new Alu();
	}

	public void work() {
		boolean running = true;
		byte adr = 0;
		byte aluMode = 0;
		byte adrMode = 0;
		short inst = 0;

		while (running) {

			switch (reg.getPhase()) {
			case Types.FETCH:
				adr = reg.getPC();
				reg.incPC();
				reg.nextPhase();
				break;

			case Types.DECODE:
				inst = ram.readData(adr);
				reg.setInst(inst);
				adrMode = (byte) (reg.getInst() >> 12);
				if (inst < 0)
					adrMode += 16;
				aluMode = (byte) ((reg.getInst() >> 8) & 0xF);
				reg.nextPhase();
				System.out.println("[DEBUG] inst= " + reg.getInst() + " adr = "
						+ adrMode + " alu= " + aluMode);
				break;
				
			case Types.READ_MEM:
				if (reg.getInst() == 0)
					running = false;
				reg.nextPhase();
				break;
			case Types.OPERATE:
				reg.nextPhase();
				break;
			case Types.WRITE_MEM:
				reg.nextPhase();
				break;

			default:
				reg.nextPhase();
				break;
			}

		}

	}
}
