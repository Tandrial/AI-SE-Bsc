public class Vector3D {

	public static Vector3D origin = new Vector3D(0, 0, 0);

	double x, y, z;

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D sub(Vector3D p) {
		return new Vector3D(x - p.x, y - p.y, z - p.z);
	}

	public Vector3D add(Vector3D p) {
		return new Vector3D(x + p.x, y + p.y, z + p.z);
	}

	public Vector3D mult(double d) {
		return new Vector3D(d * x, d * y, d * z);
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
