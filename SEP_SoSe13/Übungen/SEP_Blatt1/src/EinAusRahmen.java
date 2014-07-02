import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.text.*;
//import javax.swing.border.*;
//import javax.swing.filechooser.*;

public class EinAusRahmen /* extends JFrame */implements Runnable {

	private JFrame gui;

	static EinAusRahmen EinAusObjekt = null;
	static MyJPanel MyJPanelObjekt = null;
	static Thread AnwendungsThread = null;

	static final long serialVersionUID = 0;

	String Kommandozeile = ""; // Inhalt der Eingabezeile
	String AusSpeicher = ""; // Puffer fuer auszugebenden Text
	String testAusSpeicher = ""; // Zweiter Puffer fuer auszugebenden Text fuer
									// Testautomatisierung

	static int StatusZeilen; // Anzahl der durch Anfang(...) angeforderten
								// Statuszeilen.
	static int AusgabeZeilen; // Anzahl der durch Anfang(...) angeforderten
								// Ausgabezeilen.

	volatile static boolean threadSuspendedFlag = true;
	boolean unguelt = false; // Fehlersignalisierung fuer die Methoden
								// liesInt(...), liesLong(...), liesFloat(...)

	static boolean inAusFeld = true;
	static boolean vonDatei = false;
	static boolean inDatei = false;

	static boolean echo = true;
	static boolean tastflag = false;

	static String EingabeDatei = "";
	static String AusgabeDatei = "";

	static String QuellDateiPuffer; // Die Quell-Datei wird in diesen String
									// eingelesen.

	public static void Anfang(EinAusRahmen obj, String Kopfzeile,
			int AusgZeilen, int StatZeilen, String EingDatei) {
		EinAusRahmen.Anfang(obj, Kopfzeile, AusgZeilen, StatZeilen);
		EinAusRahmen.EingabeDatei = EingDatei;

		EinAusRahmen.EinAusObjekt
				.zeigeAus("Aktuelle Datenquelle: Eingabe Datei ("
						+ EinAusRahmen.EingabeDatei + ")");
		EinAusRahmen.vonDatei = true;
		QuellDatei.readFileToBuffer();

		EinAusRahmen.EinAusObjekt.spAusZ(EinAusRahmen.EinAusObjekt.liesZ(
				EinAusRahmen.QuellDateiPuffer/*
											 * EinAusObjekt.QuellDateiPuffer
											 */, 20));
		EinAusRahmen.EinAusObjekt
				.zeigeAus("Dies war der Anfang der Eingabe-Datei.");
	}

	public static void Anfang(EinAusRahmen obj, String Kopfzeile,
			int AusgZeilen, int StatZeilen)
	// Initialisiert für ein neuesObjekt einer Unterklasse ein neues
	// Fenster für die Ein- und Ausgabe und richtet es mit der angegebene
	// Kopfzeile und den angegebenen Zeilenzahlen für das große Ausgabe-
	// fester (ca. 20 Zeilen) und die Statuszeilen darunter (ca. 3 Zeilen)
	// ein.
	{
		StatusZeilen = StatZeilen;
		AusgabeZeilen = AusgZeilen;
		EinAusObjekt = obj;
		EinAusObjekt.erzeugeGui(Kopfzeile);
		EinAusObjekt.zeigeAus("Willkommen...");

		AnwendungsThread = new Thread(EinAusObjekt);
		AnwendungsThread.start();
	}

	public JFrame getGui() {
		return gui;
	}

	// Die folgenden Methoden Hilfe(), Antwort() und Ende() sollten von der
	// Anwendung ueberschrieben werden:

	public void Hilfe() {
		// Soll einen Hilfstext zum Anwendungsprogramm anzeigen.
		// Diese Methode sollte daher von der Klasse, die EinAusRahmen erweitert
		// ueberschrieben werden.
		spAusZ("Fuer die Anwendung steht keine Hilfe zur Verfuegung.");
		spAusZ("Anwendungsentwickler können die Methode Hilfe( ) ueberschreiben um dies zu ändern.");
		zeigeAus("");
	}

	public void Antwort(String Kommando) {
		// Soll die Reaktion des Anwendungsprogramms auf ein eingegebenes
		// Kommando realisieren. Dies ist die eigentliche Arbeit des
		// Anwendungsprogramms. Diese Methode sollte daher von der Klasse, die
		// EinAusRahmen erweitert ueberschrieben werden.
		zeigeAus("Es steht kein Anwendungsprogramm zur Bearbeitung der Eingabe: "
				+ Kommando + " zur Verfuegung.");
	}

	public void Ende() {
		// Soll bei Programm-Beendigung das Anwendungsprogramm abschlieï¿½en.
		// Diese Methode sollte daher von der Klasse, die EinAusRahmen erweitert
		// ueberschrieben werden.
		spAusZ("Es ist kein expliziter Abschluss des Anwendungsprogramms vorgesehen.\n");
		zeigeAus("Anwendungsentwickler können die Methode Ende( ) ueberschreiben um dies zu ändern.");
	}

	public boolean abgebrochen() {
		// gibt an, ob die Programm-Ausfuehrung abgebrochen werden soll, weil
		// der der Benutzer
		// dies via Button angefordert hat.
		return threadSuspendedFlag;
	}

	public void brichab() {
		threadSuspendedFlag = true;
	}

	// Public-Methode für die Eingabe in Antwort () und Ende ():
	// ---------------------------------------------------------

	public int SatzAnz(String K)
	// gibt an, wieviele Sätze der Text K enthält. Säze sind durch Semi-
	// kolon oder Komma getrennt. Typischerweise ist K das zuletzt erteilte
	// Kommando.
	{
		return AnzS(K);
	}

	public int WortAnz(String K, int s)
	// gibt an, wieviele Wï¿½rter der Satz s des Textes K enthält. Wörter
	// sind durch Leerzeichen getrennt. Für den ersten Satz gilt s = 1.
	{
		return AnzW(K, s);
	}

	public String Wort(String K, int s, int w)
	// liefert Wort w in Satz s des Textes K. Für das erste Wort gilt
	// w = 1.
	{
		return liesW(K, s, w);
	}

	public int liesInt(String K, int s, int w)
	// liest aus Wort w in Satz s des Textes K eine Int-Zahl. Wenn dort
	// keine Int-Zahl steht, wird 0 zurückgegeben und eine boolesche Vari-
	// able auf true gesetzt, die über ungueltig () gelesen werden kann.
	{
		String Zahl = liesW(K, s, w);
		int i;
		try {
			i = Integer.parseInt(Zahl);
		} catch (NumberFormatException e) {
			unguelt = true;
			i = 0;
		}
		return i;
	}

	public long liesLong(String K, int s, int w)
	// liest aus Wort w in Satz s des Textes K eine Long-Zahl. Wenn dort
	// keine Long-Zahl steht, wird 0 zurückgegeben und eine boolesche Vari-
	// able auf true gesetzt, die über ungueltig () gelesen werden kann.
	{
		String Zahl = liesW(K, s, w);
		long L;
		try {
			L = Long.parseLong(Zahl);
		} catch (NumberFormatException e) {
			unguelt = true;
			L = 0L;
		}
		return L;
	}

	public float liesFloat(String K, int s, int w)
	// liest aus Wort w in Satz s des Textes K eine Float-Zahl. Wenn dort
	// keine Float-Zahl steht, wird 0 zurückgegeben und eine boolesche Va-
	// riable auf true gesetzt, die über ungueltig () gelesen werden kann.
	{
		String Zahl = liesW(K, s, w);
		float f;
		try {
			f = Float.valueOf(Zahl).floatValue();
		} catch (NumberFormatException e) {
			unguelt = true;
			f = 0.0f;
		}
		return f;
	}

	// Die folgenden sechs Methoden haben die gleiche Wirkung wie die voran-
	// gehenden sechs Methoden, beziehen sich jedoch nicht auf einen belie-
	// bigen String, sondern auf das zuletzt gegebene Kommando.

	public int SatzAnz() {
		return SatzAnz(Kommandozeile);
	}

	public int WortAnz(int s) {
		return WortAnz(Kommandozeile, s);
	}

	public String Wort(int s, int w) {
		return Wort(Kommandozeile, s, w);
	}

	public int liesInt(int s, int w) {
		return liesInt(Kommandozeile, s, w);
	}

	public long liesLong(int s, int w) {
		return liesLong(Kommandozeile, s, w);
	}

	public float liesFloat(int s, int w) {
		return liesFloat(Kommandozeile, s, w);
	}

	public boolean ungueltig()
	// gibt an, ob bei den letzten Aufrufen von liesInt (...),
	// liesLong (...) oder liesFloat (...) ein Fehler aufgetreten ist.
	// Die boolesche Variable, die dies angibt, wird in jedem Fall auf
	// false zurï¿½ckgesetzt.
	{
		boolean u = unguelt;
		unguelt = false;
		return u;
	}

	private int AnzZ(String S)
	// Anzahl der Zeilen von S.
	{
		int p, a = 1, L;
		S = S.replace((char) 13, '\n');
		if (S == null || S.trim().length() == 0)
			return 0;
		else {
			L = S.length();
			p = S.indexOf('\n');
			while (p >= 0) {
				a++;
				if (p + 1 < L)
					p = S.indexOf('\n', p + 1);
				else
					p = -1;
			}
			return a;
		}
	}

	private int AnzS(String K)
	// Anzahl der Sï¿½tze von K.
	{
		int p, ps, pk, a = 1, L;
		if (K == null)
			return 0;
		else {
			L = K.length();
			ps = K.indexOf(';');
			pk = K.indexOf(',');
			if (ps < 0 || pk < 0)
				p = Math.max(ps, pk);
			else
				p = Math.min(ps, pk);
			while (p >= 0) {
				a++;
				if (p + 1 < L) {
					ps = K.indexOf(';', p + 1);
					pk = K.indexOf(',', p + 1);
					if (ps < 0 || pk < 0)
						p = Math.max(ps, pk);
					else
						p = Math.min(ps, pk);
				} else
					p = -1;
			}
			return a;
		}
	}

	private int AnzW(String K, int s)
	// Anzahl der Wï¿½rter in Satz s von K.
	{
		int p, a = 1;
		String S = liesS(K, s).trim();
		int L = S.length();
		if (L == 0)
			return 0;
		else {
			p = S.indexOf(' ');
			while (p >= 0) {
				a++;
				while (p + 1 < L && S.charAt(p + 1) == ' ')
					p++;
				if (p + 1 < L)
					p = S.indexOf(' ', p + 1);
				else
					p = -1;
			}
			return a;
		}
	}

	private String liesS(String K, int s)
	// Satz s von K.
	{
		int p, ps, pk, q = -1, a = 1, L;
		if (K == null || s < 1)
			return "";
		else {
			L = K.length();
			ps = K.indexOf(';');
			pk = K.indexOf(',');
			if (ps < 0 || pk < 0)
				p = Math.max(ps, pk);
			else
				p = Math.min(ps, pk);
			while (p >= 0 && a < s) {
				a++;
				q = p;
				if (p + 1 < L) {
					ps = K.indexOf(';', p + 1);
					pk = K.indexOf(',', p + 1);
					if (ps < 0 || pk < 0)
						p = Math.max(ps, pk);
					else
						p = Math.min(ps, pk);
				} else
					p = -1;
			}
			if (p < 0 && q + 1 < L && a == s)
				return K.substring(q + 1, L);
			else if (q + 1 < p && a == s)
				return K.substring(q + 1, p);
			else
				return "";
		}
	}

	private String liesW(String K, int s, int w)
	// Wort w in Satz s von K.
	{
		int p, q = 0, a = 1;
		String S = liesS(K, s).trim();
		int L = S.length();
		if (L == 0)
			return "";
		else {
			p = S.indexOf(' ');
			while (p >= 0 && a < w) {
				a++;
				while (p + 1 < L && S.charAt(p + 1) == ' ')
					p++;
				q = p;
				if (p + 1 < L)
					p = S.indexOf(' ', p + 1);
				else
					p = -1;
			}
			if (p < 0 && q < L && a == w)
				return S.substring(q, L).trim();
			else if (q < p && a == w)
				return S.substring(q, p).trim();
			else
				return "";
		}
	}

	String liesZ(String S, int z)
	// erste z Zeilen von S, falls z > 0,
	// letzte -z Zeilen von S, falls z < 0.
	{
		int p, a = 1, L;
		String Sz;
		S = S.replace((char) 13, '\n'); // Zuerst alle Return-Zeichen in
										// Zeilenwechsel-Zeichen wandeln.
		if (S == null || z == 0)
			return "";
		else if (z > 0) // Wï¿½hle erste z Zeilen aus:
		{
			L = S.length();
			p = S.indexOf('\n');
			while (p >= 0 && a < z) {
				a++;
				if (p + 1 < L)
					p = S.indexOf('\n', p + 1);
				else
					p = -1;
			}
			if (p < 0)
				Sz = S;
			else if (p > 0)
				Sz = S.substring(0, p);
			else
				Sz = "";
		} else // Wï¿½hle letzte z Zeilen aus:
		{
			z = -z;
			L = S.length();
			p = S.lastIndexOf('\n');
			while (p >= 0 && a < z) {
				a++;
				if (p > 0)
					p = S.lastIndexOf('\n', p - 1);
				else
					p = -1;
			}
			if (p < 0)
				Sz = S;
			else if (p + 1 < L)
				Sz = S.substring(p + 1, L);
			else
				Sz = "";
		}
		return Sz;
	}

	public void Echo(boolean b) {
		echo = b;
	}

	public String Spalte(String Z, int a, int b, String S)
	// Fï¿½ge den String S in die Spalten a bis abs(b) des Strings Z ein.
	// Wenn b positiv ist, wird linksbï¿½ndig, andernfalls rechtsbï¿½ndig
	// eingefï¿½gt. Die Nummerierung der Spalten beginnt mit 1. Der bis-
	// herige Inhalt der Spalten a bis b von Z wird ï¿½berschrieben. Ist
	// Z kï¿½rzer als a, so wird Z zunï¿½chst mit Leerzeichen verlï¿½ngert.
	{
		int bb = Math.abs(b), LZ, LS;
		String einfS, leer, langZ;
		if (Z == null)
			Z = "";
		if (a >= 1 && a <= bb) {
			leer = "                                                  ";
			leer += leer;
			leer += leer;
			leer += leer;
			leer += leer;

			if (b > 0)
				einfS = (S.trim() + leer).substring(0, bb - a + 1);
			else {
				einfS = leer + S.trim();
				LS = einfS.length();
				einfS = einfS.substring(LS - bb + a - 1, LS);
			}
			langZ = Z + leer;
			LZ = Math.max(bb, Z.length());
			return langZ.substring(0, a - 1) + einfS + langZ.substring(bb, LZ);
		} else
			return Z + " " + S;
	}

	public void zeigeEin(String Z)
	// Zeige die erste Zeile von Z in der Eingabezeile des Fensters an.
	{
		MyJPanelObjekt.jtf_eingabe.setText(liesZ(Z, 1));
	}

	public void spAus(String Z)
	// speichere den Text Z fï¿½r die spï¿½tere Ausgabe im Ausgabefeld. Bei
	// Aufruf von zeigeAus (X) werden alle gespeicherten Texte entsprechend
	// ihrer Reihenfolge, gefolgt von X, ausgegeben. Wenn spAus mit Para-
	// meter Z = null aufgerufen wird, dann werden alle bisher gespeicher-
	// Texte gelï¿½scht.
	{
		if (Z == null)
			AusSpeicher = "";
		else
			AusSpeicher = AusSpeicher + Z;
	}

	public void spAusZ(String Z)
	// wie spAus, jedoch wird Z mit einem Zeilenwechsel abgeschlossen.
	{
		spAus(Z + '\n');
	}

	public void zeigeAus(String Z)
	// Zeige den Text Z (nach evtl. zuvor gespeicherten Texten, siehe
	// Methode spAus) im Ausgabefeld des Fensters unter dem bisherigen
	// Inhalt des Ausgabefeldes an und/oder schreibe ihn in die Ausgabe-
	// Datei.
	{
		Z += "\n";
		if (Z != null)
			AusSpeicher = AusSpeicher + Z;

		if (inAusFeld) {
			MyJPanelObjekt.jta_ausgabe.append(AusSpeicher);
			MyJPanelObjekt.jta_ausgabe
					.setCaretPosition(MyJPanelObjekt.jta_ausgabe.getDocument()
							.getLength());
		}
		if (inDatei) {
			SenkDatei.schreibe(AusSpeicher);
		}
		testAusSpeicher += AusSpeicher;
		AusSpeicher = "";
	}

	public String leseTestAus()
	// Gibt den aktuellen Inhalt des Testspeichers aus
	{
		return testAusSpeicher;
	}

	public void loescheTestAus()
	// Leert den Testspeicher
	{
		testAusSpeicher = "";
	}

	public void testeKommando(String k) {
		Kommandozeile = k;
		Antwort("");
	}

	public void zeigeZahl(int z, int StatusZeile)
	// Zeige die Zahl z im Zahlenfeld der angegebenen Zeile der Status-
	// meldungen an. Die linke Spalte der Statusmeldungen wird mit negati-
	// ven, die rechte Spalte mit positiven Werten von StatusZeile be-
	// zeichnet. Die erste Zeile hat den Betrag 1.
	{
		int s = Math.abs(StatusZeile) - 1;
		if (0 <= s && s < StatusZeilen)
			if (StatusZeile < 0)
				MyJPanelObjekt.jtf_linksZahl[s].setText("" + z);
			else
				MyJPanelObjekt.jtf_rechtsZahl[s].setText("" + z);
	}

	public void zeigeZahl(float z, int StatusZeile)
	// Zeige die Zahl z im Zahlenfeld der angegebenen Zeile der Status-
	// meldungen an (Bedeutung von StatusZeile siehe oben).
	{
		int s = Math.abs(StatusZeile) - 1;
		if (0 <= s && s < StatusZeilen)
			if (StatusZeile < 0)
				MyJPanelObjekt.jtf_linksZahl[s].setText(MyJPanel
						.trimmeZahlString("" + z));
			else
				MyJPanelObjekt.jtf_rechtsZahl[s].setText(MyJPanel
						.trimmeZahlString("" + z));
	}

	public void zeigeZahl(String Z, int StatusZeile)
	// Zeige den Text Z im Zahlenfeld der angegebenen Zeile der Status-
	// meldungen an (Bedeutung von StatusZeile siehe oben).
	{
		int s = Math.abs(StatusZeile) - 1;
		if (0 <= s && s < StatusZeilen)
			if (StatusZeile < 0)
				MyJPanelObjekt.jtf_linksZahl[s].setText(MyJPanel
						.trimmeZahlString(liesZ(Z, 1)));
			else
				MyJPanelObjekt.jtf_rechtsZahl[s].setText(MyJPanel
						.trimmeZahlString(liesZ(Z, 1)));
	}

	public void zeigeMeld(String M, int StatusZeile)
	// Zeige die Meldung M im Meldungsfeld der angegebenen Zeile der
	// Statusmeldungen an (Bedeutung von StatusZeile siehe oben).
	{
		int s = Math.abs(StatusZeile) - 1;
		if (0 <= s && s < StatusZeilen)
			if (StatusZeile < 0)
				MyJPanelObjekt.jtf_linksMeldung[s].setText(liesZ(M, 1));
			else
				MyJPanelObjekt.jtf_rechtsMeldung[s].setText(liesZ(M, 1));
	}

	public void run() {
		while (true) {
			try {
				synchronized (this) {
					while (threadSuspendedFlag) {
						wait();
					}
				}
				if (vonDatei) {
					int ZeilenAnz = AnzZ(QuellDateiPuffer);
					for (int i = 1; i < ZeilenAnz && !threadSuspendedFlag; i++) {
						Kommandozeile = liesZ(liesZ(QuellDateiPuffer, i), -1);
						if (echo)
							zeigeAus("Dat.>>> " + Kommandozeile);
						Antwort(EinAusRahmen.EinAusObjekt.Kommandozeile);
					}
				} else {
					if (echo)
						if (tastflag)
							zeigeAus("Tast>>> " + Kommandozeile);
						else
							zeigeAus("Maus>>> " + Kommandozeile);
					Antwort(EinAusRahmen.EinAusObjekt.Kommandozeile);
				}
				threadSuspendedFlag = true;
				MyJPanelObjekt.enableStartButton();
			} catch (InterruptedException e) {
				;
			}
		}// while
	}

	public void erzeugeGui(String Kopfzeile) {

		gui = new JFrame();

		gui.setTitle(Kopfzeile);
		MenuEventHandler menu_evnt_hndlr = new MenuEventHandler();
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				EinAusRahmen.EinAusObjekt.prgEnde();
			}
		});

		/*************************************************************/
		/* Menuestruktur erzeugen: */
		/*************************************************************/

		JMenuBar menuBar;
		JMenu eingabe_menu, ausgabe_menu, help_menu;
		JMenuItem menuItem;

		// Menueleiste erzeugen:
		menuBar = new JMenuBar();
		gui.setJMenuBar(menuBar);

		// eingabe_menu aufbauen:
		eingabe_menu = new JMenu("Datenquelle");
		menuBar.add(eingabe_menu);
		eingabe_menu
				.add(menuItem = new JMenuItem("interaktiv aus Eingabezeile"));
		menuItem.addActionListener(menu_evnt_hndlr);
		eingabe_menu.add(menuItem = new JMenuItem("aus Eingabe-Datei"));
		menuItem.addActionListener(menu_evnt_hndlr);
		eingabe_menu.add(menuItem = new JMenuItem("wähle Eingabe-Datei"));
		menuItem.addActionListener(menu_evnt_hndlr);

		// ausgabe_menu aufbauen:
		ausgabe_menu = new JMenu("Datensenke");
		menuBar.add(ausgabe_menu);
		ausgabe_menu.add(menuItem = new JMenuItem("nur Anzeige"));
		menuItem.addActionListener(menu_evnt_hndlr);
		ausgabe_menu.add(menuItem = new JMenuItem("Anzeige und Ausgabe Datei"));
		menuItem.addActionListener(menu_evnt_hndlr);
		ausgabe_menu.add(menuItem = new JMenuItem("nur Ausgabe Datei"));
		menuItem.addActionListener(menu_evnt_hndlr);
		ausgabe_menu.add(menuItem = new JMenuItem("wähle Ausgabe Datei"));
		menuItem.addActionListener(menu_evnt_hndlr);

		// help_menu aufbauen:
		help_menu = new JMenu("Hilfe / Ende");
		menuBar.add(help_menu);
		help_menu.add(menuItem = new JMenuItem(
				"Hilfe zur Programmierschnittstelle"));
		menuItem.addActionListener(menu_evnt_hndlr);
		help_menu.add(menuItem = new JMenuItem("Hilfe zum Anwendungsprogramm"));
		menuItem.addActionListener(menu_evnt_hndlr);
		help_menu.add(menuItem = new JMenuItem("über den EinAusRahmen..."));
		menuItem.addActionListener(menu_evnt_hndlr);
		help_menu.addSeparator();
		help_menu.add(menuItem = new JMenuItem("Programm-Ende"));
		menuItem.addActionListener(menu_evnt_hndlr);

		/*************************************************************/
		/* In JFrame ein MyJPanel mit GridBagLayout einfuegen: */
		/*************************************************************/
		Container contentPane = gui.getContentPane();
		MyJPanelObjekt = new MyJPanel();
		contentPane.add("Center", MyJPanelObjekt);
		gui.setResizable(false);
		gui.pack();
		gui.setVisible(true);
	}

	public void prgEnde() {
		EinAusRahmen.EinAusObjekt.Ende();
		if (!EinAusRahmen.AusgabeDatei.equals("")) {
			SenkDatei.schliesse();
		}
		System.exit(0);
	}
}

class MenuEventHandler implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		String ac;
		ac = e.getActionCommand();

		/***************************************************/
		/* EINGABE-MENUE: */
		/***************************************************/

		if (ac.equals("interaktiv aus Eingabezeile")) {
			EinAusRahmen.EinAusObjekt
					.zeigeAus("Aktuelle Datenquelle: Eingabezeile.");
			EinAusRahmen.vonDatei = false;
		}

		if (ac.equals("aus Eingabe-Datei")) {
			if (EinAusRahmen.EingabeDatei.equals("")) {
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Sie haben noch keine Datei ausgewählt, die Sie als Datenquelle verwenden wollen.");
			} else {
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Aktuelle Datenquelle: Eingabe Datei ("
								+ EinAusRahmen.EingabeDatei + ").");
				EinAusRahmen.vonDatei = true;
			}
		}

		if (ac.equals("wï¿½hle Eingabe-Datei")) {
			if (QuellDatei.zeigeDialog()) { // if-Bedingung erfuellt, falls
											// Datei ausgewaehlt wurde.
				QuellDatei.readFileToBuffer();
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Aktuelle Datenquelle: Eingabe Datei ("
								+ EinAusRahmen.EingabeDatei + ")");
				EinAusRahmen.vonDatei = true;

				EinAusRahmen.EinAusObjekt.spAusZ(EinAusRahmen.EinAusObjekt
						.liesZ(EinAusRahmen.QuellDateiPuffer, 20));/*
																	 * EinAusObjekt.
																	 * QuellDateiPuffer
																	 */
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Dies war der Anfang der Eingabe-Datei.");
			}
		}

		/***************************************************/
		/* AUSGABE-MENUE: */
		/***************************************************/

		if (ac.equals("nur Anzeige")) {
			EinAusRahmen.EinAusObjekt.zeigeAus("Aktuelle Datensenke: Anzeige.");
			EinAusRahmen.inAusFeld = true;
			EinAusRahmen.inDatei = false;
		}

		if (ac.equals("Anzeige und Ausgabe Datei")) {
			if (EinAusRahmen.AusgabeDatei.equals("")) {
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Sie haben noch keine Datei ausgewählt, die Sie als Datensenke verwenden wollen.");
			} else {
				EinAusRahmen.inAusFeld = true;
				EinAusRahmen.inDatei = true;
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Aktuelle Datensenke: Anzeige und Ausgabe Datei ("
								+ EinAusRahmen.AusgabeDatei + ").");
			}
		}

		if (ac.equals("nur Ausgabe Datei")) {
			if (EinAusRahmen.AusgabeDatei.equals("")) {
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Sie haben noch keine Datei ausgewählt, die Sie als Datensenke verwenden wollen.");
			} else {
				EinAusRahmen.inDatei = true;
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Aktuelle Datensenke: Ausgabe Datei ("
								+ EinAusRahmen.AusgabeDatei + ").");
				EinAusRahmen.inAusFeld = false;
			}
		}

		if (ac.equals("wähle Ausgabe Datei")) {
			String bisherigeAusgabeDatei = EinAusRahmen.AusgabeDatei;
			if (SenkDatei.zeigeDialog()) { // if-Bedingung erfuellt, falls Datei
											// ausgewaehlt wurde.
				if (!bisherigeAusgabeDatei.equals("")) { // Ggf. bisherige
															// Datensenk-Datei
															// schliessen.
					SenkDatei.schliesse(); // Kein Problem! (Schliessen der
											// bisherigen Datei funktioniert
											// obwohl bereits ein neuer
											// Dateiname ausgewï¿½hlt wurde)
				}
				SenkDatei.oeffne();
				EinAusRahmen.inAusFeld = true;
				EinAusRahmen.inDatei = true;
				EinAusRahmen.EinAusObjekt
						.zeigeAus("Aktuelle Datensenke: Anzeige und Ausgabe Datei ("
								+ EinAusRahmen.AusgabeDatei + ").");
			}
		}

		/***************************************************/
		/* HELP-MENUE: */
		/***************************************************/

		if (ac.equals("Hilfe zur Programmierschnittstelle")) {

			String m = "";

			m += "ÜBERSICHT ZUR PROGRAMMIERSCHNITTSTELLE ZUM EINAUSRAHMEN:\n\n";

			m += "Im folgenden sind nur die Signaturen der einzelnen Methoden der Programmierschnittstelle aufgeführt.\n";
			m += "Eine vollstï¿½ndige Beschreibung der Funktionsweise dieser Methoden wird bei Praktikumsbeginn zur Verfügung gestellt.\n";
			m += "Die hier vorliegende Übersicht soll bei Kenntnis der Programmierschnittstelle als 'schnelle Gedächtnisstütze' dienen.\n\n";

			m += "ZU ÜBERSCHREIBENDE METHODEN:\n";
			m += "void Hilfe(),   void Antwort(String Kommando),   void Ende()\n\n\n";

			m += "METHODEN ZUM LESEN DER EINGABE:\n";
			m += "int SatzAnz(String K),   int WortAnz(String K, int s),   String Wort(String K, int s, int w)\n";
			m += "int liesInt(String K, int s, int w),   long liesLong(String K, int s, int w)\n";
			m += "float liesFloat(String K, int s, int w),   int SatzAnz(),   int WortAnz(int s)\n";
			m += "String Wort(int s, int w),   int liesInt(int s, int w),   long liesLong(int s, int w)\n";
			m += "int liesFloat(int s, int w),   boolean ungueltig()\n\n\n";

			m += "METHODEN ZUM SCHREIBEN DER AUSGABE:\n";
			m += "void Echo(boolean ein),   String Spalte(String Z, int a, int b, String S)\n";
			m += "void zeigeEin(String Z),   void spAus(String Z),   void spAusZ(String Z)\n";
			m += "void zeigeAus(String Z),   void zeigeZahl(int z, int StatusZeile)\n";
			m += "void zeigeZahl(float z, int StatusZeile),   void zeigeZahl(String Z, int StatusZeile)\n";
			m += "void zeigeMeld(String M, int StatusZeile)\n\n\n";

			m += "METHODEN ZUR STEUERUNG DER VERARBEITUNG:\n";
			m += "boolean abgebrochen(),   void brichab()\n";

			JOptionPane.showMessageDialog(EinAusRahmen.EinAusObjekt.getGui(),
					m, "Hilfe zur Programmierschnittstelle...",
					JOptionPane.INFORMATION_MESSAGE);
		}

		if (ac.equals("Hilfe zum Anwendungsprogramm"))
			EinAusRahmen.EinAusObjekt.Hilfe();

		if (ac.equals("über den EinAusRahmen...")) {
			Icon unilogo;
			String m = "";
			m += "Der EinAusRahmen ist eine Java Klasse zur Unterstützung des\n";
			m += "Praktikums 'Software Entwicklung und Programmierung (SEP)'\n";
			m += "Sie benutzen derzeit die Version 1.2 des EinAusRahmens.\n\n";
			m += "Infos zum Praktikum:\n";
			m += "   www.informatik.uni-essen.de/sep\n\n";
			m += "K. Echtle, M. Jochim\n";
			m += "Universitï¿½t GH-Essen\n";
			m += "Fachbereich 6 / Informatik\n";

			unilogo = new ImageIcon("keb"
					+ System.getProperty("file.separator") + "uniseplogo.jpg");
			JOptionPane.showMessageDialog(EinAusRahmen.EinAusObjekt.getGui(),
					m, "ï¿½ber den EinAusRahmen...",
					JOptionPane.INFORMATION_MESSAGE, unilogo);
		}

		if (ac.equals("Programm-Ende")) {
			Icon unilogo;
			String m = "";

			m += "Das Programm wird nun beendet.\n";
			m += "Ist das Ihre Absicht ?";

			unilogo = new ImageIcon("keb"
					+ System.getProperty("file.separator") + "uniseplogo.jpg");
			if (JOptionPane.showConfirmDialog(
					EinAusRahmen.EinAusObjekt.getGui(), m,
					"Programm beenden...", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, unilogo) == JOptionPane.YES_OPTION) {
				EinAusRahmen.EinAusObjekt.prgEnde();
			}
		}
	}
}

class QuellDatei {
	static File currentDir = null; // Die Datenfelder 'currentDir' und
									// 'selectedFile' sind...
	static File selectedFile = null; // ... fuer die beiden Klassen 'QuellDatei'
										// _und_ 'SenkDatei' relevant.

	static String filename = "";

	static boolean zeigeDialog() {
		// zeigeDialog() liefert 'true', wenn eine Datei ausgewaehlt wurde.
		// Der Name der ausgewï¿½hlten Datei wird in 'EinAusRahmen.EingabeDatei'
		// abgelegt.
		// Wird keine Datei ausgewï¿½hlt, dann wird der Wert von
		// 'EinAusRahmen.EingabeDatei'
		// nicht modifiziert.

		boolean flag = false;

		JFileChooser chooser = new JFileChooser(currentDir);
		ExampleFileFilter SEPFilter = new ExampleFileFilter("se",
				"SEP-Eingabedatei");
		chooser.addChoosableFileFilter(SEPFilter);
		chooser.setFileFilter(SEPFilter);
		ExampleFileView fileView = new ExampleFileView();

		fileView.putIcon("se",
				new ImageIcon("keb" + System.getProperty("file.separator")
						+ "sepfiles.jpg"));
		chooser.setFileView(fileView);

		int choise = chooser.showOpenDialog(EinAusRahmen.MyJPanelObjekt);
		if (choise == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			if (theFile != null) {
				selectedFile = chooser.getSelectedFile();
				filename = chooser.getSelectedFile().getAbsolutePath();
				flag = true;
			}
		}
		currentDir = chooser.getCurrentDirectory();
		EinAusRahmen.EingabeDatei = filename;
		return flag;
	}

	static void readFileToBuffer() {
		String line;

		filename = EinAusRahmen.EingabeDatei;
		EinAusRahmen.QuellDateiPuffer = "";
		try {
			File f = new File(filename);
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader rfis = new InputStreamReader(fis);
			LineNumberReader lrfis = new LineNumberReader(rfis);

			do {
				line = lrfis.readLine();

				if (line != null) {
					EinAusRahmen.QuellDateiPuffer += (line + "\n");
				}
			} while (line != null);
			lrfis.close();
		} catch (FileNotFoundException e) {
			System.out.println("f=" + filename);
			EinAusRahmen.EinAusObjekt.zeigeAus("SCHWERER FEHLER! Die Datei: "
					+ filename + " wurde nicht gefunden.");
		} catch (IOException e) {
			EinAusRahmen.EinAusObjekt.zeigeAus("SCHWERER FEHLER! Die Datei: "
					+ filename + " kann nicht gelesen werden.");
		}
	}

}

class SenkDatei {

	static String filename = "";
	static BufferedWriter SenkDateiWriter;

	static boolean zeigeDialog() {
		// zeigeDialog() liefert 'true', wenn eine Datei ausgewaehlt wurde.
		// Der Name der ausgewählten Datei wird in 'EinAusRahmen.AusgabeDatei'
		// abgelegt.
		// Wird keine Datei ausgewählt, dann wird der Wert von
		// 'EinAusRahmen.AusgabeDatei'
		// nicht modifiziert.

		boolean flag = false;

		JFileChooser chooser = new JFileChooser(QuellDatei.currentDir);
		ExampleFileFilter SEPFilter = new ExampleFileFilter("sa",
				"SEP-Ausgabedatei");
		chooser.addChoosableFileFilter(SEPFilter);
		chooser.setFileFilter(SEPFilter);
		ExampleFileView fileView = new ExampleFileView();
		fileView.putIcon("sa",
				new ImageIcon("keb" + System.getProperty("file.separator")
						+ "sepfiles.jpg"));
		chooser.setFileView(fileView);

		int choise = chooser.showDialog(EinAusRahmen.MyJPanelObjekt, "Choose");
		if (choise == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			if (theFile != null) {
				flag = true;
				filename = chooser.getSelectedFile().getAbsolutePath();
				if (!filename.endsWith(".sa")) {
					filename = filename + ".sa";
				}
			}
		}
		QuellDatei.currentDir = chooser.getCurrentDirectory();
		QuellDatei.selectedFile = chooser.getSelectedFile();
		EinAusRahmen.AusgabeDatei = filename;

		return flag;
	}

	static void oeffne() {
		try {
			FileOutputStream fos = new FileOutputStream(filename, true);
			OutputStreamWriter wfos = new OutputStreamWriter(fos);
			SenkDateiWriter = new BufferedWriter(wfos);
		} catch (IOException e) {
			EinAusRahmen.EinAusObjekt.zeigeAus("SCHWERER FEHLER! Die Datei: "
					+ filename + " laesst sich nicht als Datensenke oeffnen.");
		}
	}

	static void schreibe(String z) {
		try {
			SenkDateiWriter.write(z, 0, z.length());
			SenkDateiWriter.flush();
		} catch (IOException e) {
			EinAusRahmen.EinAusObjekt
					.zeigeAus("SCHWERER FEHLER! Ein Versuch Daten in die Datensenke: "
							+ filename + " zu schreiben schlug fehl.");
		}
	}

	static void schliesse() {
		try {
			SenkDateiWriter.flush();
			SenkDateiWriter.close();
		} catch (IOException e) {
			EinAusRahmen.EinAusObjekt
					.zeigeAus("SCHWERER FEHLER! Die Datensenke: " + filename
							+ " laesst sich nicht schliessen.");
		}
	}
}

class MyJPanel extends JPanel implements ActionListener {

	static final long serialVersionUID = 0;

	private JLabel jl_eingabe, jl_ausgabe;
	JTextField jtf_eingabe;
	JTextArea jta_ausgabe;
	private JScrollPane jsp;
	private JButton start_b, stop_b, loesch_b;
	// JLabel[] jl_linksZahl, jl_linksMeldung, jl_rechtsZahl, jl_rechtsMeldung;
	JTextField[] jtf_linksZahl, jtf_linksMeldung, jtf_rechtsZahl,
			jtf_rechtsMeldung;
	private JPanel jp_hilfe, jp_hilfe1, jp_hilfe2;

	private static final int BREITE_AUSGABEFELD = 125;
	private static final int BREITE_EINGABEFELD = 45;

	private static final int BREITE_ZAHLEN_IN_STATUSZEILE = 18;
	private static final int BREITE_MELDUNGEN_IN_STATUSZEILE = 42;

	MyJPanel() {
		/******************************************************************************/
		/*                                                                            */
		/*
		 * Das MyJPanel-Objekt wird mit einer Reihe von Gui-Komponenten
		 * bestueckt.
		 */
		/* Hierbei ist die Vorgehensweis stets die folgende: */
		/*
		 * Für jede "Zeile" die eine oder mehrere Gui-Komponenten enthaelt
		 * wird
		 */
		/* dem Bezeichner 'jp_hilfe' ein neues JPanel-Objekt mit GridBagLayout */
		/* zugewiesen. 'jp_hilfe' wird dann jeweils mit den in diese Zeile */
		/*
		 * gehoerenden Gui-Komponenten bestueckt und anschliessend wird -
		 * wiederum
		 */
		/* unter Benutzung des GridBagLayouts - das Objekt jp_hilfe in das */
		/* MyJPanel-Objekt eingefuegt. */
		/*                                                                            */
		/******************************************************************************/

		super(new GridBagLayout(), true); // double buffered Panel.

		/*********************************************************************/
		/* Eingabe-JLabel und Eingabe-JTextField: */
		/*********************************************************************/
		jl_eingabe = new JLabel("Eingabe in dieser Zeile:");
		jtf_eingabe = new JTextField(BREITE_EINGABEFELD);

		jp_hilfe = new JPanel(new GridBagLayout(), true); // double buffered
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(jl_eingabe,
				LittleHelpers.make_westanchored_constrain(0, 0, 1, 1));
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(jtf_eingabe,
				LittleHelpers.make_westanchored_constrain(1, 0, 1, 1));
		jp_hilfe.add(jl_eingabe);
		jp_hilfe.add(jtf_eingabe);

		((GridBagLayout) this.getLayout()).setConstraints(jp_hilfe,
				LittleHelpers.make_westanchored_constrain(0, 0, 1, 1));
		add(jp_hilfe);

		// Event Handling verdrahten:
		jtf_eingabe.addActionListener(this);

		/*********************************************************************/
		/* JLabel einfuegen um das Ausgabefeld zu beschriften: */
		/*********************************************************************/
		jl_ausgabe = new JLabel("Ausgaben nur in diesem Feld:");

		jp_hilfe = new JPanel(new GridBagLayout(), true); // double buffered
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(jl_ausgabe,
				LittleHelpers.make_westanchored_constrain(0, 0, 1, 1));
		jp_hilfe.add(jl_ausgabe);

		((GridBagLayout) this.getLayout()).setConstraints(jp_hilfe,
				LittleHelpers.make_westanchored_constrain(0, 1, 1, 1));
		add(jp_hilfe);

		/*********************************************************************/
		/* JTextArea in ein JScrollPane einbetten und dann einfuegen: */
		/* Die JTextArea ist das Ausgabefeld */
		/*********************************************************************/
		jta_ausgabe = new JTextArea("",
				Math.max(EinAusRahmen.AusgabeZeilen, 5), BREITE_AUSGABEFELD);
		jta_ausgabe.setFont(new Font("Monospaced", Font.PLAIN, 11));

		// ACHTUNG!!! In der folgenden Zeile niemals die Werte '*_ALWAYS' gegen
		// '*_AS_NEEDED' oder '*_NEVER'
		// austauschen. Es existiert (zumindest in der jdk Version 1.2 für NT)
		// ein Java-Bug, der sonst unter
		// bestimmten Umstï¿½nden den gesamten Bildschirmaufbau durcheinander
		// bringt.
		//
		jsp = new JScrollPane(jta_ausgabe,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jta_ausgabe.setEditable(false);
		jta_ausgabe.setBackground(Color.white);

		jp_hilfe = new JPanel(new GridBagLayout(), true); // double buffered
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(jsp,
				LittleHelpers.make_westanchored_constrain(0, 0, 1, 1));
		jp_hilfe.add(jsp);

		((GridBagLayout) this.getLayout()).setConstraints(jp_hilfe,
				LittleHelpers.make_westanchored_constrain(0, 2, 1, 1));
		add(jp_hilfe);

		/******************************************************************************************************************/
		/*
		 * Buttons für "start Berechung" und "Berechnung abbrechen" und
		 * "Ausgabefeld löschen" einfuegen:
		 */
		/******************************************************************************************************************/

		start_b = new JButton("starte Berechnung");
		stop_b = new JButton("Berechnung abbrechen");
		loesch_b = new JButton("Ausgabefeld loeschen");
		stop_b.setEnabled(false);

		JLabel jl_dummy = new JLabel("                              "); // Abstand
																		// zwischen
																		// Berechnungs-Buttons
																		// und
																		// dem
																		// Ausgabefeld-lï¿½schen-Button.

		jp_hilfe = new JPanel(new GridBagLayout(), true); // double buffered
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(start_b,
				LittleHelpers.make_westanchored_constrain(0, 0, 1, 1));
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(stop_b,
				LittleHelpers.make_westanchored_constrain(1, 0, 1, 1));
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(jl_dummy,
				LittleHelpers.make_westanchored_constrain(2, 0, 1, 1));
		((GridBagLayout) jp_hilfe.getLayout()).setConstraints(loesch_b,
				LittleHelpers.make_westanchored_constrain(3, 0, 1, 1));
		jp_hilfe.add(start_b);
		jp_hilfe.add(stop_b);
		jp_hilfe.add(jl_dummy);
		jp_hilfe.add(loesch_b);

		((GridBagLayout) this.getLayout()).setConstraints(jp_hilfe,
				LittleHelpers.make_westanchored_constrain(0, 3, 1, 1));
		add(jp_hilfe);

		// Event Handling verdrahten:
		start_b.addActionListener(this);
		stop_b.addActionListener(this);
		loesch_b.addActionListener(this);

		/*************************************************************************************/
		/* Erforderlich Anzahl von Statuszeilen erzeugen und einfuegen: */
		/*
		 * (Jede Statuszeile ist in eine linke und eine rechte Spalte
		 * unterteilt.
		 */
		/*
		 * Jede Spalte besteht aus einem JTextField das fuer die Ausgabe einer
		 * Zahl
		 */
		/* gedacht ist und einem JTextField das fuer die Ausgabe einer Meldung */
		/* gedacht ist.) */
		/*************************************************************************************/

		int anz_statusz = Math.max(EinAusRahmen.StatusZeilen, 1);

		jtf_linksZahl = new JTextField[anz_statusz];
		jtf_linksMeldung = new JTextField[anz_statusz];
		jtf_rechtsZahl = new JTextField[anz_statusz];
		jtf_rechtsMeldung = new JTextField[anz_statusz];

		jp_hilfe2 = new JPanel(new GridBagLayout(), true); // double buffered

		for (int i = 0; i < anz_statusz; i++) {

			jtf_linksZahl[i] = new JTextField(BREITE_ZAHLEN_IN_STATUSZEILE);
			jtf_linksMeldung[i] = new JTextField(
					BREITE_MELDUNGEN_IN_STATUSZEILE);
			jtf_rechtsZahl[i] = new JTextField(BREITE_ZAHLEN_IN_STATUSZEILE);
			jtf_rechtsMeldung[i] = new JTextField(
					BREITE_MELDUNGEN_IN_STATUSZEILE);

			jtf_linksZahl[i].setBorder(BorderFactory.createLineBorder(
					Color.black, 1));
			jtf_linksMeldung[i].setBorder(BorderFactory.createLineBorder(
					Color.black, 1));
			jtf_rechtsZahl[i].setBorder(BorderFactory.createLineBorder(
					Color.black, 1));
			jtf_rechtsMeldung[i].setBorder(BorderFactory.createLineBorder(
					Color.black, 1));

			jtf_linksZahl[i].setBackground(Color.white);
			jtf_linksMeldung[i].setBackground(Color.white);
			jtf_rechtsZahl[i].setBackground(Color.white);
			jtf_rechtsMeldung[i].setBackground(Color.white);

			jtf_linksZahl[i].setEditable(false);
			jtf_linksMeldung[i].setEditable(false);
			jtf_rechtsZahl[i].setEditable(false);
			jtf_rechtsMeldung[i].setEditable(false);

			jtf_linksZahl[i].setFont(new Font("Monospaced", Font.BOLD, 11));
			jtf_linksMeldung[i].setFont(new Font("Monospaced", Font.BOLD, 11));
			jtf_rechtsZahl[i].setFont(new Font("Monospaced", Font.BOLD, 11));
			jtf_rechtsMeldung[i].setFont(new Font("Monospaced", Font.BOLD, 11));

			jp_hilfe1 = new JPanel(new GridBagLayout(), true); // double
																// buffered

			((GridBagLayout) jp_hilfe1.getLayout()).setConstraints(
					jtf_linksZahl[i], LittleHelpers.make_constrain(0, 0, 1, 1));
			((GridBagLayout) jp_hilfe1.getLayout()).setConstraints(
					jtf_linksMeldung[i],
					LittleHelpers.make_constrain(1, 0, 1, 1));
			((GridBagLayout) jp_hilfe1.getLayout())
					.setConstraints(jtf_rechtsZahl[i],
							LittleHelpers.make_constrain(2, 0, 1, 1));
			((GridBagLayout) jp_hilfe1.getLayout()).setConstraints(
					jtf_rechtsMeldung[i],
					LittleHelpers.make_constrain(3, 0, 1, 1));
			jp_hilfe1.add(jtf_linksZahl[i]);
			jp_hilfe1.add(jtf_linksMeldung[i]);
			jp_hilfe1.add(jtf_rechtsZahl[i]);
			jp_hilfe1.add(jtf_rechtsMeldung[i]);

			((GridBagLayout) jp_hilfe2.getLayout()).setConstraints(jp_hilfe1,
					LittleHelpers.make_westanchored_constrain(0, i, 1, 1));

			jp_hilfe2.add(jp_hilfe1);
		}

		jp_hilfe2.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 1),
				" Statuszeile(n) "));
		((GridBagLayout) this.getLayout()).setConstraints(jp_hilfe2,
				LittleHelpers.make_westanchored_constrain(0, 4, 1, 1));
		add(jp_hilfe2);

	}

	static String trimmeZahlString(String s) {
		// Hilfsmethode fuer Werte die in der Statuszeile erscheinen sollen.
		// Fuellt den String s mit Leerzeichen auf, so das er die in
		// BREITE_ZAHLEN_IN_STATUSZEILE festgelegte Laenge hat.
		// Ist der String s zu lang, dann wird er abgeschnitten.

		String leer = "                                                                                       ";
		leer += leer;
		leer += leer;
		leer += leer; // Ausreichenden Vorrat an Leerzeichen anlegen.

		s = " " + s; // Fuehrendes Leerzeichen anbringen.

		if (s.length() >= BREITE_ZAHLEN_IN_STATUSZEILE) {
			return s.substring(0, BREITE_ZAHLEN_IN_STATUSZEILE - 1);
		} else {
			String tmp = leer.substring(0,
					BREITE_ZAHLEN_IN_STATUSZEILE - s.length());
			return s + tmp;
		}
	}

	static String trimmeMeldungString(String s) {
		// Hilfsmethode fuer Meldungen die in der Statuszeile erscheinen sollen.
		// Fuellt den String s mit Leerzeichen auf, so das er die in
		// BREITE_MELDUNGEN_IN_STATUSZEILE festgelegte Laenge hat.
		// Ist der String s zu lang, dann wird er abgeschnitten.

		String leer = "                                                                                        ";
		leer += leer;
		leer += leer;
		leer += leer; // Ausreichenden Vorrat an Leerzeichen anlegen.

		s = " " + s; // Fuehrendes Leerzeichen anbringen.

		if (s.length() >= BREITE_MELDUNGEN_IN_STATUSZEILE) {
			return s.substring(0, BREITE_MELDUNGEN_IN_STATUSZEILE - 1);
		} else {
			String tmp = leer.substring(0,
					BREITE_MELDUNGEN_IN_STATUSZEILE - s.length());
			return s + tmp;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start_b || e.getSource() == jtf_eingabe) { // Start-Button
																		// oder
																		// Return
																		// im
																		// Eingabefeld.

			if (e.getSource() == start_b) // Merken ob Start-Button oder
											// Return...
				EinAusRahmen.tastflag = false;// EinAusRahmen.EinAusObjekt.tastflag
												// = false; // ...aktiviert
												// wurde, da dies Information...
			else
				// ...benötigt wird, sofern das Echo via...
				EinAusRahmen.tastflag = true; // EinAusRahmen.EinAusObjekt.tastflag
												// = true; //
												// ...Echo(true)-Aufruf
												// eingeschaltet wurde.

			EinAusRahmen.threadSuspendedFlag = false;
			enableStopButton();
			// EinAusRahmen.EinAusObjekt.zeigeAus("Anwendungsprogramm startet...");
			EinAusRahmen.EinAusObjekt.Kommandozeile = jtf_eingabe.getText();
			synchronized (EinAusRahmen.EinAusObjekt) {
				EinAusRahmen.EinAusObjekt.notify();
			}
		}

		if (e.getSource() == stop_b) {
			enableStartButton();
			EinAusRahmen.threadSuspendedFlag = true;
			EinAusRahmen.EinAusObjekt
					.zeigeAus("Abbruchanforderung wird dem Anwendungsprogramm vorgelegt.");
		}

		if (e.getSource() == loesch_b) {
			jta_ausgabe.setText(null);
		}
	}

	void enableStopButton() {
		start_b.setEnabled(false);
		stop_b.setEnabled(true);
	}

	void enableStartButton() {
		stop_b.setEnabled(false);
		start_b.setEnabled(true);
	}

}

class LittleHelpers {

	/*************************************************************/
	/* Hilfsmethode zum Erzeugen von GridBagConstraints: */
	/*************************************************************/
	static GridBagConstraints make_constrain(int grid_x, int grid_y,
			int grid_width, int grid_height) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = grid_x;
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}

	static GridBagConstraints make_constrain(int grid_x, int grid_y,
			int grid_width, int grid_height, int insets_all_sides) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = grid_x;
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(insets_all_sides, insets_all_sides,
				insets_all_sides, insets_all_sides);

		return c;
	}

	static GridBagConstraints make_westanchored_constrain(int grid_x,
			int grid_y, int grid_width, int grid_height) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = grid_x;
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}

	static GridBagConstraints make_westanchored_constrain(int grid_x,
			int grid_y, int grid_width, int grid_height, int insets_all_sides) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = grid_x;
		c.gridy = grid_y;
		c.gridwidth = grid_width;
		c.gridheight = grid_height;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(insets_all_sides, insets_all_sides,
				insets_all_sides, insets_all_sides);

		return c;
	}
}