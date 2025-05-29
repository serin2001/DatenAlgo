package ex81;

import java.util.Arrays;

public class Auftrag implements Comparable<Auftrag> {
	int b, nr;
	double g, bpw;

	public Auftrag(int nr, double g, int b, double bpw) {
		this.nr = nr;
		this.g = g;
		this.b = b;
		this.bpw = bpw;
	}

	@Override
	public String toString() {
		return "Auftrag: {" + nr + ", " + g + ", " + b + ", " + bpw + "}";
	}

	@Override
	public int compareTo(Auftrag o) {
		return -this.bpw < -o.bpw ? -1 : (this.bpw == o.bpw ? 0 : 1);
	}
}
