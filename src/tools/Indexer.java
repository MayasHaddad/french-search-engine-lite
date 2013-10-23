/**
 * 
 */
package tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Weight file, invertedFiles, tfidf...
 * 
 * @author mhadda1
 * 
 */
public abstract class Indexer {
	/**
	 * Le répertoire du corpus
	 */
	// CHEMIN A CHANGER si nécessaire
	// protected static String COLLECTION_DIRNAME =
	// "/public/iri/projetIRI/corpus/";

	// Remove the too simple words
	public static boolean REMOVE_STOP_WORDS = false;
	// The local stop-words path
	public static String PATH_TO_STOP_WORDS = null;
	// Number of files in the corpus
	public static Integer NB_FILES_IN_CORPUS = null;
	// For each word, number of document in the corpus containing it
	public static HashMap<String, Integer> DOCUMENT_FREQUENCY = new HashMap<String, Integer>();
	// the used normalizer
	public static Normalizer NORMALIZER = new FrenchStemmer();

	public static long START_TIME = 0;
	public static long CURRENT_TIME = 0;

	public static String EXTENTION_KEEP = ".txt";

	/**
	 * Get the number of times a word appears in a file.
	 * 
	 * @param fileName
	 *            the input file
	 * @param normalizer
	 *            normalisation class used
	 * @return map<word, nbOccurences>
	 * @throws IOException
	 */
	public static HashMap<String, Integer> getTermFrequencies(
			final String fileName, final Normalizer normalizer,
			final boolean removeStopWords) throws IOException {
		// Création de la table des mots
		final HashMap<String, Integer> hits = new HashMap<String, Integer>();

		// TODO !
		// Appel de la méthode de normalisation
		// System.out.println(fileName);
		final ArrayList<String> words = normalizer.normalize(fileName,
				removeStopWords, Indexer.PATH_TO_STOP_WORDS);
		Integer number;
		// Pour chaque mot de la liste, on remplit un dictionnaire
		// du nombre d'occurrences pour ce mot
		for (String word : words) {
			word = word.toLowerCase();
			// on récupère le nombre d'occurrences pour ce mot
			number = hits.get(word);
			// Si ce mot n'était pas encore présent dans le dictionnaire,
			// on l'ajoute (nombre d'occurrences = 1)
			if (number == null) {
				hits.put(word, 1);
			}
			// Sinon, on incrémente le nombre d'occurrence
			else {
				hits.put(word, ++number);
			}
		}

		// // Affichage du résultat
		// for (final Map.Entry<String, Integer> hit : hits.entrySet()) {
		// System.out.println(hit.getKey() + "\t" + hit.getValue());
		// }
		return hits;
	}

	/**
	 * Calculate for each word, the number of document containing it
	 * 
	 * @param dirName
	 *            input directory
	 * @param normalizer
	 *            normalisation class used
	 * @return map<word, nbDocs>
	 * @throws IOException
	 */
	public static HashMap<String, Integer> getDocumentFrequency(final File dir)
			throws IOException {
		System.out.println("Calculate document frequency : df");
		// if result already calculated, make it easy
		if (!Indexer.DOCUMENT_FREQUENCY.isEmpty()) {
			return Indexer.DOCUMENT_FREQUENCY;
		}

		if (!IOManager.checkInDir(dir)) {
			throw new IOException();
			// return;
		}

		Indexer.getDocumentFrequencyRec(dir);
		return Indexer.DOCUMENT_FREQUENCY;
	}

	/**
	 * Go through the tree of files to calculate doc frequency
	 * 
	 * @param dir
	 *            input dir
	 * @throws IOException
	 */
	private static final void getDocumentFrequencyRec(final File dir)
			throws IOException {

		for (final File f : dir.listFiles()) {
			if (f.isFile()) {
				if (!f.getName().endsWith(Indexer.EXTENTION_KEEP)) {
					continue;
				}
				Indexer.analyseOneFileForDocumentFrequency(f);
			} else {
				Indexer.getDocumentFrequencyRec(f);
			}
		}
	}

	/**
	 * Update <words, nbOfOccurencesInTheCorpus>. Can be improved with a "trie"
	 * Structure for the alreadySeenInTheCurrentFile.
	 * 
	 * @param f
	 *            File used to update the doc freq
	 * @throws IOException
	 */
	private static void analyseOneFileForDocumentFrequency(final File f)
			throws IOException {
		System.out.print("-"); // thinking...
		String wordLC;
		Integer number;
		final ArrayList<String> alreadySeenInTheCurrentFile = new ArrayList<String>();

		// normalize
		final ArrayList<String> words = Indexer.NORMALIZER.normalize(
				f.getAbsolutePath(), Indexer.REMOVE_STOP_WORDS,
				Indexer.PATH_TO_STOP_WORDS);

		// increment doc freq
		for (final String word : words) {
			wordLC = word.toLowerCase();
			number = Indexer.DOCUMENT_FREQUENCY.get(wordLC);
			// (word !in doc_freq)?(add it):(increment freq);
			if (number == null) {
				Indexer.DOCUMENT_FREQUENCY.put(wordLC, 1);
			} else if (!alreadySeenInTheCurrentFile.contains(wordLC)) {
				Indexer.DOCUMENT_FREQUENCY.put(wordLC, ++number);
				alreadySeenInTheCurrentFile.add(wordLC);
			}
		}
	}

	/**
	 * Calculate tfidf
	 * 
	 * @param fileName
	 *            input file
	 * @param dfs
	 *            <word, freq in the corpus>
	 * @param documentNumber
	 *            nb of docs in the corpus
	 * @param normalizer
	 *            normalisation class used
	 * @param removeStopWords
	 *            true : remove too usual words
	 * @return <wordsInFileName, tfidf>
	 * @throws IOException
	 */
	public static HashMap<String, Double> getTfIdf(final String fileName,
			final HashMap<String, Integer> dfs, final int documentNumber,
			final Normalizer normalizer, final boolean removeStopWords)
			throws IOException {

		final HashMap<String, Double> tfIdfs = new HashMap<String, Double>();

		Double logtf = 0.0, idf = 0.0;
		for (final Map.Entry<String, Integer> entry : Indexer
				.getTermFrequencies(fileName, normalizer, removeStopWords)
				.entrySet()) {
			// <word, tf>
			if (entry.getValue() == 0) {
				logtf = 0D;
			} else {
				logtf = 1 + Math.log10(entry.getValue());
			}
			idf = Math.log10(documentNumber / dfs.get(entry.getKey()));
			// System.out.println(word + "\t" + tfIdf);
			tfIdfs.put(entry.getKey(), logtf * idf);
		}
		return tfIdfs;
	}

}