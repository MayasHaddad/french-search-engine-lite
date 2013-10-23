package expes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ExpesStructures {

	private static final int MAX = 10000000;

	private static void constructionTableauInt() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de type int");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final int[] tab = new int[ExpesStructures.MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab[i] = i;
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void constructionTableauInteger() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de type Integer");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final Integer[] tab = new Integer[ExpesStructures.MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab[i] = new Integer(i);
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void constructionArrayListInteger() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'une ArrayList de type Integer");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final ArrayList<Integer> tab = new ArrayList<Integer>();
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab.add(i);
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void constructionTableauChar() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de char");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final char[] tab = new char[ExpesStructures.MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab[i] = 'a';
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void constructionString() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'un String");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final String tab = "";
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab.concat("a");
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void constructionHashMap() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'une HashMap<Integer, String>");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final HashMap<Integer, String> tab = new HashMap<Integer, String>();
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab.put(i, "test");
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void constructionTableauString() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de String");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final String[] tab = new String[ExpesStructures.MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab[i] = "test";
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void constructionTableauTableauChar() {
		final int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de tableaux de char");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		final char[][] tab = new char[ExpesStructures.MAX][];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab[i] = new char[4];
			tab[i][0] = 't';
			tab[i][1] = 'e';
			tab[i][2] = 's';
			tab[i][3] = 't';
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out
				.println("------------------------------------------------------");
	}

	private static void testArrayList() {
		final ArrayList<Integer> tab = new ArrayList<Integer>();
		System.out.println("Test ArrayList");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Ajout d'éléments");
		int chronoId = Utils.startChrono();
		for (int i = 0; i < ExpesStructures.MAX / 1000; i++) {
			tab.add(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Recherche d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0; i < ExpesStructures.MAX / 100; i++) {
			tab.contains(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Suppression d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab.remove(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out
				.println("------------------------------------------------------");
	}

	private static void testHashSet() {
		final HashSet<Integer> tab = new HashSet<Integer>();
		System.out.println("Test HashSet");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Ajout d'éléments");
		int chronoId = Utils.startChrono();
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab.add(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Recherche d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0; i < ExpesStructures.MAX / 100; i++) {
			tab.contains(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out.println("  Suppression d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0; i < ExpesStructures.MAX; i++) {
			tab.remove(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId)
				+ " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory()
				/ 1024 + " Ko");
		System.out
				.println("------------------------------------------------------");
	}

	public static void main(final String[] args) {
		// Tableau vs Liste d'entiers
		// ///////////////////////////////
		System.out.println("On démarre...");
		// Utils.waitKeyPressed(); // Commande à utiliser pour lancer ou
		// consulter la jconsole
		ExpesStructures.constructionTableauInt();
		// Utils.waitKeyPressed();
		System.out.println("Lancement du gc");
		System.gc();
		// Utils.waitKeyPressed();
		ExpesStructures.constructionTableauInteger();
		// Utils.waitKeyPressed();
		System.out.println("Lancement du gc");
		System.gc();
		// Utils.waitKeyPressed();
		ExpesStructures.constructionArrayListInteger();
		// Utils.waitKeyPressed();
		System.out.println("Lancement du gc");
		System.gc();
		// Utils.waitKeyPressed();
		// Tableau vs Chaîne de caractères
		// ///////////////////////////////
		// constructionTableauChar();
		// Utils.waitKeyPressed();
		// System.gc();
		// Utils.waitKeyPressed();
		// constructionString();
		// Utils.waitKeyPressed();
		// System.gc();
		// Utils.waitKeyPressed();
		// HashMap vs Tableau vs Tableau de Tableaux
		// ///////////////////////////////
		// constructionHashMap();
		// Utils.waitKeyPressed();
		// System.gc();
		// Utils.waitKeyPressed();
		// constructionTableauString();
		// Utils.waitKeyPressed();
		// System.gc();
		// Utils.waitKeyPressed();
		// constructionTableauTableauChar();
		// Utils.waitKeyPressed();
		// System.gc();
		// ArrayList vs HashSet
		// ///////////////////////////////
		// testArrayList();
		// Utils.waitKeyPressed();
		// System.gc();
		// Utils.waitKeyPressed();
		// testHashSet();
		// Utils.waitKeyPressed();
	}

}
