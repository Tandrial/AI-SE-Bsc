package Engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CubeTest {

	private long lastTimeFPS = 0;
	private int frames = 0;

	double rotX = 0.0;
	double totalX = 0;
	double rotY = 0.0;
	double totalY = 0;
	double rotZ = 0.0;
	double totalZ = 0;
	double scale = 1.0;

	boolean forceDraw = false;
	boolean running = false;

	int xOff = 0;
	int yOff = 0;

	int sleep = 200;

	List<Face> faces = new ArrayList<Face>();

	public CubeTest() {
	}

	public void initScene() {
		StdDraw.setScale(-2, 2);
		StdDraw.setScale(-2, 2);
		StdDraw.setTitle("0 FPS");
		lastTimeFPS = System.nanoTime();

		// faces.add(Geometry.SquareXY(0, 0, 0, 1));
		faces.addAll(OBJ_Parser.readFile(new File("plane.obj")));
		draw();
	}

	public void startLoop() {
		running = true;
		while (running) {
			draw();

			update();

			try {
				Thread.sleep((long) sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Exiting.");
		System.exit(0);
	}

	private void update() {

		for (Face f : faces) {
			f.rotX(Math.PI / 30);
			// f.trans(0, 0.1, 0);
		}

	}

	private void draw() {

		frames++;
		if (System.nanoTime() - lastTimeFPS >= 1000000000L) {
			StdDraw.setTitle(frames + " FPS");
			frames = 0;
			lastTimeFPS = System.nanoTime();
		}

		StdDraw.clear();
		for (Face f : faces) {
			double[][] a = f.toArray();
			StdDraw.polygon(a[0], a[1]);
		}

	}

	public static void main(String[] args) {
		CubeTest t = new CubeTest();

		t.initScene();
		t.startLoop();
	}
}