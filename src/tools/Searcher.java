/**
 * 
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
 * @author user
 *
 */
public class Searcher {
	
	// Retrieve all the files which contain the query
	public static Map<String, TreeSet<String>> getContainingFilesOfThisQuery(final ArrayList<String> queryNormalized, final File invertedFile) throws IOException{
		
		Map<String, TreeSet<String>> filesContainingQueryWords = new HashMap<String, TreeSet<String>>();
		
		// lecture du fichier texte
				final InputStream ips = new FileInputStream(invertedFile);
				final InputStreamReader ipsr = new InputStreamReader(ips);
				final BufferedReader br = new BufferedReader(ipsr);
				String line;
				while ((line = br.readLine()) != null) {
					// I could've chosen used ArrayList, but complexity is the same O(n) and requires an object 
						if(queryNormalized.contains(line.split("\t")[0])){
							// store the files containing the word
							ArrayList<String> filenamesArrayList = new ArrayList<String>();
							for(String filename : line.split("\t")[2].split(",")){
								filenamesArrayList.add(filename);
							}
							
							TreeSet<String> filenamesTreeSet = new TreeSet<String>(filenamesArrayList);
							filesContainingQueryWords.put(line.split("\t")[0], filenamesTreeSet);
						}
				}
				br.close();
		return filesContainingQueryWords;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage : java " + Searcher.class.getName() + " collectionDir weightsDir invertedFile");
			System.err.println("Example : java " + Searcher.class.getName()	+ " /in /weight /inverted-file.txt");
			System.exit(1);
		}
		
		// getting the program's command line arguments
		//String collectionDirectoryPath = args[0];
		//String wightsDirectoryPath = args[1];
		String invertedFilePath = args[0];
		
		// getting the user's query from the keybord
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			//Creating the needed files
			//File collectionDirectory = new File(collectionDirectoryPath);
			//File weightsDirectory = new File(wightsDirectoryPath);
			File invertedFile = new File(invertedFilePath);
			
			System.out.println("Ecrire votre requête");
			String query = inputReader.readLine();
			ArrayList<String> queryNormalized = Indexer.NORMALIZER.normalize(query);
			
			Map<String, TreeSet<String>> filesContainingQueryWords = Searcher.getContainingFilesOfThisQuery(queryNormalized, invertedFile);

		}catch(IOException e) {
			System.out.println("error: " + e);
		}
	}
}
