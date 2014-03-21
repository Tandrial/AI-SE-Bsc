package fahrzeugverwaltung;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class FahrzeugTableListener implements TableModelListener {

	private final FahrzeugVerwaltung fahrzeugVerwaltung = FahrzeugVerwaltung.getInstance();

	@Override
	public void tableChanged(TableModelEvent e) {
		int changedColumn = e.getColumn(); // Spalte, die geaendert wurde
		int changedRow = e.getFirstRow(); // Zeile, die geaendert wurde
		// System.out.println("In Zeile " + changedRow + " hat sich Spalte " + changedColumn + " ver�ndert!");
		int eventType = e.getType(); // Welches Ereignis ist aufgetreten?
		FahrzeugTableModel mdl = (FahrzeugTableModel) e.getSource();

		if (changedRow == -1) {
			return; // Komisches Tabellenereignis! Hier hat sich iwie nichts geaendert also gibt es auch nichts zu tun...
		}

		// Reagiere entsprechend auf das Ereignis...
		if (eventType == TableModelEvent.INSERT) {
			int nr = Integer.parseInt(mdl.getValueAt(changedRow, 0).toString());
			// Falls das Fahrzeug noch nicht existiert, soll ein entsprechendes Fahrzeug angelegt werden
			if (!fahrzeugVerwaltung.fahrzeugNrVorhanden(nr)) {
				String fahrzeugArt = mdl.getValueAt(changedRow, 1).toString();
				String muellArt = mdl.getValueAt(changedRow, 2).toString();
				String team = mdl.getValueAt(changedRow, 3).toString();
				boolean verfuegbar = mdl.getValueAt(changedRow, 4).toString() == "true" ? true : false;
				Fahrzeug f = new Fahrzeug(nr, fahrzeugArt, muellArt, team, verfuegbar);
				fahrzeugVerwaltung.add(f);
			}
		} else if (eventType == TableModelEvent.DELETE) {
			fahrzeugVerwaltung.remove(changedRow);
			// System.out.println("Entferne Fahrzeug " + changedRow);
		} else if (eventType == TableModelEvent.UPDATE) {
			String s = mdl.getValueAt(changedRow, changedColumn).toString();
			Fahrzeug f = fahrzeugVerwaltung.getListe().get(changedRow); // Fahrzeug, dessen Attribute geandert werden muessen
			switch (changedColumn) {
			case 1:
				// System.out.println("FahrzeugArt -> " + s);
				f.setFahrzeugArt(s);
				break;
			case 2:
				// System.out.println("Muellart -> " + s);
				f.setMuellArt(s);
				break;
			case 3:
				// System.out.println("Team -> " + s);
				f.setTeam(s);
				break;
			case 4:
				boolean b = s == "true" ? true : false;
				// System.out.println("Verfuegbar -> " + b);
				f.setVerfuegbar(b);
				break;
			default:
				break;
			}
			// fahrzeugVerwaltung.writeToFile();
			 FahrzeugVerwaltung.saveToFile();
		}
	}
}
