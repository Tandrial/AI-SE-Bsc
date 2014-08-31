import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import ram.Ram;

public class RamTest {
	private static Ram ram;

	@Test
	public void LoadFile() {
		ram = new Ram(new File("test/ram_hex/ram1a.hex"));
		short correct = (short) 0xAFFF;
		short is = ram.readData((byte) 0x0d);

		if (is != correct)
			fail(String.format(
					"Load from File failed RAM@0D = %d (should be %d)", is,
					correct));
	}

	@Test
	public void WriteMem() {
		ram = new Ram();
		short write = (short) 0xAFFF;
		byte adr = 0;

		ram.writeData(write, adr);
		short is = ram.readData(adr);
		if (is != write)
			fail(String.format(
					"Write to RAM failed RAM@00 = %d (should be %d)", is,
					write));
	}
}
