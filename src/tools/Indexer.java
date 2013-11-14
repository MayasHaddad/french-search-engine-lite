/**
 * 
 */
package tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import expes.Utils;

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

	// For each word, number of document in the corpus containing it
	public static HashMap<String, Integer> DOCUMENT_FREQUENCY = new HashMap<String, Integer>();

	public static int cpt = 0;

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
			final InputStream fileInputStream, final Normalizer normalizer,
			final boolean removeStopWords) throws IOException {
		// Création de la table des mots
		final HashMap<String, Integer> hits = new HashMap<String, Integer>();

		// TODO !
		// Appel de la méthode de normalisation
		// System.out.println(fileName);
		final ArrayList<String> words = normalizer.normalize(fileInputStream,
				removeStopWords, Const.PATH_TO_STOP_WORDS);
		Integer number;
		// Pour chaque mot de la liste, on remplit un dictionnaire
		// du nombre d'occurrences pour ce mot
		for (String word : words) {
			word = word.toLowerCase();
			if (Const.LETTRES_ET_CHIFFRES.contains(word.substring(0, 1))) {
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
		}

		// // Affichage du résultat
		// for (final Map.Entry<String, Integer> hit : hits.entrySet()) {
		// System.out.println(hit.getKey() + "\t" + hit.getValue());
		// }
		return hits;
	}

	/**
	 * Calculate the df in several directories
	 * 
	 * @param dir
	 *            list of directories
	 * @return map<word, nbDocs>
	 * @throws IOException
	 */
	public static HashMap<String, Integer> getDocumentFrequencyFromSeveralDirs(
			final File[] dir) throws IOException {
		if (!Indexer.DOCUMENT_FREQUENCY.isEmpty()) {
			return Indexer.DOCUMENT_FREQUENCY;
		}

		final HashMap<String, Integer> docFreqTot = new HashMap<String, Integer>();
		HashMap<String, Integer> docFreqTmp = new HashMap<String, Integer>();
		for (final File d : dir) {
			docFreqTmp = Indexer.getDocumentFrequency(d);
			for (final String s : docFreqTmp.keySet()) {
				docFreqTmp.put(s, docFreqTmp.get(s));
			}
		}
		return docFreqTot;
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
		System.out.println("---Calculate document frequency (df) in "
				+ dir.getName());
		// if result already calculated, make it easy
		if (!Indexer.DOCUMENT_FREQUENCY.isEmpty()) {
			return Indexer.DOCUMENT_FREQUENCY;
		}

		if (!IOManager.checkInDir(dir)) {
			throw new IOException();
			// return;
		}

		Const.CURRENT_NUMBER_OF_FILE = 0;
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
	private static final boolean getDocumentFrequencyRec(final File dir)
			throws IOException {

		for (final File f : dir.listFiles()) {
			if (f.isFile()) {
				if (!f.getName().endsWith(Const.EXTENTION_KEEP)) {
					continue;
				}
				Indexer.analyseOneFileForDocumentFrequency(f);
				Const.CURRENT_NUMBER_OF_FILE++;
				if (Const.CURRENT_NUMBER_OF_FILE > Const.MAX_NUMBER_OF_FILE) {
					System.out.println("DF, reached max : "
							+ Const.MAX_NUMBER_OF_FILE + " files");
					return false;
				}
			} else {
				if (Indexer.getDocumentFrequencyRec(f) == false) {
					return false;
				}

			}

		}
		return true;
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
		Indexer.cpt++;
		if (Indexer.cpt % 100 == 0) {
			System.gc();
			System.out.println(Indexer.cpt); // thinking...
			System.out.println((double) Utils.getUsedMemory()
					/ (double) 1073741824);
		}
		String wordLC;
		Integer number;
		final ArrayList<String> alreadySeenInTheCurrentFile = new ArrayList<String>();

		// normalize
		final ArrayList<String> words = Const.NORMALIZER.normalize(
				f.getAbsolutePath(), Const.REMOVE_STOP_WORDS,
				Const.PATH_TO_STOP_WORDS);

		// increment doc freq
		for (final String word : words) {
			wordLC = word.toLowerCase();
			// Ajouté par Seb : on teste directement si le mot est
			// "interessant", a savoir s'il commence par un caractère contenu
			// dans const.LETTRES_ET_CHIFFRES
			if (Const.LETTRES_ET_CHIFFRES.contains(wordLC.substring(0, 1))) {
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
	public static HashMap<String, Double> getTfIdf(
			final InputStream fileInputStream,
			final HashMap<String, Integer> dfs, final int documentNumber,
			final Normalizer normalizer, final boolean removeStopWords)
			throws IOException {

		final HashMap<String, Double> tfIdfs = new HashMap<String, Double>();

		Double logtf = 0.0, idf = 0.0;
		for (final Map.Entry<String, Integer> entry : Indexer
				.getTermFrequencies(fileInputStream, normalizer,
						removeStopWords).entrySet()) {
			// <word, tf>
			if (entry.getValue() == 0) {
				logtf = 0D;
			} else {
				logtf = 1 + Math.log10(entry.getValue());
			}
			idf = Math.log10((double) documentNumber / dfs.get(entry.getKey()));
			// System.out.println(word + "\t" + tfIdf);
			tfIdfs.put(entry.getKey(), logtf * idf);
		}
		return tfIdfs;
	}

}
