package de.unidue.mkrane.crypto;

import de.unidue.iem.tdr.nis.client.TaskObject;

//	Faktorisierung
//		Zufallszahl in Primfaktoren zerlegen.
//	Input: ia[0]: Zufallszahl als Integer
//	Output: Primfaktoren, aufsteigend sortiert und mit Sternchen(*) getrennt als String. (z.B.: 
//			„2*2*5*7“)

public class Aufgabe04 {

	public static String getSolution(TaskObject t) {
		return getSolution(t.getIntArray(0));
	}

	public static String getSolution(int ia0) {
		int[] faktors = Utils.StringToIntArray(Utils.Faktor(ia0), ",");

		for (int i = 0; i < faktors.length; i++) {
			for (int j = i + 1; j < faktors.length; j++) {
				if (faktors[i] > faktors[j]) {
					int tmp = faktors[i];
					faktors[i] = faktors[j];
					faktors[j] = tmp;
				}
			}
		}

		return Utils.IntArrayToString(faktors, "*");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Aufgabe 04
		int ia0 = 26572;

		System.out.println("2*2*7*13*73     <<<Musterlösung");
		System.out.println(Aufgabe04.getSolution(ia0));
	}
}