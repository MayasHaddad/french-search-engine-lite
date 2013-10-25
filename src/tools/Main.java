package tools;

import java.io.File;

public class Main {

	public static Double RATIO_MEMORY = 0.8;

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
		Const.PATH_TO_STOP_WORDS = args[2];
		Const.REMOVE_STOP_WORDS = Boolean.parseBoolean(new String(args[3]));
		Const.EXTENTION_KEEP = args[4];
		try {
			System.out.println("DEBUG: begin");

			final File in = new File(inDir);// /public/iri/projetIRI/corpus/0000/000000/
			final File out = new File(outDir);
			System.out.println("Launch calculus");

			// count document
			// System.out.println(IOManager.countDocumentRecursively(in));

			// calculate the weights
			Weights.getWeightFiles(new File(Const.PATH_TO_LITTLE_CORPUS),
					new File(Const.PATH_TO_WEIGHT_FILES), new FrenchStemmer());

			// Create inverted files of maximum memory
			// InvertedFile.calculateInvertedFile(out, Indexer.NORMALIZER,
			// Const.REMOVE_STOP_WORDS);

			// Fusion and merge invertedFiles
			// final InvertedFileMerger a = new InvertedFileMerger();
			// a.run();

			System.out.println("DEBUG: end");

		} catch (final Exception e) {
			System.out.println("Problem : " + e);
			e.printStackTrace();
		}
	}
}
