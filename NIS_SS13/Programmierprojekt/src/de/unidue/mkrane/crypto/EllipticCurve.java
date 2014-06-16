package de.unidue.mkrane.crypto;

public class EllipticCurve {
	private int a;
	private int b;
	private int discriminant;

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public EllipticCurve(int a, int b) {
		this.a = a;
		this.b = b;
		this.discriminant = (int) (-16 * (4 * Math.pow(a, 3) + 27 * Math.pow(b,
				2)));
		if (!isSmooth())
			try {
				throw new Exception("The curve " + this + " is not smooth!");
			} catch (Exception e) {
				System.out.println(e);
			}
	}

	public boolean isSmooth() {
		return discriminant != 0;
	}

	public boolean testPoint(Point p) {
		int y_sqr = (int) Math.pow(p.getY(), 2);
		int x_cub = (int) Math.pow(p.getX(), 3);

		return y_sqr == x_cub + a * p.getX() + b;
	}

	@Override
	public String toString() {
		return String.format("y^2 = x^3 + %dx + %d", a, b);
	}

	@Override
	public boolean equals(Object that) {
		if (that.getClass() == EllipticCurve.class) {
			EllipticCurve t = (EllipticCurve) that;
			return (this.a == t.getA()) && (this.b == t.getB());
		} else
			return false;
	}

	public static void main(String[] args) {
		// Tests

		EllipticCurve C = new EllipticCurve(-2, 4);
		Point P = new Point(C, 3, 5);
		Point Q = new Point(C, -2, 0);
		System.out.println(P.sub(Q));

		System.out.println(Q.sub(P.mul(3)));

	}
}

class Point {
	private int x;
	private int y;
	protected EllipticCurve curve;

	public Point() {
		x = 0;
		y = 0;
	}

	public Point(EllipticCurve c, int x, int y) {
		this.curve = c;
		this.x = x;
		this.y = y;

		if (!curve.testPoint(this))
			try {
				throw new Exception("The point " + this
						+ " is not on the given curve " + curve);
			} catch (Exception e) {
				System.out.println(e);
			}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point neg() {
		return new Point(curve, x, -y);
	}

	public Point add(Point Q) {

		int x2 = Q.getX();
		int y2 = Q.getY();

		double m;

		if (x == x2 && y == y2) {
			if (y == 0)
				return new Ideal(curve);
			m = (3 * Math.pow(x, 2) + curve.getA() / (2 * y));
		} else {
			if (x == x2)
				return new Ideal(curve);
			m = (y2 - y) / (x2 - x);
		}

		double x3 = Math.pow(m, 2) - x2 - x;
		double y3 = m * (x3 - x) + y;
		return new Point(curve, (int) x3, (int) -y3);
	}

	public Point sub(Point Q) {
		return this.add(Q.neg());
	}

	public Point mul(int n) {
		if (n < 0)
			return this.mul(-n);
		else if (n == 0)
			return new Ideal(curve);
		else {
			Point Q = this;
			Point R = ((n & 1) == 1) ? this : new Ideal(curve);

			int i = 2;
			while (i <= n) {
				Q = Q.add(Q);

				if ((n & i) == i)
					R = R.add(Q);
				i = i << 1;
			}
			return R;
		}
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}
}

class Ideal extends Point {

	public Ideal(EllipticCurve c) {
		this.curve = c;
	}

	@Override
	public Point neg() {
		return this;
	}

	@Override
	public Point add(Point Q) {
		return Q;
	}

	@Override
	public Point mul(int n) {
		return this;
	}

	@Override
	public String toString() {
		return "Ideal";
	}
}