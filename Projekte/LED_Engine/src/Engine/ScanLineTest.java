package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLineTest {

	public static void fillBottomFlatTriangle(Vector3D v1, Vector3D v2,
			Vector3D v3) {
		double invslope1 = (v2.x - v1.x) / (v2.y - v1.y) / 10;
		double invslope2 = (v3.x - v1.x) / (v3.y - v1.y) / 10;

		double curx1 = v1.x;
		double curx2 = v1.x;

		for (double scanlineY = v1.y; scanlineY <= v2.y; scanlineY += .1) {
			StdDraw.line(curx1, scanlineY, curx2, scanlineY);
			curx1 += invslope1;
			curx2 += invslope2;
		}
	}

	public static void fillTopFlatTriangle(Vector3D v1, Vector3D v2, Vector3D v3) {
		double invslope1 = (v3.x - v1.x) / (v3.y - v1.y) / 10;
		double invslope2 = (v3.x - v2.x) / (v3.y - v2.y) / 10;

		double curx1 = v3.x;
		double curx2 = v3.x;

		for (double scanlineY = v3.y; scanlineY > v1.y; scanlineY -= .1) {
			StdDraw.line(curx1, scanlineY, curx2, scanlineY);
			curx1 -= invslope1;
			curx2 -= invslope2;
		}
	}

	public static void drawTriangle(Vector3D v1, Vector3D v2, Vector3D v3) {
		List<Vector3D> vs = new ArrayList<Vector3D>();
		vs.add(v1);
		vs.add(v2);
		vs.add(v3);

		Collections.sort(vs, (s, t) -> (int) (s.y - t.y));

		v1 = vs.get(0);
		v2 = vs.get(1);
		v3 = vs.get(2);

		if (v2.y == v3.y) {
			fillBottomFlatTriangle(v1, v2, v3);
		} else if (v1.y == v2.y) {
			fillTopFlatTriangle(v1, v2, v3);
		} else {
			Vector3D v4 = new Vector3D((v1.x + ((v2.y - v1.y) / (v3.y - v1.y))
					* (v3.x - v1.x)), v2.y, 0, Vector3D.VECTOR_POSITION);
			fillBottomFlatTriangle(v1, v2, v4);
			fillTopFlatTriangle(v2, v4, v3);
		}
	}

	public static void main(String[] args) {

		StdDraw.setScale(-2, 2);

		// Vector3D v1 = new Vector3D(-1, -1, 0);
		// Vector3D v2 = new Vector3D(1, -1, 0);
		// Vector3D v3 = new Vector3D(0, 1, 0);

		Vector3D v1 = new Vector3D(-.5, .1, 0, Vector3D.VECTOR_POSITION);
		Vector3D v2 = new Vector3D(.4, -1, 0, Vector3D.VECTOR_POSITION);
		Vector3D v3 = new Vector3D(-.4, 1, 0, Vector3D.VECTOR_POSITION);

		List<Vector3D> vs = new ArrayList<>();
		vs.add(v1);
		vs.add(v2);
		vs.add(v3);

		double[][] res = new double[3][3];
		int pos = 0;
		for (Vector3D p : vs) {
			res[0][pos] = p.x;
			res[1][pos] = p.y;
			res[2][pos++] = p.z;
		}

		StdDraw.polygon(res[0], res[1]);
		drawTriangle(v1, v2, v3);
	}
}
