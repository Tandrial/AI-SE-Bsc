package Engine;

public class Matrix {

	private double[][] values;

	static Matrix rotX_M(double alpha) {
		return new Matrix(new double[][] { { 1, 0, 0, 0 },
				{ 0, Math.cos(alpha), -Math.sin(alpha), 0 },
				{ 0, Math.sin(alpha), Math.cos(alpha), 0 }, { 0, 0, 0, 1 } });
	}

	static Matrix rotY_M(double alpha) {
		return new Matrix(new double[][] {
				{ Math.cos(alpha), 0, Math.sin(alpha), 0 }, { 0, 1, 0, 0 },
				{ -Math.sin(alpha), 0, Math.cos(alpha), 0 }, { 0, 0, 0, 1 } });
	}

	static Matrix rotZ_M(double alpha) {
		return new Matrix(new double[][] {
				{ Math.cos(alpha), -Math.sin(alpha), 0, 0 },
				{ Math.sin(alpha), Math.cos(alpha), 0, 0 }, { 0, 0, 1, 0 },
				{ 0, 0, 0, 1 } });
	}

	static Matrix scale_M(double x, double y, double z) {
		return new Matrix(new double[][] { { x, 0, 0, 0 }, { 0, y, 0, 0 },
				{ 0, 0, z, 0 }, { 0, 0, 0, 1 } });
	}

	static Matrix trans_M(double x, double y, double z) {
		return new Matrix(new double[][] { { 1, 0, 0, x }, { 0, 1, 0, y },
				{ 0, 0, 1, z }, { 0, 0, 0, 1 } });
	}

	public Matrix(int x, int y) {
		values = new double[x][y];
	}

	public Matrix(double[][] values) {
		this.values = values;
	}

	public Matrix(Vector3D p) {
		this(4, 1);
		values[0][0] = p.x;
		values[1][0] = p.y;
		values[2][0] = p.z;
		values[3][0] = p.w;
	}

	public Vector3D mult(Vector3D p) {
		Matrix m = mult(new Matrix(p));
		if (m.values.length == 4 && m.values[0].length == 1) {
			return new Vector3D(m.values[0][0], m.values[1][0], m.values[2][0],
					p.w);
		}
		return null;
	}

	private Matrix mult(Matrix m) {

		Matrix res = new Matrix(values.length, m.values[0].length);

		for (int i = 0; i < res.values.length; i++) {
			for (int j = 0; j < res.values[0].length; j++) {
				for (int k = 0; k < m.values.length; k++) {
					res.values[i][j] += this.values[i][k] * m.values[k][j];
				}
			}
		}
		return res;
	}

	public static double prepDot(Vector3D v1, Vector3D v2) {
		return (v1.x * v2.y - v1.y * v2.x);
	}
}
