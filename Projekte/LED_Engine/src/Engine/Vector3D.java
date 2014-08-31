package Engine;

public class Vector3D {

	public static final int VECTOR_POSITION = 1;
	public static final int VECTOR_DIRECTION = 0;

	public static Vector3D origin = new Vector3D(0, 0, 0, VECTOR_POSITION);
	public static Vector3D x_Axis = new Vector3D(1, 0, 0, VECTOR_DIRECTION);
	public static Vector3D y_Axis = new Vector3D(0, 1, 0, VECTOR_DIRECTION);
	public static Vector3D z_Axis = new Vector3D(0, 0, 1, VECTOR_DIRECTION);

	double x, y, z, w;

	public Vector3D(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector3D(double x, double y, double z) {
		this(x, y, z, VECTOR_POSITION);
	}

	public Vector3D sub(Vector3D p) {
		return new Vector3D(x - p.x, y - p.y, z - p.z, Math.max(w, p.w));
	}

	public Vector3D add(Vector3D p) {
		return new Vector3D(x + p.x, y + p.y, z + p.z, Math.max(w, p.w));
	}

	public Vector3D mult(double d) {
		return new Vector3D(d * x, d * y, d * z, d);
	}

	public double length() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	@Override
	public String toString() {
		// return String.format("x=%s y=%s z=%s w=%s\n", x, y, z);
		return "x=" + x + " y=" + y + " z=" + z + "\n";
	}
}
