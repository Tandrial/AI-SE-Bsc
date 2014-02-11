package allgemein;

import java.util.Arrays;
import java.util.List;

public class Konstanten {

	public static double VERSION = 1.0; /* Programmversion */
	// alle Wochentage
	public static int alleTage = 0;
	// Die Wochentage
	public static int montag = 1;
	public static int dienstag = 2;
	public static int mittwoch = 3;
	public static int donnerstag = 4;
	public static int freitag = 5;

	/**
	 * Aktiviert bzw. deaktiviert Konsolenausgaben zu Debuggingzwecken
	 */
	public static final boolean DEBUG = false;

	public static final int MAX_FILE_SIZE = 20;
	public static final long MAX_GRAPH_SIZE = 58000;

	public static final String MUELLART_ALLE = "Bitte auswaehlen"; // Kodierung:
																	// 0
	public static final String MUELLART_PLASTIK = "Plastik"; // 1
	public static final String MUELLART_REST = "Restmuell"; // 2
	public static final String MUELLART_PAPIER = "Papier"; // 3
	public static final String MUELLART_BIO = "Biomuell"; // 4

	public static final String FAHRZEUG_ART_SMALL = "SEP-Mini";
	public static final String FAHRZEUG_ART_MEDIUM = "SEP-Medium";
	public static final String FAHRZEUG_ART_LARGE = "SEP-Maxi";

	public static final int FAHRZEUG_CAP_SMALL = 10000;
	public static final int FAHRZEUG_CAP_MEDIUM = 17000;
	public static final int FAHRZEUG_CAP_LARGE = 28000;

	// Muellmenge pro Meter
	public static final float MUELLMENGE_HAUPT = 300 / 100f; // 300 L auf 100 m
	public static final List<String> HAUPTSTRASSENTYPEN = Arrays
			.asList(new String[] { "primary", "primary_link", "secondary",
					"secondary_link", "tertiary", "tertiary_link", "road" });

	public static final float MUELLMENGE_NEBEN = 160 / 100f; // 160 L auf 100 m
	public static final List<String> NEBENSTRASSENTYPEN = Arrays
			.asList(new String[] { "living_street", "residential" });

	public static final List<String> NULLMUELLSTRASSE = Arrays
			.asList(new String[] { /* "service", */"gesperrt" });

	public static final float maxBeladung = 0.95f;

	// Maximale Muellmenge, die ein Fahrzeug an einem Tag (8 Std.) entsorgen
	// kann
	/*
	 * ANNAHMEN: entleeren dauert inkl. Fahrten 30 min; 
	 * Alle Werte sind Durchschnittswerte ueber alle Strassentypen
	 * 
	 * LARGE -> 4 h zum fuellen -> max 1/Tag entleeren 
	 * = 30 min + 30 min für letztes Abladen und Rueckkehr zum Depot 
	 * = 1h verlust 
	 * --- 2 Fahrten/Tag, wobei bei einer 25% weniger aufgenommen wird <=> 175% ---
	 * 
	 * MEDIUM -> 3 h zum fuellen -> max 2/Tag entleeren 
	 * = 2 * 30 min + 30 min für letztes Abladen und Rueckkehr zum Depot 
	 * = 1.5h verlust 
	 * --- 3 Fahrten/Tag, wobei bei einer 50% weniger aufgenommen wird <=> 250% ---
	 * 
	 * SMALL -> 2 h zum fuellen -> max 3/Tag entleeren 
	 * = 3 * 30 min + 30 min für letztes Abladen und Rueckkehr zum Depot 
	 * = 2h verlust 
	 * --- 3 Fahrten/Tag <=> 300% ---
	 */
	public static final int maxMuellProTag_SMALL = 3 * FAHRZEUG_CAP_SMALL;
	public static final int maxMuellProTag_MEDIUM = (int) (2.5 * FAHRZEUG_CAP_MEDIUM);
	public static final int maxMuellProTag_LARGE = (int) (1.75 * FAHRZEUG_CAP_LARGE);

	public static float getMengeTyp(String tag) {
		if (HAUPTSTRASSENTYPEN.contains(tag))
			return MUELLMENGE_HAUPT;
		if (NEBENSTRASSENTYPEN.contains(tag))
			return MUELLMENGE_NEBEN;
		return 0f;
	}

}
