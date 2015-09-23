package uni.dc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GraphVizPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane = new JScrollPane();
	private JLabel imageLabel = new JLabel("No GraphViz output generated");
	private StringBuilder dotString = null;
	private GraphViz graphViz = new GraphViz();
	private BufferedImage img = null;

	public GraphVizPanel() {
		super(new BorderLayout());
		this.scrollPane = new JScrollPane(imageLabel);
		this.add(scrollPane, BorderLayout.CENTER);
		imageLabel.setMinimumSize(new Dimension(128, 128));
	}

	public void setDot(StringBuilder dotString) {
		this.dotString = dotString;
		updateImage();
	}

	public void saveToFile(StringBuilder dotString, File filename) {
		try {
			ImageIO.write(graphViz.renderToImage(dotString), "png", filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateImage() {
		try {
			img = graphViz.renderToImage(dotString);
			imageLabel.setIcon(new ImageIcon(img));
			imageLabel.setText(null);
		} catch (IOException e) {
			imageLabel.setText(e.getMessage());
			e.printStackTrace();
		}
		imageLabel.setSize(this.getSize());
		imageLabel.repaint();
		imageLabel.revalidate();
	}
}
