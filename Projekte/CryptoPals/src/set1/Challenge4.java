package set1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Challenge4 {
	public static void main(String[] args) {
		List<String> results = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader("4.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				results.addAll(Challenge3.genPossibleDecrypts(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(results, new EnglishLangSort());
		System.out.println(String.format("decrypt : %s", results.get(0)));
	}
}
