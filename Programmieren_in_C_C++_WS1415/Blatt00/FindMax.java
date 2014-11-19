public class FindMax {
	public static void findSecondLargest(int[] xs) {
		if (xs.length < 2)
			throw new IllegalArgumentException("Das Array hat < 2 Elemente!");

		int largest = Integer.MIN_VALUE;
		int sLargest = Integer.MIN_VALUE;

		for (int x : xs) {
			if (x > largest) {
				sLargest = largest;
				largest = x;
			} else if (x > sLargest)
				sLargest = x;
		}
		System.out.printf("Das zweitgrößte Element ist : %d\n", sLargest);
	}

	public static void main(String[] args) {
		int[] test = { 1, 2, 3, 4, 5, 6 };
		findSecondLargest(test);

		test = new int[] { 1, 1, 1, 1, 1, 4 };
		findSecondLargest(test);

		test = new int[] { -1, -2, -5, -6, -10 };
		findSecondLargest(test);

		test = new int[] { 3, 10 };
		findSecondLargest(test);

		test = new int[] { 1 };
		findSecondLargest(test);
	}
}