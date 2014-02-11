public class Moped extends Fahrzeug implements Landfahrzeug, Zweirad {

	public Moped() {
	}

	public void printType() {
		System.out.println("Moped");
	}

	public void fahren() {
	}

	public boolean hatBalance() {
		return true;
	}
}