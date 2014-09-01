/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu;

import java.io.File;

import mkrane.cpu.types.CtrlMode;
import mkrane.cpu.types.Types;

public class Ctrl {

	private Register reg;
	private Alu alu;
	private Ram ram;
	private File f;

	private short[] stack;

	public Ctrl(File f) {
		this.f = f;
		reg = new Register();
		alu = new Alu();
		reset();
	}

	public void reset() {
		reg.reset();
		stack = new short[Types.RAM_SIZE];
		ram = new Ram(f);
	}

	public void work() {
		byte adr = 0;
		byte aluMode = 0;
		byte[] adrMode = { 0, 0, 0, 0 };
		short direktValue = 0;
		short inst = 0;
		short op1 = 0;
		short op2 = 0;
		short out = 0;

		while (true) {

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
				byte mode = (byte) (inst >> 12);
				if (inst < 0) {
					mode += 16;
				}
				adrMode = CtrlMode.MODES[mode];
				aluMode = (byte) ((inst >> 8) & 0xF);
				direktValue = (short) (inst & 0xFF);

				System.out
						.println(String
								.format("[DEBUG] inst= 0x%04X adr = %s alu= %s direktValue = %s",
										reg.getInst(), adrMode, aluMode,
										direktValue));
				reg.nextPhase();
				break;

			case Types.READ_MEM:

				op1 = getOp(adrMode[0], direktValue);
				op2 = getOp(adrMode[1], direktValue);

				System.out.println("[DEBUG] op1 = " + op1 + " op2 = " + op2);
				reg.nextPhase();
				break;

			case Types.OPERATE:
				alu.setMode(aluMode);
				alu.operate(op1, op2, reg.getAcc(), reg.getCarry());
				out = alu.getResult();
				System.out.println("[DEBUG] out = " + out + " ACC = "
						+ reg.getAcc());

				reg.nextPhase();
				break;

			case Types.WRITE_MEM:
				if (aluMode == 15)
					out = op1;
				if (updateCond(adrMode[3]))
					writeData(adrMode[2], out, direktValue);

				if (adrMode[4] == Types.UPCARRY_TRUE)
					reg.setCarry(alu.getCarry_out());

				reg.setZero(reg.getAcc() == 0);
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

	private boolean updateCond(byte adrMode) {
		if (adrMode == Types.UPCOND_ALWAYS)
			return true;
		else if (adrMode == Types.UPCOND_CARRY)
			return reg.getCarry();
		else
			return reg.getZero();

	}

	private void writeData(byte adrMode, short aluResult, short direktValue) {
		switch (adrMode) {
		case Types.ACC:
			reg.setAcc(aluResult);
			System.out.println("[DEBUG] ACC = " + aluResult);
			break;
		case Types.PC:
			reg.setPC((byte) aluResult);
			System.out.println("[DEBUG] PC = " + aluResult);
			break;
		case Types.AMEM:
			ram.writeData(aluResult, (byte) direktValue);
			System.out.println(String
					.format("[DEBUG] Writing Mem @ 0x%02X = %s", direktValue,
							aluResult));
			break;
		case Types.RMEM:
			ram.writeData(aluResult, (byte) (reg.getPC() + direktValue + 1));
			System.out.println(String
					.format("[DEBUG] Writing Mem @ 0x%02X = %s", direktValue,
							aluResult));
			break;
		case Types.SMEM:
			reg.incSP();
			stack[reg.getSP()] = aluResult;
			System.out.println(String.format(
					"[DEBUG] Pushing onto STACK [size = %s] = %s", reg.getSP(),
					aluResult));
			break;
		case Types.IREG:
			if (direktValue == 0) {
				reg.setPC((byte) (aluResult + 1));
				System.out.println("[DEBUG] PC = " + aluResult);
			} else {
				reg.setAcc(aluResult);
				System.out.println("[DEBUG] ACC = " + aluResult);
			}
			break;
		default:
			break;
		}
	}

	private short getOp(byte adrMode, short value) {
		switch (adrMode) {
		case Types.NONE:
			return 0;
		case Types.ACC:
			return reg.getAcc();
		case Types.PC:
			return reg.getPC();
		case Types.INST:
			return value;
		case Types.AMEM:
			return ram.readData((byte) value);
		case Types.RMEM:
			return ram.readData((byte) (reg.getPC() + value + 1));
		case Types.SMEM:
			short s = stack[reg.getSP()];
			reg.decSP();
			return s;
		case Types.IREG:
			return value == 0 ? reg.getPC() : reg.getAcc();
		default:
			return 0;
		}
	}
}
