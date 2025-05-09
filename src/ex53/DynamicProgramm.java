package ex53;

import java.util.Arrays;
import java.util.Random;

public class DynamicProgramm {

	public static void main(String[] args) {
		int n = 8;
		int[] a = new int[n];
		a = new Random().ints(n, 1, 4).toArray(); // Generiere n ganze Zahlen aus [1, 3]
		System.out.println("Zahlen: " + Arrays.toString(a));

		System.out.println("Antwort: " + Anzahl(a));
	}
	
	public static int Anzahl(int[] a) {
		int n = a.length;
		int[] m = new int[n];
		m[n-1] = 0;		// Java zaehlt ab 0
		for (int i = n - 2; i >= 0; i--) {		
			if (a[i] == 1 || i == n - 2) m[i] = 1 + m[i+1];
			else if (a[i] == 2 || i == n - 3 && a[i] >= 2) m[i] = 1 + Math.min(m[i+1], m[i+2]);
			else if (a[i] == 3 && i <= n - 4) m[i] = 1 + Math.min(Math.min(m[i+1], m[i+2]), m[i+3]);
		}
		System.out.println(" Zuege: " + Arrays.toString(m));
		return m[0];
	}
}