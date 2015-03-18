package lala.lib;

public class UntypedChurchConstants {
	public static final String TRUE = "(λt.(λf.(t)))";
	public static final String FALSE = "(λt.(λf.(f)))";
	public static final String NOT = "(λb.(((b)" + FALSE + ")" + TRUE + "))";
	public static final String AND = "(λb.(λc.(((b)(c))" + FALSE + ")))";
	public static final String OR = "(λb.(λc.(((b)" + TRUE + ")(c))))";

	public static final String SUCC = "(λn.(λs.(λz.((s)(((n)(s))(z))))))";
	public static final String PLUS = "(λm.(λn.(λs.(λz.(((m)(s))(((n)(s))(z)))))))";
	public static final String PRED = "(λn.(λf.(λx." + "(((" + "(n)(λg.("
			+ "λh.((h)((g)(f)))" + ")" + ")" + ")"

			+ "(λu.(x)))" + "(λu.(u)))" + ")))";

	public static final String churchInt(int i) {
		String ret = "(λs.(λz.(z)))";
		for (int j = 1; j <= i; j++) {
			ret = ret.replaceAll("\\(z\\)", "((s)(z))");
		}
		return ret;
	}

	// terrible brute force code....
	public static final int intFromChurchInt(String s) {
		int i = 0;
		String churchNum = churchInt(0);

		while (!s.equals(churchNum) && churchNum.length() <= s.length()) {
			i++;
			churchNum = churchInt(i);
		}

		if (s.equals(churchNum))
			return i;

		throw new RuntimeException(
				"not syntactical identical to our church nums");
	}

}
