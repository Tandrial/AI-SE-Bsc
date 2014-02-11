class Liste {
	Element Kopf, Fuss;

	public void einfuegen(int zahl) {
		Element neu = new Element(zahl);
		if (Kopf == null) {
			Kopf = Fuss = neu;
		} else {
			Fuss.Nf = neu;
			Fuss = Fuss.Nf;
		}
	}

}
