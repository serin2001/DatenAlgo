package ex43;

import java.util.Arrays;
import java.util.Random;

public class Anzahl {

	public static void main(String[] args) {
		int n = 10;
		int[] a = new int[n];
		a = new Random().ints(n, 0, 101).toArray(); // Generiere n ganze Zahlen aus [0, 100]
		Arrays.sort(a); // sortiere
		System.out.println(Arrays.toString(a));

		int c = count(a, 0, n - 1);
		System.out.println("Es gibt " + c + " verschiedene Zahlen im Array");
	}

	public static int count(int[] array, int a, int b) {
		if (a == b)
			return 1;
		else {
			int m = (a + b) / 2; // Da int nur ganze Zahlen aufnehmen kann, wird bei Zahlen >= 0 automatisch abgerundet
			int l = count(array, a, m);
			int r = count(array, m + 1, b);
			if (array[m] != array[m + 1])
				return l + r; // wenn beide Teile nicht mit der gleichen Zahlen enden bzw. beginnen
			else
				return l + r - 1; // wenn beide Teile mit der gleichen Zahlen enden bzw. beginnen
		}
	}

}
