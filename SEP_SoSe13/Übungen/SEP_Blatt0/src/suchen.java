public class suchen {

	public boolean suche(String s1, String s2) {
		int pos = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) == s2.charAt(pos))
				pos++;
			else
				pos = 0;
			if (pos == s2.length()) {
				return true;
			}
		}
		return false;
	}

	public static void main(String args[]) {

		suchen a = new suchen();

		boolean s1 = a.suche("Das Leben ist schoen", "ist");
		boolean s2 = a.suche("Das Leben ist schoen", "Leben");
		boolean s3 = a.suche("Das Leben ist schoen", "leben");

		System.out.println("Der String lautet: Das Leben ist schoen");
		System.out
				.println("Suche: ist    Ergebnis:" + s1 + " richtig ist true");
		System.out
				.println("Suche: Leben  Ergebnis:" + s2 + " richtig ist true");
		System.out.println("Suche: leben  Ergebnis:" + s3
				+ " richtig ist false");
	}
}