package set1;

import java.util.Base64;

public class Challenge1 {
	public static String hexToBase64(String s) {
		return Base64.getEncoder().encodeToString(s.getBytes());
	}

	public static void main(String[] args) {
		String in = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		String out = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
		String myOut = hexToBase64(in);
		System.out.println(
				String.format("in    : %s \nmyOut : %s \nout   : %s \nequal? %s", in, myOut, out, myOut.equals(out)));
	}

}
