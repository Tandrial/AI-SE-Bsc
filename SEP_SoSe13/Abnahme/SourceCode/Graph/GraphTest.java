package Graph;

import java.util.ArrayList;
import java.util.List;

import routenberechnung.eulerkreis;

public class GraphTest {

	public static void main(String[] args) {
		Graph g = new Graph();
		
		Knoten k1 = new Knoten("1", "");
		Knoten k2 = new Knoten("2", "");
		Knoten k3 = new Knoten("3", "");
		Knoten k4 = new Knoten("4", "");
		Knoten k5 = new Knoten("5", "");
		Knoten k6 = new Knoten("6", "");

		
		
		ArrayList<Knoten> knotenListe= new ArrayList<Knoten>();
		knotenListe.add(k1);
		knotenListe.add(k2);
		knotenListe.add(k3);
		knotenListe.add(k4);
		knotenListe.add(k5);
		knotenListe.add(k6);

		g.setKnoten(knotenListe);
		
		// Kanten erzeugen
		Kante k13 = new Kante(k1, k3, false, "");
		Kante k12 = new Kante(k1, k2, false, "");
		Kante k14 = new Kante(k1, k4, false, "");
		
		Kante k23 = new Kante(k2, k3, false, "");
		Kante k24 = new Kante(k2, k4, false, "");
		
		Kante k34 = new Kante(k3, k4, false, "");
		Kante k35 = new Kante(k3, k5, false, "");
		
		Kante k45 = new Kante(k4, k5, false, "");
		
		// Sonderkanten
		Kante k16 = new Kante(k1, k6, false, "");
		Kante k26 = new Kante(k2, k6, false, "");
		
		// Kanten hinzufügen
		k1.addKante(k13);
		k3.addKante(k13);
		
		k1.addKante(k12);
		k2.addKante(k12);
		
		k1.addKante(k14);
		k4.addKante(k14);
		
		k2.addKante(k23);
		k3.addKante(k23);
		
		k2.addKante(k24);
		k4.addKante(k24);
		
		k3.addKante(k34);
		k4.addKante(k34);
		
		k3.addKante(k35);
		k5.addKante(k35);
		
		k4.addKante(k45);
		k5.addKante(k45);
		
		// Sonderkanten für Eulergraph
		k1.addKante(k16);
		k6.addKante(k16);
		
		k2.addKante(k26);
		k6.addKante(k26);
		
//		System.out.println(k1);
//		System.out.println(k2);
//		System.out.println(k3);
//		System.out.println(k4);
//
//		System.out.println(k13);
//		System.out.println(k12);
//		System.out.println(k14);
//		System.out.println(k23);
//		System.out.println(k24);
//		System.out.println(k34);
//		System.out.println(k35);
//		System.out.println(k45);
		
//		System.out.println(k16);
//		System.out.println(k26);
		
		List<Knoten> way = eulerkreis.machDenHolzmichl(g, k1);
		System.out.print("Die Tour lautet: ");
		for(Knoten k : way){
			System.out.print(k + " ");
		}

		System.out.println("Die Kantenliste lautet:");
		for(Kante k : eulerkreis.getEulerKanten()){
			System.out.println(k + " ");
		}
	}
}
