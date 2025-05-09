package ex52;

import java.util.Arrays;
import java.util.Random;

public class ClosestPair {

	public static void main(String[] args) {
		int n = 12;
		int[] h = new int[n];
		h = new Random().ints(n, 0, 101).toArray(); // Generiere n ganze Zahlen aus [0, 100]

		int m = 7;
		int[] s = new int[m];
		s = new Random().ints(m, 0, 101).toArray(); // Generiere m ganze Zahlen aus [0, 100]
		Arrays.sort(s); // sortiere

		System.out.println("Haeuser: " + Arrays.toString(h));
		System.out.println("Sirenen: " + Arrays.toString(s));

		int[] d = new int[n];
		for (int i = 0; i < n; i++) {
			d[i] = Math.abs(findClosest(s, 0, m - 1, h[i]) - h[i]);
		}
		Arrays.sort(d); // sortiere
		System.out.println("Distanzen: " + Arrays.toString(d));
		System.out.println("Max. Distanz: " + d[n - 1]);
	}

	public static int findClosest(int[] array, int a, int b, int x) {
		if (a == b)
			return array[a];
		else {
			int mi = (a + b) / 2;
			if (x <= array[mi]) { // wenn x im linken Teilarray liegt, soll nur dort gesucht werden
				return findClosest(array, a, mi, x);
			} else { // wenn x im rechten Teilarray liegt, soll nur dort gesucht werden
				return findClosest(array, mi + 1, b, x);
			}
		}
	}

}
