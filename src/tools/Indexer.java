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
	protected static String COLLECTION_DIRNAME = "/public/iri/projetIRI/corpus/";

	/**
	 * filter files according to their extention
	 * 
	 * @param fileNamesList
	 *            The list of file to filter
	 * @param extension
	 *            The extensions to keep
	 * @return List of filtered files
	 */
	public static ArrayList<String> keepExtension(final String[] fileNamesList,
			final String extension) {

		final ArrayList<String> fileNamesArrayListWithoutUndesiredExtension = new ArrayList<String>();
		// keep the file with the good extention
		for (final String fileName : fileNamesList) {
			if (fileName.contains(extension)) {
				fileNamesArrayListWithoutUndesiredExtension.add(fileName);
			}
		}

		return fileNamesArrayListWithoutUndesiredExtension;
	}

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
		System.out.println(fileName);
		final ArrayList<String> words = normalizer.normalize(fileName,
				removeStopWords);
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
		for (final Map.Entry<String, Integer> hit : hits.entrySet()) {
			System.out.println(hit.getKey() + "\t" + hit.getValue());
		}
		return hits;
	}

	/**
	 * For each word, the number of document containing it
	 * 
	 * @param dirName
	 *            input directory
	 * @param normalizer
	 *            normalisation class used
	 * @return map<word, nbDocs>
	 * @throws IOException
	 */
	private static HashMap<String, Integer> getDocumentFrequency(
			final String dirName, final Normalizer normalizer,
			final boolean removeStopWords) throws IOException {
		// Création de la table des mots
		final HashMap<String, Integer> hits = new HashMap<String, Integer>();
		final File dir = new File(dirName);
		String wordLC;
		if (dir.isDirectory()) {
			// Liste des fichiers du répertoire
			// ajouter un filtre (FileNameFilter) sur les noms
			// des fichiers si nécessaire
			final String[] fileNames = dir.list();

			// Parcours des fichiers et remplissage de la table

			// TODO !
			Integer number;
			final ArrayList<String> alreadySeenInTheCurrentFile = new ArrayList<String>();
			for (final String fileName : fileNames) {
				alreadySeenInTheCurrentFile.clear();
				System.err.println("Analyse du fichier " + fileName);
				// Appel de la méthode de normalisation
				final ArrayList<String> words = normalizer.normalize(dirName
						+ File.separator + fileName, removeStopWords);
				// Pour chaque mot de la liste, on remplit un dictionnaire
				// du nombre d'occurrences pour ce mot
				for (final String word : words) {
					wordLC = word;
					wordLC = wordLC.toLowerCase();
					number = hits.get(wordLC);
					// Si ce mot n'était pas encore présent dans le
					// dictionnaire,
					// on l'ajoute (nombre d'occurrences = 1)
					if (number == null) {
						hits.put(wordLC, 1);
					}
					// Sinon, on incrémente le nombre d'occurrence
					else {
						if (!alreadySeenInTheCurrentFile.contains(wordLC)) {
							hits.put(wordLC, ++number);
						}
					}
					alreadySeenInTheCurrentFile.add(wordLC);
				}
			}
		}

		// Affichage du résultat (avec la fréquence)
		for (final Map.Entry<String, Integer> hit : hits.entrySet()) {
			// System.out.println(hit.getKey() + "\t" + hit.getValue());
		}
		return hits;
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

		for (final Map.Entry<String, Integer> entry : Indexer
				.getTermFrequencies(fileName, normalizer, removeStopWords)
				.entrySet()) {
			final String word = entry.getKey();
			final Integer tf = entry.getValue();
			final Double idf = Math.log(documentNumber / dfs.get(word));
			final Double tfIdf = tf * idf;
			System.out.println(word + "\t" + tfIdf);
			tfIdfs.put(word, tfIdf);
		}
		return tfIdfs;
	}

	/**
	 * So far, don't do anything usefull
	 * 
	 * @param inDirName
	 * @param outDirName
	 * @param normalizer
	 * @param removeStopWords
	 * @param extensionToKeep
	 * @throws IOException
	 */
	public static void getWeightFiles(final String inDirName,
			final String outDirName, final Normalizer normalizer,
			final boolean removeStopWords, final String extensionToKeep)
			throws IOException {
		final File dir = new File(inDirName);

		if (dir.isDirectory()) {
			final ArrayList<String> fileNames = Indexer.keepExtension(
					dir.list(), extensionToKeep);
			System.out.println(fileNames);
			/*
			 * int numberDocuments = fileNames.size(); HashMap<String, Integer>
			 * dfs = getDocumentFrequency(inDirName, normalizer,
			 * removeStopWords); for(String file : fileNames){ HashMap<String,
			 * Double> tfidfs = getTfIdf(inDirName + file, dfs, numberDocuments,
			 * normalizer, true);
			 * 
			 * PrintWriter writer = new PrintWriter(outDirName + file +
			 * ".poids", "UTF-8");
			 * 
			 * for (Map.Entry<String, Double> tfidf : tfidfs.entrySet()) {
			 * writer.println(tfidf.getKey() + "\t" + tfidf.getValue()); }
			 * writer.close(); }
			 */
		}
	}

	public static void main(final String[] args) {
		try {
			Indexer.getWeightFiles("/public/iri/projetIRI/corpus/0000/000000/",
					"net/k3/u/etudiant/mhadda1/IRI/weights/",
					new FrenchStemmer(), true, ".txt");
		} catch (final IOException e) {
			System.out.println("Problem : " + e);
		}
	}

}
