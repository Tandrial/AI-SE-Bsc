public class Uebung12_Tests {

	public static void main(String[] args) {
		Fahrzeug[] test = new Fahrzeug[5];
		test[0] = new Auto(4);
		test[1] = new Motorrad(true);
		test[2] = new Bus(30);
		test[3] = new Schiff();
		test[4] = new Moped();
		FahrzeugListe liste = new FahrzeugListe();

		for (int i = 0; i < test.length; i++) {
			test[i].printType();

			if (test[i] instanceof Landfahrzeug) {
				if (test[i] instanceof Zweirad) {
					System.out.println("Hat 2 Räder");
				} else {
					System.out.println("Hat 4 Räder");
				}
			} else if (test[i] instanceof Wasserfahrzeug) {
				System.out.println("Ist ein Wasserfahrzeug");
			}
			liste.fuegeEin(test[i]);
		}
		liste.ausgabe();
	}
}
