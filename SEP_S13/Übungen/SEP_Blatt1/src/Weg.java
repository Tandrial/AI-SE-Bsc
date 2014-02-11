public class Weg {
	int Laenge;
	Stadt[] Ziel;

	Weg(Stadt Anfang, int Laenge, Stadt Ende) {
		this.Laenge = Laenge;
		Ziel = new Stadt[2];
		Ziel[0] = Anfang;
		Ziel[1] = Ende;
	}
} 