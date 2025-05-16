package ex62;

import java.util.Arrays;

public class Rucksack {
	public static void main(String[] args) {
		int[] g = { 11, 4, 5, 3, 1 };
		int[] v = { 13, 5, 6, 3, 3 };
		System.out.println("G: " + Arrays.toString(g));
		System.out.println("V: " + Arrays.toString(v));
		System.out.println("Antwort: " + Rekursion(g.length, g, v, 12));
	}

	public static int Rekursion(int n, int[] g, int[] v, int G) {
		int[][] opt = new int[n][G + 1];

		for (int j = 0; j <= G; j++) {
			if (j < g[0]) {
				opt[0][j] = 0;
			} else {
				opt[0][j] = v[0];
			}
		}
		for (int i = 1; i < n; i++) {
			for (int j = 0; j <= G; j++) {
				if (g[i] <= j) {
					opt[i][j] = Math.max(opt[i - 1][j], v[i] + opt[i - 1][j - g[i]]);
				} else {
					opt[i][j] = opt[i - 1][j];
				}
			}
		}
		for (int i = 0; i < n; i++) {
			System.out.println(Arrays.toString(opt[n - 1 - i]));
		}
		return opt[n - 1][G];
	}
}
