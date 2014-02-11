package testSuite;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Graph.*;
import OSMparser.*;

public class GraphTests {

	OSMParser parser;
	Graph g;

	@Before
	public void setUp() throws Exception {
		parser = new OSMParser(new File("graphTest.osm"));

		assert parser.getKnoten().size() > 0;

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetClosest() {

		g = new Graph(parser.getMin(), parser.getMax(), parser.getWays());

		long correctNodeID = 287142087;

		Knoten closest = g.getClosestKnoten(51.45f, 6.9696f);
		if (closest.getId() != correctNodeID)
			fail("Nächster Knoten NICHT korrekt gefunden. Gefunden: "
					+ closest.toString());
	}
}