package tools;

import java.io.File;

import expes.Utils;

public class Main {

	public static Double RATIO_MEMORY = 0.9;

	/**
	 * Main function
	 * 
	 * @param args
	 *            command arguments
	 */
	public static void main(final String[] args) {
		try {
			System.out.println("Start program, wait for key pressed");
			Utils.waitKeyPressed();
			System.out.println("DEBUG: begin");

			// count document
			// System.out.println(IOManager.countDocumentRecursively(in));

			// calculate the weights
			// Weights.getWeightFiles(new File(Const.PATH_TO_LITTLE_CORPUS),
			// new File(Const.PATH_TO_WEIGHT_FILES), new FrenchStemmer());

			// Create inverted files of maximum memory
			// InvertedFile.calculateInvertedFile(new File(
			// Const.PATH_TO_WEIGHT_FILES), Const.NORMALIZER,
			// Const.REMOVE_STOP_WORDS);

			// Fusion and merge invertedFiles
			// final InvertedFileMerger a = new InvertedFileMerger();
			// a.run();
			System.out.println("Debut nb " + new java.util.Date());
			// Get number of files
			if (Const.MAX_NUMBER_OF_FILE != null
					&& Const.MAX_NUMBER_OF_FILE < 1998425) {
				Const.NB_FILES_IN_CORPUS = Const.MAX_NUMBER_OF_FILE;
			} else {
				Const.NB_FILES_IN_CORPUS = IOManager.getNbFiles(new File(
						Const.PATH_TO_CORPUS));
			}
			System.out.println("Fin nb " + new java.util.Date());

			final AdvancedIndexer b = new AdvancedIndexer(Const.PATH_TO_CORPUS);
			b.run();

			final InvertedFileMerger a = new InvertedFileMerger(
					Const.INVERTEDFILETMP);
			a.run();

			System.out.println("DEBUG: end");

		} catch (final Exception e) {
			System.out.println("Problem : " + e);
			e.printStackTrace();
		}
	}
}
