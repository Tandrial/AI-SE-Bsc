import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ledControl.BoardController;
import ledControl.KeyBuffer;

public class CubeTest {

	private final String ip = "192.168.69.11";

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

	int xOff = 0;
	int yOff = 0;

	int sleep = 12;

	private static BoardController controller;
	private static KeyBuffer input;
	private static LedScreen leds;
	private static KeyControl keys;

	RenderMode rMode = RenderMode.FACES;
	DisplayMode dMode = DisplayMode.EMU;
	Model model = Model.CUBE;
	boolean running = false;

	List<Face> faces = new ArrayList<Face>();

	public CubeTest(DisplayMode mode) {
		this.dMode = mode;
		if (controller == null)
			controller = BoardController.getBoardController();
		if (mode == DisplayMode.NETWORK)
			controller.addNetWorkHost(ip);

		if (input == null)
			input = controller.getKeyBuffer();
		if (leds == null)
			leds = new LedScreen(controller);
		if (keys == null)
			keys = new KeyControl();
	}

	public void initScene() {
//		StdDraw.setScale(-2, 2);
		if (dMode == DisplayMode.DEBUG) {
			StdDraw.setScale(-2, 2);
			StdDraw.setTitle("0 FPS");
			lastTimeFPS = System.nanoTime();
		}
		switch (model) {
		case CUBE:
			faces.addAll(Geometry.Cube(0, 0, 0, 1));
			break;
		case OBJFILE:
			faces.addAll(OBJ_Parser.readFile(new File("tri.obj")));
			break;
		case MONKEY:
			faces.addAll(OBJ_Parser.readFile(new File("monkey.obj")));
			break;
		default:
			break;
		}
		draw();
	}

	public void startLoop() {
		running = true;
		while (running) {
			if (rotX != 0 || rotY != 0 || rotZ != 0 || forceDraw) {
				draw();
				forceDraw = false;
			}
			update();

			try {
				Thread.sleep((long) sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		leds.clearLED();
		leds.update();
		System.out.println("Exiting.");
		System.exit(0);
	}

	private void draw() {
		leds.draw(faces, rMode);

		if (dMode == DisplayMode.DEBUG) {
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
	}

	private void update() {
		keys.processInput(this, input.pop());

		totalX += rotX;
		totalY += rotY;
		totalZ += rotZ;

		rot(rotX, rotY, rotZ);
	}

	public void rot(double x, double y, double z) {
		for (Face face : faces) {
			if (x != 0)
				face.rotX(x);
			if (y != 0)
				face.rotY(y);
			if (z != 0)
				face.rotZ(z);
		}
	}

	public static void main(String[] args) {
		CubeTest t;
//		 args = new String[] { "d" };
		if (args.length == 1 && args[0].charAt(0) == 'b')
			t = new CubeTest(DisplayMode.EMU);
		else if (args.length == 1 && args[0].charAt(0) == 'd')
			t = new CubeTest(DisplayMode.DEBUG);
		else
			t = new CubeTest(DisplayMode.NETWORK);

		t.initScene();
		t.startLoop();
	}
}