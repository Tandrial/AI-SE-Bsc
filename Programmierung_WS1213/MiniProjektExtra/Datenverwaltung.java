public abstract class Datenverwaltung {

	public abstract void einfuegen(Beschreibung eintrag);

	public abstract void loeschen(int id);

	public abstract DatenListe getAll(int id);

	public abstract int anzahlEintraege();

	public abstract void allesLoeschen();

}