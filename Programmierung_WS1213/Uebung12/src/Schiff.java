public class Schiff extends Fahrzeug implements Wasserfahrzeug {

	public Schiff() {
	}

	public void printType() {
		System.out.println("Schiff");
	}

	public void schwimmen() {
	}

	public boolean hatBalance() {
		return true;
	}
}