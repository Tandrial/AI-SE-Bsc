
public class Matrix {

	private double[][] values;

	static Matrix rotX_M(double alpha) {
		return new Matrix(new double[][] { { 1, 0, 0 },
				{ 0, Math.cos(alpha), -Math.sin(alpha) },
				{ 0, Math.sin(alpha), Math.cos(alpha) } });
	}

	static Matrix rotY_M(double alpha) {
		return new Matrix(new double[][] {
				{ Math.cos(alpha), 0, Math.sin(alpha) }, { 0, 1, 0 },
				{ -Math.sin(alpha), 0, Math.cos(alpha) } });
	}

	static Matrix rotZ_M(double alpha) {
		return new Matrix(new double[][] {
				{ Math.cos(alpha), -Math.sin(alpha), 0 },
				{ Math.sin(alpha), Math.cos(alpha), 0 }, { 0, 0, 1 } });
	}

	static Matrix scale(double x, double y, double z) {
		return new Matrix(new double[][] { { x, 0, 0 }, { 0, y, 0 },
				{ 0, 0, z } });
	}

	public Matrix(int x, int y) {
		values = new double[x][y];
	}

	public Matrix(double[][] values) {
		this.values = values;
	}

	public Matrix(Vector3D p) {
		this(3, 1);
		values[0][0] = p.x;
		values[1][0] = p.y;
		values[2][0] = p.z;
	}

	public Vector3D mult(Vector3D p) {
		Matrix m = mult(new Matrix(p));
		if (m.values.length == 3 && m.values[0].length == 1) {
			return new Vector3D(m.values[0][0], m.values[1][0], m.values[2][0]);
		}
		return null;
	}

	private Matrix mult(Matrix m) {

		Matrix res = new Matrix(values.length, m.values[0].length);
		int uf = 4;

		for (int i = 0; i < res.values.length; i++) {
			for (int k = 0; k < m.values.length; k++) {
				double tmp = this.values[i][k];
				int size = res.values[0].length;
				for (int j = 0; j < size / uf; j += uf) {
					res.values[i][j] += tmp * m.values[k][j];
					res.values[i][j + 1] += tmp * m.values[k][j + 1];
					res.values[i][j + 2] += tmp * m.values[k][j + 2];
					res.values[i][j + 3] += tmp * m.values[k][j + 3];
				}

				for (int j = size - (size % uf); j < size; j++) {
					res.values[i][j] += tmp * m.values[k][j];
				}
			}
		}

		return res;
	}

	public static double prepDot(Vector3D v1, Vector3D v2) {
		return (v1.x * v2.y - v1.y * v2.x);
	}
}
