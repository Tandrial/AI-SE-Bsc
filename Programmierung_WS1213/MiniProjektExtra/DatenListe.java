public class DatenListe extends Datenverwaltung {
	private ListenElement anfang, ende;

	public ListenElement getAnfang() {
		return anfang;
	}

	public ListenElement getEnde() {
		return ende;
	}

	public void einfuegen(Beschreibung eintrag) {
		anfang = fuegeEinRek(eintrag, anfang, null);
	}

	private ListenElement fuegeEinRek(Beschreibung eintrag,
			ListenElement jetzigesElement, ListenElement vorgaenger) {
		if (jetzigesElement == null) {
			ende = new ListenElement(vorgaenger, null, eintrag);
			return ende;
		} else if (jetzigesElement.getEintrag().getProduktId() > eintrag
				.getProduktId()) {
			ListenElement tmp = new ListenElement(vorgaenger, jetzigesElement,
					eintrag);
			jetzigesElement.setVorgaenger(tmp);
			return tmp;
		} else {
			jetzigesElement.setNachfolger(fuegeEinRek(eintrag,
					jetzigesElement.getNachfolger(), jetzigesElement));
			return jetzigesElement;
		}
	}

	// Aufgabe 1
	public int anzahlEintraege() {
		int count = 0;
		ListenElement laeufer = anfang;

		while (laeufer != null) {
			count++;
			laeufer = laeufer.getNachfolger();
		}
		return count;
	}

	// Aufgabe 2
	public void loeschen(int id) {
		if (anfang == null || ende == null)
			return;
		if (anfang.getEintrag().getProduktId() == id) {
			if (anzahlEintraege() == 1) {
				allesLoeschen();
				return;
			} else {
				this.anfang = anfang.getNachfolger();
				this.anfang.setVorgaenger(null);
				loeschen(id);
			}
		} else if (ende.getEintrag().getProduktId() == id) {
			this.ende = ende.getVorgaenger();
			this.ende.setNachfolger(null);
			loeschen(id);
		} else {
			ListenElement tmp = anfang;
			while (tmp != null) {
				if (tmp.getEintrag().getProduktId() == id) {

					if (tmp.getNachfolger() != null)
						tmp.getNachfolger().setVorgaenger(tmp.getVorgaenger());
					if (tmp.getVorgaenger() != null)
						tmp.getVorgaenger().setNachfolger(tmp.getNachfolger());				
				}
				tmp = tmp.getNachfolger();
			}
		}
	}	

	public void allesLoeschen() {
		anfang = ende = null;
	}

	public DatenListe getAll(int id) {
		DatenListe tmp = new DatenListe();
		ListenElement laeufer = anfang;
		while (laeufer != null && laeufer.getEintrag().getProduktId() <= id) {
			if (laeufer.getEintrag().getProduktId() == id)
				tmp.einfuegen(laeufer.getEintrag());
			laeufer = laeufer.getNachfolger();
		}
		return tmp;
	}
}