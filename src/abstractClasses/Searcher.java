/**
 * 
 */
package abstractClasses;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import tools.Const;
import tools.FrenchStemmer;
import tools.IOManager;
import tools.Indexer;

/**
 * Answer to a request Take and normalize the request, return the first best
 * files.
 * 
 * @author user
 * 
 */
public abstract class Searcher {

	public static Map<String, Integer> DOCUMENT_FRENQUENCIES_QUERY_WORDS = new HashMap<String, Integer>();

	/*
	 * This method returns the files which contain all the query words at once
	 */

	public TreeSet<String> getOnlyCommonFilesOfQueryWords(Map<String, TreeSet<String>> filesContainingQueryWords){

		ArrayList<String> result = new ArrayList<String>();
		
		Map.Entry<String, TreeSet<String>> fE = ((Entry<String, TreeSet<String>>) filesContainingQueryWords.entrySet().toArray()[0]);
		result = new ArrayList<String>(fE.getValue());
		for(String fileName : fE.getValue()){
			for(Map.Entry<String, TreeSet<String>> entry : filesContainingQueryWords.entrySet()){
				if(!entry.getValue().contains(fileName)){
					result.remove(fileName);
					break;
				}
			}
		}
		
		return new TreeSet<String>(result);
	}

	/**
	 * 
	 * @param weightsOfQuery
	 * @param fileName
	 *            a file .poids
	 * @return a similarity measurement
	 */
	private double getSimilarity(
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

	public TreeMap<Double, TreeSet<String>> getSimilarDocuments(
			final String query,
			final String weightsDirectoryPath,
			final int numberOfDocumentsInTheCorpus,
			final Map<String, TreeSet<String>> filenamesContainingQueryWords) throws IOException {

		final ArrayList<String> alreadyVisitedFilename = new ArrayList<String>();

		final HashMap<String, Double> weightsOfQuery = Indexer.getTfIdf(
				(InputStream) new ByteArrayInputStream(query.getBytes()),
				(HashMap<String, Integer>) Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS,
				numberOfDocumentsInTheCorpus + 1, new FrenchStemmer(),
				Const.REMOVE_STOP_WORDS);

		final Map<Double, TreeSet<String>> result = new HashMap<Double, TreeSet<String>>();

		for (final TreeSet<String> filenamesListContainingQueryWord : filenamesContainingQueryWords
				.values()) {
			for (final String filename : filenamesListContainingQueryWord) {
				if (!alreadyVisitedFilename.contains(filename)) {

					final Double similarity = this.getSimilarity(
							weightsOfQuery, new File(weightsDirectoryPath
									+ File.separator + filename)); // file is in
					// .txt.poid
					TreeSet<String> filenamesList = result.get(similarity);

					if (filenamesList == null) {
						filenamesList = new TreeSet<String>();
					}

					filenamesList.add(filename);
					result.put(similarity, filenamesList);
					alreadyVisitedFilename.add(filename);
				}
			}
		}
		return new TreeMap(result);
	}

	public TreeMap<Double, TreeSet<String>> getResult(
			final String query, final File Corpus, final Map<String, TreeSet<String>> filenamesContainingQueryWords) throws IOException {

		return this.getSimilarDocuments(query,	Const.PATH_TO_WEIGHT_FILES, IOManager.getNbFiles(Corpus), filenamesContainingQueryWords);
	}

	public void printSimilarDocuments(int topNResults,
			final TreeMap<Double, TreeSet<String>> filesBySimilarity) {

		Map.Entry<Double, TreeSet<String>> element = filesBySimilarity
				.lastEntry();

		while (element != null && topNResults > 0) {
			if (element.getValue().size() > 1) {
				for (final String currentFileName : element.getValue()) {
					System.out.println(currentFileName + "\t"
							+ element.getKey());
				}
			} else {
				System.out.println(element.getValue().first() + "\t"
						+ element.getKey());
			}
			topNResults++;
			filesBySimilarity.remove(element.getKey());
			element = filesBySimilarity.lastEntry();
		}
	}
}
