package set1;

public class Challenge5 {

	private static String cyclicXOR(String in, String key) {
		byte[] res = in.getBytes();
		byte[] keyArr = key.getBytes();
		
		for (int i = 0; i < res.length; i++) {
			res[i] ^= keyArr[i % keyArr.length];
		}
		return Challenge2.toHexString(res);
	}

	public static void main(String[] args) {
		String in = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
		String key = "ICE";
		String out = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
		String myOut = cyclicXOR(in, key);

		System.out.println(
				String.format("in    : %s \nmyOut : %s \nout   : %s \nequals? %s", in, myOut, out, myOut.equals(out)));
	}
}
