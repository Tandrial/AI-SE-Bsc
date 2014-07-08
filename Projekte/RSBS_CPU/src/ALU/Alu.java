package ALU;

public class Alu {

	// Input from Ctrl
	private byte mode;
	private short op1;
	private short op2;
	private boolean carry_in;

	// Output to Ctrl

	private short result;
	private boolean carry_out;

	public void operate(byte mode, short op1, short op2, boolean carry_in) {
		this.mode = mode;
		if (mode == 0xF)
			return;
		this.op1 = op1;
		this.op2 = op2;
		this.carry_in = carry_in;
		result = 0;
		carry_out = false;
		setResult();
	}

	private void setResult() {
		switch (mode) {
		case 0: // ADD
			int res = op1 + op2;
			if (res > Short.MAX_VALUE)
				carry_out = true;
			result = (short) res;
			break;
		case 1: // AND
			result = (short) (op1 & op2);
			break;
		case 2: // MUL
			res = op1 * op2;
			if (res > Short.MAX_VALUE)
				carry_out = true;
			result = (short) res;
			break;
		case 3: // ADC
			res = op1 + op2 + (carry_in ? 1 : 0);
			if (res > Short.MAX_VALUE)
				carry_out = true;
			result = (short) res;
			break;
		case 4: // SUB

			break;
		case 5: // NEG

			break;
		case 6: // OR
			result = (short) (op1 | op2);
			break;
		case 7: // NOR

			break;
		case 8: // SHR

			break;
		case 9: // SHL

			break;
		case 10: // RCR

			break;
		case 11: // RCL

			break;
		case 12:

			break;
		case 13:

			break;
		case 14:

			break;
		default: // BYPASS
			break;
		}
	}

	public short getResult() {
		return result;
	}

	public boolean getCarry_out() {
		return carry_out;
	}

	public static void main(String[] args) {
		int test = Short.MAX_VALUE;
		System.out.println(test);
		test++;
		System.out.println(test);
		short t = (short) test;
		System.out.println(t);

	}

}
