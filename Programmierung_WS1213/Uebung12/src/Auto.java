public class Auto extends Fahrzeug implements Landfahrzeug {

	int sitzplaetze;

	public Auto(int sitzplaetze) {
		this.sitzplaetze = sitzplaetze;
	}

	public void printType() {
		System.out.println("Auto");
		System.out.println(this.sitzplaetze + " Sitze");
	}

	public void fahren() {
	}
}
