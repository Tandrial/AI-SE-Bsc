package fahrzeugverwaltung;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import allgemein.Konstanten;

import Graph.Kante;
import Graph.Routenabschnitt;

public class Fahrzeug implements Serializable {

	private static final long serialVersionUID = -5427488102415540047L;

	private int nr; // Fahrzeugnr (dient als eine Art FahrzeugID und ist daher EINDEUTIG)

	private String fahrzeugArt;
	private String muellArt;
	private String team;
	private boolean verfuegbar;
	private int cap; // Fassungsvermoegen des Fahrzeugs

	public ArrayList<MapPolygon> manuellAdded = new ArrayList<MapPolygon>(); 
	public ArrayList<Integer> manuellAddedWochentagMapping = new ArrayList<Integer>();
	public ArrayList<Kante> manuellDeleted = new ArrayList<Kante>();
	public ArrayList<Integer> manuellDeletedWochentagMapping = new ArrayList<Integer>();
	
	// TODO Zuweisung der neuen Attribute in Konstruktoren
	private int dailyCap; // Maximale Muellmenge, die das Fahrzeug an einem Tag einsammeln kann
	private float aktLadung; // aktuelle Beladung des Fahrzeugs
	private float tagLadung; // Muellmenge, die das Fahrzeug bisher am aktuellen Tag eingesammelt hat
	private LinkedList<Routenabschnitt>[] routen = new LinkedList[5];
	
	// Konstruktoren
	/**
	 * Erzeugt ein Fahrzeug mit den uebergebenen Eigenschaften
	 * 
	 * @param fahrzeugArt
	 *            - String, der die Art des Fahrzeug beschreibt
	 * @param muellArt
	 *            - String, der die Art von Muell beschreibt, den das Fahrzeug entsorgen kann
	 * @param team
	 *            - String, der die Namen der Teammitglieder des Fahrzeugs repraesentiert
	 * @param verfuegbar
	 *            - Wahrheitswert, der angibt, ob das Fahrzeug fuer die Routenberechnung verfuegbar ist oder nicht
	 */
	public Fahrzeug(String fahrzeugArt, String muellArt, String team, boolean verfuegbar) {
		FahrzeugVerwaltung fahrzeugVerwaltung = FahrzeugVerwaltung.getInstance();
		fahrzeugVerwaltung.setFahrzeugCounter(fahrzeugVerwaltung.getFahrzeugCounter() + 1);
		setNr(fahrzeugVerwaltung.getFahrzeugCounter());
		setFahrzeugArt(fahrzeugArt);
		setMuellArt(muellArt);
		setTeam(team);
		setVerfuegbar(verfuegbar);
		setAktLadung(0);
		for (int i = 0; i < routen.length; i++)
			routen[i] = new LinkedList<Routenabschnitt>();
	}

	/**
	 * Dieser Konstruktor dient AUSSCHLIESSLICH dem Import von Fahrzeugen aus der Textdatei (daher ist die Sichtbarkeit auch package private)
	 */
	Fahrzeug(int nr, String fahrzeugArt, String muellArt, String team, boolean verfuegbar) {
		setNr(nr);
		setFahrzeugArt(fahrzeugArt);
		setMuellArt(muellArt);
		setTeam(team);
		setVerfuegbar(verfuegbar);
	}

	// Getter und Setter
	public String getFahrzeugArt() {
		return fahrzeugArt;
	}

	/**
	 * Setzt das Attribut fahrzeugArt des auf den uebergebenen Wert und aendert entsprechend die Kapazitaten des Fahrzeugs.
	 * @param fahrzeugArt gewuenschte Fahrzeugart
	 * @see allgemein.Konstanten
	 */
	public void setFahrzeugArt(String fahrzeugArt) {
		if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_SMALL)) {
			this.fahrzeugArt = fahrzeugArt;
		} else if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_MEDIUM)) {
			this.fahrzeugArt = fahrzeugArt;
		} else if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_LARGE)) {
			this.fahrzeugArt = fahrzeugArt;
		}
		// Kapazitaeten des Fahrzeugs entsprechend anpassen
		setCap();
		setDailyCap();
	}

	public String getMuellArt() {
		return muellArt;
	}
	
	public int getMuellArtNr() { /* JL: Kodierte Rückgabe der MUELLART */
		
		if (this.muellArt.equals(Konstanten.MUELLART_ALLE))
			return 0;
		
		if (this.muellArt.equals(Konstanten.MUELLART_PLASTIK))
			return 1;
		
		if (this.muellArt.equals(Konstanten.MUELLART_REST))
			return 2;
		
		if (this.muellArt.equals(Konstanten.MUELLART_PAPIER))
			return 3;
		
		if (this.muellArt.equals(Konstanten.MUELLART_BIO))
			return 4;
		
		return (Integer) null;
	}

	public void setMuellArt(String muellArt) {
		if (muellArt.equals(allgemein.Konstanten.MUELLART_PLASTIK) || muellArt.equals(allgemein.Konstanten.MUELLART_REST)
				|| muellArt.equals(allgemein.Konstanten.MUELLART_PAPIER) || muellArt.equals(allgemein.Konstanten.MUELLART_BIO)) {
			this.muellArt = muellArt;
		}
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public boolean isVerfuegbar() {
		return verfuegbar;
	}

	public void setVerfuegbar(boolean verfuegbar) {
		this.verfuegbar = verfuegbar;
	}

	public int getNr() {
		return nr;
	}

	private void setNr(int nr) {
		this.nr = nr;
	}

	public int getCap() {
		return cap;
	}

	/**
	 * Setzt das Attribut cap des Fahrzeugs je nach Fahrzeugart auf den entsprechenden Wert
	 * @see allgemein.Konstanten
	 */
	private void setCap() {
		String fahrzeugArt = getFahrzeugArt();
		if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_LARGE)) {
			this.cap = allgemein.Konstanten.FAHRZEUG_CAP_LARGE;
		} else if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_MEDIUM)) {
			this.cap = allgemein.Konstanten.FAHRZEUG_CAP_MEDIUM;
		} else if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_SMALL)) {
			this.cap = allgemein.Konstanten.FAHRZEUG_CAP_SMALL;
		}
	}

	public int getDailyCap() {
		return dailyCap;
	}

	/**
	 * Setzt das Attribut dailyCap des Fahrzeugs je nach Fahrzeugart auf den entsprechenden Wert
	 * @see allgemein.Konstanten
	 */
	private void setDailyCap() {
		String fahrzeugArt = getFahrzeugArt();
		if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_LARGE)) {
			this.dailyCap = allgemein.Konstanten.maxMuellProTag_LARGE;
		} else if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_MEDIUM)) {
			this.dailyCap = allgemein.Konstanten.maxMuellProTag_MEDIUM;
		} else if (fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_SMALL)) {
			this.dailyCap = allgemein.Konstanten.maxMuellProTag_SMALL;
		}
	}

	public float getAktLadung() {
		return aktLadung;
	}

	public void setAktLadung(float aktLadung) {
		this.aktLadung = aktLadung;
	}

	public float getTagLadung() {
		return tagLadung;
	}

	public void setTagLadung(float tagLadung) {
		this.tagLadung = tagLadung;
	}

	public LinkedList<Routenabschnitt>[] getRouten() {
		return this.routen;
	}

	public void setRouten(LinkedList<Routenabschnitt>[] routen) {
		this.routen = routen;
	}

	public LinkedList<Routenabschnitt> getRouteTag(int tag) {
		return this.routen[tag];
	}

	public void setRouteTag(LinkedList<Routenabschnitt> route, int tag) {
		this.routen[tag] = route;
	}

}
