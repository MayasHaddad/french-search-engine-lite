/**
 * 
 */
package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

	// Remove the too simple words
	private static boolean REMOVE_STOP_WORDS = false;
	// The local stop-words path
	private static String PATH_TO_STOP_WORDS = null;
	// Number of files in the corpus
	private static Integer NB_FILES_IN_CORPUS = null;
	// For each word, number of document in the corpus containing it
	private static HashMap<String, Integer> DOCUMENT_FREQUENCY = new HashMap<String, Integer>();
	// the used normalizer
	private static Normalizer NORMALIZER = null;
	// The only extention that we care about
	private static String EXTENSION = null;

	private static long START_TIME = 0;
	private static long CURRENT_TIME = 0;

	private static BufferedReader BR = null;
	private static BufferedWriter BW = null;

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
	private static HashMap<String, Integer> getDocumentFrequency(final File dir)
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
				f.getAbsolutePath(), Indexer.REMOVE_STOP_WORDS, Indexer.PATH_TO_STOP_WORDS);

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

		for (final Map.Entry<String, Integer> entry : Indexer
				.getTermFrequencies(fileName, normalizer, removeStopWords)
				.entrySet()) {
			final String word = entry.getKey();
			final Integer tf = entry.getValue();
			final Double idf = Math.log(documentNumber / dfs.get(word));
			final Double tfIdf = tf * idf;
			// System.out.println(word + "\t" + tfIdf);
			tfIdfs.put(word, tfIdf);
		}
		return tfIdfs;
	}

	/**
	 * print file weight recursively according to the parameters given by the
	 * user
	 * 
	 * @param inDir
	 *            input directory
	 * @param outDir
	 *            output directory
	 * @param n
	 *            normalizer
	 * @param removeStopWords
	 *            true = remove too common words
	 * @param extension
	 *            we just look the files with this extension (so far not
	 *            implemented)
	 * @throws IOException
	 */
	public static void getWeightFiles(final File inDir, final File outDir,
			final Normalizer n, final boolean removeStopWords,
			final String extension) throws IOException {
		Indexer.NORMALIZER = n;
		Indexer.EXTENSION = extension;

		// check inDir and outDir
		if (!IOManager.checkInDir(inDir) || !IOManager.checkOutDir(outDir)) {
			throw new IOException();
			// return;
		}

		Indexer.START_TIME = System.nanoTime();
		// initialise the number of files in the corpus
		System.out.print("Count the number of documents : N = ");
		if (Indexer.NB_FILES_IN_CORPUS == null) {
			Indexer.NB_FILES_IN_CORPUS = IOManager
					.countDocumentRecusively(inDir);
		}
		Indexer.CURRENT_TIME = System.nanoTime();
		System.out.println(Indexer.NB_FILES_IN_CORPUS + "|| temps(ms) = "
				+ (Indexer.CURRENT_TIME - Indexer.START_TIME) / 1000000);

		// initialise the document frequency in the corpus
		if (Indexer.DOCUMENT_FREQUENCY.isEmpty()) {
			Indexer.DOCUMENT_FREQUENCY = Indexer.getDocumentFrequency(inDir);
		}
		System.out.println("temps(ms) pour la frequence = "
				+ (System.nanoTime() - Indexer.CURRENT_TIME) / 1000000);
		Indexer.CURRENT_TIME = System.nanoTime();

		System.out.println("Datas calculated, start to generate files");
		Indexer.getWeightFilesRec(inDir, outDir);
		System.out.println("temps(ms) pour la generation des fichiers = "
				+ (System.nanoTime() - Indexer.CURRENT_TIME) / 1000000);
		System.out.println("temps total = "
				+ (System.nanoTime() - Indexer.START_TIME) / 1000000);
	}

	/**
	 * print file weight through the tree of files
	 * 
	 * @param inDirName
	 *            input dir
	 * @param outDirName
	 *            output dir
	 * @throws IOException
	 */
	private static void getWeightFilesRec(final File inDir, final File outDir)
			throws IOException {

		for (final File f : inDir.listFiles()) {
			// Ignore if you can't read
			if (!f.canRead()) {
				continue;
			}

			// Recursive processing
			if (f.isDirectory()) {
				System.out.println("@");
				// Create output dir
				final File out = IOManager.createWriteDir(outDir
						.getAbsolutePath() + File.separator + f.getName());
				if (out == null) {
					continue;
				}
				// work on the dir recursively
				Indexer.getWeightFilesRec(f, out);
			} else {
				// it's a file, create output file
				final File out = IOManager.createWriteFile(outDir
						.getAbsolutePath()
						+ File.separator
						+ f.getName()
						+ ".poid");
				if (out == null) {
					continue;
				}
				// work on the file
				Indexer.createWeightFile(f, out);
			}
		}
	}

	/**
	 * Calculate for a given file his tfidf and print it in a file .poid
	 * 
	 * @param inFile
	 *            input file to analyse
	 * @param outFile
	 *            output file .poid
	 * @throws IOException
	 */
	private static void createWeightFile(final File inFile, final File outFile)
			throws IOException {

		// calculate tf idf
		final HashMap<String, Double> tfIdf = Indexer.getTfIdf(
				inFile.getAbsolutePath(), Indexer.DOCUMENT_FREQUENCY,
				Indexer.NB_FILES_IN_CORPUS, Indexer.NORMALIZER,
				Indexer.REMOVE_STOP_WORDS);

		// open output
		Indexer.BW = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile)));
		System.out.print("-");

		// print
		for (final Map.Entry<String, Double> eltTfIdf : tfIdf.entrySet()) {
			Indexer.BW.write(eltTfIdf.getKey() + "\t" + eltTfIdf.getValue()
					+ "\n");
		}
		// time to flush all this :
		Indexer.BW.close();
	}

	/**
	 * Main function
	 * 
	 * @param args
	 *            command arguments
	 */
	public static void main(final String[] args) {
		if(args.length != 4){
			System.err.println("Usage : java " + Indexer.class.getName() + " inDirectory outDirectory stopWordsPath removeStopWords");
			System.err.println("Example : java " + Indexer.class.getName() + " /in /out /stop-words.txt false");
			
			System.exit(1);
		}
		final String inDir = args[0];
		final String outDir = args[1];
		Indexer.PATH_TO_STOP_WORDS = args[2];
		Indexer.REMOVE_STOP_WORDS = Boolean.parseBoolean(new String(args[3]));
		
		try{
			System.out.println("DEBUG: begin");
			final File in = new File(inDir);// /public/iri/projetIRI/corpus/0000/000000/
			final File out = new File(outDir);
			System.out.println("Launch calculus");
			Indexer.getWeightFiles(in, out, new FrenchStemmer(), Indexer.REMOVE_STOP_WORDS, ".txt");
			System.out.println("DEBUG: end");

		} catch (final IOException e) {
			System.out.println("Problem : " + e);
			e.printStackTrace();
		}
	}
}
