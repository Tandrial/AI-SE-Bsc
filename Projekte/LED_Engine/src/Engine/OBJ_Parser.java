package Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJ_Parser {

	public static List<Face> readFile(File f) {

		List<Vector3D> vertices = new ArrayList<Vector3D>();
		List<Face> faces = new ArrayList<Face>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;

			while ((line = br.readLine()) != null) {
				String[] words = line.split(" ");
				switch (words[0]) {
				case "v":
					Vector3D v = new Vector3D(Double.parseDouble(words[1]),
							Double.parseDouble(words[2]),
							Double.parseDouble(words[3]),
							Vector3D.VECTOR_POSITION);
					vertices.add(v);
					break;
				case "f":
					Vector3D v1 = vertices.get(Integer.parseInt(words[1]
							.split("/")[0]) - 1);
					Vector3D v2 = vertices.get(Integer.parseInt(words[2]
							.split("/")[0]) - 1);
					Vector3D v3 = vertices.get(Integer.parseInt(words[3]
							.split("/")[0]) - 1);
					faces.add(new Face(Vector3D.origin, v1, v2, v3));
					break;

				default:
					break;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return faces;
	}
}
