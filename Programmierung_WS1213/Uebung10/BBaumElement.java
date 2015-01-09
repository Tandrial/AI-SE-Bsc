public class BBaumElement {
	private BBaumElement kleiner, groesserGleich;
	private int id;

	public BBaumElement(BBaumElement kleiner, BBaumElement groesserGleich,
			int id) {
		super();
		this.kleiner = kleiner;
		this.groesserGleich = groesserGleich;
		this.id = id;
	}

	public BBaumElement(int id) {
		super();
		this.id = id;
	}

	public BBaumElement getKleiner() {
		return kleiner;
	}

	public void setKleiner(BBaumElement kleiner) {
		this.kleiner = kleiner;
	}

	public BBaumElement getGroesserGleich() {
		return groesserGleich;
	}

	public void setGroesserGleich(BBaumElement groesserGleich) {
		this.groesserGleich = groesserGleich;
	}

	public int getEintrag() {
		return id;
	}
}