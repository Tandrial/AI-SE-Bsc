package networkGenerator.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SwingSandobox {

	public static void main(String[] args) throws IOException {
		JFrame frame = buildFrame();

		final BufferedImage image = ImageIO.read(new File(
				"C:\\Projects\\MavenSandbox\\src\\main\\resources\\img.jpg"));

		JPanel pane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		frame.add(pane);
	}

	private static JFrame buildFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		frame.setVisible(true);
		return frame;
	}
}