package set1;

import java.util.Base64;

public class Challenge1 {

	public static byte[] hexToInt(byte[] bytes) {
		byte[] res = new byte[bytes.length / 2];
		for (int i = 0; i < bytes.length / 2; i++) {
			res[i] = (byte) (hexToDec(bytes[i * 2]) * 16 + hexToDec(bytes[i * 2 + 1]));
		}
		return res;
	}

	public static int hexToDec(byte c) {
		if ('0' <= c && '9' >= c) {
			return c - '0';
		} else {
			return c - 'a' + 10;
		}
	}

	public static String hexToBase64(String s) {
		return Base64.getEncoder().encodeToString(hexToInt(s.getBytes()));
	}

	public static void main(String[] args) {
		String in = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		String out = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
		String myOut = hexToBase64(in);
		System.out.println(
				String.format("in    : %s \nmyOut : %s \nout   : %s \nequals? %s", in, myOut, out, myOut.equals(out)));
	}
}
