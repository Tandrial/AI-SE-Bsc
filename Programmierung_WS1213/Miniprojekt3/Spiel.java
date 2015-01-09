class Spiel {
	Spiel nf;
	int heim;
	int gast;
	int toreH;
	int toreG;

	public Spiel() {
		heim = -1;
		gast = -1;
		toreH = -1;
		toreG = -1;
		nf = null;
	}

	public void setzeSpiel(Spiel spiel) {
		this.nf = spiel;
	}

	public void setzeSpielParameter(int heimTore, int gastTore,
			int mannschaftH, int mannschaftG) {
		this.gast = mannschaftG;
		this.heim = mannschaftH;
		this.toreG = gastTore;
		this.toreH = heimTore;
	}

	public int getHeim() {
		return this.heim;
	}

	public int getGast() {
		return this.gast;
	}

	public int getToreH() {
		return this.toreH;
	}

	public int getToreG() {
		return this.toreG;
	}

	// Mit dieser Methode kann man auf das nächste Spiel in der Liste zugreifen
	public Spiel naechstesSpielElement() {
		return this.nf;
	}

}