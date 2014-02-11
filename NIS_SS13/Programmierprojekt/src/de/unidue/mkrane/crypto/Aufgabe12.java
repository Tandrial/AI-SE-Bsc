package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	AES: MixColumns()
//		Durchführung der MixColumns‐Funktion. Der Input ist spaltenweise angegeben. D.h. die 
//		ersten vier Byte des Inputs entsprechen der ersten Spalte.
//	Input: sa[0]: HEX‐String (128Bit)
//	Output: Gemischte Spalten als ein HEX‐String (128Bit) (z.B. „21ae5….“)

public class Aufgabe12 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0));
	}

	public static String getSolution(String sa0) {
		String[][] tmp = AES.getStateMatrix(sa0);
		tmp = AES.mixColumns(tmp);

		return AES.getStringFromMatrix(tmp);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 12
		String sa0 = "60866a0a1a4624d36ccdb602aa73a762";

		System.out
				.println("31c32c5809297af1202ed0cb1fdc2af5     <<<Musterlösung");
		System.out.println(Aufgabe12.getSolution(sa0));
	}
}