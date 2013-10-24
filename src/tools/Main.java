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
		Indexer.PATH_TO_STOP_WORDS = args[2];
		Indexer.REMOVE_STOP_WORDS = Boolean.parseBoolean(new String(args[3]));
		Indexer.EXTENTION_KEEP = args[4];
		Indexer.NORMALIZER = new FrenchStemmer();
		try {
			System.out.println("DEBUG: begin");

			final File in = new File(inDir);// /public/iri/projetIRI/corpus/0000/000000/
			final File out = new File(outDir);
			System.out.println("Launch calculus");

			// count document
			// System.out.println(IOManager.countDocumentRecursively(in));

			// calculate the weights
			// Weights.getWeightFiles(in, out, new FrenchStemmer());
			InvertedFileMerger a = new InvertedFileMerger();
			a.run();
			// calculate the indexes
			//InvertedFile.calculateInvertedFile(out, Indexer.NORMALIZER,
			//		Indexer.REMOVE_STOP_WORDS);

			// System.out.println(Utils.isMemoryFull(0.9
			// ));

			System.out.println("DEBUG: end");

		} catch (final Exception e) {
			System.out.println("Problem : " + e);
			e.printStackTrace();
		}
	}
}
