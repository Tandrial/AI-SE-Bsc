public class Bus extends Fahrzeug implements Landfahrzeug {

	int sitzplaetze;

	public Bus(int sitzplaetze) {
		this.sitzplaetze = sitzplaetze;
	}

	public void printType() {
		System.out.println("Bus");
		System.out.println(this.sitzplaetze + " Sitze");
	}

	public void fahren() {
	}

}