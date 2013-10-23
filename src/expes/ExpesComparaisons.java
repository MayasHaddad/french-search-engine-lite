package expes;

public class ExpesComparaisons {

	private static final int MAX = 1000000000;

	private static void testComparaisonString() {
		final String s1 = "tableau";
		final String s2 = "sable";
		final String s3 = "tablette";

		int chronoId = Utils.startChrono();
		System.out.println("Comparaison de chaînes de " + s1 + " et de " + s2);
		for (int i = 0; i < ExpesComparaisons.MAX; i++) {
			if (s1.equals(s2)) {
				System.out.println("    " + i);
			}
		}
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		chronoId = Utils.startChrono();
		System.out.println("Comparaison de chaînes de " + s1 + " et de " + s3);
		for (int i = 0; i < ExpesComparaisons.MAX; i++) {
			if (s1.equals(s2)) {
				System.out.println("    " + i);
			}
		}
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void testComparaisonInt() {
		final int i1 = Integer.parseInt("1100000000001011011101010111000", 2); // 1610988216
		final int i2 = Integer.parseInt("1100000000001011011101010111001", 2); // 1610988217
		final int i3 = Integer.parseInt("0100000000001011011101010111000", 2); // 537246392

		int chronoId = Utils.startChrono();
		System.out.println("Comparaison de " + i1 + " et de " + i2);
		for (int i = 0; i < ExpesComparaisons.MAX; i++) {
			if (i1 == i2) {
				System.out.println("    " + i);
			}
		}
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		chronoId = Utils.startChrono();
		System.out.println("Comparaison de " + i1 + " et de " + i3);
		for (int i = 0; i < ExpesComparaisons.MAX; i++) {
			if (i1 == i2) {
				System.out.println("    " + i);
			}
		}
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	public static void main(final String[] args) {
		ExpesComparaisons.testComparaisonString();
		ExpesComparaisons.testComparaisonInt();
	}
}
