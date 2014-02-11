public class Uebung9_Aufgabe1 {
	private Knoten wurzel;

	public static void main(String[] args) {
		Uebung9_Aufgabe1 s = new Uebung9_Aufgabe1();
		int value[] = { 15, 10, 7, 20, 17, 21, 22, 18 };
		s.sortiereEin(value);
		s.traversePreOrder(s.wurzel);

		System.out.print("Knoten mit einem Nachfolger:\n");
		s.einNachfolger();
	}

	public void traversePreOrder(Knoten wurzel) {
		System.out.println(wurzel.getValue() + " ");
		if (wurzel.links != null)
			traversePreOrder(wurzel.links);
		if (wurzel.rechts != null)
			traversePreOrder(wurzel.rechts);
	}

	public void setRoot(Knoten k) {
		wurzel = k;
	}

	public Knoten getRoot() {
		return wurzel;
	}

	// Aufgabe 1a	
	public void sortiereEin(int[] value) {
		for (int i = 0; i < value.length; i++) {
			this.setRoot(sortiereEin(value[i], this.getRoot()));
		}
	}

	public Knoten sortiereEin(int wert, Knoten k) {
		if (k == null)
			return new Knoten(wert);
		else if (wert < k.getValue()) {
			k.links = sortiereEin(wert, k.links);
			return k;
		} else {
			k.rechts = sortiereEin(wert, k.rechts);
			return k;
		}
	}

	// Aufgabe 1b
	public void einNachfolger() {
		einNachfolger(this.getRoot());
	}

	public void einNachfolger(Knoten k) {
		//^ bedeutet XOR
		if (k.links != null ^ k.rechts != null)
			System.out.println(k.getValue());
		if (k.links != null)
			einNachfolger(k.links);
		if (k.rechts != null)
			einNachfolger(k.rechts);
	}
}

class Knoten {
	private int value;
	Knoten links, rechts;

	public Knoten(int value) {
		this.setValue(value);
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}