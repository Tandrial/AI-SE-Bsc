public class zaehlen2 {

	public void zaehleZeichen(String s) {
		Pair[] result = new Pair[s.length()];

		int pos = 0;
		result[pos] = new Pair(s.charAt(pos++));
		outer: for (int i = 1; i < s.length(); i++) {
			char c = s.charAt(i);
			for (int j = 0; j < pos; j++) {
				if (result[j].c == c) {
					result[j].count++;
					continue outer;
				}
			}
			result[pos++] = new Pair(c);
		}
		System.out.println("Der String \"" + s
				+ "\" enthaelt die folgenden Zeichen:");
		for (int i = 0; i < pos; i++) {
			System.out.print(result[i].print());
		}
	}

	public static void main(String args[]) {

		zaehlen2 a = new zaehlen2();
		System.out.println("Erwartete Ausgabe:");
		System.out
				.println("Der String \"Das Leben ist schoen!\" enthaelt die folgenden Zeichen:\n\'D\': 1 \'a\': 1 \'s\': 3 \' \': 3 \'L\': 1 \'e\': 3 \'b\': 1 \'n\': 2 \'i\': 1 \'t\': 1 \'c\': 1 \'h\': 1 \'o\': 1 \'!\': 1\nDer String \"Programmierung\" enthaelt die folgenden Zeichen:\n\'P\': 1 \'r\': 3 \'o\': 1 \'g\': 2 \'a\': 1 \'m\': 2 \'i\': 1 \'e\': 1 \'u\': 1 \'n\': 1");
		System.out.println();
		System.out.println("Ihre Ausgabe:");
		a.zaehleZeichen("Das Leben ist schoen!");
		System.out.println();
		a.zaehleZeichen("Programmierung");
	}
}

class Pair {
	char c;
	int count = 1;

	Pair(char c) {
		this.c = c;
	}

	public String print() {
		return String.format("'%c': %d ", this.c, this.count);
	}
}