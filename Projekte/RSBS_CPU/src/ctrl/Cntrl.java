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
		short direktValue = 0;
		short inst = 0;
		short op1 = 0;
		short op2 = 0;
		short out = 0;

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
				adrMode = (byte) (inst >> 12);
				aluMode = (byte) ((inst >> 8) & 0xF);
				direktValue = (short) (inst & 0xFF);
				if (inst < 0) {
					adrMode += 16;
				}

				reg.nextPhase();
				System.out.println("[DEBUG] inst= " + reg.getInst() + " adr = "
						+ adrMode + " alu= " + aluMode + " direktValue = "
						+ direktValue);
				break;

			case Types.READ_MEM:

				op1 = getOp1(adrMode, direktValue);
				op2 = getOp2(adrMode, direktValue);

				reg.nextPhase();
				break;

			case Types.OPERATE:
				alu.setMode(aluMode);
				alu.operate(op1, op2, reg.getCarry());
				out = alu.getResult();
				if (aluMode != 15)
					reg.setAcc(out);
				reg.nextPhase();
				break;

			case Types.WRITE_MEM:
				if (aluMode == 15)
					out = op1;

				switch (adrMode) {

				case 0:
				case 1:
				case 2:
				case 8:
				case 9:
				case 11:
				case 12:
					reg.setAcc(out);
					break;

				case 4:
				case 5:
				case 6:
					reg.setPC((byte) out);
					break;

				case 10:
					ram.writeData(out, (byte) direktValue);
					break;

				case 13:
				case 14:
				case 15:
				default:
					break;
				}

				if (reg.getInst() == 0)
					running = false;
				reg.nextPhase();
				break;

			default:
				reg.nextPhase();
				break;
			}
		}
		ram.dumpMemory();

	}

	private short getOp1(byte adrMode, short direktValue) {
		switch (adrMode) {
		case 0:
		case 1:
		case 3:
		case 9:
		case 10:
		case 12:
		case 13:
			return reg.getACC();

		case 2:
		case 4:
		case 5:
		case 6:
			return direktValue;

		case 8:
		case 11:
		case 14:
		case 15:
		default:
			return 0;
		}
	}

	private short getOp2(byte adrMode, short direktValue) {
		switch (adrMode) {
		case 0:
		case 1:
			return direktValue;

		case 2:
		case 3:
		case 10:
		case 13:
		case 14:
		case 15:
			return 0;

		case 4:
		case 5:
		case 6:
			return reg.getPC();

		case 8:
		case 11:
			return reg.getACC();

		case 9:
		case 12:
		default:
			return 0;
		}
	}
}
