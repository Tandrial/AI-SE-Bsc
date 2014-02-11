public class Uebung5_Aufgabe3 {

	public static void main(String[] args) {
		for (int i = 20; i < 99; i++) {
			System.out.println(toString(i));
		}
	}

	static String[] einer = new String[] { "", "ein", "zwei", "drei", "vier",
			"fünf", "sechs", "sieben", "acht", "neun" };
	static String[] zehner = new String[] { "", "", "zwanzig", "dreißig",
			"vierzig", "fünfzig", "sechzig", "siebzig", "achzig", "neunzig" };

	static String toString(int zahl) {
		int zehnerStelle = zahl / 10;
		int einerStelle = zahl - zehnerStelle * 10;
		if (einerStelle == 0)
			return zehner[zehnerStelle];
		else
			return einer[einerStelle] + "und" + zehner[zehnerStelle];
	}

}
