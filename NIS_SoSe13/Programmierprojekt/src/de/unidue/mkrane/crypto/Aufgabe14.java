package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	AES: Initiale & zwei weitere Runden
//		Berechnung von „Initial Round“ und zwei weiterer „Standard Rounds“;.
//	Input: sa[0]: Datenblock als HEX‐String (128Bit), sa[1]: Key als HEX‐String (128Bit), 
//		sa[2]: Keyroom als Zahlen‐String (z.B. „128“)
//	Output: Ausgabe (des verschlüsselten Textes) aller drei Runden als ein HEX‐String 
//		(128Bit), wobei die verschlüsselten Texte der Reihenfolge nach sortiert sind und jeweils 
//		durch einen Unterstrich („_“) getrennt werden (z.B. 34e2…_e7c…_a45b…)

public class Aufgabe14 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1),
				t.getStringArray(2));
	}

	public static String getSolution(String sa0, String sa1, String sa3) {
		if (!sa3.equals("176"))
			return " ";

		String[][] keys = AES.getKeySchedule(AES.getStateMatrix(sa1));
		String[][] text = AES.getStateMatrix(sa0);

		text = AES.addRoundKey(text, keys, 0);

		String result = AES.getStringFromMatrix(text);

		for (int i = 1; i < 3; i++) {
			text = AES.subBytes(text, AESD.S);
			text = AES.shiftRows(text);
			text = AES.mixColumns(text);
			text = AES.addRoundKey(text, keys, i);
			result += "_" + AES.getStringFromMatrix(text);
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 14
		String sa0 = "8eac3d33c3bebc832228e55d5d988d64";
		String sa1 = "63d272d563c88719290287c3a59c493c";
		String sa2 = "176";

		System.out
				.println("ed7e4fe6a0763b9a0b2a629ef804c458_9e43056aa2baaa0f91d880693635a0f1_cb8c89435c79973f3ae6d374bd031498     <<<Musterlösung");
		System.out.println(Aufgabe14.getSolution(sa0, sa1, sa2));
	}
}