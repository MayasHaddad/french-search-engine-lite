/**
 * 
 */
package tools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	
	public static Map<String, Integer> DOCUMENT_FRENQUENCIES_QUERY_WORDS = new HashMap<String, Integer>();
	
	// Retrieve all the files which contain the query
	public static Map<String, TreeSet<String>> getContainingFilesOfThisQuery(final ArrayList<String> queryNormalized, final File invertedFile) throws IOException{
		
		Map<String, TreeSet<String>> filesContainingQueryWords = new HashMap<String, TreeSet<String>>();
		
		// lecture du fichier texte
				final InputStream ips = new FileInputStream(invertedFile);
				final InputStreamReader ipsr = new InputStreamReader(ips);
				final BufferedReader br = new BufferedReader(ipsr);
				String line;
				while ((line = br.readLine()) != null) {
						if(queryNormalized.contains(line.split("\t")[0])){
							// the current word is in the query
							// store the files containing the word
							ArrayList<String> filenamesArrayList = new ArrayList<String>();
							for(String filename : line.split("\t")[2].split(",")){
								filenamesArrayList.add(filename);
							}
							
							TreeSet<String> filenamesTreeSet = new TreeSet<String>(filenamesArrayList);
							filesContainingQueryWords.put(line.split("\t")[0], filenamesTreeSet);
							Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS.put(line.split("\t")[0], Integer.parseInt(line.split("\t")[1]));
						}
			}
		br.close();
		return filesContainingQueryWords;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage : java " + Searcher.class.getName() + " collectionDir weightsDir invertedFile");
			System.err.println("Example : java " + Searcher.class.getName()	+ " /in /weight /inverted-file.txt");
			System.exit(1);
		}

		// getting the program's command line arguments
		//String collectionDirectoryPath = args[0];
		String weightsDirectoryPath = args[0];
		String invertedFilePath = args[1];
		
		// getting the user's query from the keybord
		final BufferedReader inputReader = new BufferedReader(
				new InputStreamReader(System.in));

		try {
			//Creating the needed files
			//File collectionDirectory = new File(collectionDirectoryPath);
			File weightsDirectory = new File(weightsDirectoryPath);
			File invertedFile = new File(invertedFilePath);
			
			System.out.println("Ecrire votre requête");
			String query = inputReader.readLine();
			
			ArrayList<String> queryNormalized = (new FrenchStemmer()).normalize(query);
			
			Map<String, TreeSet<String>> filenamesContainingQueryWords = Searcher.getContainingFilesOfThisQuery(queryNormalized, invertedFile);
			System.out.println(filenamesContainingQueryWords);
			/*ArrayList<String> alreadyVisitedFilename = new ArrayList<String>();
			HashMap<String, Double> weightsOfQuery = Indexer.getTfIdf(
																		(InputStream)(new ByteArrayInputStream(query.getBytes())),
																		(HashMap)Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS,
																		Weights.countDocumentRecursively(new File("")),
																		(new FrenchStemmer()),
																		Indexer.REMOVE_STOP_WORDS
																	);
			/*for(TreeSet<String> filenamesListContainingQueryWord : filenamesContainingQueryWords.values()){
				for(String filename : filenamesListContainingQueryWord){
					if(!alreadyVisitedFilename.contains(filename)){
						//	getSimilarity(weightsOfQuery, filename);
					}
				}
			}
			System.out.println(weightsOfQuery);*/
		}catch(IOException e) {
			System.out.println("error: " + e);
		}
	}
}
