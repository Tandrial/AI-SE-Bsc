package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	Diffie-Hellman
//		Die Lösung von Teil 1 wird mit der Methode moreParams des Connection-Objekts an den 
//		Server geschickt. Die Lösung von Teil 2 wie gewohnt mit sendSolution.
//
//	Teil 1 (Public Key-Berechnung):
//			Berechnung des Public Keys.
//		Input: ia[0]: p , ia[1]: g, da[0]: B (Variablenbezeichnungen aus den 
//			Vorlesungsunterlagen)
//		Output: A als String
//
//	Teil 2 (Entschlüsselung eines Chiffretextes):
//			Entschlüsseln einer Nachricht mithilfe des in Teil 1 berechneten Schlüssels. Klartext
//			und Schlüssel wurden XOR‐verknüpft.
//		Input: sa[0]: Chiffretext beliebiger Länge als String. (ASCII‐Werte des Chiffretextes 
//			durch Unterstrich („_“) getrennt)
//		Output: Klartext als String (Zeichenfolge, keine ASCII‐Werte, 
//				Groß‐/Kleinschreibung wird nicht berücksichtigt.)

public class Aufgabe18 {

	public static String[] getParams(TaskObject t) {
		return getParams(t.getIntArray(0), t.getIntArray(1),
				t.getDoubleArray(0));
	}

	public static String[] getParams(int ia0, int ia1, double da0) {
		int a = Utils.randomInt(1, ia0 - 2);

		int A = DiffieHellman.genPublicKey(ia1, a, ia0);
		int s = DiffieHellman.genSharedSecret(da0, a, ia0);

		return new String[] { A + "", s + "" };
	}

	public static String[] sendParams(String[] params) {
		return new String[] { params[0] };
	}

	public static String getSolution(TaskObject t, String[] params) {
		return getSolution(t.getStringArray(0), params[1]);
	}

	public static String getSolution(String sa0, String sa1) {
		int s = Integer.parseInt(sa1);

		return DiffieHellman.decrypt(sa0.split("_"), s);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 18

		int ia0 = 29; // p
		int ia1 = 19; // g
		double da0 = 27; // B
		int a = 6;

		int A = DiffieHellman.genPublicKey(ia1, a, ia0);
		int s = DiffieHellman.genSharedSecret(da0, a, ia0);

		String[] params = new String[] { A + "", s + "" };

		System.out.println("22	<<<Musterlösung A");
		System.out.println(params[0]);

		String sa0 = "99_104_101_116_127_118_114_111_105_104";

		System.out.println("encryption     <<<Musterlösung");
		System.out.println(Aufgabe18.getSolution(sa0, params[1]));
	}
}