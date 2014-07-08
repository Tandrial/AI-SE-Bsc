import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ALU.Alu;

public class AluTests {
	private static Alu alu;
	byte mode;
	short op1;
	short op2;
	boolean c_i;

	short res;
	short res_cor;
	boolean c_o;
	boolean c_o_cor;

	@Before
	public void setUp() throws Exception {
		alu = new Alu();
	}

	@Test
	public void ADDModeTest() {
		mode = 0;
		c_i = true;

		op1 = 0;
		op2 = 0;
		res_cor = (short) (op1 + op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		c_o = alu.getCarry_out();
		if (res != res_cor || c_o)
			fail(String
					.format("Add failed %d + %d = %d carry_out = %b (should be %d , false)",
							op1, op2, res, c_o, res_cor));

		op1 = 10;
		op2 = 35;
		res_cor = (short) (op1 + op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		c_o = alu.getCarry_out();
		if (res != res_cor || c_o)
			fail(String
					.format("Add failed %d + %d = %d carry_out = %b (should be %d , false)",
							op1, op2, res, c_o, res_cor));

		op1 = 10;
		op2 = -35;
		res_cor = (short) (op1 + op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		c_o = alu.getCarry_out();
		if (res != res_cor || c_o)
			fail(String
					.format("Add failed %d + %d = %d carry_out = %b (should be %d , false)",
							op1, op2, res, c_o, res_cor));

		op1 = -1;
		op2 = Short.MIN_VALUE;
		res_cor = (short) (op1 + op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		c_o = alu.getCarry_out();
		if (!c_o)
			fail(String
					.format("Add failed %d + %d = %d carry_out = %b (should be %d , true)",
							op1, op2, res, c_o, res_cor));
		op1 = 1;
		op2 = Short.MAX_VALUE;
		res_cor = (short) (op1 + op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		c_o = alu.getCarry_out();
		if (!c_o)
			fail(String
					.format("Add failed %d + %d = %d carry_out = %b (should be %d , true)",
							op1, op2, res, c_o, res_cor));
	}

	@Test
	public void ANDModeTest() {
		mode = 1;
		c_i = false;

		op1 = 0;
		op2 = 0;
		res_cor = (short) (op1 & op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		if (res != res_cor)
			fail(String.format("AND failed %d & %d = %d (should be %d)", op1,
					op2, res, res_cor));

		op1 = 0xAA;
		op2 = 0x55;
		res_cor = (short) (op1 & op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		if (res != res_cor)
			fail(String.format("AND failed %d & %d = %d (should be %d)", op1,
					op2, res, res_cor));

		op1 = 0xFF;
		op2 = 0xFF;
		res_cor = (short) (op1 & op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		if (res != res_cor)
			fail(String.format("AND failed %d & %d = %d (should be %d)", op1,
					op2, res, res_cor));

		op1 = 0x0F;
		op2 = 0x0F;
		res_cor = (short) (op1 & op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		if (res != res_cor)
			fail(String.format("AND failed %d & %d = %d (should be %d)", op1,
					op2, res, res_cor));
	}

	@Test
	public void ADCModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void SUBModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void NEGModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void ORModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void NORModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void SHRModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void SHLModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void RCRModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void RCLModeTest() {
		fail("Not yet implemented");
	}

	@Test
	public void BYPASSModeTest() {
		mode = 0;
		c_i = true;
		op1 = 10;
		op2 = 35;
		res_cor = (short) (op1 + op2);
		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		c_o = alu.getCarry_out();

		mode = 15;
		op1 = 80;
		op2 = 15;
		c_i = false;
		res_cor = res;
		c_o_cor = c_o;

		alu.operate(mode, op1, op2, c_i);
		res = alu.getResult();
		c_o = alu.getCarry_out();

		if (res != res_cor || c_o != c_o_cor)
			fail("BYPASS failed, previous result was overwritten.");
	}
}
