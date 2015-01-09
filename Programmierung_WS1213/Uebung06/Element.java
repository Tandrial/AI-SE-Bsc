class Element {
	int Zahl;
	Element Nf;

	public Element(int Zahl, Element Nf) {
		this.Zahl = Zahl;
		this.Nf = Nf;
	}

	public Element(int Zahl) {
		this(Zahl, null);
	}

	public void ausgabe() {
		System.out.println(Zahl);
	}
}