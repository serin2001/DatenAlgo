package ex72;

import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class Tankstops {
	static int m = 80; // Tank
	static int n = 9; // Anzahl Tankstellen
	static int k = 250; // Strecke
	static int pos = 0; // Aktuelle Position
	static int[] p = {40, 85, 111, 126, 149, 156, 174, 210,248}; // new int[n]; //
	static int[] e = new int[n]; //

	public static void main(String[] args) {
		Arrays.sort(p);
		e = new Random().ints(n, 20, 81).toArray(); // Abgabemengen

		System.out.println("Tanken:  " + Arrays.toString(p));
		System.out.println("Abgabe:  " + Arrays.toString(e));
		System.out.println("==========================");
		System.out.println("Anzahl Stops " + Rekursion(k, m));
		System.out.println("Restsprit " + m);
	}

	public static int Rekursion(int km, int liter) {
		if (km <= liter) {
			m = liter - km;
			return 0;
		} else {
			// Passende Tankstellen finden
			ArrayList<Integer> tankshops = new ArrayList<>();
			ArrayList<Integer> tankorte = new ArrayList<>();
			ArrayList<Integer> tankbilanz = new ArrayList<>();
			for (int i = 0; i < p.length; i++) {
				if (p[i] <= pos + liter && p[i] > pos) {
					tankshops.add(i);
					tankorte.add(p[i] - pos);
					tankbilanz.add(liter - p[i] + pos + e[i]);
				}
			}
			// Fehler wenn Tankstellen zu weit voneinander sind
			int maximum = tankbilanz.get(0);
			int maxIndex = 0;
			for (int i = 0; i < tankbilanz.size(); i++) {
				if (maximum <= tankbilanz.get(i)) {
					maximum = tankbilanz.get(i);
					maxIndex = i;
				}
			}
			pos += tankorte.get(maxIndex);
			System.out.println("Tank at km " + pos);
			System.out.println("Remaining distance = " + "" + (km - tankorte.get(maxIndex)));
			System.out.println("Liter = " + "" + tankbilanz.get(maxIndex));
			return 1 + Rekursion(km - tankorte.get(maxIndex), tankbilanz.get(maxIndex));
		}
	}

}
