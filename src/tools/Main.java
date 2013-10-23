package tools;

import java.io.File;
import java.io.IOException;

public class Main {

	/**
	 * Main function
	 * 
	 * @param args
	 *            command arguments
	 */
	public static void main(final String[] args) {
		if (args.length != 5) {
			System.err
					.println("Usage : java "
							+ Indexer.class.getName()
							+ " inDirectory outDirectory stopWordsPath removeStopWords extension");
			System.err.println("Example : java " + Indexer.class.getName()
					+ " /in /out /stop-words.txt false .html");
			System.exit(1);
		}
		final String inDir = args[0];
		final String outDir = args[1];
		Indexer.PATH_TO_STOP_WORDS = args[2];
		Indexer.REMOVE_STOP_WORDS = Boolean.parseBoolean(new String(args[3]));
		Indexer.EXTENTION_KEEP = args[4];
		Indexer.NORMALIZER = new FrenchStemmer();
		try {
			System.out.println("DEBUG: begin");

			final File in = new File(inDir);// /public/iri/projetIRI/corpus/0000/000000/
			final File out = new File(outDir);
			System.out.println("Launch calculus");
			Weights.getWeightFiles(in, out, new FrenchStemmer());
			// final TreeMap<String, TreeSet<String>> invertedFile =
			// InvertedFile
			// .getInvertedFile(in, Indexer.NORMALIZER,
			// Indexer.REMOVE_STOP_WORDS);
			// Indexer.printInvertedFile(invertedFile);
			// final File invertedFileOutput = new File(outDir + "index");
			// invertedFileOutput.createNewFile();
			// InvertedFile.saveInvertedFile(invertedFile, invertedFileOutput);

			System.out.println("DEBUG: end");

		} catch (final IOException e) {
			System.out.println("Problem : " + e);
			e.printStackTrace();
		}
	}

}
