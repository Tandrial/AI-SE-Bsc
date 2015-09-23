package set1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Challenge6 {

	public static final int minKEYSIZE = 2;
	public static final int maxKEYSIZE = 10;

	private static int hammingDistance(String a, String b) {
		if (a.length() != b.length())
			return -1;
		int res = 0;
		byte[] bytesA = a.getBytes();
		byte[] bytesB = b.getBytes();

		for (int i = 0; i < a.length(); i++) {
			res += Integer.bitCount(bytesA[i] ^ bytesB[i]);
		}
		return res;
	}

	private static List<String> splitIntoLines(String a, int n) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < n; i++) {
			String res = "";
			for (int pos = i; pos < a.length(); pos += n) {
				res += a.charAt(pos);
			}
			result.add(res);
		}
		FileUtils
		
		return result;
	}

	private static void findPossibleKeysize(String a) {
		double minDist = Double.MAX_VALUE;
		int size = -1;
		System.out.println(a);
		for (int keySize = minKEYSIZE; keySize <= maxKEYSIZE; keySize++) {
			String s1 = a.substring(0, keySize * 2);
			String s2 = a.substring(keySize * 2, 4 * keySize);

			System.out.println(s1 + " " + s2);
			double ham = hammingDistance(s1, s2) / (double) keySize;
			if (ham < minDist) {
				size = keySize;
				minDist = ham;
				System.out.println("size = " + keySize + " ==> minDist " + ham);
			}
		}

	}

	public static void main(String[] args) {
		String in = "this is a test";
		String in2 = "wokka wokka!!!";
		int out = 37;
		int myOut = hammingDistance(in, in2);

		System.out.println(String.format("in : %s \nmyOut : %d \nout : %d \nequals? %s", in + ";" + in2, myOut, out,
				myOut == out));

		in = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272";
		findPossibleKeysize(in);

		// StringBuilder sb = new StringBuilder();
		// try (BufferedReader br = new BufferedReader(new FileReader("6.txt")))
		// {
		// String line;
		// while ((line = br.readLine()) != null) {
		// sb.append(line);
		// sb.append("\n");
		// }
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// String text = sb.toString();

	}
}
