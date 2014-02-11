public class Uebung10_Aufgabe2 {
	public static void main(String[] args) {
		Hashtable test = new Hashtable();

		test.insert(new Book("J.R.R.", "Hobbit", "toll"));
		test.insert(new Book("J.R.R.", "Hobbit", "toll"));
		test.insert(new Book("J.R.R.", "HdR1", "toll"));
		test.insert(new Book("J.R.R.", "HdR2", "toll"));
		test.insert(new Book("J.R.R.", "HdR3", "toll"));
		test.print();
		System.out.println("---------------------------------");
		Book help = test.get("J.R.R.", "HdR3");
		if (help != null)
			help.print();
		System.out.println();
		System.out.println("---------------------------------");
		test.delete("J.R.R.", "Hobbit");
		test.delete("J.R.R.", "HdR1");
		test.print();

		System.out.println("");
	}
}

class Hashtable {
	// Die Hashtable besteht aus einem Byte großen Array (0-255) aus Listen die
	// die Bücher enthalten

	BookList[] table;

	public Hashtable() {
		table = new BookList[256];
	}

	public void insert(Book entry) {
		int hash = hash(entry.getAutor(), entry.getTitel());

		if (table[hash] == null) {
			table[hash] = new BookList();
			table[hash].setKopf(entry);
		} else {
			table[hash].hinzufuegen(entry);
		}
	}

	public Book get(String author, String title) {
		int hash = hash(author, title);
		if (table[hash] == null)
			return null;
		else {
			return table[hash].sucheBuch(author, title);
		}
	}

	public boolean delete(String author, String title) {
		int hash = hash(author, title);
		if (table[hash] == null)
			return false;
		else {
			boolean suc = table[hash].loeschen(author, title);
			if (table[hash].getKopf() == null)
				table[hash] = null;
			return suc;
		}
	}

	public int hash(String author, String title) {
		// Die eigentlich Hash-Funktion. Math.abs stellt sicher das die Zahl
		// positiv ist und modulo 256 stellt sicher, dass die Zahl ein gültiger
		// Index vom Array ist.
		int hash = Math.abs(author.hashCode() + title.hashCode()) % 256;
		return hash;
	}

	public void print() {
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				System.out.print(i);
				table[i].print();
			}
		}
	}
}

class BookList {
	// Selbes Konzept wie bei der Liste im Miniprojekt 3

	private Book kopf;

	public BookList() {
		kopf = null;
	}

	public Book getKopf() {
		return kopf;
	}

	public Book setKopf(Book kopf) {
		return this.kopf = kopf;
	}

	public void hinzufuegen(Book neu) {
		if (getKopf() == null) {
			setKopf(neu);
		} else {
			Book tmp = getKopf();
			while (tmp.getNachfolger() != null) {
				tmp = tmp.getNachfolger();
			}
			if (tmp.getNachfolger() != null)
				neu.setNachfolger(tmp.getNachfolger());
			tmp.setNachfolger(neu);
		}
	}

	public Book sucheBuch(String author, String title) {
		Book tmpKurs = this.kopf;
		while (tmpKurs != null) {
			if (tmpKurs.equals(author, title))
				return tmpKurs;
			tmpKurs = tmpKurs.getNachfolger();
		}
		return null;
	}

	public boolean loeschen(String author, String title) {
		if (this.kopf == null)
			return false;

		if (this.kopf.equals(author, title)) {
			this.kopf = this.kopf.getNachfolger();
			return true;
		} else {
			Book tmpKurs = this.kopf;
			while (tmpKurs != null && tmpKurs.getNachfolger() != null) {
				if (tmpKurs.getNachfolger().equals(author, title)) {
					tmpKurs.setNachfolger(tmpKurs.getNachfolger()
							.getNachfolger());
					return true;
				}
				tmpKurs = tmpKurs.getNachfolger();
			}
			return false;
		}
	}

	// Gibt die komplette Liste aus
	public void print() {
		Book tmpKurs = this.kopf;
		System.out.print("-> ");
		while (tmpKurs != null) {
			tmpKurs.print();
			System.out.print("-> ");
			tmpKurs = tmpKurs.getNachfolger();
		}
		System.out.println();
	}
}

class Book {
	private String autor;
	private String titel;
	private String recension;
	// NEU:
	private Book nachfolger;

	public Book(String autor, String titel, String recension) {
		this.autor = autor;
		this.titel = titel;
		this.recension = recension;
		this.nachfolger = null;
	}

	public String getAutor() {
		return autor;
	}

	public String getTitel() {
		return titel;
	}

	public String getRecension() {
		return recension;
	}

	// NEU:
	public boolean equals(String author, String titel) {
		return this.autor.equals(author) && this.titel.equals(titel);
	}

	// NEU:
	public Book getNachfolger() {
		return nachfolger;
	}

	// NEU:
	public void setNachfolger(Book nf) {
		nachfolger = nf;
	}

	// Druckt die Informationen
	public void print() {
		System.out.print(this.autor + " " + this.titel);
	}
}