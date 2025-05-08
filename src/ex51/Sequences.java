package ex51;

public class Sequences {

		public static void main(String[] args) {
			System.out.println("Teil a)");
			double sum = 1;

			for (int i = 1; i < 6; i++) {
				double sum2 = 0;
				sum = 2 * sum + 4;
				sum2 = 5 * Math.pow(2, i) - 4;
				//for (int j = 0; j <= i; j++) {
					//sum2 = sum2 + Math.pow(4, i - j) * Math.pow(2, 2 * j);
				//}
				System.out.println(i + ": " + sum);
				System.out.println(i + ": " + sum2);
				//System.out.println(i + ": " + Math.pow(4, i) * (i + 1));
			}

			System.out.println("Teil b)");
			sum = 2;

			for (int i = 1; i < 6; i++) {
				double sum2 = 0;
				sum = 2* sum + 2 * Math.pow(2, i);
				sum2 = Math.pow(2, i + 1) * (i+1);
				System.out.println(i + ": " + sum);
				System.out.println(i + ": " + sum2);
			}

			System.out.println("Teil c)");
			sum = 3;

			for (int i = 1; i < 6; i++) {
				double sum2 = 0;
				sum = 3 * sum + 3 * Math.pow(3, 3*i) + 3 * Math.pow(3, i);
				for (int j = 1; j < i; j++) {
					sum2 = sum2 + Math.pow(3, j) * (Math.pow(3, 3*(i-j+1)) + Math.pow(3, i-j+1));
				}
				sum2 = sum2 + Math.pow(3, i) * (3+ 27 + 3);
				System.out.println(i + ": " + sum);
				System.out.println(i + ": " + sum2);
				System.out.println(i + ": " + Math.pow(3, i+1)* (11 + (8*i - 89 + Math.pow(9, i+1))/8));
			}

		}

	}

