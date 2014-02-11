public class Uebung8_Aufgabe2 {

	private int[] feld; // feld-Array
	private int links; // linke Grenze des Suchbereichs
	private int rechts; // rechte Grenze des Suchbereichs
	private int mitte; // Mitte des Suchbereichs
	private int gesucht; // gesuchte Element

	public int binaereSuche(int n, int[] feld) {
		this.links = 0;
		this.rechts = feld.length - 1;
		this.gesucht = n;
		this.feld = feld;
		return binaerSucheRekusiv();
	}

	private int binaerSucheRekusiv() {
		if (this.links > this.rechts) {
			return -1;
		}
		this.mitte = this.links + ((this.rechts - this.links) / 2);

		if (this.gesucht > this.feld[this.mitte]) {
			this.links = mitte + 1;
			return binaerSucheRekusiv();
		}

		if (this.gesucht < this.feld[this.mitte]) {
			this.rechts = this.mitte - 1;
			return binaerSucheRekusiv();
		}

		return mitte;
	}

	private int binaerSucheRekursiv(int links, int rechts, int gesucht,
			int[] feld) {
		if (links > rechts) {
			return -1;
		}
		int mitte = links + ((rechts - links) / 2);

		if (gesucht > feld[mitte]) {
			return binaerSucheRekursiv(mitte + 1, rechts, gesucht, feld);
		}

		if (gesucht < feld[mitte]) {
			return binaerSucheRekursiv(links, mitte - 1, gesucht, feld);
		}

		return mitte;
	}

	public void sortiere(int[] feld) {
		for (int i = 0; i < feld.length; i++) {
			for (int j = i; j < feld.length; j++) {
				if (feld[j] < feld[i]) {
					vertausche(i, j, feld);
				}
			}
		}
	}

	public void ausgabe(int[] feld) {
		for (int i = 0; i < feld.length; i++) {
			System.out.print(feld[i] + " ");
		}
		System.out.println();
	}

	public void vertausche(int a, int b, int[] feld) {
		int temp = feld[a];
		feld[a] = feld[b];
		feld[b] = temp;
	}

	public static void main(String[] args) {
		int[] feld = { 1, 14, 22, 3, 34, 55, 8, 9 };
		Uebung8_Aufgabe2 s = new Uebung8_Aufgabe2();
		System.out.println("Unsortiertes Array:");
		s.ausgabe(feld);
		System.out.println("Sortiertes Array:");
		s.sortiere(feld);
		s.ausgabe(feld);
		System.out.println("Position:" + (s.binaereSuche(14, feld)));
		System.out.println("Position:"
				+ (s.binaerSucheRekursiv(0, feld.length - 1, 14, feld)));
	}
}