package Graph;

import org.openstreetmap.gui.jmapviewer.Style;

public class Routenabschnitt extends Kante {

	private static final long serialVersionUID = -8670866372856126799L;
	private boolean aktiv;
	
	private Style stil; /* Farbe für die Kartendarstellung, nur für die SlippyMap *JL */
	private String stilname; /* Analog */

	// Konstruktor(en)
	public Routenabschnitt(Knoten from, Knoten to, boolean aktiv) {
		super(from, to, false, "");
		setAktiv(aktiv);
	}

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	/**
	 * @return Stil des Abschnitts. Wird für die Darstellung benötigt. * JL
	 */
	public Style getStil() {
		return stil;
	}

	/**
	 * @param Stil des Abschnitts. Wird für die Darstellung benötigt. * JL
	 */
	public void setStil(Style stil) {
		this.stil = stil;
	}

	/**
	 * @return the stilname
	 */
	public String getStilname() {
		return stilname;
	}

	/**
	 * @param stilname the stilname to set
	 */
	public void setStilname(String stilname) {
		this.stilname = stilname;
	}
	
}
