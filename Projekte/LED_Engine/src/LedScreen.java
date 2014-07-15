import java.util.ArrayList;
import java.util.Collections;
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
		controller.resetColors();
		z_value = new HashMap<>();
		for (Face f : faces) {
			if (mode == RenderMode.FACES)
				drawFace(f);

			double[][] a = f.toArray();
			for (int i = 0; i < a[0].length; i++) {
				switch (mode) {
				case POINTS:
				case FACES:
					drawLED(a[0][i], a[1][i], a[2][i], false);
					break;
				case LINES:
					drawLine(f.pts.get(i), f.pts.get((i + 1) % a[0].length),
							false);
					break;
				default:
					drawLED(a[0][i], a[1][i], a[2][i], false);
					break;
				}
			}
		}
		update();
	}

	private void drawFace(Face f) {
		List<Vector3D> vs = new ArrayList<Vector3D>(f.pts);

		Collections.sort(vs, (s, t) -> (int) (s.y - t.y));

		Vector3D v1 = vs.get(0);
		Vector3D v2 = vs.get(1);
		Vector3D v3 = vs.get(2);

		if (v2.y == v3.y) {
			fillBottomFlatTriangle(v1, v2, v3);
		} else if (v1.y == v2.y) {
			fillTopFlatTriangle(v1, v2, v3);
		} else {
			Vector3D v4 = new Vector3D((v1.x + ((v2.y - v1.y) / (v3.y - v1.y))
					* (v3.x - v1.x)), v2.y, 0);
			fillBottomFlatTriangle(v1, v2, v4);
			fillTopFlatTriangle(v2, v4, v3);
		}
	}

	public void fillBottomFlatTriangle(Vector3D v1, Vector3D v2, Vector3D v3) {
		double invslope1 = (v2.x - v1.x) / (v2.y - v1.y) / 10;
		double invslope2 = (v3.x - v1.x) / (v3.y - v1.y) / 10;

		double curx1 = v1.x;
		double curx2 = v1.x;

		for (double scanlineY = v1.y; scanlineY <= v2.y; scanlineY += .1) {
			drawLine(new Vector3D(curx1, scanlineY, 0), new Vector3D(curx2,
					scanlineY, 0), true);
			curx1 += invslope1;
			curx2 += invslope2;
		}
	}

	public void fillTopFlatTriangle(Vector3D v1, Vector3D v2, Vector3D v3) {
		double invslope1 = (v3.x - v1.x) / (v3.y - v1.y) / 10;
		double invslope2 = (v3.x - v2.x) / (v3.y - v2.y) / 10;

		double curx1 = v3.x;
		double curx2 = v3.x;

		for (double scanlineY = v3.y; scanlineY > v1.y; scanlineY -= .1) {
			drawLine(new Vector3D(curx1, scanlineY, 0), new Vector3D(curx2,
					scanlineY, 0), true);
			curx1 -= invslope1;
			curx2 -= invslope2;
		}
	}

	public void clearLED() {
		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 12; y++) {
				controller.setColor(x, y, 0, 0, 0);

			}
		}
	}

	private void drawLine(Vector3D p1, Vector3D p2, boolean face) {
		Vector3D v = p2.sub(p1);
		double l = v.length();

		for (float d = 0; d < l; d += STEP_SIZE) {
			Vector3D p_d = p1.add(v.mult(d / l));
			drawLED(p_d, face);
		}
	}

	private void drawLED(Vector3D p, boolean face) {
		drawLED(p.x, p.y, p.z, face);
	}

	private void drawLED(double x, double y, double z, boolean face) {
		int transX = (int) (SIZE / (2 * MAX) * x + SIZE / 2);
		int transY = (int) (-SIZE / (2 * MAX) * y + SIZE / 2);
		int color = Math.max(
				Math.min((int) (2 * HIGH / SIZE * z + HIGH / 2), HIGH), LOW);
		// if (face)
		// System.out.printf("x: %s ==>%d y: %s ==>%d z: %s ==>%d\n", x,
		// transX, y, transY, z, color);

		if (transX >= 0 && transX < SIZE && transY >= 0 && transY < SIZE) {
			Double value = z_value.get(transX * SIZE + transY);

			if (value == null || z > value) {
				z_value.put(transX * SIZE + transY, z);
				if (face) {
					controller.setColor(transX, transY, 0, color, 0);

				} else
					controller.setColor(transX, transY, color, 0, 0);
			}
		}
	}

	public void update() {
		controller.updateLedStripe();
	}
}
