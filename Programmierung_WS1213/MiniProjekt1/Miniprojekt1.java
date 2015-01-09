public class Miniprojekt1 {

	public double endkapital(double Ka, double p, int n) {
		// K_v = K_a * (1* p/100)^n
		// hier die Lösung eintragen...
		double kv = Ka * potenz(n, 1 + p / 100);
		return kv;
	}

	public double anfangskapital(double Kv, double p, int n) {
		// K_a = K_v / (1+p/100)^n
		// hier die Lösung eintragen...
		double ka = Kv / potenz(n, 1 + p / 100);
		return ka;
	}

	public double zinszatz(double Kv, double Ka, int n) {
		// p = 100* ((K_v/K_a)^(1/n)-1)
		// hier die Lösung eintragen...
		double p = 100 * (wurzel(n, Kv / Ka) - 1);
		return p;
	}

	public double anzahljahre(double Kv, double Ka, double p) {
		// n = lg(K_v/K_a)/lg(1+p/100)
		// hier die Lösung eintragen...
		double n = logarithmus(Kv / Ka) / logarithmus(1 + p / 100);
		return n;
	}

	public double logabc(double a, double b, double c) {
		// lgabc = lg(a^(1/2))+lg(b^2)-1/4*lg(c)
		// hier die Lösung eintragen...
		double lgabc = logarithmus(wurzel(2, a)) + logarithmus(potenz(2, b))
				- 1 / 4.0 * logarithmus(c);
		return lgabc;
	}

	public double wurzel(double n_wurzel, double term) {
		return Math.pow(term, 1 / n_wurzel);
	}

	public double potenz(double n_potenz, double term) {
		return Math.pow(term, n_potenz);
	}

	public double logarithmus(double x) {
		return Math.log(x);
	}

	static int Fkt(int x) {
		System.out.println(x);
		if (x < 9)
			return x;
		if (Fkt(x / 2) % 2 == 0)
			return Fkt(x - 10);
		else
			return Fkt(x - 20);
	}

	public static void main(String args[]) {
		Miniprojekt1 m1 = new Miniprojekt1();
		System.out.println(34 + Fkt(34));

		System.out
				.println("Ka=1000, p=10%, n=10 Jahre ergibt einen Endkapital von "
						+ m1.endkapital(1000, 10, 10));
		System.out
				.println("Kv=2500,p= 4% und n=5 Jahre benötigt einen Anfangskapital von "
						+ m1.anfangskapital(2500, 4, 5));
		System.out
				.println("Kv=2500,Ka=2000 und n=5 Jahre benötigt einen Zinszatz von "
						+ m1.zinszatz(2500, 2000, 5));
		System.out.println("Kv=2000, Ka=1000, p=4% benötigt "
				+ m1.anzahljahre(2000, 1000, 4) + " Jahre");
		System.out.println("a=20, b=10, c=4 in logabc ist gleich "
				+ m1.logabc(20, 10, 4));
	}
}