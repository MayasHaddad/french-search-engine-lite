/**
 * 
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import abstractClasses.XplodedIndexSearcher;

/**
 * @author mhadda1
 *
 */
public class XplodedIndexSimpleWeightFileSearcher extends XplodedIndexSearcher{

	public double getSimilarity(
			final HashMap<String, Double> weightsOfQuery, final File f2)
					throws IOException {

		if (!f2.exists()) {
			System.err.println("getSimilarity, fileName:" + f2.getName()
					+ " doesn't exist");
			throw new IOException();
		}
		if (!f2.isFile() || !f2.canRead()) {
			System.err.println("getSimilarity, fileName:" + f2.getName()
					+ " isn't a file or can't be read");
			throw new IOException();
		}

		final TreeMap<String, Double> sortedWeightsOfQuery = new TreeMap<String, Double>(
				weightsOfQuery);

		final BufferedReader br2 = new BufferedReader(new InputStreamReader(
				new FileInputStream(f2)));
		String l2; // the current line in f1 and f2
		String[] t2; // table word, tfidf for f1 and f2
		String w1 = null, w2 = null; // the current word in f1 and f2
		Boolean b1, b2; // are w1 and w2 already entered in the di and dk calcul
		// ?
		Double tfidf1 = 0D, tfidf2 = 0D;

		Double d1 = 0D; // for the di and dk calcul
		Double d2 = 0D; // for the di and dk calcul
		Double d1d2 = 0D; // for the didj calculus

		Map.Entry<String, Double> l1 = sortedWeightsOfQuery.firstEntry();
		sortedWeightsOfQuery.remove(l1.getKey());
		l2 = br2.readLine();
		b1 = true;
		b2 = true;

		// while we are not at the end of one of the two files
		while (l1 != null && l2 != null) {
			// System.out.println("d1d2="+d1d2+",d1="+d1+",d2="+d2);
			if (b1) {
				w1 = l1.getKey();
				tfidf1 = l1.getValue();
				d1 += tfidf1 * tfidf1;
				b1 = false;
			}
			if (b2) {
				t2 = l2.split("\t");
				w2 = t2[0];
				tfidf2 = Double.parseDouble(t2[1]);
				d2 += tfidf2 * tfidf2;
				b2 = false;
			}

			if (w1.equals(w2)) {
				d1d2 += tfidf1 * tfidf2;
				sortedWeightsOfQuery.remove(l1.getKey());
				l1 = sortedWeightsOfQuery.firstEntry();

				b1 = true;
				l2 = br2.readLine();
				b2 = true;
			} else if (w1.compareToIgnoreCase(w2) < 0) {
				sortedWeightsOfQuery.remove(l1.getKey());
				l1 = sortedWeightsOfQuery.firstEntry();

				b1 = true;
			} else {
				l2 = br2.readLine();
				b2 = true;
			}
		}

		if (!b1) {
			System.out.println("d1d2=" + d1d2 + ",d1=" + d1 + ",d2=" + d2
					+ "#1");
			sortedWeightsOfQuery.remove(l1.getKey());
			l1 = sortedWeightsOfQuery.firstEntry();
			b1 = false;
		}
		if (!b2) {
			System.out.println("d1d2=" + d1d2 + ",d1=" + d1 + ",d2=" + d2
					+ "#2");
			l2 = br2.readLine();
			b2 = false;
		}

		// rest of file 1
		while (l1 != null) {
			sortedWeightsOfQuery.remove(l1.getKey());
			tfidf1 = l1.getValue();
			d1 += tfidf1 * tfidf1;
			l1 = sortedWeightsOfQuery.firstEntry();
		}
		// rest of file 2
		while (l2 != null) {
			t2 = l2.split("\t");
			tfidf2 = Double.parseDouble(t2[1]);
			d2 += tfidf2 * tfidf2;
			l2 = br2.readLine();
		}
		br2.close();

		return d1d2 / (Math.sqrt(d1) * Math.sqrt(d2));
	}

	public static void main(final String[] args) {

		try {
			System.out.println("Ecrire votre requ�te");
			
			final BufferedReader inputReader = new BufferedReader(
					new InputStreamReader(System.in));
			final String query = inputReader.readLine();
			
			XplodedIndexSearcher s = new XplodedIndexSimpleWeightFileSearcher();
			
			System.out.println(s.getResult(query, 
					new File("/public/iri/projetIRI/corpus/0000/"), 
					s.getContainingFilesOfThisQuery(
							Const.NORMALIZER.normalize(query),
							new File(Const.PATH_TO_INVERTED_FILE_FROM_MERGER))));

		} catch (final IOException e) {
			System.out.println("error: " + e);
		}
	}
}
