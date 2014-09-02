/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu;

import java.io.File;

import mkrane.cpu.types.CtrlMode;
import mkrane.cpu.types.Phases;

public class Ctrl {

	public static int RAM_SIZE = 1 << 8; // 256;
	public static int STACK_SIZE = 1 << 8; // 256;

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
		stack = new short[STACK_SIZE];
		ram = new Ram(f);
	}

	public void work() {
		byte[] ctrl_mode = { 0, 0, 0, 0 };
		short direktOp = 0;

		byte alu_mode = 0;
		short alu_op1 = 0;
		short alu_op2 = 0;
		short alu_result = 0;

		while (true) {

			if (ram.readData((byte) 0xFF) == 0xFF || reg.getPC() > 255)
				break;

			switch (reg.getPhase()) {

			case Phases.FETCH:
				reg.setMAR(reg.getPC());
				System.out.println("[DEBUG] PC = " + reg.getPC());
				reg.incPC();
				break;

			case Phases.DECODE:
				short mdr = ram.readData(reg.getMAR());
				reg.setINST(mdr);

				byte mode = (byte) (mdr >> 12);
				if (mdr < 0) {
					mode += 16;
				}
				ctrl_mode = CtrlMode.MODES[mode];
				alu_mode = (byte) ((mdr >> 8) & 0xF);
				direktOp = (short) (mdr & 0xFF);

				System.out
						.println(String
								.format("[DEBUG] inst= 0x%04X adr = %s alu= %s direktValue = %s",
										reg.getINST(), mode, alu_mode, direktOp));
				break;

			case Phases.READ_MEM:
				System.out.print("[DEBUG] Op1");
				alu_op1 = getOp(ctrl_mode[0], direktOp);
				System.out.print("[DEBUG] Op2");
				alu_op2 = getOp(ctrl_mode[1], direktOp);

				System.out.println("[DEBUG] op1 = " + alu_op1 + " op2 = "
						+ alu_op2);
				break;

			case Phases.OPERATE:
				alu.setMode(alu_mode);
				alu.operate(alu_op1, alu_op2, reg.getCarry());
				if (alu_mode == 15)
					alu_result = alu_op1;
				else
					alu_result = alu.getResult();
				System.out.println("[DEBUG] out = " + alu_result + " ACC = "
						+ reg.getAcc());
				break;

			case Phases.WRITE_MEM:
				if (updateCond(ctrl_mode[3]))
					writeData(ctrl_mode[2], alu_result, direktOp);

				if (ctrl_mode[4] == CtrlMode.UPCARRY_TRUE)
					reg.setCarry(alu.getCarry_out());

				reg.setZero(reg.getAcc() == 0);
				System.out.println();
				break;

			default:
				break;
			}
			reg.nextPhase();
		}
		ram.dumpMemory();
	}

	private boolean updateCond(byte adrMode) {
		if (adrMode == CtrlMode.UPCOND_ALWAYS)
			return true;
		else if (adrMode == CtrlMode.UPCOND_CARRY)
			return reg.getCarry();
		else
			return reg.getZero();

	}

	private void writeData(byte adrMode, short aluResult, short direktValue) {
		switch (adrMode) {
		case CtrlMode.ACC:
			reg.setAcc(aluResult);
			System.out.println("[DEBUG] Setting ACC = " + aluResult);
			break;

		case CtrlMode.PC:
			reg.setPC((byte) aluResult);
			System.out.println("[DEBUG] Setting PC = " + aluResult);
			break;

		case CtrlMode.AMEM:
			ram.writeData(aluResult, (byte) direktValue);
			System.out.println(String
					.format("[DEBUG] Writing Mem @ 0x%02X = %s", direktValue,
							aluResult));
			break;

		case CtrlMode.RMEM:
			ram.writeData(aluResult, (byte) (reg.getPC() + direktValue + 1));
			System.out.println(String
					.format("[DEBUG] Writing Mem @ 0x%02X = %s", direktValue,
							aluResult));
			break;

		case CtrlMode.SMEM:
			reg.incSP();
			stack[reg.getSP()] = aluResult;
			System.out.println(String.format(
					"[DEBUG] Pushing onto STACK [size = %s] = %s", reg.getSP(),
					aluResult));
			break;

		case CtrlMode.IREG:
			if (direktValue == 0) {
				reg.setPC((byte) (aluResult + 1));
				System.out.println("[DEBUG] Setting PC = " + aluResult);
			} else {
				reg.setAcc(aluResult);
				System.out.println("[DEBUG] Setting ACC = " + aluResult);
			}
			break;

		default:
			break;
		}
	}

	private short getOp(byte adrMode, short value) {
		short tmp;
		switch (adrMode) {
		case CtrlMode.NONE:
			System.out.println(" NONE");
			return 0;
		case CtrlMode.ACC:
			System.out.println(" loading from ACC");
			return reg.getAcc();
		case CtrlMode.PC:
			System.out.println(" loading from PC");
			return reg.getPC();
		case CtrlMode.INST:
			System.out.println(" loading 8bit direkt Value");
			return value;
		case CtrlMode.AMEM:
			System.out.println(String.format(" loading from Mem @ 0x%02X",
					value));
			return ram.readData((byte) value);
		case CtrlMode.RMEM:
			System.out.println(String.format(" loading from Mem @ 0x%02X",
					reg.getPC() + value + 1));
			return ram.readData((byte) (reg.getPC() + value + 1));
		case CtrlMode.SMEM:
			System.out.println(" loading from Stack @ " + reg.getSP());
			tmp = stack[reg.getSP()];
			reg.decSP();
			return tmp;
		case CtrlMode.IREG:
			System.out.println(value == 0 ? " loading from PC"
					: " loading from ACC");
			return value == 0 ? reg.getPC() : reg.getAcc();
		case CtrlMode.PMEM:
			System.out.println(" loading 16bit direkt Value");
			tmp = ram.readData((byte) reg.getPC());
			reg.incPC();
			return tmp;
		default:
			return 0;
		}
	}
}
