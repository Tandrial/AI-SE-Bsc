package uni.dc.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class GraphViz {

	public static String GRAPHVIZ_BIN_DIR = "";

	public static enum OutputFormatEnum {
		BMP("bmp"), PNG("png"), DOT("dot");

		private String formatName;

		private OutputFormatEnum(String formatName) {
			this.formatName = formatName;
		}

		public String getName() {
			return this.formatName;
		}
	}

	private InputStream createDiagramStream(StringBuilder dotString, OutputFormatEnum fmt) throws IOException {
		try {
			ProcessBuilder builder = new ProcessBuilder(GRAPHVIZ_BIN_DIR + "dot", "-T", fmt.getName());
			builder.directory(new File("."));
			Process process = builder.start();

			BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			InputStream rv = new BufferedInputStream(process.getInputStream());

			stdin.append(dotString);
			stdin.newLine();
			stdin.close();

			return rv;
		} catch (Exception e) {
			if (setGraphVizLocation()) {
				return createDiagramStream(dotString, fmt);
			} else {
				e.printStackTrace();
				return null;
			}
		}
	}

	private boolean setGraphVizLocation() {
		JFileChooser c = new JFileChooser();
		c.setCurrentDirectory(new java.io.File("."));
		c.setDialogTitle("Please select the GraphViz Folder!");
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		c.setAcceptAllFileFilterUsed(false);
		if (c.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {
			GRAPHVIZ_BIN_DIR = c.getSelectedFile().getAbsolutePath() + "\\";
			return true;
		} else {
			return false;
		}
	}

	public BufferedImage renderToImage(StringBuilder dotString) throws IOException {
		BufferedImage rv = ImageIO.read(createDiagramStream(dotString, OutputFormatEnum.PNG));
		return rv;
	}

	public static String dotUid(Object o) {
		return String.format("%s%d", o.getClass().getSimpleName(), o.hashCode());
	}
}
