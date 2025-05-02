package ex42;

public class Sequences {

	public static void main(String[] args) {
		System.out.println("Teil a)");
		double sum = 0;

		for (int i = 0; i < 6; i++) {
			double sum2 = 0;
			sum = 4 * sum + Math.pow(2, 2 * i);
			for (int j = 0; j <= i; j++) {
				sum2 = sum2 + Math.pow(4, i - j) * Math.pow(2, 2 * j);
			}
			System.out.println(i + ": " + sum);
			System.out.println(i + ": " + sum2);
			System.out.println(i + ": " + Math.pow(4, i) * (i + 1));
		}

		System.out.println("Teil b)");
		sum = 1;

		for (int i = 1; i < 6; i++) {
			double sum2 = 0;
			sum = sum + 3 * Math.pow(3, i);
			sum2 = (Math.pow(3, i + 1) - 1) * 3 / 2 - 2;
			System.out.println(i + ": " + sum);
			System.out.println(i + ": " + sum2);
		}

		System.out.println("Teil c)");
		sum = 1;

		for (int i = 1; i < 6; i++) {
			double sum2 = 0;
			sum = 2 * sum + 8;
			for (int j = 0; j <= i; j++) {
				sum2 = sum2 + 8 * Math.pow(2, j);
			}
			sum2 = sum2 - 7 * Math.pow(2, i);
			System.out.println(i + ": " + sum);
			System.out.println(i + ": " + sum2);
		}

	}

}
