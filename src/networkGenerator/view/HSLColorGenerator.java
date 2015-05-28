package networkGenerator.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class HSLColorGenerator {

	public static final int H_MASK = 0b00001111_00001111_00000000_11001111;
	public static final int S_MASK = 0b00110000_00110000_00110011_00010000;
	public static final int B_MASK = 0b01000000_11000000_11001100_00100100;

	public static final float H_BEGIN = 0.0f;
	public static final float H_END = 1.0f;

	public static final float S_BEGIN = 0.8f;
	public static final float S_END = 0.2f;

	public static final float B_BEGIN = 0.9f;
	public static final float B_END = 0.1f;

	public static final int H_REVERSE_MASK = revertBits(H_MASK);
	public static final int S_REVERSE_MASK = revertBits(S_MASK);
	public static final int B_REVERSE_MASK = revertBits(B_MASK);

	public static final int H_RANGE = maskValueRange(H_MASK);
	public static final int S_RANGE = maskValueRange(S_MASK);
	public static final int B_RANGE = maskValueRange(B_MASK);

	protected static int oneBitCount(int x) {
		int r = 0;
		while (x != 0) {
			r += x & 0b1;
			x = x >> 1;
		}
		return r;
	}

	protected static int maskValueRange(int x) {
		return maskAndPack(x, -1) + 1;
	}

	protected static int revertBits(int x) {

		int r = 0;
		for (int i = 0; i < 32; i++) {
			r = (r << 1) | x & 0b1;
			x = x >> 1;
		}
		return r;
	}

	protected static int maskAndPack(int mask, int x) {
		int r = 0;
		while (mask != 0) {
			if (mask < 0) {
				r = r << 1;
				r |= (x < 0) ? 1 : 0;
			}
			mask = mask << 1;
			x = x << 1;
		}
		return r;
	}

	public Color getColor(int index) {
		if (index < 0)
			throw new IllegalArgumentException(String.format(
					"Color index %d is negative - not allowed!", index));
		int reverseIndex = revertBits(index);
		float h = H_BEGIN
				+ ((float) maskAndPack(H_REVERSE_MASK, reverseIndex) / H_RANGE)
				* (H_END - H_BEGIN);
		float s = S_BEGIN
				+ ((float) maskAndPack(S_REVERSE_MASK, reverseIndex) / S_RANGE)
				* (S_END - H_BEGIN);
		float b = B_BEGIN
				+ ((float) maskAndPack(B_REVERSE_MASK, reverseIndex) / B_RANGE)
				* (B_END - H_BEGIN);
		return Color.getHSBColor(h, s, b);
	}

	public static void main(String[] args) {
		HSLColorGenerator gen = new HSLColorGenerator();
		for (int i = 0; i < 100; i++) {
			Color c = gen.getColor(i);
			System.out.printf("i=%d, r=0x%02x, g=0x%02x, b=0x%02x\n", i,
					c.getRed(), c.getGreen(), c.getBlue());
		}

		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i = 0; i < 300; i++) {
			JLabel l = new JLabel(String.format("%03d", i));
			Color c = gen.getColor(i);
			l.setBackground(c);
			l.setOpaque(true);
			l.setToolTipText(String.format("r=0x%02x, g=0x%02x, b=0x%02x",
					c.getRed(), c.getGreen(), c.getBlue()));
			l.setFont(new Font("monospaced", Font.PLAIN, 16));
			frame.add(l);
		}
		frame.setSize(640, 480);
		frame.setVisible(true);
		/*
		 * System.out.println(maskValueRange(H_MASK));
		 * 
		 * int range = maskValueRange(H_MASK); int reverseMask =
		 * revertBits(H_MASK); for (int i=0;i<300;i++){ int reverseValue =
		 * revertBits(i); int extracted = maskAndPack(reverseMask,
		 * reverseValue);
		 * System.out.printf("i=%d, extracted=%d, %.4f\n",i,extracted
		 * ,(double)extracted/range); }
		 */
	}
}
