package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	ElGamal: Entschlüsselung
//		Die Lösung von Teil 1 wird mit der Methode getTask(int taskId, String[] 
//		params) des Connection-Objekts an den Server geschickt. D.h. der öffentliche Schlüssel 
//		wird schon bei der Aufgabenanforderung mitgeschickt.
//		Die Lösung von Teil 2 wie gewohnt mit sendSolution.
//
//	Teil 1 (Public Key-Berechnung):
//			Client generiert zufälliges Schlüsselpaar und sendet den Public Key an den Server.
//		Output: Public Key als String-Array (String[] array = {p, α, β})
//
//	Teil 2 (Entschlüsselung):
//			Zufallstext, der mit dem Public Key des Clients (aus Teil 1) verschlüsselt ist, soll 
//			entschlüsselt werden.
//		Input: sa[0]: Chiffretext als String (Format wie Output bei ElGamal: Verschlüsselung, 
//			also y1_hex1_hex2_...)	
//		sOutput: Klartext als String (Groß/Kleinschreibung wird nicht berücksichtigt.)

public class Aufgabe22 {

	public static String[] calcParams() {
		return ElGamal.genKeys();
	}

	public static String[] sendParams(String[] params) {
		return new String[] { params[0], params[1], params[2] };
	}

	public static String getSolution(TaskObject t, String[] params) {
		return getSolution(t.getStringArray(0), params);
	}

	public static String getSolution(String sa0, String[] params) {
		return ElGamal.decrypt(sa0, params);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 22
		String sa0 = "5ed_a86_9e9_14f5_6c1_d11_52d_71_52d_10d6_14f5";
		String[] params = { "6217", "5", "404", "2294" };

		System.out.println("cryptology     <<<Musterlösung");
		System.out.println(Aufgabe22.getSolution(sa0, params));
	}

}
