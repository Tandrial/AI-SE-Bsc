package fahrzeugverwaltung;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class FahrzeugTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 8413961473094361917L;

	private boolean DEBUG = false; // aktiviert Konsolenausgaben zum Debugging

	// Hier folgen die Default-Werte fuer ein neu erstelltes Fahrzeug
	private final String DEFAULT_COLUMN_2 = allgemein.Konstanten.FAHRZEUG_ART_SMALL;
	private final String DEFAULT_COLUMN_3 = allgemein.Konstanten.MUELLART_PLASTIK;
	private final String DEFAULT_COLUMN_4 = "<Name Teammitglied1>, <Name Teammitglied2>";
	private final boolean DEFAULT_COLUMN_5 = true;

	// Welche Spalten sind editierbar
	private final boolean COLUMN_1_EDITABLE = false;
	private final boolean COLUMN_2_EDITABLE = true;
	private final boolean COLUMN_3_EDITABLE = true;
	private final boolean COLUMN_4_EDITABLE = true;
	private final boolean COLUMN_5_EDITABLE = true;

	private FahrzeugVerwaltung fahrzeugVerwaltung;

	/**
	 * Erzeugt ein neues FahrzeugTableModle mit der uebergebenen Anzahl von Zeilen und Spalten Falls moeglich, werden Fahrzeuge aus der Fahrzeugdatei eingelesen
	 * 
	 * @param rowCount
	 *            - Anzahl der Tabellenzeilen
	 * @param columnCount
	 *            - Anzahl der Tabellenpalten
	 */
	public FahrzeugTableModel(int rowCount, int columnCount) {
		super(rowCount, columnCount);
		addTableModelListener(new FahrzeugTableListener());
		// Beschaffe Referenzen auf die Fahrzeugverwaltung und die Fahrzeugliste
		fahrzeugVerwaltung = FahrzeugVerwaltung.getInstance();
		ArrayList<Fahrzeug> fahrzeuge = fahrzeugVerwaltung.getListe();
		// Fuege alle bereits vorhandenen Fahrzeuge zur Tabelle hinzu
		for (Fahrzeug f : fahrzeuge) {
			addRow(f);
		}
	}

	/**
	 * Fuegt der Fahrzeugverwaltung einen neuen Datensatz mit entsprechenden Default-Werten hinzu
	 */
	public void addRow() {
		Fahrzeug f = new Fahrzeug(DEFAULT_COLUMN_2, DEFAULT_COLUMN_3, DEFAULT_COLUMN_4, DEFAULT_COLUMN_5);
		addRow(f);
	}

	/**
	 * Fuegt das uebergebene Fahrzeug der Fahrzeugverwaltung und der Fahrzeugtabelle hinzu
	 * 
	 * @param f
	 *            - Fahrzeug, das hinzugefuegt werden soll
	 */
	public void addRow(Fahrzeug f) {
		// fahrzeuge.add(f);
		Object[] datensatz = { f.getNr(), f.getFahrzeugArt(), f.getMuellArt(), f.getTeam(), f.isVerfuegbar() };
		addRow(datensatz);

		if (DEBUG) {
			fahrzeugVerwaltung.print();
		}
	}

	/**
	 * Entfernt den Datensatz in der uebergebenen Zeile
	 * 
	 * @param row
	 *            - Zeile, die geloescht werden soll
	 */
	public void removeRow(int row) {
		// fahrzeuge.remove(row);
		super.removeRow(row);

		if (DEBUG) {
			fahrzeugVerwaltung.print();
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		switch (col) {
		case 0:
			return COLUMN_1_EDITABLE;
		case 1:
			return COLUMN_2_EDITABLE;
		case 2:
			return COLUMN_3_EDITABLE;
		case 3:
			return COLUMN_4_EDITABLE;
		case 4:
			return COLUMN_5_EDITABLE;
		default:
			return false;
		}
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
