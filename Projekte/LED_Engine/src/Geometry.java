import java.util.ArrayList;
import java.util.List;

public class Geometry {

	public static List<Face> Cube(double x, double y, double z, double size) {
		List<Face> faces = new ArrayList<Face>();

		faces.add(Geometry.SquareXY(x, y, z + size, size));
		faces.add(Geometry.SquareXY(x, y, z - size, size));

		faces.add(Geometry.SquareXZ(x, y + size, z, size));
		faces.add(Geometry.SquareXZ(x, y - size, z, size));

		faces.add(Geometry.SquareYZ(x + size, y, z, size));
		faces.add(Geometry.SquareYZ(x - size, y, z, size));

		return faces;
	}

	public static Face SquareXY(double x, double y, double z, double size) {
		Vector3D[] p = new Vector3D[] { new Vector3D(x - size, y - size, z),
				new Vector3D(x + size, y - size, z),
				new Vector3D(x + size, y + size, z),
				new Vector3D(x - size, y + size, z) };

		return new Face(new Vector3D(x, y, z), p);
	}

	public static Face SquareXZ(double x, double y, double z, double size) {
		Vector3D[] p = new Vector3D[] { new Vector3D(x - size, y, z - size),
				new Vector3D(x + size, y, z - size),
				new Vector3D(x + size, y, z + size),
				new Vector3D(x - size, y, z + size) };

		return new Face(new Vector3D(x, y, z), p);
	}

	public static Face SquareYZ(double x, double y, double z, double size) {
		Vector3D[] p = new Vector3D[] { new Vector3D(x, y - size, z - size),
				new Vector3D(x, y + size, z - size),
				new Vector3D(x, y + size, z + size),
				new Vector3D(x, y - size, z + size) };

		return new Face(new Vector3D(x, y, z), p);
	}

	public static Face TriangleXY(double x, double y, double z, double size) {
		Vector3D[] p = new Vector3D[] {
				new Vector3D(x - size, y - size, z + size),
				new Vector3D(x + size, y - size, z + size),
				new Vector3D(x, y + size, z) };

		return new Face(new Vector3D(x, y, z), p);
	}

	public static Face TriangleXZ(double x, double y, double z, double size) {
		Vector3D[] p = new Vector3D[] { new Vector3D(x - size, y, z - size),
				new Vector3D(x + size, y, z - size) };

		return new Face(new Vector3D(x, y, z), p);
	}

	public static Face TriangleYZ(double x, double y, double z, double size) {
		Vector3D[] p = new Vector3D[] { new Vector3D(x, y - size, z - size),
				new Vector3D(x, y + size, z - size),
				new Vector3D(x, y + size, z + size) };

		return new Face(new Vector3D(x, y, z), p);
	}

	public static List<Face> Pyramid(double x, double y, double z, double size) {
		List<Face> faces = new ArrayList<Face>();

		faces.add(Geometry.TriangleXY(x, y, z, size));
//		Face f = Geometry.TriangleXY(x, y, z, size);
//		f.rotX(Math.PI / 4);
//		faces.add(f);

		return faces;
	}
}
