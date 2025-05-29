package ex81;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;

public class Greedy {

	static int n = 7;
	static int[] g = new int[n]; //
	static int[] b = new int[n]; //
	static int[] scheduling = new int[n];
	static double[] bonus_per_week = new double[n];
	static double[] einnahmen = new double[n];

	static Auftrag[] auftrag = new Auftrag[n];

	public static void main(String[] args) {
		g = new Random().ints(n, 1, 36).toArray(); // Generiere n ganze Zahlen aus
		// [1, 35]
		b = new Random().ints(n, 1, 11).toArray();
		for (int i = 0; i < n; i++) {
			bonus_per_week[i] = g[i] * b[i] / 100.0;
			auftrag[i] = new Auftrag(i, g[i], b[i], bonus_per_week[i]);
			// System.out.println(auftrag[i].toString());
		}
		System.out.println("==========================");
		Arrays.sort(auftrag);
		for (int i = 0; i < n; i++) {
			scheduling[i] = auftrag[i].nr;
			System.out.println(auftrag[i].toString());
			einnahmen[i] = auftrag[i].g + (n - 1 - i) * auftrag[i].bpw;
		}
		System.out.println("G: GE " + Arrays.toString(g));
		System.out.println("BpW % " + Arrays.toString(b));
		System.out.println("==========================");
		System.out.println("Bonus pro Woche, GE " + Arrays.toString(bonus_per_week));
		System.out.println("Scheduling " + Arrays.toString(scheduling));
		System.out.println("Einnahmen pro Auftrag, GE " + Arrays.toString(einnahmen));
		System.out.println("Einnahmen, GE " + DoubleStream.of(einnahmen).sum());
	}
}