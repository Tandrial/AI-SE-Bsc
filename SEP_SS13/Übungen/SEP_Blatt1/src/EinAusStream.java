import java.io.IOException;
import java.io.OutputStream;

public class EinAusStream extends OutputStream {

	private static final String newLine = System.getProperty("line.separator")
			.toString();

	private StringBuilder internalBuffer = new StringBuilder();
	private EinAusRahmen subject;

	public EinAusStream(EinAusRahmen subject) {
		this.subject = subject;
	}

	@Override
	public void write(int value) throws IOException {
		char character = (char) value;
		internalBuffer.append(character);
		if (internalBuffer.toString().endsWith(newLine)) {
			subject.zeigeAus(internalBuffer.toString());
			internalBuffer = new StringBuilder();
		}
	}

}
