package ex61;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class DynamicProgramm {

	public static void main(String[] args) {
		int n = 6;
		int[] a = new int[n]; // { 7, 1, 22, 4, 6 }; //
		a = new Random().ints(n, 1, 26).toArray(); // Generiere n ganze Zahlen aus
		// [1, 25]
		System.out.println("Zahlen: " + Arrays.toString(a));
		System.out.println("Antwort: " + Rekursion(a, a.length));
	}

	public static int Rekursion(int[] a, int n) {
		int[] S = new int[a.length];
		S[0] = a[0];
		for (int i = 1; i < a.length; i++) {
			S[i] = a[i];
			for (int j = 1; j <= i; j++) {
				if (a[i] >= a[i - j] && S[i] < a[i] + S[i - j]) {
					S[i] = a[i] + S[i - j];
				}
				System.out.println("Teilsummen: " + i + ":" + j + " " + S[i]);
			}
		}
		System.out.println("Summen: " + Arrays.toString(S));
		
		return Arrays.stream(S).max().getAsInt(); 
	}
}
