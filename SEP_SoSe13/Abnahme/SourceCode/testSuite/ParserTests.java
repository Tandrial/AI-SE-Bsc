package testSuite;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Graph.Graph;
import Graph.Kante;
import OSMparser.OSMParser;
import OSMparser.Way;

public class ParserTests {
	private File mapFile;
	private OSMParser parser;

	@Before
	public void setUp() throws Exception {
		mapFile = new File("baustelle.osm");
		this.parser = new OSMParser(mapFile);
		parser.createGraphFromKarte();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOSMParserFile() {
		try {
			int anzahlGesperrt = 0;

			for (Way way : parser.getWays()) {
				if (way.isGesperrt())
					anzahlGesperrt += way.getKnoten().size() - 1;
			}

			List<Kante> kanten = Graph.getGraph().getAlleKanten();
			int gefunden = 0;

			for (Kante kante : kanten) {
				if (kante.getGesperrt()) {
					gefunden++;
					System.out.println(kante);
				}
			}

			if (anzahlGesperrt != gefunden) {
				fail("Es wurden" + gefunden
						+ " gepserrte Kanten gefunden. Korrekt ist "
						+ anzahlGesperrt);
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("EXCEPTION");

		}
	}
}