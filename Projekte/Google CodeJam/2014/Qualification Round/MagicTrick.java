import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MagicTrick {

	public static void main(String[] args) {
		try {
			String[] ans = new String[1];

			int guess1, guess2;
			int[][] x;
			int[][] x2;

			File[] listOfFiles = new File(".").listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				int pos = 0;
				if (listOfFiles[i].isFile()
						&& checkFileExtension(listOfFiles[i], ".in")) {
					System.out.println("File " + listOfFiles[i].getName());
					Scanner in = new Scanner(listOfFiles[i]);
					ans = new String[Integer.parseInt(in.nextLine())];
					System.out.println("T = " + ans.length);
					while (in.hasNextLine()) {
						x = new int[4][];
						guess1 = Integer.parseInt(in.nextLine());
						for (int j = 0; j < x.length; j++) {
							x[j] = splitString(in.nextLine(), " ");
						}
						guess2 = Integer.parseInt(in.nextLine());
						
						x2 = new int[4][];
						for (int j = 0; j < x2.length; j++) {
							x2[j] = splitString(in.nextLine(), " ");
						}
						ans[pos++] = magic(guess1, guess2, x, x2);

					}
					writeArrayToFile(listOfFiles[i].getName().split("\\.")[0],
							"Case #%d: %s", 1, ans);
					in.close();
					System.out.println("-------------------------------");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	private static String magic(int guess1, int guess2, int[][] x, int[][] x2) {
		int[] l1 = x[guess1 - 1];
		int[] l2 = x2[guess2 - 1];
		int same = -1;
		int cnt = 0;
		for (int i : l1) {
			for (int j : l2) {
				if (i == j) {
					same = i;
					cnt++;
				}
			}
		}
		switch (cnt) {
		case 0:
			return "Volunteer cheated!";
		case 1:
			return same + "";
		default:
			return "Bad magician!";
		}
	}

	private static void writeArrayToFile(String name, String fmt, int num,
			String[] answ) {

		try {
			PrintWriter out = new PrintWriter(name + ".txt");
			for (String i : answ) {
				out.println(String.format(fmt, num++, i));
			}
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}

	private static boolean checkFileExtension(File file, String string) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".")).equals(string);
		} catch (Exception e) {
			return false;
		}
	}

	private static int[] splitString(String toSplit, String delim) {
		String[] chars = toSplit.split(delim);
		int[] res = new int[chars.length];
		int pos = 0;
		for (String s : chars) {
			res[pos++] = Integer.parseInt(s);
		}
		return res;
	}
}
