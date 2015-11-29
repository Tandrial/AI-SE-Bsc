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

	public static boolean debug = false;

	private Register reg;
	private Alu alu;
	private Ram ram;
	private File f;

	private short[] stack;

	public Ctrl() {
		reg = new Register();
		alu = new Alu();
		stack = new short[STACK_SIZE];
		ram = new Ram();
	}

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
				debug("[DEBUG] PC = " + reg.getPC());
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

				debug(String
						.format("[DEBUG] inst= 0x%04X adr = %s alu= %s direktValue = %s",
								reg.getINST(), mode, alu_mode, direktOp));
				break;

			case Phases.READ_MEM:
				if (debug)
					System.out.print("[DEBUG] Op1");
				alu_op1 = getOp(ctrl_mode[0], direktOp);
				if (debug)
					System.out.print("[DEBUG] Op2");
				alu_op2 = getOp(ctrl_mode[1], direktOp);

				debug(String.format("[DEBUG] op1 = %s op2 = %s", alu_op1,
						alu_op2));
				break;

			case Phases.OPERATE:
				alu.setMode(alu_mode);
				alu.operate(alu_op1, alu_op2, reg.getCarry());
				if (alu_mode == 15)
					alu_result = alu_op1;
				else
					alu_result = alu.getResult();
				debug(String.format("[DEBUG] out = %s ACC = %s", alu_result,
						reg.getAcc()));
				break;

			case Phases.WRITE_MEM:
				if (updateCond(ctrl_mode[3]))
					writeData(ctrl_mode[2], alu_result, direktOp);

				if (ctrl_mode[4] == CtrlMode.UPCARRY_TRUE)
					reg.setCarry(alu.getCarry_out());

				reg.setZero(reg.getAcc() == 0);
				if (debug)
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
			debug("[DEBUG] Setting ACC = " + aluResult);
			break;

		case CtrlMode.PC:
			reg.setPC((byte) aluResult);
			debug("[DEBUG] Setting PC = " + aluResult);
			break;

		case CtrlMode.AMEM:
			ram.writeData((byte) direktValue, aluResult);
			debug(String.format("[DEBUG] Writing Mem @ 0x%02X = %s",
					direktValue, aluResult));
			break;

		case CtrlMode.RMEM:
			ram.writeData((byte) (reg.getPC() + direktValue + 1), aluResult);
			debug(String.format("[DEBUG] Writing Mem @ 0x%02X = %s",
					direktValue, aluResult));
			break;

		case CtrlMode.SMEM:
			reg.incSP();
			stack[reg.getSP()] = aluResult;
			debug(String.format("[DEBUG] Pushing onto STACK [size = %s] = %s",
					reg.getSP(), aluResult));
			break;

		case CtrlMode.IREG:
			if (direktValue == 0) {
				reg.setPC((byte) (aluResult + 1));
				debug("[DEBUG] Setting PC = " + aluResult);
			} else {
				reg.setAcc(aluResult);
				debug("[DEBUG] Setting ACC = " + aluResult);
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
			debug(" NONE");
			return 0;
		case CtrlMode.ACC:
			debug(" loading from ACC");
			return reg.getAcc();
		case CtrlMode.PC:
			debug(" loading from PC");
			return reg.getPC();
		case CtrlMode.INST:
			debug(" loading 8bit direkt Value");
			return value;
		case CtrlMode.AMEM:
			debug(String.format(" loading from Mem @ 0x%02X", value));
			return ram.readData((byte) value);
		case CtrlMode.RMEM:
			debug(String.format(" loading from Mem @ 0x%02X", reg.getPC()
					+ value + 1));
			return ram.readData((byte) (reg.getPC() + value + 1));
		case CtrlMode.SMEM:
			debug(" loading from Stack @ " + reg.getSP());
			tmp = stack[reg.getSP()];
			reg.decSP();
			return tmp;
		case CtrlMode.IREG:
			debug(value == 0 ? " loading from PC" : " loading from ACC");
			return value == 0 ? reg.getPC() : reg.getAcc();
		case CtrlMode.PMEM:
			debug(" loading 16bit direkt Value");
			tmp = ram.readData((byte) reg.getPC());
			reg.incPC();
			return tmp;
		default:
			return 0;
		}
	}

	public void debug(String s) {
		if (debug)
			System.out.println(s);
	}
}
