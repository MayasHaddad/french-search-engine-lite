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
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import abstractClasses.Searcher;
import abstractClasses.XplodedIndexSearcher;

public class XplodedIndexXplodedWeightFileSearcher extends XplodedIndexSearcher{

	public String getLineStartingWith(String word, BufferedReader br) throws IOException{
		String line = br.readLine();
		while(line != null){
			if(word.compareToIgnoreCase(line.split("\t")[0]) == 0){
				return line;
			}
			line = br.readLine();
		}
		return null;
	}
	
	// Fills the DocumentFrequency structure for a given query
	public void fillDFStructure(String query, File invertedFileDirectory) throws IOException{
		
		ArrayList<String> queryNormalized = Const.NORMALIZER.normalize(query);
		for(String queryWord : queryNormalized){
			if (queryWord.length() >= 2) {

				final BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(getInvertedFileOfQueryWord(queryWord, invertedFileDirectory))));

						Integer df = Integer.parseInt(getLineStartingWith(queryWord, br).split("\t")[1]);

				Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS.put(queryWord, df);
			}
		}
	}
	
	public String getCorrespondingDenominatorFile(String updatedFileName) {
		return Integer.toString(Integer.parseInt(updatedFileName.substring(0, 5)) + 1) + ".txt";
	}
	// Retrieves the files containing the query words and performs a similarity calculus at the same time
	public TreeMap<Double, TreeSet<String>> getSimilarDocuments(
			final String query,
			final String weightsDirectoryPath,
			final int numberOfDocumentsInTheCorpus,
			final File invertedFileDirectory) throws IOException {

		// This will contain the result of the method
		TreeMap<Double, TreeSet<String>> result = new TreeMap<Double, TreeSet<String>>();

		// This map contains the fileName as an integer the similarity numerator 
		HashMap<Integer, Double> fileNameByNumerator = new HashMap<Integer, Double>();

		// filling the df structure
		this.fillDFStructure(query, invertedFileDirectory);

		// Calculating query's words weights
		final HashMap<String, Double> weightsOfQuery = Indexer.getTfIdf(
				(InputStream) new ByteArrayInputStream(query.getBytes()),
				(HashMap<String, Integer>) Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS,
				numberOfDocumentsInTheCorpus + 1, Const.NORMALIZER,
				Const.REMOVE_STOP_WORDS);

		for(Map.Entry<String, Double> weightsOfQueryEntry : weightsOfQuery.entrySet()){

			String queryWord = weightsOfQueryEntry.getKey();
			Double queryWordWeight = weightsOfQueryEntry.getValue();

			if (queryWord.length() >= 2) {

				// Getting into the exploded inverted file containing the query word
				final BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(getInvertedFileOfQueryWord(queryWord, invertedFileDirectory))));

				// Getting the inverted file line of the query word
				String line = getLineStartingWith(queryWord, br);

				// Getting the last part of the line
				String filesAndWeights = line.split("\t")[2];

				for(String filesAndWeightsElement : filesAndWeights.split(",")){
					Integer fileName = Integer.parseInt(filesAndWeightsElement.split(":")[0]);
					Double queryWordWeightInThatFile = Double.parseDouble(filesAndWeightsElement.split(":")[1]);

					Double lastValue = 0D;
					if(fileNameByNumerator.containsKey(fileName)){
						lastValue = fileNameByNumerator.get(fileName);
					}
					fileNameByNumerator.put(fileName, lastValue + (queryWordWeightInThatFile * queryWordWeight));
				}
			}
		}
		for(Map.Entry<Integer, Double> fileNameByNumeratorEntry : fileNameByNumerator.entrySet()){
			Double numerator = fileNameByNumeratorEntry.getValue();
			String fileName = fileNameByNumeratorEntry.getKey().toString();
			// Adding the 0s to get a correct fileName
			// must be in a method
			
			String updatedFileName = "";
			final int a = 8 - fileName.length();
			for (int i = 0; i < a; i++) {
				updatedFileName += "0";
			}
			updatedFileName = updatedFileName
					+ fileName + ".txt";
			//System.out.println(updatedFileName + "\t" + this.getCorrespondingDenominatorFile(updatedFileName));
			
			// Opening the new weightFile (weightfileTmp)
			final BufferedReader brOfDenominator = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("/projet/iri/bvh/WeightFileTmp/" + this.getCorrespondingDenominatorFile(updatedFileName)))));
			
			// Getting the denominator files' line containing the query word
			String lineOfDenominator = getLineStartingWith(fileName, brOfDenominator);

			Double denominator = Double.parseDouble(lineOfDenominator.split("\t")[1]);
			
			Double similarity = numerator / denominator;
			
			TreeSet<String> fileNamesList = new TreeSet<String>();
			// filling the result structure with the similarity and the files having that very same similarity value
			if(result.containsKey(similarity)){
				fileNamesList = result.get(similarity);
			}
			fileNamesList.add(updatedFileName);
			result.put(similarity, fileNamesList);
		}
		
		return result;
	}

	public TreeMap<Double, TreeSet<String>> getResult(
			final String query, final File Corpus) throws IOException {

		return this.getSimilarDocuments(query,	Const.PATH_TO_WEIGHT_FILES, IOManager.getNbFiles(Corpus), new File(Const.PATH_TO_INVERTED_FILE_FROM_MERGER));
	}

	public static void main(String[] args) throws IOException{

		System.out.println("Ecrire votre requ�te");

		final BufferedReader inputReader = new BufferedReader(
				new InputStreamReader(System.in));
		final String query = inputReader.readLine();

		XplodedIndexXplodedWeightFileSearcher s = new XplodedIndexXplodedWeightFileSearcher();

		System.out.println(s.getResult(query, 
				new File("/public/iri/projetIRI/corpus/0000/")));
	}
}