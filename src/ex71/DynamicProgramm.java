package ex71;

import java.util.Arrays;
import java.util.Random;

public class DynamicProgramm {
	static int m = 6;
	static int n = 9;
	static int[] a = { 11, 1, 23, 23, 36, 23, 17, 23, 27 }; // new int[n]; //
	static int[] b = { 1, 31, 33, 15, 20, 28 }; // new int[m]; //
	static int[] f = new int[a.length];
	static int[][] mass = new int[a.length][b.length];

	public static void main(String[] args) {
		// a = new Random().ints(n, 1, 36).toArray(); // Generiere n ganze Zahlen aus
		// [1, 35]
		// b = new Random().ints(m, 1, 36).toArray();
		Arrays.fill(f, -1);
		int k = 0;
		while (k < a.length) {
			Arrays.fill(mass[k], -1); // Integer.MIN_VALUE);
			k++;
		}
		System.out.println("Reihe A:  " + Arrays.toString(a));
		System.out.println("Reihe B:  " + Arrays.toString(b));
		System.out.println("==========================");

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < Math.min(b.length, i + 1); j++) {
				mass[i][j] = R(i, j);
			}
		}

		int i = a.length - 1;
		int j = b.length - 1;
		while (i >= 0 && j >= 0) {
			f[i] = j;
			if (i == 0)
				break;
			if (j >= 0 && mass[i][j] == Math.max(mass[i - 1][j], Math.abs(a[i] - b[j]))) {
				System.out.println("i--: " + i + j);
				i--;
			} else if (j >= 1 && mass[i][j] == Math.max(mass[i - 1][j - 1], Math.abs(a[i] - b[j]))) {
				System.out.println("ij--: " + i + j);
				i--;
				j--;
			}
			System.out.println("f  " + Arrays.toString(f));
		}
		// Reste
		while (i >= 0) {
			System.out.println("i: " + i + ", j: " + j);
			f[i] = 0;
			i--;
		}
		k = 0;
		while (k < a.length) {
			System.out.println("Mass:  " + Arrays.toString(mass[k]));
			k++;
		}

		System.out.println("Antwort:  " + mass[a.length - 1][b.length - 1]);

		System.out.println("f:  " + Arrays.toString(f));
	}

	public static int R(int i, int j) {
		if (i == 0 && j == 0) {
			return Math.abs(a[i] - b[j]);
		} else {
			if (mass[i][j] < 0) {
				if (i > 0 && j == 0) {
					mass[i][j] = Math.max(R(i - 1, j), Math.abs(a[i] - b[j]));
				} else if (i == j && i > 0) {
					mass[i][j] = Math.max(Math.abs(a[i] - b[j]), R(i - 1, j - 1));
				} else {
					// Das ist das gleiche wie min(max(R(i - 1, j - 1), |a[i] - b[j]|), max(R(i - 1, j), |a[i] - b[j]|), aber kuerzer
					mass[i][j] = Math.max(Math.min(R(i - 1, j - 1), R(i - 1, j)), Math.abs(a[i] - b[j]));
				}
			}
		}
		return mass[i][j];
	}
}
