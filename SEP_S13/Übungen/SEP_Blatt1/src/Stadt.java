public class Stadt {
	String Name;
	Weg[] Tor;
	Stadt Nf;

	Stadt(String Name, Stadt Nf) {
		this.Name = Name;
		this.Nf = Nf;
		Tor = new Weg[4];
		for (int i = 0; i < 4; i++)
			Tor[i] = null;
	}
}