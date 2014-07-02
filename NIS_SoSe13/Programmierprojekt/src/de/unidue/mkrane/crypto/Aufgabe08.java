package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	DES: Feistel-Funktion
//		Einmalige Anwendung der f-Funktion mit vorgegebenem Rundenschlüssel. Die ersten 32 
//		Bit des Inputs bilden den L-Block, die zweiten 32 Bit den R-Block. 
//	Input: sa[0]: Input als Binär-String(64Bit), sa[1]: Rundenschlüssel als Binär-String 
//		(48Bit)	
//	Output:  L-Block XOR R-Block als Binär-String

public class Aufgabe08 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getStringArray(0), t.getStringArray(1));
	}

	public static String getSolution(String sa0, String sa1) {
		String r = DES.GetR(sa0);
		String l = DES.GetL(sa0);
		r = DES.feistel(r, sa1);
		return Utils.XOR(l, r);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 08
		String sa0 = "0000011100111001101001010010100111010110110110011011111001011000";
		String sa1 = "101010000001000100111101111000000111110001101110";

		System.out
				.println("00100001111000110110011101111011     <<<Musterlösung");
		System.out.println(Aufgabe08.getSolution(sa0, sa1));
	}
}
