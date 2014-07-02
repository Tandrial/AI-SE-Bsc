package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	AES: Multiplikation im Raum GF8
//		Multiplikation zweier HEX‐Zahlen in GF(28)
//	Input: sa[0]: HEX‐Zahl1 als String, sa[1]: HEX‐Zahl2 als String
//	Output: Ergebnis der GF8‐Multiplikation als HEX‐String (Groß‐/Kleinschreibung wird 
//		nicht berücksichtigt.) (z.B. „2f“) Führende Nullen sind mit anzugeben.

public class Aufgabe10 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1));
	}

	public static String getSolution(String sa0, String sa1) {
		sa0 = Utils.HexToBin(sa0);
		sa1 = Utils.HexToBin(sa1);		

		return Utils.BinToHex(Utils.GF8(sa0, sa1));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 10
		String sa0 = "02";
		String sa1 = "0c";

		System.out.println("18     <<<Musterlösung");
		System.out.println(Aufgabe10.getSolution(sa0, sa1));
	}
}