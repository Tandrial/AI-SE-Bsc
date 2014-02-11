package fahrzeugverwaltung;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Diese Klasse enthaelt eine ArrayList, die verwendet wird, um den Fahrpark aus Muellwagen zu verwalten.<br>
 * Da wir nur einen einzigen Fahrzeugbestand haben, verwendet diese Klasse das Singleton-Pattern, sodass stets nur eine einzige Instanz existiert.
 * 
 */
public class FahrzeugVerwaltung implements Serializable {

	private static final long serialVersionUID = -7686290878878919980L;
	private static FahrzeugVerwaltung instance = loadFromFile();
	private ArrayList<Fahrzeug> liste = new ArrayList<Fahrzeug>(); // In dieser ArrayList werden alle existierenden Fahrzeuge gehalten

	private int fahrzeugCounter = 0; // stellt die aktuell hoechste fahrzeugNr dar
	// private static final String TXT_FILE = "FahrzeugListe.txt"; // Dateiname fuer die Textdatei, in der die Fahrzeuge persistent gespeichert werden
	// private static final String TXT_DELIM = "-----"; // Trennzeichen, das am Ende von jedem Datensatz in der Textdatei stehen MUSS
	private static final String SER_FILENAME = "FahrzeugListe.ser"; // Dateiname fuer die .ser-Datei, in der die FahrzeugVerwaltung gespeichert wird

	/**
	 * Erstellt die (eine) Instanz von FahrzeugVerwaltung.
	 */
	private FahrzeugVerwaltung() {
	};

	public static FahrzeugVerwaltung getInstance() {
		return instance;
	}

	public int getFahrzeugCounter() {
		return fahrzeugCounter;
	}

	/**
	 * Setzt den FahrzeugCounter auf den uebergebenen Wert
	 * 
	 * @param fahrzeugCounter
	 */
	void setFahrzeugCounter(int fahrzeugCounter) {
		this.fahrzeugCounter = fahrzeugCounter;
	}

	// ----------------------------- Methoden zur Bearbeitung der ArrayList mit den Fahrzeugen ------------------------------------------------
	/**
	 * Fuegt ein Fahrzeug zur ArrayList hinzu und speichert die neue Liste in der entsprechenden Textdatei
	 * 
	 * @param f
	 *            - Fahrzeug, das der ArrayList hinzugefuegt werden soll
	 * @return true (as specified by Collection.add(E))
	 */
	public boolean add(Fahrzeug f) {
		boolean b = liste.add(f);
		saveToFile();
		return b;
	}

	/**
	 * Entfernt ein Fahrzeug aus der ArrayList und speichert die neue Liste in der entsprechenden Textdatei
	 * 
	 * @param f
	 *            - Fahrzeug, das der ArrayList hinzugef�gt werden soll
	 * @return true (as specified by Collection.add(E))
	 */
	public boolean remove(Fahrzeug f) {
		boolean b = liste.remove(f);
		saveToFile();
		return b;
	}

	public Fahrzeug remove(int index) {
		Fahrzeug f = liste.remove(index);
		saveToFile();
		return f;
	}

	public Fahrzeug get(int index) {
		return liste.get(index);
	}

	/**
	 * Liefert das Fahrzeug-Objekt zurueck, das die uebergebene Fahrzeugnummer hat
	 * @param fahrzeugNr FahrzeugNr, zu der das Objekt gesucht wird
	 * @return Fahrzeugobjekt mit uebergebener Nr - Falls kein Fahrzeug mit dieser Nr existiert, wird NULL zurueckgegeben
	 */
	public Fahrzeug getFahrzeugByNr(int fahrzeugNr) {
		for (Fahrzeug f : liste) {
			if (f.getNr() == fahrzeugNr) {
				return f;
			}
		}
		return null;
	}

	public int size() {
		return liste.size();
	}

	// ---------------------------------- Methoden der Klasse Fahrzeugverwaltung ------------------------------------------------
	/**
	 * Liefert eine Liste mit allen Fahrzeugen zurueck
	 * @return ArrayList, die alle Fahrzeuge enthaelt 
	 */
	public ArrayList<Fahrzeug> getListe() {
		return liste;
	}

	/**
	 * Liefert eine ArrayList mit allen Fahrzeugen einer bestimmten Muellart (unabhaengig von der Verfuegbarkeit)
	 * @param Muellart - Gibt die Muellart an, fuer die die Fahrzeuge zurueckgegeben werden sollen
	 * @return ArrayList mit Fahrzeugen der uebergebenen Muellart
	 */
	public ArrayList<Fahrzeug> getListeArt(String Muellart) {
		ArrayList<Fahrzeug> ausgabe = new ArrayList<Fahrzeug>();
		for (int i = 0; i < liste.size(); i++) {
			if (liste.get(i).getMuellArt().equals(Muellart)) {
				ausgabe.add(liste.get(i));
			}
		}
		return ausgabe;
	}

	/**
	 * Liefert eine ArrayList mit allen fuer die Muellentleerung verfuegbaren Fahrzeuge
	 * @return ArrayList mit verfuegbaren Fahrzeugen
	 */
	public Vector<Fahrzeug> getListeVerf() {
		Vector<Fahrzeug> ausgabe = new Vector<Fahrzeug>();
		for (Fahrzeug f : liste) {
			if (f.isVerfuegbar()) {
				ausgabe.add(f);
			}
		}
		return ausgabe;
	}

	/**
	 * Liefert eine ArrayList mit allen fuer die Muellentleerung verfuegbaren Fahrzeuge EINER BESTIMMTEN Muellart
	 * @param muellArt - Gibt die Muellart an, fuer die alle verfuegbaren Fahrzeuge zurueckgegeben werden sollen
	 * @return ArrayList mit verfuegbaren Fahrzeugen der uebergebenen Muellart
	 */
	public ArrayList<Fahrzeug> getListeArtVerf(String muellArt) {
		ArrayList<Fahrzeug> ausgabe = new ArrayList<Fahrzeug>();
		for (Fahrzeug f : liste) {
			if (f.isVerfuegbar() && f.getMuellArt().equals(muellArt)) {
				ausgabe.add(f);
			}
		}
		return ausgabe;
	}

	/**
	 * Gibt auf der Konsole den aktuellen Zustand der Fahrzeugliste aus
	 */
	public void print() {
		System.out.println("Die Fahrzeugliste enthaelt zurzeit die folgenden " + liste.size() + " Elemente:");
		for (Fahrzeug f : liste) {
			System.out.println(f.getNr());
		}
	}

	/**
	 * Liefert fuer eine bestimmte Fahrzeugart und eine bestimmte Muellart die Summe der Kapazitaeten ALLER vorhandenen Fahrzeuge (z.B. die Gesamtkapazitaet aller
	 * mittleren Fahrzeuge fuer Biomuell)
	 * 
	 * @param fahrzeugArt
	 * @param muellArt
	 * @return
	 */
	public int gesamtMuellMenge(String fahrzeugArt, String muellArt) {
		int total = 0;
		if ((fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_SMALL) || fahrzeugArt.equals(allgemein.Konstanten.FAHRZEUG_ART_MEDIUM) || fahrzeugArt
				.equals(allgemein.Konstanten.FAHRZEUG_ART_LARGE))) {
			if (muellArt.equals(allgemein.Konstanten.MUELLART_PLASTIK) || muellArt.equals(allgemein.Konstanten.MUELLART_REST)
					|| muellArt.equals(allgemein.Konstanten.MUELLART_PAPIER) || muellArt.equals(allgemein.Konstanten.MUELLART_BIO)) {
				// Fahrzeugart und Muellart sind gueltig
				for (Fahrzeug f : this.getListeVerf()) {
					if (fahrzeugArt.equals(f.getFahrzeugArt()) && muellArt.equals(f.getMuellArt())) {
						total += f.getDailyCap();
					}
				}
			}
		}
		return total;
	}

	// /**
	// * Schreibt die gesamte Fahrzeugliste in eine Textdatei, deren Pfad in der Konstante TXT_FILE festgelegt ist
	// */
	// public void writeToFile() {
	// ArrayList<Fahrzeug> list = liste;
	// File datei = new File(TXT_FILE);
	// try {
	// PrintWriter printWriter = new PrintWriter(new FileWriter(datei));
	// Iterator<Fahrzeug> iter = list.iterator();
	// while (iter.hasNext()) { // Schreibe alle Fahrzeugdaten in die Textdatei (----- markiert das Ende eines Datensatzes)
	// Fahrzeug f = iter.next();
	// printWriter.println(f.getNr());
	// printWriter.println(f.getFahrzeugArt());
	// printWriter.println(f.getMuellArt());
	// printWriter.println(f.getTeam());
	// printWriter.println(f.isVerfuegbar());
	// printWriter.println(TXT_DELIM);
	// }
	// printWriter.close();
	// } catch (IOException e) {
	// System.err.println("Fehler beim Schreiben der Fahrzeugliste in die Textdatei!!");
	// e.printStackTrace();
	// }
	// }
	//
	// /**
	// * Liest die gespeicherte Textdatei mit den Fahrzeugen aus und stellt die entsprechende Fahrzeugliste wieder her
	// */
	// public ArrayList<Fahrzeug> readFile() {
	// ArrayList<Fahrzeug> fahrzeuge = new ArrayList<Fahrzeug>();
	// if (allgemein.Konstanten.DEBUG) {
	// System.out.println("Starte Auslesen der Textdatei fuer die Fahrzeuge...");
	// }
	// try {
	// Scanner reader = new Scanner(new FileReader(TXT_FILE));
	// try {
	// while (reader.hasNextLine()) {
	// int nr = Integer.parseInt(reader.nextLine());
	// String fahrzeugArt = reader.nextLine();
	// String muellArt = reader.nextLine();
	// String team = reader.nextLine();
	// boolean verfuegbar = reader.nextLine().trim().equals("true") ? true : false;
	// String delim = reader.nextLine();
	// // Falls der Datensatz korrekt mit dem Trennzeichen abgeschlossen wurde, wird ein entsprechendes Fahrzeug angelegt
	// if (delim.equals(TXT_DELIM)) {
	// fahrzeuge.add(new Fahrzeug(nr, fahrzeugArt, muellArt, team, verfuegbar));
	// }
	// }
	// } catch (NoSuchElementException e) {
	// // Unerwartetes Dateiende -> Datensatz wird verworfen (es wird KEIN Fahrzeug erstellt)
	// } catch (NumberFormatException e) {
	// // Fehler beim Einlesen eines Datensatzes (String kann nicht in int gewandelt werden) -> Einlesen wird abgebrochen!
	// } finally {
	// reader.close();
	// }
	// } catch (FileNotFoundException e) {
	// // Keine Datei gefunden -> Nichts passiert
	// if (allgemein.Konstanten.DEBUG) {
	// System.out.println("Textdatei fuer Fahrzeuge wurde nicht gefunden!");
	// }
	// }
	// return fahrzeuge;
	// }

	/**
	 * Speichert die (serialisierte) Fahrzeugverwaltung in einer .ser-Datei
	 * 
	 */
	public static void saveToFile() {
		try {
			FileOutputStream fileOut = new FileOutputStream(SER_FILENAME);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(FahrzeugVerwaltung.getInstance());
			out.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lädt die (serialisierte) Fahrzeugverwaltung aus der .ser-Datei
	 * 
	 * @return Die geladene Fahrzeugverwaltung
	 * 
	 */
	public static FahrzeugVerwaltung loadFromFile() {
		FahrzeugVerwaltung f = null;
		try {
			FileInputStream fileIn = new FileInputStream(SER_FILENAME);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			f = (FahrzeugVerwaltung) in.readObject();
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei mit Fahrzeugdaten nicht gefunden!");
			f = new FahrzeugVerwaltung(); // neue FahrzeugVerwaltung anlegen, da keine gespeicherte vorhanden
		} catch (Exception e) {
			e.printStackTrace();
			f = new FahrzeugVerwaltung(); // neue FahrzeugVerwaltung anlegen, da Auslesen nich moeglich
		}
		return f;
	}

	/**
	 * Durchsucht die gesamte Fahrzeugliste nach einem Fahrzeug mit der uebergebenen Fahrzeugnummer.
	 * @param nr Fahrzeugnummer, nach der gesucht werden soll
	 * @return ture, falls bereits ein Fahrzeug mit der uebergebenen Nummer existiert, sonst false
	 */
	public boolean fahrzeugNrVorhanden(int nr) {
		for (Fahrzeug f : liste) {
			if (f.getNr() == nr) {
				return true;
			}
		}
		return false;
	}

	public boolean routenVorhanden() {
		for (Fahrzeug f : liste) {
			if (f.getRouten() != null) {
				return true;
			}
		}
		return false;
	}

}
