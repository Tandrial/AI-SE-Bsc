import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MinesweeperMaster {
	public static void main(String[] args) {
		try {
			String[] ans = new String[1];

			int[] x;

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
						x = splitString(in.nextLine(), " ");
						ans[pos++] = solve(x[0], x[1], x[2]);
					}
					writeArrayToFile(listOfFiles[i].getName().split("\\.")[0],
							"Case #%d:", 1, ans);
					in.close();
					System.out.println("-------------------------------");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	private static String solve(int R, int C, int M) {
		char[][] feld = new char[R][C];

		for (int i = 0; i < feld.length; i++) {
			for (int j = 0; j < feld[i].length; j++) {
				feld[i][j] = '.';
			}
		}
		feld[R - 1][C - 1] = 'c';

		int distance = getDistance(0, 0, R, C) - 1;
		while (M > 0) {
			if (distance <= 1 && M > 0)
				return "Impossible";
			for (int i = 0; i < feld.length; i++) {
				for (int j = 0; j < feld[i].length; j++) {
					if (feld[i][j] != 'c' && feld[i][j] != '*'
							&& getDistance(i, j, R, C) > distance && M > 0) {
						M--;
						feld[i][j] = '*';
					}
				}
			}
			distance--;
		}
		StringBuilder s = new StringBuilder();

		for (char[] c : feld) {
			for (char cs : c) {
				s.append(cs);
			}
			s.append("\n");
		}
		s.setLength(s.length() - 1);
		System.out.println("---");
		System.out.println(s.toString());
		return s.toString();
	}

	private static int getDistance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	private static void writeArrayToFile(String name, String fmt, int num,
			String[] answ) {

		try {
			PrintWriter out = new PrintWriter(name + ".txt");
			for (String i : answ) {
				out.println(String.format(fmt, num++));
				out.println(i);
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
