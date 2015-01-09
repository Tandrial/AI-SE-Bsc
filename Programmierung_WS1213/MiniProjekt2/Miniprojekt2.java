public class Miniprojekt2 {

	public double eulerreihe() {
		double e = 0.0, e_neu = e, fak = 1;
		int n = 1;
		do {
			e = e_neu;
			e_neu = e + 1.0 / fak;
			fak *= n++;
		} while (e != e_neu);
		return e;
	}

	public double durchschnitttemp(double[] temp) {
		double durchschnitt = 0.0;
		for (int i = 0; i < temp.length; i++) {
			durchschnitt += temp[i];
		}
		return durchschnitt / temp.length;
	}

	public int mintemp(double[] temp) {
		int pos = 0;
		for (int i = 1; i < temp.length; i++) {
			if (temp[i] <= temp[pos]) {
				pos = i;
			}
		}
		return pos;
	}

	public double maxtemp(double[] temp) {
		int highest = 0, ndhighest = mintemp(temp);

		for (int i = 2; i < temp.length; i++) {
			if (temp[i] == temp[highest] || temp[i] == temp[ndhighest])
				continue;
			if (temp[i] > temp[highest]) {
				ndhighest = highest;
				highest = i;
			} else if (temp[i] > temp[ndhighest]) {
				ndhighest = i;
			}
		}
		return temp[ndhighest];
	}

	public static void main(String args[]) {
		Miniprojekt2 m2 = new Miniprojekt2();

		double[] temp = { 20.0, -10.0, 15.5, 15.5, 20.0, -10.0 };

		System.out.println("eulerreihe ergibt: " + m2.eulerreihe());
		System.out
				.println("durchschnitttemp mit ( 20.0, -10.0, 15.5, 15.5, 20.0, -10.0) ergibt: "
						+ m2.durchschnitttemp(temp) + " C");
		System.out
				.println("mintemp mit ( 20.0, -10.0, 15.5, 15.5, 20.0, -10.0) ergibt: "
						+ m2.mintemp(temp));
		System.out
				.println("maxtemp mit ( 20.0, -10.0, 15.5, 15.5, 20.0, -10.0) ergibt: "
						+ m2.maxtemp(temp) + " C");
	}
}