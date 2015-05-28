package uni.dc.networkGenerator.swingUI.graphviz;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class GraphViz {

	public static final String GRAPHVIZ_BIN_DIR = "./bin/graphviz-2.38/";

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

	private InputStream createDiagramStream(StringBuilder dotString,
			OutputFormatEnum fmt) throws IOException {
		List<String> command = new ArrayList<String>();

		command.add(GRAPHVIZ_BIN_DIR + "dot.exe");
		command.add(String.format("-T"));
		command.add(fmt.getName());
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File(System.getenv("temp")));
		final Process process = builder.start();

		BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(
				process.getOutputStream()));
		InputStream rv = new BufferedInputStream(process.getInputStream());

		stdin.append(dotString);
		stdin.newLine();
		stdin.close();

		return rv;
	}

	public StringBuilder renderToString(StringBuilder dotString,
			OutputFormatEnum fmt) throws IOException {
		BufferedReader stdout = new BufferedReader(new InputStreamReader(
				createDiagramStream(dotString, fmt)));
		String outLine;
		StringBuilder rv = new StringBuilder();
		while ((outLine = stdout.readLine()) != null) {
			rv.append(outLine + "\n");
		}
		return rv;
	}

	public Image renderToImage(StringBuilder dotString) throws IOException {
		Image rv = ImageIO.read(createDiagramStream(dotString,
				OutputFormatEnum.BMP));
		return rv;
	}

	public static String dotUid(Object o) {
		return String
				.format("%s%d", o.getClass().getSimpleName(), o.hashCode());
	}

}
