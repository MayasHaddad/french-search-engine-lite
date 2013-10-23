/**
 * 
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

/**
 * Answer to a request Take and normalize the request, return the first best
 * files.
 * 
 * @author user
 * 
 */
public class Searcher {

	/**
	 * Retrieve for each word, the files containing those words <word,
	 * OrderedDocList>
	 * 
	 * @param queryNormalized
	 *            the normalized user query
	 * @param invertedFile
	 *            the corpus index <word, listOfDocuments>
	 * @return the files containing those words <word,OrderedDocList>
	 * @throws IOException
	 */
	public static Map<String, TreeSet<String>> getContainingFilesOfThisQuery(
			final ArrayList<String> queryNormalized, final File invertedFile)
			throws IOException {

		final Map<String, TreeSet<String>> filesContainingQueryWords = new HashMap<String, TreeSet<String>>();

		// read text file
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(invertedFile)));
		String line;
		String[] tokens;
		while ((line = br.readLine()) != null) {
			// I could've chosen used ArrayList, but complexity is the same O(n)
			// and requires an object
			tokens = line.split("\t");
			if (queryNormalized.contains(tokens[0])) {
				// store the files containing the word
				final LinkedList<String> filenamesList = new LinkedList<String>();
				for (final String filename : tokens[2].split(",")) {
					filenamesList.add(filename);
				}

				final TreeSet<String> filenamesTreeSet = new TreeSet<String>(
						filenamesList);
				filesContainingQueryWords.put(tokens[0], filenamesTreeSet);
			}
		}
		br.close();
		return filesContainingQueryWords;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length != 1) {
			System.err.println("Usage : java " + Searcher.class.getName()
					+ " collectionDir weightsDir invertedFile");
			System.err.println("Example : java " + Searcher.class.getName()
					+ " /in /weight /inverted-file.txt");
			System.exit(1);
		}

		// getting the program's command line arguments
		// String collectionDirectoryPath = args[0];
		// String wightsDirectoryPath = args[1];
		final String invertedFilePath = args[0];

		// getting the user's query from the keybord
		final BufferedReader inputReader = new BufferedReader(
				new InputStreamReader(System.in));

		try {
			// Creating the needed files
			// File collectionDirectory = new File(collectionDirectoryPath);
			// File weightsDirectory = new File(wightsDirectoryPath);
			final File invertedFile = new File(invertedFilePath);

			System.out.println("Ecrire votre requ�te");
			final String query = inputReader.readLine();
			final ArrayList<String> queryNormalized = Indexer.NORMALIZER
					.normalize(query);

			final Map<String, TreeSet<String>> filesContainingQueryWords = Searcher
					.getContainingFilesOfThisQuery(queryNormalized,
							invertedFile);

		} catch (final IOException e) {
			System.out.println("error: " + e);
		}
	}
}
