package ex63;

import java.util.Arrays;
import java.util.Random;

public class Gewinn {

	static int[] kauf; // = {1, 0, 0, 1, 1, 1, 0};
	static int g = 1;
	static int n = 7;
	static int[] a = new int[n]; // { 5, 10, 7, 11, 11, 15 }; // new int[n];
	static int[] gewinn;

	public static void main(String[] args) {
		a = new Random().ints(n, 1, 36).toArray(); // Generiere n ganze Zahlen aus
		// [1, 35]
	
		kauf = new int[a.length];
		// In der letzten Periode wird immer verkauft
		// Hinten immer eine 0 dran
		gewinn = new int[(int) Math.pow(2, a.length - 1)];
		int max = Integer.MIN_VALUE;
		int maxIndex = -1;
		int[] optKauf = new int[a.length];
		// Durchprobieren aller Strategien
		for (int i = 0; i < Math.pow(2, a.length - 1); i++) {
			String s = String.format("%" + (a.length - 1) + "s", Integer.toBinaryString(i)).replace(' ', '0') + 0;
			for (int ij = 0; ij < kauf.length; ij++) {
				try {
					kauf[ij] = Integer.parseInt(s.substring(ij, ij + 1));
				} catch (NumberFormatException nfe) {
					System.out.println("NOTE: write something here if you need to recover from formatting errors");
				}
			}
			// System.out.println("Kauf: " + " " + " " + Arrays.toString(kauf));
			gewinn[i] = A(a, a.length, 0);
			if (gewinn[i] > max) {
				max = gewinn[i];
				maxIndex = i;
				optKauf = Arrays.copyOfRange(kauf, 0, kauf.length);
			}
		}
	//	System.out.println("Max. gewinn " + Arrays.stream(gewinn).max().getAsInt());
		System.out.println("Max.Gewinn Index: " + maxIndex);
		System.out.println("OptKauf:" + Arrays.toString(optKauf));

		System.out.println("Kurse:  " + Arrays.toString(a));

		kauf = Arrays.copyOfRange(optKauf, 0, optKauf.length);
		System.out.println("Kauf: " + " " + " " + Arrays.toString(kauf));
		System.out.println("==========================");
		System.out.println("Gewinn " + ":" + " " + A(a, a.length, 0));
	}

	public static int A(int[] S, int i, int j) {
		if (i == 0) {
			if (j == 0) {
				return 0;
			} else {
				return -g - S[0];
			}
		} else {
			if (kauf[i - 1] == j) {
				return A(S, i - 1, j);
			} else {
				if (kauf[i - 1] == 0) {
					return A(S, i - 1, 0) - S[i] - g;
				} else {
					return A(S, i - 1, 1) + S[i] - g;
				}
			}
		}
	}
}
