package Graph;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.io.Serializable;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import allgemein.Konstanten;

public class Kante implements Serializable, Comparable<Kante> {
	private static final long serialVersionUID = 3225424268243577145L;
	// StartKnoten
	private final Knoten from;
	// EndKnoten
	private final Knoten to;
	// Abstand der Start und Endknoten in Metern
	private float abstand;

	private Point2D.Float midPoint;

	private String typ;

	private boolean gesperrt;

	private boolean erreichbar;

	private float plastikMenge = 0f;
	private float restMenge = 0f;
	private float papierMenge = 0f;
	private float bioMenge = 0f;

	private float muellmenge = 0f;

	public int id;

	public Kante(Knoten from, Knoten to, boolean gesperrt, String typ) {
		this.from = from;
		this.to = to;
		this.abstand = Kante.CalcDistance(from, to);
		this.gesperrt = gesperrt;
		this.typ = typ;
		this.calcMidPoint();
		this.calcMuellmenge();
	}

	// TODO Methode verteileMuell, die für jede Muellart den float Wert auf den
	// Wert dert Variable muellmenge setzt

	public void calcMuellmenge() {
		this.muellmenge = Konstanten.getMengeTyp(this.typ) * this.abstand;
		this.plastikMenge = muellmenge;
		this.restMenge = muellmenge;
		this.bioMenge = muellmenge;
		this.papierMenge = muellmenge;
	}

	public void addMuell(float muell) {
		this.muellmenge += muell;
		this.bioMenge += muell;
		this.papierMenge += muell;
		this.plastikMenge += muell;
		this.restMenge += muell;
	}

	/**
	 * Liefert die Menge fuer eine bestimmte Muellart (bei einem leeren String
	 * wird das Attribut muellMenge zurueckgeliefert)
	 * 
	 * @param muellArt
	 * @return die Menge einer bestimmten Muellart
	 */
	public float getMuellMengeArt(String muellArt) {
		if (muellArt.equals(allgemein.Konstanten.MUELLART_PLASTIK)) {
			return plastikMenge;
		} else if (muellArt.equals(allgemein.Konstanten.MUELLART_REST)) {
			return restMenge;
		} else if (muellArt.equals(allgemein.Konstanten.MUELLART_PAPIER)) {
			return papierMenge;
		} else if (muellArt.equals(allgemein.Konstanten.MUELLART_BIO)) {
			return bioMenge;
		} else {
			return this.muellmenge;
		}
	}

	public void setMuellMengeArt(float muellMenge, String muellArt) {
		if (muellArt.equals(allgemein.Konstanten.MUELLART_PLASTIK)) {
			this.plastikMenge = muellMenge;
		} else if (muellArt.equals(allgemein.Konstanten.MUELLART_REST)) {
			this.restMenge = muellMenge;
		} else if (muellArt.equals(allgemein.Konstanten.MUELLART_PAPIER)) {
			this.papierMenge = muellMenge;
		} else if (muellArt.equals(allgemein.Konstanten.MUELLART_BIO)) {
			this.bioMenge = muellMenge;
		} else {
			this.muellmenge = muellMenge;
		}
	}

	public void setMuellMengeAll(float muellMenge) {
		this.muellmenge = muellMenge;
		this.bioMenge = muellMenge;
		this.papierMenge = muellMenge;
		this.plastikMenge = muellMenge;
		this.restMenge = muellMenge;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getTyp() {
		return this.typ;
	}

	public void setGeperrt(boolean gesperrt) {
		this.gesperrt = gesperrt;
	}

	public boolean getGesperrt() {
		return this.gesperrt;
	}

	public void setErreichbar(boolean erreichbar) {
		this.erreichbar = erreichbar;
	}

	public boolean getErreichbar() {
		return this.erreichbar;
	}

	public Knoten getTo() {
		return this.to;
	}

	public Knoten getFrom() {
		return this.from;
	}

	public float getAbstand() {
		return this.abstand;
	}

	public void setAbstand(float abstand) {
		this.abstand = abstand;
	}

	public Point2D.Float getMidPoint() {
		return this.midPoint;
	}

	public void calcMidPoint() {
		this.midPoint = new Point2D.Float((this.getTo().getLat() + this
				.getFrom().getLat()) / 2, (this.getTo().getLon() + this
				.getFrom().getLon()) / 2);
	}

	public Knoten getOther(Knoten knoten) {
		if (knoten == from)
			return to;
		else if (knoten == to)
			return from;
		else
			return null;
	}

	static private float CalcDistance(Knoten from, Knoten to) {
		final int R = 6371; // Radius of the earth
		float lat1 = from.getLat();
		float lon1 = from.getLon();
		float lat2 = to.getLat();
		float lon2 = to.getLon();
		Double latDistance = toRad(lat2 - lat1);
		Double lonDistance = toRad(lon2 - lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(toRad(lat1)) * Math.cos(toRad(lat2))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float abstand = (float) (R * c) * 1000;
		return abstand;
	}

	private static Double toRad(float value) {
		return value * Math.PI / 180;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		// result.append("[[abstand=" + this.abstand + "] [typ=" + this.typ
		// + "] [erreichbar=" + this.erreichbar + "] [zu=" + this.gesperrt
		// + "]]\n");

		result.append("  " + to + "\n");
		result.append("  " + from + "\n");

		return result.toString();
	}

	@Override
	public int compareTo(Kante k) {
		if (this.getAbstand() == k.getAbstand())
			return 0;
		else if (this.getAbstand() > k.getAbstand())
			return 1;
		else
			return -1;
	}
}

// TODO vor Release entfernen, wenn nicht benoetigt
// public void writeExternal(ObjectOutput out) {
// try {
// System.out.println("Coming into the writeExternal of Kante :");
// out.writeFloat(abstand);
// out.writeObject(typ);
// out.writeBoolean(gesperrt);
// out.writeBoolean(erreichbar);
// out.writeBoolean(besucht);
// out.writeFloat(plastikMenge);
// out.writeFloat(restMenge);
// out.writeFloat(papierMenge);
// out.writeFloat(bioMenge);
// out.writeFloat(muellmenge);
// out.writeInt(id);
//
// // out.writeObject(innerObject);
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
//
// public void readExternal(ObjectInput in) {
// try {
// System.out.println("Coming into the readExternal of Kante:");
//
// min = (Point2D.Float) in.readObject();
// max = (Point2D.Float) in.readObject();
//
// // knoten = (ArrayList<Knoten>) in.readObject();
// int knotenAnzahl = in.readInt();
// knoten.clear();
// for (int i = 0; i < knotenAnzahl; i++) {
// knoten.add((Knoten) in.readObject());
// }
//
// plastik = (Knoten) in.readObject();
// papier = (Knoten) in.readObject();
// rest = (Knoten) in.readObject();
// bio = (Knoten) in.readObject();
// depot = (Knoten) in.readObject();
//
// // innerObject=(Obj)in.readObject();
// } catch (Exception e) {
// e.printStackTrace();
// }
// }