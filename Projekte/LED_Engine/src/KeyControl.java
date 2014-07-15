import java.awt.event.KeyEvent;
import java.io.File;

public class KeyControl {

	public void processInput(CubeTest c, KeyEvent event) {
		if (event != null && event.getID() == KeyEvent.KEY_PRESSED) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				c.rotY -= Math.PI / 20;
				break;
			case KeyEvent.VK_RIGHT:
				c.rotY += Math.PI / 20;
				break;
			case KeyEvent.VK_UP:
				c.rotX += Math.PI / 20;
				break;
			case KeyEvent.VK_DOWN:
				c.rotX -= Math.PI / 20;
				break;
			case KeyEvent.VK_PAGE_UP:
				c.rotZ += Math.PI / 20;
				break;
			case KeyEvent.VK_PAGE_DOWN:
				c.rotZ -= Math.PI / 20;
				break;

			case KeyEvent.VK_NUMPAD4:
				c.yOff -= 1;
				break;
			case KeyEvent.VK_NUMPAD6:
				c.yOff += 1;
				break;
			case KeyEvent.VK_NUMPAD2:
				c.xOff -= 1;
				break;
			case KeyEvent.VK_NUMPAD8:
				c.xOff += 1;
				break;
			case KeyEvent.VK_N:
				c.model = c.model.getNext();
				c.rMode = RenderMode.POINTS;
			case KeyEvent.VK_R:
				c.rotX = 0;
				c.totalX = 0;
				c.rotY = 0;
				c.totalY = 0;
				c.rotZ = 0;
				c.totalZ = 0;
				c.faces.clear();
				switch (c.model) {
				case CUBE:
					c.faces.addAll(Geometry.Cube(0, 0, 0, 1));
					break;
				case OBJFILE:
					c.faces.addAll(OBJ_Parser.readFile(new File("tri.obj")));
					break;
				case MONKEY:
					c.faces.addAll(OBJ_Parser.readFile(new File("monkey.obj")));
				default:
					break;
				}

				break;
			case KeyEvent.VK_Q:
				for (Face face : c.faces)
					face.scale(1 / c.scale, 1 / c.scale, 1 / c.scale);
				c.scale = 1;
				break;
			case KeyEvent.VK_L:
				c.rMode = c.rMode.getNext();
				break;
			case KeyEvent.VK_W:
				c.scale += .1;
				for (Face face : c.faces)
					face.scale(1.1, 1.1, 1.1);
				break;
			case KeyEvent.VK_S:
				c.scale -= .1;
				if (c.scale <= 0)
					c.scale = .1;
				for (Face face : c.faces)
					face.scale(0.9, 0.9, 0.9);
				break;

			case KeyEvent.VK_U:
				c.sleep += 20;
				break;
			case KeyEvent.VK_J:
				c.sleep -= 20;
				break;
			case KeyEvent.VK_ESCAPE:
				c.running = false;
				break;
			}
			c.forceDraw = true;
		}
	}
}
