package ex81;

import java.util.Arrays;

public class Auftrag implements Comparable<Auftrag> {
	int bp, nr;
	double ge, bpw;

	public Auftrag(int nr, double ge, int bp, double bpw) {
		this.nr = nr;
		this.ge = ge;
		this.bp = bp;
		this.bpw = bpw;
	}

	@Override
	public String toString() {
		return "Auftrag: {" + nr + ", " + ge + ", " + bp + ", " + bpw + "}";
	}

	@Override
	public int compareTo(Auftrag o) {
		// Absteigend
		return -this.bpw < -o.bpw ? -1 : (this.bpw == o.bpw ? 0 : 1);
	}
}
