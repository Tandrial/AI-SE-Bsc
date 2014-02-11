public class Uebung9_Aufgabe2 {
	public static void main(String[] args) {
		Uebung9_Aufgabe2 A2 = new Uebung9_Aufgabe2();
		Liste l1 = new Liste();
		Liste l2 = new Liste();

		l1.hinzufuegen(new Element(1));
		l1.hinzufuegen(new Element(4));
		l1.hinzufuegen(new Element(9));
		l1.hinzufuegen(new Element(10));
		l1.hinzufuegen(new Element(12));
		l1.hinzufuegen(new Element(15));

		System.out.print("\nListe 1:\n");
		l1.ausgabe();
		l2.hinzufuegen(new Element(11));
		l2.hinzufuegen(new Element(14));
		System.out.print("\nListe 2:\n");
		l2.ausgabe();
		A2.mergeList(l1, l2);
		System.out.print("\nListe 1 nach mergeList():\n");
		l1.ausgabe();
		System.out.print("\nWert von averageValue() mit Liste 1:\n");
		System.out.println(A2.averageValue(l1));
		A2.reverseList(l1);
		System.out.print("\nListe 1 nach reverseList():\n");
		l1.ausgabe();
	}

	// Aufgabe 2a
	// Da die Elemente beim Einfügen korrekt einsortiert werden kann man hier
	// einfach nur die Werte von Liste2 hinzufügen
	public void mergeList(Liste list1, Liste list2) {
		Element tmp = list2.getKopf().nachfolger;
		while (tmp != null) {
			list1.hinzufuegen(new Element(tmp.getValue()));
			tmp = tmp.nachfolger;
		}
	}

	// Aufgabe 2b
	public double averageValue(Liste list1) {
		double average = 0.0d;
		int count = 0;

		Element tmp = list1.getKopf().nachfolger;
		while (tmp != null) {
			count++;
			average += tmp.getValue();
			tmp = tmp.nachfolger;
		}

		if (count != 0)
			return average / count;
		else
			return 0.0d;
	}

	// Aufgabe 2c
	public void reverseList(Liste list1) {

		Element position = list1.getKopf().nachfolger;
		Element kopfReverseList = null;

		while (position != null) {

			// zwischenspeichern der Position in der "vorwärst" Liste
			Element tmp = position;
			// Weiter durch die "vorwärst" Liste gehen
			position = tmp.nachfolger;

			// der zwischen gespeicerten "alten" Position der "vorwärst" Liste
			// wird als Nachfolger der neue Kopf zugeweisen.
			// Bei ersten mal null, danach wir der Kopf vom Durchlauf davor an
			// die neue "rückwärst" Liste angehängt.
			tmp.nachfolger = kopfReverseList;
			// Speichern des neuen Kopfes der "rückwärst" Liste
			kopfReverseList = tmp;
		}
		// Laut Lösung soll sich die Position von [Kopf] nicht verändern, also
		// .nachfolger zuweisen
		list1.getKopf().nachfolger = kopfReverseList;
	}
}

class Liste {
	private Element kopf;

	public Liste() {
		kopf = new Element(-1);
		kopf.nachfolger = null;
	}

	public Element getKopf() {
		return kopf;
	}

	public Element setKopf(Element kopf) {
		return this.kopf = kopf;
	}

	// Aufgabe 2a
	public void hinzufuegen(Element neu) {
		if (getKopf() == null) {
			setKopf(neu);
		} else {
			Element tmp = getKopf();
			while (tmp.nachfolger != null
					&& tmp.nachfolger.getValue() < neu.getValue()) {
				tmp = tmp.nachfolger;
			}
			if (tmp.nachfolger != null)
				neu.nachfolger = tmp.nachfolger;
			tmp.nachfolger = neu;
		}

	}

	public void ausgabe() {
		Element temp = kopf;
		while (temp != null) {
			temp.ausgabe();
			temp = temp.nachfolger;
		}
		if (temp == null)
			System.out.print("null\n");
	}
}

class Element {
	private int value = 0;
	Element nachfolger;

	public Element(int value) {
		setValue(value);
	}

	public void setValue(int index) {
		value = index;
	}

	public int getValue() {
		return value;
	}

	public Element copyElement() {
		int value = this.getValue();
		Element theClone = new Element(value);
		return theClone;
	}

	public void ausgabe() {
		if (getValue() != -1)
			System.out.print("[" + getValue() + "]" + "-->");
		else
			System.out.print("[Kopf]" + "-->");
	}
}
