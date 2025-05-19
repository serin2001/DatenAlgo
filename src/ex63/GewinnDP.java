package ex63;

import java.util.Arrays;
import java.util.Random;

public class GewinnDP {

	static int g = 1;
	static int n = 7;
	static int[] a = new int[n]; // { 5, 10, 9, 7, 11, 11, 15 }; //
	static int[] gewinn0;
	static int[] gewinn1;

	public static void main(String[] args) {
		a = new Random().ints(n, 1, 36).toArray(); // Generiere n ganze Zahlen aus
		// [1, 35]

		gewinn0 = new int[a.length];
		Arrays.fill(gewinn0, Integer.MIN_VALUE);
		gewinn1 = new int[a.length];
		Arrays.fill(gewinn1, Integer.MIN_VALUE);

		System.out.println("Kurse:  " + Arrays.toString(a));
		System.out.println("==========================");
		System.out.println("Gewinn " + ":" + " " + A(a, a.length - 1, 0));
	}

	public static int A(int[] S, int i, int j) {
		if (i == 0) {
			if (j == 0) {
				gewinn0[i] = 0;
				return 0;
			} else {
				gewinn1[i] = -g - S[0];
				return -g - S[0];
			}
		} else {
			int zero;
			if (gewinn0[i - 1] <= Integer.MIN_VALUE)
				zero = A(S, i - 1, 0);
			else
				zero = gewinn0[i - 1];
			int one;
			if (gewinn1[i - 1] <= Integer.MIN_VALUE)
				one = A(S, i - 1, 1);
			else
				one = gewinn1[i - 1];

			if (j == 0) {
				gewinn0[i] = Math.max(zero, one + S[i] - g);
				return gewinn0[i];
			} else {
				gewinn1[i] = Math.max(one, zero - S[i] - g);
				return gewinn1[i];
			}
		}
	}
}
