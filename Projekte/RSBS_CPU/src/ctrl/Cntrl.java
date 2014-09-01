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

	private short[] stack;

	public Cntrl(File f) {
		reg = new Register();
		ram = new Ram(f);
		alu = new Alu();
		stack = new short[Types.RAM_SIZE];
	}

	public void work() {
		byte adr = 0;
		byte aluMode = 0;
		byte adrMode = 0;
		short direktValue = 0;
		short inst = 0;
		short op1 = 0;
		short op2 = 0;
		short out = 0;
		int count = 0;

		while (true ){//&& count++ < 32000) {

			if (ram.readData((byte) 0xFF) == 0xFF || reg.getPC() > 255)
				break;

			switch (reg.getPhase()) {
			case Types.FETCH:
				adr = reg.getPC();
				System.out.println("[DEBUG] PC = " + reg.getPC());
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

				System.out
						.println(String
								.format("[DEBUG] inst= 0x%04X adr = %s alu= %s direktValue = %s",
										reg.getInst(), adrMode, aluMode,
										direktValue));
				reg.nextPhase();
				break;

			case Types.READ_MEM:

				op1 = getOp1(adrMode, direktValue);
				op2 = getOp2(adrMode, direktValue);

				System.out.println("[DEBUG] op1 = " + op1 + " op2 = " + op2);
				reg.nextPhase();
				break;

			case Types.OPERATE:
				alu.setMode(aluMode);
				alu.operate(op1, op2, reg.getACC(), reg.getCarry());
				out = alu.getResult();
				System.out.println("[DEBUG] out = " + out + " ACC = "
						+ reg.getACC());

				reg.nextPhase();
				break;

			case Types.WRITE_MEM:
				if (aluMode == 15)
					out = op1;

				switch (adrMode) {

				case 1:
				case 9:
				case 12:
					reg.setCarry(alu.getCarry_out());
					reg.setAcc(out);
					System.out.println("[DEBUG] ACC = " + out);
					break;

				case 0:
				case 2:
				case 8:
				case 11:
					reg.setAcc(out);
					System.out.println("[DEBUG] ACC = " + out);
					break;

				case 5:
					if (reg.getCarry()) {
						reg.setPC((byte) out);
						System.out.println("[DEBUG] PC = " + out);
					}
					break;
				case 6:
					if (reg.getZero()) {
						reg.setPC((byte) out);
						System.out.println("[DEBUG] PC = " + out);
					}
					break;
				case 4:
					reg.setPC((byte) out);
					System.out.println("[DEBUG] PC = " + out);
					break;

				case 10:
					ram.writeData(out, (byte) direktValue);
					System.out.println(String.format(
							"[DEBUG] Writing Mem @ 0x%02X = %s", direktValue,
							out));
					break;

				case 13:
					ram.writeData(out, (byte) (reg.getPC() + direktValue + 1));
					System.out.println(String.format(
							"[DEBUG] Writing Mem @ 0x%02X = %s", direktValue,
							out));
					break;

				case 14:
					if (direktValue == 0) {
						reg.setPC((byte) out);
						System.out.println("[DEBUG] PC = " + out);
					} else {
						reg.setAcc(out);
						System.out.println("[DEBUG] ACC = " + out);
					}
					break;
				case 15:
					stack[reg.getSP()] = out;
					reg.incSP();
					System.out.println(String.format(
							"[DEBUG] Pushing onto STACK [size = %s] = %s",
							reg.getSP() - 1, out));
				default:
					break;
				}
				reg.setZero(reg.getACC() == 0);
				reg.nextPhase();
				System.out.println();
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
			return ram.readData((byte) direktValue);

		case 11:
			return ram.readData((byte) (reg.getPC() + direktValue + 1));

		case 14:
			short value = stack[reg.getSP()];
			reg.decSP();
			return value;

		case 15:
			return direktValue == 0 ? reg.getPC() : reg.getACC();

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
			return ram.readData((byte) direktValue);

		case 12:
			return ram.readData((byte) (reg.getPC() + direktValue + 1));
		default:
			return 0;
		}
	}
}
