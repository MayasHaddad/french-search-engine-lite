package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class Weights {

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
			final Normalizer n) throws IOException {
		Indexer.NORMALIZER = n;

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
					.countDocumentRecursively(inDir);
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
		Weights.getWeightFilesRec(inDir, outDir);
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
				// Create output dir to repeat the folder hierarchy
				// Here we use a flat folder so we comment that
				// final File out = IOManager.createWriteDir(outDir
				// .getAbsolutePath() + File.separator + f.getName());
				// if (out == null) {
				// continue;
				// }
				// work on the dir recursively
				Weights.getWeightFilesRec(f, outDir);
			} else {
				if (!f.getName().endsWith(Indexer.EXTENTION_KEEP)) {
					continue;
				}
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
				Weights.createWeightFile(f, out);
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
				(new FileInputStream(inFile.getAbsolutePath())), Indexer.DOCUMENT_FREQUENCY,
				Indexer.NB_FILES_IN_CORPUS, Indexer.NORMALIZER,
				Indexer.REMOVE_STOP_WORDS);

		// open output
		IOManager.BW = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile)));
		System.out.print("-");

		// print
		for (final Map.Entry<String, Double> eltTfIdf : tfIdf.entrySet()) {
			IOManager.BW.write(eltTfIdf.getKey() + "\t" + eltTfIdf.getValue()
					+ "\n");
		}
		// time to flush all this :
		IOManager.BW.close();
	}

}
