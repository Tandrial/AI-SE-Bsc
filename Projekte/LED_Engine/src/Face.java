import java.util.ArrayList;
import java.util.List;

public class Face {

	List<Vector3D> pts;
	Vector3D center;

	public Face(Vector3D center, Vector3D... points) {
		this.center = center;
		pts = new ArrayList<Vector3D>();
		for (int i = 0; i < points.length; i++) {
			pts.add(points[i]);
		}
	}

	public double[][] toArray() {
		double[][] res = new double[3][pts.size()];
		int pos = 0;
		for (Vector3D p : pts) {
			res[0][pos] = p.x;
			res[1][pos] = p.y;
			res[2][pos++] = p.z;
		}
		return res;
	}

	public void rotX(double alpha) {
		for (int i = 0; i < pts.size(); i++) {
			pts.set(i, rot(Matrix.rotX_M(alpha), pts.get(i)));
		}
	}

	public void rotY(double alpha) {
		for (int i = 0; i < pts.size(); i++) {
			pts.set(i, rot(Matrix.rotY_M(alpha), pts.get(i)));
		}
	}

	public void rotZ(double alpha) {
		for (int i = 0; i < pts.size(); i++) {
			pts.set(i, rot(Matrix.rotZ_M(alpha), pts.get(i)));
		}
	}

	public Vector3D rot(Matrix m, Vector3D p) {
		return m.mult(p);
	}

	public void scale(double x_fak, double y_fak, double z_fak) {
		for (int i = 0; i < pts.size(); i++) {
			pts.set(i, Matrix.scale(x_fak, y_fak, z_fak).mult(pts.get(i)));
		}
	}

	public static List<Vector3D> line(Vector3D p1, Vector3D p2) {
		List<Vector3D> res = new ArrayList<Vector3D>();
		int dx = (int) Math.abs(p1.x - p2.x);
		int dy = (int) Math.abs(p1.y - p2.y);

		int x0 = (int) p1.x;
		int y0 = (int) p1.y;
		int x1 = (int) p2.x;
		int y1 = (int) p2.y;

		int sx = -1;
		if (p1.x < p2.x)
			sx = 1;
		int sy = -1;
		if (p1.y < p2.y)
			sy = 1;

		int err = dx - dy;

		do {
			res.add(new Vector3D(x0, y0, 0));
			if (x0 == x1 && y0 == y1)
				break;
			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				x0 += sx;
			}
			if (e2 < dx) {
				err += dx;
				y0 += sy;
			}
		} while (true);
		return res;
	}
}
