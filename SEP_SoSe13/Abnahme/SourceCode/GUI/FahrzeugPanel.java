package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import fahrzeugverwaltung.FahrzeugTableModel;

public class FahrzeugPanel extends JPanel {

	private static final long serialVersionUID = 3473063398465837672L;

	FahrzeugTableModel mdl;
	JTable table;
	JComboBox<String> comboBoxMuellart;
	JComboBox<String> comboBoxFahrzeugart;

	public FahrzeugPanel() {
		super(new GridLayout(1, 0));
		final String[] COLUMN_NAMES = { "Nr.", "Fahrzeugtyp", "Muellart", "Team", "Verfuegbarkeit" };

		// TableModel erstellen, Spaltennamen zuweisen und Listener anhaengen
		mdl = new FahrzeugTableModel(0, 5);
		mdl.setColumnIdentifiers(COLUMN_NAMES);

		// Tabelle mit Model initialisieren
		table = new JTable(mdl);
		// Eigenschaften der Tabelle festlegen
		this.table.setRowHeight(17);
		this.table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		this.table.setFillsViewportHeight(true);
		this.table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		this.table.getTableHeader().setReorderingAllowed(false);
		this.table.setShowGrid(false);
		
		// Ausrichtung fuer jede Spalte setzen (die LETZTE Spalte WIRD NICHT editiert, da sonst die Checkbox verschwindet!) // TODO warum?!
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		if (this.table.getRowCount() > 0) {
			for (int i = 0; i < this.table.getColumnCount() - 1; i++) {
				this.table.setDefaultRenderer(this.table.getColumnClass(i), dtcr);
			}
		}

		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); // Notwendig um die Exceptions zu vermeiden, die auftreten, wenn man die
																			// letzte Zeile loescht, waehrend eine Combobox aktiv ist
		this.table.getColumnModel().getColumn(0).setPreferredWidth(5);
		// Ende der Tabelleneigenschaften

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Fiddle with the Muell column's cell editors/renderers.
		setUpMuellColumn(table, table.getColumnModel().getColumn(2));
		setUpArtColumn(table, table.getColumnModel().getColumn(1));

		// Add the scroll pane to this panel.
		add(scrollPane);

	}

	/**
	* Fuegt der Fahrzeugtabelle eine neue Zeile mit entsprechenden Default-Werten hinzu
	*/
	public void addRow() {
		mdl.addRow();
	}

	public void deleteRow() {
		int selectedRow = this.table.getSelectedRow();

		if (selectedRow != -1) {
			mdl.removeRow(selectedRow);
		}
	}

	public void setUpMuellColumn(JTable table, TableColumn muellColumn) {
		comboBoxMuellart = new JComboBox<String>();
		// Set up the editor for the Muell cells.
		comboBoxMuellart.addItem(allgemein.Konstanten.MUELLART_PLASTIK);
		comboBoxMuellart.addItem(allgemein.Konstanten.MUELLART_REST);
		comboBoxMuellart.addItem(allgemein.Konstanten.MUELLART_PAPIER);
		comboBoxMuellart.addItem(allgemein.Konstanten.MUELLART_BIO);
		muellColumn.setCellEditor(new DefaultCellEditor(comboBoxMuellart));

	}

	public void setUpArtColumn(JTable table, TableColumn artColumn) {
		comboBoxFahrzeugart = new JComboBox<String>();
		// Set up the editor for the Muell cells.
		comboBoxFahrzeugart.addItem(allgemein.Konstanten.FAHRZEUG_ART_SMALL);
		comboBoxFahrzeugart.addItem(allgemein.Konstanten.FAHRZEUG_ART_MEDIUM);
		comboBoxFahrzeugart.addItem(allgemein.Konstanten.FAHRZEUG_ART_LARGE);
		artColumn.setCellEditor(new DefaultCellEditor(comboBoxFahrzeugart));
	}

}
