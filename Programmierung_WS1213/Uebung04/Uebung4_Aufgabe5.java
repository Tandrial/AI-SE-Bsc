
public class Uebung4_Aufgabe5 {

	public static void main(String[] args) {
		int start = 2;
		int ende = 100000;		
		int count = 0;

		Boolean[] gestrichen = new Boolean[ende];
		java.util.Arrays.fill(gestrichen, Boolean.FALSE);

		long startZeit = System.nanoTime();

		for (int i = start; i < ende; i++) {
			if (!gestrichen[i]) {
				count++;
				for (int j = i * 2; j < ende; j += i) {
					gestrichen[j] = true;
				}
			}
		}

		long stop = System.nanoTime();
		System.out.println(count + " Primzahlen gefunden. Dauer: "
				+ (stop - startZeit) / Math.pow(10, 9) + " s");
	}
}
