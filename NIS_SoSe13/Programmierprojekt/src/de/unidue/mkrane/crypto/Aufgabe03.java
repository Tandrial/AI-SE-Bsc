package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	Modulo
//		Modulo‐Berechnung zweier Integer.
//	Input: ia[0]: Integerzahl1, ia[1]: Integerzahl2
//	Output: ia[0] mod ia[1]

public class Aufgabe03 {
	
	public static String getSolution(TaskObject t) {
		return getSolution(t.getIntArray(0), t.getIntArray(1));
	}	

	public static String getSolution(int ia0, int ia1) {
		return "" + Utils.Modulo(ia0, ia1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 03
		int ia0 = 5021129;
		int ia1 = 257;

		System.out.println("120     <<<Musterlösung");
		System.out.println(Aufgabe03.getSolution(ia0, ia1));
	}
}