public class Motorrad extends Fahrzeug implements Landfahrzeug, Zweirad {

	boolean mitBeiwagen;

	public Motorrad(boolean mitBeiwagen) {
		this.mitBeiwagen = mitBeiwagen;
	}

	public void printType() {
		System.out.println("Motorrad");
		System.out.println("Hat einen Beiwagen" + this.mitBeiwagen);
	}

	public void fahren() {
	}

	public boolean hatBalance() {
		return true;
	}
}
