import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class CookieClickerAlpha {

	public static void main(String[] args) {
		try {
			String[] ans = new String[1];

			float[] x;

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
						ans[pos++] = simulate(x[0], x[1], x[2]);

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

	private static String simulate(float C, float F, float X) {
		if (C >= X)
			return (X / 2) + "";

		BigDecimal cB = new BigDecimal(C + "");
		BigDecimal cF = new BigDecimal(F + "");
		BigDecimal cX = new BigDecimal(X + "");

		BigDecimal cps = new BigDecimal("2.0");

		// Double timeTillDone = (X / cps);
		BigDecimal ttD = cX.divide(cps, 6, BigDecimal.ROUND_HALF_UP);
		BigDecimal newTime = new BigDecimal("0.0");
		for (int i = 1; i < 1000; i++) {
			cps = new BigDecimal("2.0");
			newTime = cB.divide(cps, 6, BigDecimal.ROUND_HALF_UP);

			for (int j = i - 1; j > 0; j--) {
				cps = cps.add(cF);
				newTime = newTime.add(cB.divide(cps, 6,
						BigDecimal.ROUND_HALF_UP));

			}
			cps = cps.add(cF);
			newTime = newTime.add(cX.divide(cps, 6, BigDecimal.ROUND_HALF_UP));

			if (newTime.compareTo(ttD) < 0) {
				ttD = newTime;
			} else {
				break;
			}
		}
		System.out.println("Best Time " + (ttD.toString()));
		return ttD.toString();
	}

	private static void writeArrayToFile(String name, String fmt, int num,
			String[] answ) {

		try {
			PrintWriter out = new PrintWriter(name + ".txt");
			for (String i : answ) {
				out.println(String.format(fmt, num++, i).replace(',', '.'));
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

	private static float[] splitString(String toSplit, String delim) {
		String[] chars = toSplit.split(delim);
		float[] res = new float[chars.length];
		int pos = 0;
		for (String s : chars) {
			res[pos++] = Float.parseFloat(s);
		}
		return res;
	}
}
