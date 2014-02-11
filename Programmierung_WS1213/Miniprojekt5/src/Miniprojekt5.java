public class Miniprojekt5 {
	public static void main(String[] args) {
		Graph gr = new Graph();
		Knoten a, b, c, d, e, f;
		a = new Knoten("A");
		gr.fuegeEin(a);
		b = new Knoten("B");
		gr.fuegeEin(b);
		c = new Knoten("C");
		gr.fuegeEin(c);
		d = new Knoten("D");
		gr.fuegeEin(d);
		e = new Knoten("E");
		gr.fuegeEin(e);
		f = new Knoten("F");
		gr.fuegeEin(f);

		a.fuegeEin(new Kante(1, b));
		b.fuegeEin(new Kante(1, c));
		b.fuegeEin(new Kante(9, c));
		c.fuegeEin(new Kante(2, a));
		c.fuegeEin(new Kante(3, b));
		c.fuegeEin(new Kante(4, d));
		d.fuegeEin(new Kante(5, e));
		e.fuegeEin(new Kante(5, d));

		System.out.println("Anzahl der Kanten: " + gr.anzahlKanten());
		System.out.println("Anzahl der Knoten: " + gr.anzahlKnoten());
		System.out.println("Anzahl der Ausgehendenkanten des Knotens: "
				+ gr.anzahlAusKanten(c));
		System.out.println("Anzahl der Hinfuehrendenkanten des Knotens: "
				+ gr.anzahlHinKanten(c));
		System.out
				.println("Differenz zwischen Aus- und HinfuehrendenKanten des Knotens: "
						+ gr.diff(c));
		System.out
				.println("Anzahl der Aus- und HinfuehrendenKanten des Knotens: "
						+ gr.anzahlKanten(c));
		System.out.println("gefunden?: " + gr.suche(a));
		System.out.println("Anzahl der Senken: " + gr.anzSenke());
		System.out.println("Existiert eine Kante? " + gr.exKante(a, b));
		System.out.println("Max Kantenzahl: " + gr.maxKantenZahl());
		System.out.println("Anzahl der Knoten mit max Kantenzahl: "
				+ gr.anzKnotenMaxKantenZahl());
		System.out.println("alone? :" + gr.alone(f));
		System.out.println("doppeltVerbunden? :" + gr.doppeltVerbunden(e, d));
		System.out.println("Quelle? :" + gr.quelle(d));
		
		System.out.println("chris? :" + gr.diffKantenZahl(a)+"  :" + gr.diffKantenZahl2(a));
		System.out.println("chris? :" + gr.diffKantenZahl(b)+"  :" + gr.diffKantenZahl2(b));
		System.out.println("chris? :" + gr.diffKantenZahl(c)+"  :" + gr.diffKantenZahl2(c));
		System.out.println("chris? :" + gr.diffKantenZahl(d)+"  :" + gr.diffKantenZahl2(d));
		System.out.println("chris? :" + gr.diffKantenZahl(e)+"  :" + gr.diffKantenZahl2(e));
		System.out.println("chris? :" + gr.diffKantenZahl(f)+"  :" + gr.diffKantenZahl2(f));
		System.out.println("chris? :" + gr.diffKantenZahl(null)+"  :" + gr.diffKantenZahl2(null));
		
	}
}