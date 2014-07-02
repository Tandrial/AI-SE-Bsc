import java.util.HashMap;
import java.util.List;

import ledControl.BoardController;

public class LedScreen {

	private final int SIZE = 12;
	private final int HIGH = 127;
	private final int LOW = 5;
	private final int MAX = 2;
	private final float STEP_SIZE = .3f;

	private static BoardController controller;
	private HashMap<Integer, Double> z_value;

	public LedScreen(BoardController controller) {
		LedScreen.controller = controller;
	}

	public void draw(List<Face> faces, RenderMode mode) {
		clearLED();
		z_value = new HashMap<>();
		for (Face f : faces) {
			if (mode == RenderMode.FACES)
				if (f.pts.size() == 3)
					scanline(f);
				else
					drawFace(f);

			double[][] a = f.toArray();
			for (int i = 0; i < a[0].length; i++) {
				switch (mode) {
				case POINTS:
					drawLED(a[0][i], a[1][i], a[2][i], true);
					break;
				case LINES:
				case FACES:
					drawLine(f.pts.get(i), f.pts.get((i + 1) % a[0].length));
					break;
				default:
					drawLED(a[0][i], a[1][i], a[2][i], true);
					break;
				}
			}
		}
		update();
	}

	private void scanline(Face f) {
		Vector3D vt1 = f.pts.get(0);
		Vector3D vt2 = f.pts.get(1);
		Vector3D vt3 = f.pts.get(2);

		double maxX = Math.max(vt1.x, Math.max(vt2.x, vt3.x));
		double minX = Math.min(vt1.x, Math.min(vt2.x, vt3.x));
		
		double maxY = Math.max(vt1.y, Math.max(vt2.y, vt3.y));
		double minY = Math.min(vt1.y, Math.min(vt2.y, vt3.y));
		
		double maxZ = Math.max(vt1.z, Math.max(vt2.z, vt3.z));
		double minZ = Math.min(vt1.z, Math.min(vt2.z, vt3.z));

		Vector3D vs1 = new Vector3D(vt2.x - vt1.x, vt2.y - vt1.y,vt2.z - vt1.z);
		Vector3D vs2 = new Vector3D(vt3.x - vt1.x, vt3.y - vt1.y,vt3.z - vt1.z);

		for (double x = minX; x <= maxX; x++) {
			for (double y = minY; y <= maxY; y++) {
				for (double z = minZ; z <= maxZ; z++) {
				Vector3D q = new Vector3D(x - vt1.x, y - vt1.y,z - vt1.z);

				float s = (float) Matrix.crossProduct(q, vs2) / Matrix.crossProduct(vs1, vs2);
				float t = (float) Matrix.crossProduct(vs1, q) / Matrix.crossProduct(vs1, vs2);

				if ((s >= 0) && (t >= 0) && (s + t <= 1)) { /* inside triangle */
					drawLED(x,y,z, true);
				}
			}
			}}

	}

	public void clearLED() {
		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 12; y++) {
				controller.setColor(x, y, 0, 0, 0);

			}
		}
	}

	private void drawLine(Vector3D p1, Vector3D p2) {
		Vector3D v = p2.sub(p1);
		double l = v.length();

		for (float d = 0; d < l; d += STEP_SIZE) {
			Vector3D p_d = p1.add(v.mult(d / l));
			drawLED(p_d, true);
		}
	}

	private void drawFace(Face f) {
		// TODO implement Faces
		// TODO MAGIC NUMBERS!!!

		Vector3D p = f.pts.get(0);

		Vector3D v1 = p.sub(f.pts.get(1));
		Vector3D v2 = p.sub(f.pts.get(f.pts.size() - 1));

		double l_v1 = v1.length();
		double l_v2 = v2.length();

		double sl_v1 = Math.sqrt(l_v1);
		double sl_v2 = Math.sqrt(l_v2);

		for (double i = .2; i < sl_v1 - .2; i += .1) {
			for (double j = .2; j < sl_v2 - .2; j += .1) {
				double x = p.x - v1.x * i - v2.x * j;
				double y = p.y - v1.y * i - v2.y * j;
				double z = p.z - v1.z * i - v2.z * j;
				Vector3D neu = new Vector3D(x, y, z);
				double l_neu = neu.length();
				if (l_neu < l_v1 - .45 && l_neu < l_v2 - .45)
					drawLED(neu, false);
			}
		}
	}

	private void drawLED(Vector3D p, boolean line) {
		drawLED(p.x, p.y, p.z, line);
	}

	private void drawLED(double x, double y, double z, boolean line) {
		int transX = (int) (SIZE / (2 * MAX) * x + SIZE / 2);
		int transY = (int) (SIZE / (2 * MAX) * y + SIZE / 2);
		int color = Math.max(
				Math.min((int) (2 * HIGH / SIZE * z + HIGH / 2), HIGH), LOW);
		// System.out.printf("x=%d y=%d c=%d\n", transX, transY, color);

		if (transX >= 0 && transX < SIZE && transY >= 0 && transY < SIZE) {
			Double value = z_value.get(transX * SIZE + transY);

			if (value == null || z > value) {
				z_value.put(transX * SIZE + transY, z);
				if (line)
					controller.setColor(transX, transY, color, 0, 0);
				else
					controller.setColor(transX, transY, 0, color, 0);
			}
		}
	}

	public void update() {
		controller.updateLedStripe();
	}
}
