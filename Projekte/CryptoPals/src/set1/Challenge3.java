package set1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class EnglishLangSort implements Comparator<String> {

	private static int getScore(String s) {
		int score = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'z')
				score++;
			else 
				score--;
		}
		return score;
	}

	@Override
	public int compare(String a, String b) {
		int scoreA = getScore(a);
		int scoreB = getScore(b);

		if (scoreA == scoreB)
			return 0;
		else if (scoreA > scoreB)
			return -1;
		else
			return 1;
	}
}

public class Challenge3 {

	private static String xorWithSingleByte(String in, char x) {
		byte[] res = Challenge1.hexToInt(in.getBytes());

		for (int i = 0; i < res.length; i++) {
			res[i] ^= x;
		}

		return new String(res);
	}

	static List<String> genPossibleDecrypts(String s) {
		List<String> results = new ArrayList<>();
		for (int i = 0; i <= 0xFF; i++) {
			String res = xorWithSingleByte(s, (char) i);
			if (res.length() == s.length() / 2)
				results.add(res);
		}
		return results;
	}

	public static void main(String[] args) {
		String in = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
		List<String> results = genPossibleDecrypts(in);

		Collections.sort(results, new EnglishLangSort());
		System.out.println(String.format("in      : %s \ndecrypt : %s", in, results.get(0)));
	}
}
