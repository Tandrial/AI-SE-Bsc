package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	XOR
//		Binäre XOR‐Verknüpfung zweier HEX‐Strings.
//	Input: sa[0], sa[1] (jeweils ein zufälliger HEX‐String)
//	Output: Binäre Zahl als ein String (z.B. „101001010…“).

public class Aufgabe02 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1));
	}

	public static String getSolution(String sa0, String sa1) {
		sa0 = Utils.HexToBin(sa0);
		sa1 = Utils.HexToBin(sa1);

		String result = Utils.XOR(sa0, sa1);
		return Utils.removePadding(result);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 02
		String sa0 = "457e76";
		String sa1 = "55db1";

		System.out.println("10000000010001111000111     <<<Musterlösung");
		System.out.println(Aufgabe02.getSolution(sa0, sa1));
	}
}