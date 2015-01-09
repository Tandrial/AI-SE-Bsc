public class Uebung7_Aufgabe2 {

	double a;
	double b;
	double c;

	Uebung7_Aufgabe2(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	double calculateValue(double x) {
		return this.a * Math.pow(x, 2) + this.b * Math.pow(x, 1) + c;
	}

	boolean checkValuePair(double x, double f_x) {
		double correctValue = calculateValue(x);

		if (Math.abs(f_x - correctValue) < 0.0000001)
			return true;
		else
			return false;
	}
}
