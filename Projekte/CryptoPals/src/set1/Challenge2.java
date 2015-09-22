package set1;

public class Challenge2 {

	private static String StringXor(String in, String key) {
		byte[] inBytes = Challenge1.hexToInt(in.getBytes());
		byte[] keyBytes = Challenge1.hexToInt(key.getBytes());

		byte[] res = new byte[inBytes.length];

		for (int i = 0; i < res.length; i++) {
			res[i] = (byte) (inBytes[i] ^ keyBytes[i]);
		}

		return toHexString(res);
	}

	public static String toHexString(byte[] ba) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < ba.length; i++)
			str.append(String.format("%02x", ba[i]));
		return str.toString();
	}

	public static void main(String[] args) {
		String in = "1c0111001f010100061a024b53535009181c";
		String key = "686974207468652062756c6c277320657965";
		String out = "746865206b696420646f6e277420706c6179";
		String myOut = StringXor(in, key);
		System.out.println(
				String.format("in    : %s \nmyOut : %s \nout   : %s \nequals? %s", in, myOut, out, myOut.equals(out)));
	}
}
