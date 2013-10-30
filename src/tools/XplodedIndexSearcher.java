/**
 * This class inherits Searcher
 * It implements a search of query on an exploded index
 * Example :
 * (Simple index) uniqueIndex.txt (contains) 
 * [abc file1]
 * [bcg file2]
 * (Exploded index is) ab.txt and bc.txt (containing)
 * ab.txt : [abc file1]
 * bc.txt : [bcg file2] 
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author mhadda1
 *
 */
abstract class XplodedIndexSearcher extends Searcher{
	
	public Map<String, TreeSet<String>> getContainingFilesOfThisQuery(final ArrayList<String> queryNormalized, final File invertedFilesDirectory) 
			throws IOException {
		final Map<String, TreeSet<String>> filesContainingQueryWords = new HashMap<String, TreeSet<String>>();

		if (invertedFilesDirectory.isDirectory()) {
			for (final String queryWord : queryNormalized) {
				boolean queryWordIsInTheCorpus = false;

				// We won't search for a word which has only one
				if (queryWord.length() >= 2) {

					final File invertedFile = new File(invertedFilesDirectory
							+ File.separator + queryWord.substring(0, 2)
							+ ".txt");

					// no need to process a word for which there are no inverted
					// file ...
					if (invertedFile.exists()) {

						// lecture du fichier texte
						final InputStream ips = new FileInputStream(
								invertedFile);
						final InputStreamReader ipsr = new InputStreamReader(
								ips);
						final BufferedReader br = new BufferedReader(ipsr);
						String line;
						while ((line = br.readLine()) != null) {
							if (queryNormalized.contains(line.split("\t")[0])) {
								// the current word is in the query
								// store the files containing the word
								final ArrayList<String> filenamesArrayList = new ArrayList<String>();
								for (final String filename : line.split("\t")[2]
										.split(",")) {
									String updatedFileName = "";
									final int a = 8 - filename.length();
									for (int i = 0; i < a; i++) {
										updatedFileName += "0";
									}
									updatedFileName = updatedFileName
											+ filename + ".txt.poid";
									filenamesArrayList.add(updatedFileName);
									// System.out.println(updatedFileName);
								}

								final TreeSet<String> filenamesTreeSet = new TreeSet<String>(
										filenamesArrayList);
								filesContainingQueryWords.put(
										line.split("\t")[0], filenamesTreeSet);
								Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS.put(
										line.split("\t")[0],
										Integer.parseInt(line.split("\t")[1]));
								queryWordIsInTheCorpus = false;
							}
						}
						br.close();
					}
				}
				if (!queryWordIsInTheCorpus) {
					// even though the word is not in the corpus, it's in the
					// query, so df = 1
					Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS
					.put(queryWord, 1);
				}
			}
		}
		return filesContainingQueryWords;
	}
}