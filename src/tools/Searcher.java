/**
 * 
 */
package tools;

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
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Answer to a request Take and normalize the request, return the first best
 * files.
 * 
 * @author user
 * 
 */
public class Searcher {

	public static Map<String, Integer> DOCUMENT_FRENQUENCIES_QUERY_WORDS = new HashMap<String, Integer>();

	// Retrieve all the files which contain the query
	/*	public static Map<String, TreeSet<String>> getContainingFilesOfThisQuery(
			final ArrayList<String> queryNormalized, final File invertedFile)
					throws IOException {

		final Map<String, TreeSet<String>> filesContainingQueryWords = new HashMap<String, TreeSet<String>>();

		// lecture du fichier texte
		final InputStream ips = new FileInputStream(invertedFile);
		final InputStreamReader ipsr = new InputStreamReader(ips);
		final BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			if (queryNormalized.contains(line.split("\t")[0])) {
				// the current word is in the query
				// store the files containing the word
				final ArrayList<String> filenamesArrayList = new ArrayList<String>();
				for (final String filename : line.split("\t")[2].split(",")) {
					filenamesArrayList.add(filename);
				}

				final TreeSet<String> filenamesTreeSet = new TreeSet<String>(
						filenamesArrayList);
				filesContainingQueryWords.put(line.split("\t")[0],
						filenamesTreeSet);
				Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS.put(
						line.split("\t")[0],
						Integer.parseInt(line.split("\t")[1]));
			}
		}
		br.close();
		return filesContainingQueryWords;
	}
	 */
	public static Map<String, TreeSet<String>> getContainingFilesOfThisQueryExplodedIndex(
			final ArrayList<String> queryNormalized,
			final File invertedFilesDirectory) throws IOException {


		Map<String, TreeSet<String>> filesContainingQueryWords = new HashMap<String, TreeSet<String>>();

		if(invertedFilesDirectory.isDirectory()){
			for(String queryWord : queryNormalized){
				boolean queryWordIsInTheCorpus = false;
				
				// We won't search for a word which has only one 
				if(queryWord.length() >= 2){

					File invertedFile = new File(invertedFilesDirectory + File.separator + queryWord.substring(0, 2) + ".txt");
					
					// no need to process a word for which there are no inverted file ...
					if(invertedFile.exists()){

						// lecture du fichier texte
						final InputStream ips = new FileInputStream(invertedFile);
						final InputStreamReader ipsr = new InputStreamReader(ips);
						final BufferedReader br = new BufferedReader(ipsr);
						String line;
						while ((line = br.readLine()) != null) {
							if (queryNormalized.contains(line.split("\t")[0])) {
								// the current word is in the query
								// store the files containing the word
								final ArrayList<String> filenamesArrayList = new ArrayList<String>();
								for (final String filename : line.split("\t")[2]
										.split(",")) {
									String updatedFileName = "";
									int a = 8-filename.length();
									for(int i=0;i<a;i++){
										updatedFileName += "0";
									}
									updatedFileName = updatedFileName + filename+".txt.poid";
									filenamesArrayList.add(updatedFileName);
									//System.out.println(updatedFileName);
								}

								final TreeSet<String> filenamesTreeSet = new TreeSet<String>(
										filenamesArrayList);
								filesContainingQueryWords.put(line.split("\t")[0],
										filenamesTreeSet);
								Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS.put(
										line.split("\t")[0],
										Integer.parseInt(line.split("\t")[1]));
								queryWordIsInTheCorpus = false;
							}
						}
						br.close();
					}
				}
				if(!queryWordIsInTheCorpus){
					// even though the word is not in the corpus, it's in the query, so df = 1
					Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS.put(queryWord, 1);
				}
			}
		}
		return filesContainingQueryWords;
	}

	/**
	 * 
	 * @param weightsOfQuery
	 * @param fileName
	 *            a file .poids
	 * @return a similarity measurement
	 */
	public static double getSimilarity(
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

	public static TreeMap<Double, TreeSet<String>> getSimilarDocuments(
			final String query, final File invertedFilesDir,
			final String weightsDirectoryPath,
			final int numberOfDocumentsInTheCorpus) throws IOException {

		final ArrayList<String> queryNormalized = new FrenchStemmer()
		.normalize(query);

		final Map<String, TreeSet<String>> filenamesContainingQueryWords = Searcher
				.getContainingFilesOfThisQueryExplodedIndex(queryNormalized,
						invertedFilesDir);

		final ArrayList<String> alreadyVisitedFilename = new ArrayList<String>();

		final HashMap<String, Double> weightsOfQuery = Indexer.getTfIdf(
				(InputStream) new ByteArrayInputStream(query.getBytes()),
				(HashMap) Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS,
				numberOfDocumentsInTheCorpus + 1, new FrenchStemmer(),
				Const.REMOVE_STOP_WORDS);

		final Map<Double, TreeSet<String>> result = new HashMap<Double, TreeSet<String>>();

		for (final TreeSet<String> filenamesListContainingQueryWord : filenamesContainingQueryWords
				.values()) {
			for (final String filename : filenamesListContainingQueryWord) {
				if (!alreadyVisitedFilename.contains(filename)) {

					final Double similarity = Searcher.getSimilarity(
							weightsOfQuery, new File(weightsDirectoryPath
									+ File.separator + filename)); // file is in .txt.poid
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
	
	public static TreeMap<Double, TreeSet<String>> getResult(String query, File Corpus) throws IOException{
		
		return Searcher.getSimilarDocuments(
				query, (new File(Const.PATH_TO_INVERTED_FILE_FROM_MERGER)),
				Const.PATH_TO_WEIGHT_FILES,
				IOManager.getNbFiles(Corpus));
	}
	public static void printSimilarDocuments(int topNResults, TreeMap<Double, TreeSet<String>> filesBySimilarity){

		Map.Entry<Double, TreeSet<String>> element = filesBySimilarity.lastEntry();

		while(element != null && topNResults > 0){
			if(element.getValue().size() > 1 ){
				for(String currentFileName : element.getValue()){
					System.out.println(currentFileName + "\t" + element.getKey());
				}
			}else{
				System.out.println(element.getValue().first() + "\t" + element.getKey());
			}
			topNResults++;
			filesBySimilarity.remove(element.getKey());
			element = filesBySimilarity.lastEntry();
		}
	}
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length != 3) {
			System.err.println("Usage : java " + Searcher.class.getName()
					+ " weightsDir invertedFilesDir NUMBEROFFILES");
			System.err.println("Example : java " + Searcher.class.getName()
					+ " /weight /inverted-file.txt/");
			System.exit(1);
		}
		
		// getting the program's command line arguments
		final String weightsDirectoryPath = args[0];
		final String invertedFilesDirPath = args[1];
		final int numberOfDocumentsInTheCorpus = Integer.parseInt(args[2]);

		// getting the user's query from the keybord
		final BufferedReader inputReader = new BufferedReader(
				new InputStreamReader(System.in));

		try {
			// Creating the needed files
			final File weightsDirectory = new File(weightsDirectoryPath);
			final File invertedFilesDir = new File(invertedFilesDirPath);

			System.out.println("Ecrire votre requ�te");
			final String query = inputReader.readLine();

			/*final ArrayList<String> queryNormalized = new FrenchStemmer()
			.normalize(query);
			
			
			final ArrayList<String> queryNormalized = Const.NORMALIZER.normalize(new ByteArrayInputStream(query.getBytes()), Const.REMOVE_STOP_WORDS, Const.PATH_TO_STOP_WORDS);
					
			System.out.println(Searcher
					.getContainingFilesOfThisQueryExplodedIndex(
							queryNormalized, invertedFilesDir));

			// int numberOfDocumentsInTheCorpus =
			// IOManager.countDocumentRecursively(new File("F:\\lemonde"));

			//printSimilarDocuments(100, Searcher.getSimilarDocuments(query, invertedFilesDir, weightsDirectoryPath, numberOfDocumentsInTheCorpus));

			for (final Map.Entry<Double, TreeSet<String>> similarity : Searcher
					.getSimilarDocuments(query, invertedFilesDir,
							weightsDirectoryPath, numberOfDocumentsInTheCorpus)
							.entrySet()) {
				for (final String similarFile : similarity.getValue()) {
					System.out.println(similarFile + " " + similarity.getKey());
				}
			}
			System.out.println();
			// System.out.println(weightsOfQuery);*/
			System.out.println(getResult("Bazin", new File("/public/iri/projetIRI/corpus/0000")));
			
		} catch (final IOException e) {
			System.out.println("error: " + e);
		}
	}
}
