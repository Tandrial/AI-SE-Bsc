package GUI;

import Graph.Graph;
import Kartendarstellung.SEPSlippyMap;
import fahrzeugverwaltung.Fahrzeug;
import fahrzeugverwaltung.FahrzeugVerwaltung;
import routenberechnung.routenberechnung;
import OSMparser.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.SystemColor;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import allgemein.Konstanten;

import java.io.*;
import java.util.Vector;

// Die Hauptklasse der GUI beinhaltet die Main Methode des Programms
@SuppressWarnings("serial")
public class mainWindow extends JFrame {

	private JPanel contentPane;

	// Actionlistener
	final listener derListener = new listener();
	final listenerAnfahrt derListenerAnfahrt = new listenerAnfahrt();
	final listenerRoute derListenerRoute = new listenerRoute();

	static final boolean debug = true; // DEBUG FUER DAS SYSTEM.OUT.PRINTLN

	// Die Zwischenspeicher fuer das popupMenue auf der Karte fuer die
	// Anfahrtspunkte
	String popupAuswahlSpeicher = "";
	static double x;
	static double y;
	static SEPSlippyMap kartenAnzeige;
	
	public static String aktMuellart = "";
	
	public static boolean serGeladen = false;

	public static Object routensperrungVorhanden;

	// Starten der Anwendung
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow frame = new mainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Generierung des Fensters und aller seiner Komponenten
	public mainWindow() {

		// Einstellung das Fenster betreffend
		setMinimumSize(new Dimension(900, 630));
		setTitle("SEPTrash v" + Konstanten.VERSION);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				mainWindow.class
						.getResource("/GUI/SEPTrashSchriftzugKleiner.png")));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Graph.saveToFile(new File("Graph.ser"), Graph.getGraph());
				System.exit(EXIT_ON_CLOSE);
			}
		});
		setBounds(100, 100, 900, 630);

		// Das Popupmenue auf der Map "Anfahrtspunkte" und seine MenueItems
		final JPopupMenu anfahrtspunkte = new JPopupMenu();
		final JMenuItem depot = new JMenuItem();
		final JMenuItem plastik = new JMenuItem();
		final JMenuItem papier = new JMenuItem();
		final JMenuItem rest = new JMenuItem();
		final JMenuItem bio = new JMenuItem();

		// Zuweisen der Texte der MenueItems
		depot.setText("Fahrzeugdepot");
		plastik.setText("Muellentleerungsort (Plastik)");
		papier.setText("Muellentleerungsort (Papier)");
		rest.setText("Muellentleerungsort (Restmuell)");
		bio.setText("Muellentleerungsort (Biomuell)");

		// Zuweisen der MenueItems zum Popup "Anfahrtspunkte"
		anfahrtspunkte.add(depot);
		anfahrtspunkte.add(plastik);
		anfahrtspunkte.add(papier);
		anfahrtspunkte.add(rest);
		anfahrtspunkte.add(bio);

		// Der MouseListener und die Entscheidung was ausgewaehlt wurde im Popup
		// "Anfahrtspunkte"
		// reagiert auf strg+rClick

		depot.addActionListener(derListenerAnfahrt);
		plastik.addActionListener(derListenerAnfahrt);
		papier.addActionListener(derListenerAnfahrt);
		rest.addActionListener(derListenerAnfahrt);
		bio.addActionListener(derListenerAnfahrt);

		final MouseListener ml = new MouseAdapter() {

			public void mouseReleased(MouseEvent me) {

				x = SEPSlippyMap.mousePointerX; /*
												 * Dies sind die X und Y
												 * Variablen vom
												 * MouseMOTIONListener der
												 * SlippyMap
												 */
				y = SEPSlippyMap.mousePointerY; /*
												 * Werden konstant geupdated,
												 * sobald die Maus auf der Karte
												 * geschoben wird
												 */

				System.out.println("Mausclick bei x: " + x + "   y:" + y);

				if (me.isControlDown() == true) {
					anfahrtspunkte
							.show(me.getComponent(), me.getX(), me.getY());
				}
			}
		};

		/* Contextmenu fuer Kartennachbearbeitung */
		// Das Popupmenue auf der Map "Anfahrtspunkte" und seine MenueItems
		final JPopupMenu kartennachbearbeitung = new JPopupMenu();
		final JMenuItem fahrtSetze = new JMenuItem();
		final JMenuItem fahrtLoesche = new JMenuItem();
		final JMenuItem betriebsfahrtSetze = new JMenuItem();

		// Zuweisen der Texte der MenueItems
		fahrtSetze.setText("Setze Fahrt");
		fahrtLoesche.setText("Loesche Fahrt");
		betriebsfahrtSetze.setText("Setze Betriebsfahrt");

		// Zuweisen der MenueItems zum Popup "Anfahrtspunkte"
		kartennachbearbeitung.add(fahrtSetze);
		kartennachbearbeitung.add(betriebsfahrtSetze);
		kartennachbearbeitung.add(fahrtLoesche);

		// Der MouseListener und die Entscheidung was ausgewaehlt wurde im Popup
		// "Routenberechnung"
		// reagiert auf strg+rClick

		/* FÜR DIE KARTENNACHBEARBEITUNG */
		fahrtSetze.addActionListener(derListenerRoute);
		fahrtLoesche.addActionListener(derListenerRoute);
		betriebsfahrtSetze.addActionListener(derListenerRoute);

		final MouseListener mlroutenberechnung = new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {

				x = SEPSlippyMap.mousePointerX; /*
												 * Dies sind die X und Y
												 * Variablen vom
												 * MouseMOTIONListener der
												 * SlippyMap
												 */
				y = SEPSlippyMap.mousePointerY; /*
												 * Werden konstant geupdated,
												 * sobald die Maus auf der Karte
												 * geschoben wird
												 */

				System.out.println("Mausclick bei x: " + x + "   y:" + y);

				if (me.isControlDown() == true) {
					kartennachbearbeitung.show(me.getComponent(), me.getX(),
							me.getY());
				}

			};
		};

		// Die ContentPanes des Borderlayouts im mainWindow selbst
		contentPane = new JPanel(); // = Center
		contentPane.setBackground(SystemColor.activeCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(30, 30));

		JPanel northPanel = new JPanel();
		northPanel.setBackground(SystemColor.activeCaptionBorder);
		northPanel.setSize(new Dimension(100, 100));
		contentPane.add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel westPanel = new JPanel();
		westPanel.setBackground(SystemColor.activeCaptionBorder);
		contentPane.add(westPanel, BorderLayout.WEST);

		JPanel southPanel = new JPanel();
		southPanel.setBackground(SystemColor.activeCaptionBorder);
		contentPane.add(southPanel, BorderLayout.SOUTH);

		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(SystemColor.activeCaptionBorder);
		contentPane.add(eastPanel, BorderLayout.EAST);

		// Das Panel in dem die Buttons im MenuePunkt Routenberechnung liegen
		// "Wochentagauswahl" usw
		final JPanel anzeigeOptionenPanel = new JPanel();
		anzeigeOptionenPanel.setVisible(false);
		anzeigeOptionenPanel.setBackground(SystemColor.menu);
		anzeigeOptionenPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1,
				true));
		anzeigeOptionenPanel.setEnabled(false);

		// Die Buttons der GUI und deren Startsichtbarkeiten
		JButton beendenButton = new JButton("Beenden");

		final JButton kartenimportButton = new JButton("Kartenimport");

		final JToggleButton anfahrtspunkteButton = new JToggleButton(
				"Anfahrtspunkte");
		anfahrtspunkteButton.setEnabled(false);

		final JButton neuesFahrzeugButton = new JButton("Neues Fahrzeug");
		neuesFahrzeugButton.setVisible(false);

		final JButton fahrzeugLoeschenButton = new JButton("Fahrzeug loeschen");
		fahrzeugLoeschenButton.setVisible(false);

		final JToggleButton fahrzeugverwaltungButton = new JToggleButton(
				"Fahrzeugverwaltung");
		fahrzeugverwaltungButton.setEnabled(false);

		final JToggleButton routenberechnungButton = new JToggleButton(
				"Routenberechnung");
		routenberechnungButton.setEnabled(false);

		// Die Komboboxen fuer den Menuepunkt
		// "Routenberechnung"/Anzeigeoptionen
		final JLabel anzeigeoptionenLabel = new JLabel("Anzeigeoptionen");
		anzeigeoptionenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JComboBox fahrzeugtypComboBox = new JComboBox();

		JLabel fahrzeugeLabel = new JLabel("Fahrzeug(e):");
		fahrzeugeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Die Logik der Kombobox Wochentage:
		final String[] anzeigeWochentage = { "ganze Woche", "Montag",
				"Dienstag", "Mittwoch", "Donnerstag", "Freitag" };

		final JComboBox wochentagComboBox = new JComboBox(anzeigeWochentage);

		JLabel wochentageLabel = new JLabel("Wochentag(e):");
		wochentageLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		wochentagComboBox
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (wochentagComboBox.getSelectedIndex() == 0) {
							System.out.println(anzeigeWochentage[0]
									+ " gewählt");
							kartenAnzeige.setAnzeigeFilter_wochentag(0);

						} else if (wochentagComboBox.getSelectedIndex() == 1) {
							System.out.println(anzeigeWochentage[1]
									+ " gewählt");
							kartenAnzeige.setAnzeigeFilter_wochentag(1);

						} else if (wochentagComboBox.getSelectedIndex() == 2) {
							System.out.println(anzeigeWochentage[2]
									+ " gewählt");
							kartenAnzeige.setAnzeigeFilter_wochentag(2);

						} else if (wochentagComboBox.getSelectedIndex() == 3) {
							System.out.println(anzeigeWochentage[3]
									+ " gewählt");
							kartenAnzeige.setAnzeigeFilter_wochentag(3);

						} else if (wochentagComboBox.getSelectedIndex() == 4) {
							System.out.println(anzeigeWochentage[4]
									+ " gewählt");
							kartenAnzeige.setAnzeigeFilter_wochentag(4);

						} else if (wochentagComboBox.getSelectedIndex() == 5) {
							System.out.println(anzeigeWochentage[5]
									+ " gewählt");
							kartenAnzeige.setAnzeigeFilter_wochentag(5);

						}
					}
				});

		// Die Logik der Kombobox Muellart
		final String[] muellarten = { allgemein.Konstanten.MUELLART_ALLE,
				allgemein.Konstanten.MUELLART_PLASTIK,
				allgemein.Konstanten.MUELLART_REST,
				allgemein.Konstanten.MUELLART_PAPIER,
				allgemein.Konstanten.MUELLART_BIO };

		final JComboBox muellartCombobox = new JComboBox(muellarten);

		JLabel muellartenLabel = new JLabel("Muellart(en):");
		muellartenLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		muellartCombobox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (muellartCombobox.getSelectedIndex() == 0) {
					aktMuellart = muellarten[0];
					System.out.println(muellarten[0] + " gewählt");
					kartenAnzeige.setAnzeigeFilter_muellart(-1);

				} else if (muellartCombobox.getSelectedIndex() == 1) {
					aktMuellart = muellarten[1];
					System.out.println(muellarten[1] + " gewählt");
					kartenAnzeige.setAnzeigeFilter_muellart(1);

				} else if (muellartCombobox.getSelectedIndex() == 2) {
					aktMuellart = muellarten[2];
					System.out.println(muellarten[2] + " gewählt");
					kartenAnzeige.setAnzeigeFilter_muellart(2);

				} else if (muellartCombobox.getSelectedIndex() == 3) {
					aktMuellart = muellarten[3];
					System.out.println(muellarten[3] + " gewählt");
					kartenAnzeige.setAnzeigeFilter_muellart(3);

				} else if (muellartCombobox.getSelectedIndex() == 4) {
					aktMuellart = muellarten[4];
					System.out.println(muellarten[4] + " gewählt");
					kartenAnzeige.setAnzeigeFilter_muellart(4);

				} else if (muellartCombobox.getSelectedIndex() == 5) {
					aktMuellart = muellarten[5];
					System.out.println(muellarten[5] + " gewählt");
					kartenAnzeige.setAnzeigeFilter_muellart(5);

				}
			}
		});

		// Das Menue, dass einem anzeigt in welchem Bereich der Gui man sich
		// befindet
		final JLabel menueShow = new JLabel("     Hauptmenue");
		menueShow.setForeground(Color.RED);
		menueShow.setBackground(SystemColor.activeCaptionBorder);
		menueShow.setHorizontalTextPosition(SwingConstants.LEFT);
		menueShow.setHorizontalAlignment(SwingConstants.LEFT);
		menueShow.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		menueShow.setBorder(null);
		northPanel.add(menueShow);

		// das Label fuer die Routensperrung
		final JLabel routensperrungVorhanden = new JLabel(
				"Staßenabschnitt(e) nicht befahrbar! Bitte die betroffenen Anwohner informieren.");
		routensperrungVorhanden.setFont(new Font("Tahoma", Font.BOLD, 11));
		routensperrungVorhanden.setHorizontalAlignment(SwingConstants.CENTER);
		routensperrungVorhanden.setForeground(Color.RED);
		routensperrungVorhanden.setVisible(false);

		// Das SepTrash Logo aus dem GUI Protoyp
		JLabel sepTrashLogo = new JLabel("");
		sepTrashLogo.setBackground(SystemColor.activeCaptionBorder);
		sepTrashLogo.setBorder(null);
		sepTrashLogo.setIcon(new ImageIcon(mainWindow.class
				.getResource("/GUI/SEPTrashSchriftzugKleiner.png")));
		northPanel.add(sepTrashLogo);

		// Das ScrollPane fuer die Kartenanzeige
		kartenAnzeige = new SEPSlippyMap();
		kartenAnzeige.setDisplayPositionByLatLon(51.462036, 7.016925, 17); // Schuetzenbahn
		kartenAnzeige.setBorder(null);
		kartenAnzeige.setBackground(SystemColor.menu);

		// Dieses Panel ist zum "Umschalten" zwischen der Fahrzeugverwaltung und
		// der Map gedacht
		JPanel diverseFunktionenPanel = new JPanel();
		diverseFunktionenPanel.setBorder(null);
		diverseFunktionenPanel.setForeground(UIManager
				.getColor("CheckBox.focus"));
		diverseFunktionenPanel.setBackground(SystemColor.activeCaptionBorder);
		contentPane.add(diverseFunktionenPanel, BorderLayout.CENTER);

		// Dieses Panel ist fuer das Einblenden der Buttons Fahrzeug hinzufuegen
		// und Fahrzeug loeschen verantwortlich
		final JPanel fahrzeugEigenschaftenPanel = new JPanel();
		fahrzeugEigenschaftenPanel.setVisible(false);
		fahrzeugEigenschaftenPanel.setBackground(SystemColor.menu);
		fahrzeugEigenschaftenPanel.setLayout(new BorderLayout(0, 0));

		// Die Tabelle mit der Fahrzeugverwaltung
		final FahrzeugPanel tabelleFahrzeuge = new FahrzeugPanel();
		tabelleFahrzeuge.setOpaque(true); // content panes must be opaque

		// Das ScrollPane in dem die Tabelle der Fahrzeugverwaltung liegt
		final JScrollPane fahrzeugeigenschaftenScrollPane = new JScrollPane(
				tabelleFahrzeuge);
		fahrzeugEigenschaftenPanel.add(fahrzeugeigenschaftenScrollPane,
				BorderLayout.CENTER);

		// Das Label unter der Fahrzeugverwaltung mit SEPMini, SEPMidi usw.
		final JLabel fahrzeugdatenLabelTest = new JLabel("");
		fahrzeugdatenLabelTest.setIcon(new ImageIcon(mainWindow.class
				.getResource("/GUI/fahrzeugdaten.png")));
		fahrzeugEigenschaftenPanel.add(fahrzeugdatenLabelTest,
				BorderLayout.SOUTH);
		fahrzeugdatenLabelTest.setHorizontalAlignment(SwingConstants.CENTER);
		diverseFunktionenPanel.setLayout(new CardLayout(0, 0));
		diverseFunktionenPanel.add(kartenAnzeige, "name_38101966964022");
		diverseFunktionenPanel.add(fahrzeugEigenschaftenPanel,
				"name_38102002634337");

		// Falls schon einmal ein Graph berechnet wurde wird dieser aus der
		// Datei geladen
		File f = new File("Graph.ser");
		if (f.exists()) {
			if (Graph.loadFromFile(f) != null) {
				kartenAnzeige.setGraph(Graph.getGraph());
				kartenAnzeige.setDisplayToFitMapRectangles();
				kartenAnzeige.zoomIn();
				kartenAnzeige.getVisibleRect();
				kartenAnzeige.setVisible(true);
				anfahrtspunkteButton.setEnabled(true);
				fahrzeugverwaltungButton.setEnabled(true);
				// gibt es Baustellen?
				routensperrungVorhanden
						.setVisible(Kartendarstellung.SEPSlippyMap.baustelleGefunden);
				serGeladen = true;
				System.err.println("serGeladen true");
			}} else {
			serGeladen = false;
			System.err.println("serGeladen false");
		}
		

		// Die Layouts - NICHT DRAN RUMFUMMELN :-)

		// Layout des WestPanels
		// Beinhaltetdie Buttons kartenimport, anfhartspunkte,
		// fahrzeugverwaltung, neuesfahrzeug, fahrzeugloeschen
		// und das anzeigeoptionen Panel im Menuepunkt routenplanung
		GroupLayout gl_westPanel = new GroupLayout(westPanel);
		gl_westPanel
				.setHorizontalGroup(gl_westPanel
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_westPanel
										.createSequentialGroup()
										.addGroup(
												gl_westPanel
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_westPanel
																		.createSequentialGroup()
																		.addGroup(
																				gl_westPanel
																						.createParallelGroup(
																								Alignment.TRAILING,
																								false)
																						.addComponent(
																								kartenimportButton,
																								Alignment.LEADING,
																								GroupLayout.PREFERRED_SIZE,
																								150,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								anfahrtspunkteButton,
																								Alignment.LEADING,
																								GroupLayout.PREFERRED_SIZE,
																								150,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								fahrzeugverwaltungButton,
																								Alignment.LEADING,
																								GroupLayout.PREFERRED_SIZE,
																								150,
																								GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_westPanel
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addComponent(
																								neuesFahrzeugButton,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								fahrzeugLoeschenButton,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)))
														.addGroup(
																gl_westPanel
																		.createSequentialGroup()
																		.addGap(6)
																		.addComponent(
																				anzeigeOptionenPanel,
																				GroupLayout.DEFAULT_SIZE,
																				300,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		gl_westPanel
				.setVerticalGroup(gl_westPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_westPanel
										.createSequentialGroup()
										.addComponent(kartenimportButton)
										.addGap(54)
										.addComponent(anfahrtspunkteButton)
										.addGap(66)
										.addGroup(
												gl_westPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																fahrzeugverwaltungButton)
														.addComponent(
																neuesFahrzeugButton))
										.addGap(11)
										.addComponent(fahrzeugLoeschenButton)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(anzeigeOptionenPanel,
												GroupLayout.PREFERRED_SIZE,
												201, GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE)));
		gl_westPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {
				kartenimportButton, anfahrtspunkteButton, neuesFahrzeugButton,
				fahrzeugLoeschenButton, fahrzeugverwaltungButton });

		// Das Layout des SouthPanels beinhaltet die Buttons:
		// routenberechnung und beenden
		GroupLayout gl_southPanel = new GroupLayout(southPanel);
		gl_southPanel.setHorizontalGroup(gl_southPanel.createParallelGroup(
				Alignment.LEADING)
				.addGroup(
						gl_southPanel
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(routenberechnungButton)
								.addGap(18)
								.addComponent(beendenButton,
										GroupLayout.PREFERRED_SIZE, 146,
										GroupLayout.PREFERRED_SIZE)
								.addGap(49)
								.addComponent(routensperrungVorhanden,
										GroupLayout.DEFAULT_SIZE, 526,
										Short.MAX_VALUE)));
		gl_southPanel.setVerticalGroup(gl_southPanel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_southPanel
						.createParallelGroup(Alignment.BASELINE)
						.addComponent(routensperrungVorhanden,
								GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
						.addComponent(beendenButton)
						.addComponent(routenberechnungButton)));
		southPanel.setLayout(gl_southPanel);

		// Layout des anzeigeOptionenPanels mit den noch leeren Comboboxen foer
		// die Routenplanung
		GroupLayout gl_anzeigeOptionenPanel = new GroupLayout(
				anzeigeOptionenPanel);
		gl_anzeigeOptionenPanel
				.setHorizontalGroup(gl_anzeigeOptionenPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_anzeigeOptionenPanel
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_anzeigeOptionenPanel
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																gl_anzeigeOptionenPanel
																		.createParallelGroup(
																				Alignment.LEADING,
																				false)
																		.addComponent(
																				wochentageLabel,
																				GroupLayout.PREFERRED_SIZE,
																				115,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				muellartenLabel,
																				GroupLayout.PREFERRED_SIZE,
																				115,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																fahrzeugeLabel,
																GroupLayout.PREFERRED_SIZE,
																115,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_anzeigeOptionenPanel
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_anzeigeOptionenPanel
																		.createSequentialGroup()
																		.addGroup(
																				gl_anzeigeOptionenPanel
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addComponent(
																								muellartCombobox,
																								0,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								anzeigeoptionenLabel,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								wochentagComboBox,
																								0,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addGap(32))
														.addGroup(
																gl_anzeigeOptionenPanel
																		.createSequentialGroup()
																		.addComponent(
																				fahrzeugtypComboBox,
																				0,
																				115,
																				Short.MAX_VALUE)
																		.addContainerGap()))));
		gl_anzeigeOptionenPanel
				.setVerticalGroup(gl_anzeigeOptionenPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_anzeigeOptionenPanel
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(anzeigeoptionenLabel,
												GroupLayout.PREFERRED_SIZE, 0,
												Short.MAX_VALUE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_anzeigeOptionenPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																fahrzeugtypComboBox,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																fahrzeugeLabel,
																GroupLayout.PREFERRED_SIZE,
																26,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(
												gl_anzeigeOptionenPanel
														.createParallelGroup(
																Alignment.BASELINE,
																false)
														.addComponent(
																wochentageLabel,
																GroupLayout.PREFERRED_SIZE,
																26,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																wochentagComboBox,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(
												gl_anzeigeOptionenPanel
														.createParallelGroup(
																Alignment.BASELINE,
																false)
														.addComponent(
																muellartenLabel,
																GroupLayout.PREFERRED_SIZE,
																26,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																muellartCombobox,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(42)));
		gl_anzeigeOptionenPanel
				.linkSize(SwingConstants.VERTICAL, new Component[] {
						wochentageLabel, muellartCombobox, muellartenLabel,
						anzeigeoptionenLabel, fahrzeugtypComboBox,
						fahrzeugeLabel, wochentagComboBox });
		gl_anzeigeOptionenPanel
				.linkSize(SwingConstants.HORIZONTAL, new Component[] {
						wochentageLabel, muellartCombobox, muellartenLabel,
						anzeigeoptionenLabel, fahrzeugtypComboBox,
						fahrzeugeLabel, wochentagComboBox });
		anzeigeOptionenPanel.setLayout(gl_anzeigeOptionenPanel);
		westPanel.setLayout(gl_westPanel);

		// Hier sind die Funktionen aller Buttons hinterlegt:

		// Logik KartenImport Button
		kartenimportButton.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent arg0) {
				setIconImage(Toolkit
						.getDefaultToolkit()
						.getImage(
								mainWindow.class
										.getResource("/GUI/SEPTrashSchriftzugKleiner.png")));

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter(
						"OSM Kartenmaterial", "osm"));
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					System.out.println(selectedFile.getName());

					int choice = 0;

					if (selectedFile.length() > allgemein.Konstanten.MAX_FILE_SIZE * 1024 * 1024) {
						Object[] options = { "OK", "Cancel" };
						choice = JOptionPane.showOptionDialog(
								null,
								"Die Karte "
										+ selectedFile.getName()
										+ " ist größer als "
										+ allgemein.Konstanten.MAX_FILE_SIZE
										+ "MByte. Das Importieren kann deshalb eine lange Zeit dauern.",
								"Import Abbrechen?", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE, null, options,
								options[0]);
					}

					if (choice == 0) {
						try {
							OSMParser parser = new OSMParser(selectedFile);
							parser.createGraphFromKarte();

							kartenAnzeige.setGraph(Graph.getGraph());
							kartenAnzeige.setDisplayToFitMapRectangles();
							kartenAnzeige.zoomOut();
							kartenAnzeige.setVisible(true);
							anfahrtspunkteButton.setEnabled(true);
							fahrzeugverwaltungButton.setEnabled(true);

							// gibt es Baustellen?
							if (Kartendarstellung.SEPSlippyMap.baustelleGefunden == true) {
								routensperrungVorhanden.setVisible(true);
							} else {
								routensperrungVorhanden.setVisible(false);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		// Die Logik des Beenden Buttons
		beendenButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				Graph.saveToFile(new File("Graph.ser"), Graph.getGraph());
				System.exit(0);
			}
		});

		// Logik des Buttons Routenberechnung
		if (f.exists()) {
			routenberechnungButton.setEnabled(true);
		}
		routenberechnungButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (routenberechnungButton.isSelected() == true) {
					kartenAnzeige.addMouseListener(mlroutenberechnung);
					anzeigeOptionenPanel.setVisible(true);
					menueShow.setText("     Plandarstellung");
					kartenimportButton.setEnabled(false);
					kartenimportButton.setVisible(false);
					fahrzeugverwaltungButton.setEnabled(false);
					anfahrtspunkteButton.setEnabled(false);

					// Logik des FahrzeugauswahlButtons
					fahrzeugtypComboBox.addActionListener(derListener);
					fahrzeugtypComboBox.addItem("Alle Fahrzeuge");

					Vector<Fahrzeug> verfuegbareFahrzeuge = FahrzeugVerwaltung
							.getInstance().getListeVerf();

					System.out.println();
					for (int i = 0; i < verfuegbareFahrzeuge.size(); i++) {

						System.out.println("i = " + i + " NR:    "
								+ verfuegbareFahrzeuge.get(i).getNr());
					}

					for (int i = 0; i < verfuegbareFahrzeuge.size(); i++) {
						fahrzeugtypComboBox.addItem(verfuegbareFahrzeuge.get(i)
								.getNr());
					}

					routenberechnung.calculateroute();
					System.err.println("!! ROUTENBERECHNUNG ABGESCHLOSSEN !!");
					kartenAnzeige.aktualisiereDarstellung();

				} else {
					kartenAnzeige.removeMouseListener(mlroutenberechnung);
					fahrzeugtypComboBox.removeActionListener(derListener);
					fahrzeugtypComboBox.removeAllItems();
					anzeigeOptionenPanel.setVisible(false);
					menueShow.setText("     Hauptmenue");
					kartenimportButton.setEnabled(true);
					kartenimportButton.setVisible(true);
					fahrzeugverwaltungButton.setEnabled(true);
					anfahrtspunkteButton.setEnabled(true);

				}
			}
		});

		// Logik des Fahrzeugverwaltung Buttons
		fahrzeugverwaltungButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (fahrzeugverwaltungButton.isSelected() == true) {
					neuesFahrzeugButton.setVisible(true);
					fahrzeugLoeschenButton.setVisible(true);
					fahrzeugEigenschaftenPanel.setVisible(true);
					menueShow.setText("     Fahrzeuge");
					kartenAnzeige.setVisible(false);
					routenberechnungButton.setEnabled(false);
					kartenimportButton.setEnabled(false);
					kartenimportButton.setVisible(false);
					anfahrtspunkteButton.setEnabled(false);

				} else {
					neuesFahrzeugButton.setVisible(false);
					fahrzeugLoeschenButton.setVisible(false);
					fahrzeugEigenschaftenPanel.setVisible(false);
					menueShow.setText("     Hauptmenue");
					kartenAnzeige.setVisible(true);
					routenberechnungButton.setEnabled(true);
					kartenimportButton.setEnabled(true);
					kartenimportButton.setVisible(true);
					anfahrtspunkteButton.setEnabled(true);
				}
			}
		});

		// Logik des Buttons neues Fahrzeug
		neuesFahrzeugButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				tabelleFahrzeuge.addRow();
			}
		});

		// Logik des Buttons Fahrzeug loeschen
		fahrzeugLoeschenButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				tabelleFahrzeuge.deleteRow();
			}
		});

		// Logik des Buttons Anfahrtspunkte
		anfahrtspunkteButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (anfahrtspunkteButton.isSelected() == true) {
					menueShow.setText("     Anfahrtspunkte");
					routenberechnungButton.setEnabled(false);
					fahrzeugverwaltungButton.setEnabled(false);
					kartenimportButton.setEnabled(false);
					kartenimportButton.setVisible(false);
					kartenAnzeige.addMouseListener(ml);

				} else {
					fahrzeugEigenschaftenPanel.setVisible(false);
					menueShow.setText("     Hauptmenue");
					routenberechnungButton.setEnabled(true);
					fahrzeugverwaltungButton.setEnabled(true);
					kartenimportButton.setEnabled(true);
					kartenimportButton.setVisible(true);
					// TODO: evtl entfernen
					// // Wurde ein Depot gesetzt???
					// if (Graph.getGraph().getDepot() != null)
					// Graph.getGraph().checkGraphZusammenhang();

					kartenAnzeige.aktualisiereDarstellung();
					kartenAnzeige.removeMouseListener(ml);
				}
			}
		});
	}
}