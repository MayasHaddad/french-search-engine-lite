package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import expes.Utils;

public class AdvancedIndexer {

	private String pathATraiter = null;
	private Integer cpt = 0;
	private TreeMap<String, TreeSet<String>> res = new TreeMap<String, TreeSet<String>>();

	public AdvancedIndexer(String pathToCorpus) {
		pathATraiter = pathToCorpus;

	}

	public void run() throws IOException {
		System.out.println(new java.util.Date());

		if (Indexer.DOCUMENT_FREQUENCY.isEmpty()) {
			Indexer.DOCUMENT_FREQUENCY = Indexer.getDocumentFrequency(new File(
					pathATraiter));
		}

		traitement(new File(pathATraiter));
		System.out.println(new java.util.Date());

	}

	
	
	private void traitement(final File dir) throws IOException {
		if (!IOManager.checkInDir(dir)) {
			throw new IOException();
			// return;
		}		
		traitementRec(dir);
		InvertedFile.saveInvertedFile(res,
				InvertedFile.generateInvertedFileName());
	}

	private void traitementRec(final File dir) throws IOException {

		for (final File f : dir.listFiles()) {
			if (f.isFile()) {
				if (!f.getName().endsWith(Const.EXTENTION_KEEP)) {
					continue;
				}
			traiterFichier(f);
			
			} else {
				traitementRec(f);
			}
		}
	}

	private void traiterFichier(File file) throws IOException {
		cpt++;
		if (cpt%1000==0 && Utils.isMemoryFull(Main.RATIO_MEMORY)) {
			InvertedFile.saveInvertedFile(res,
					InvertedFile.generateInvertedFileName());
			res.clear();
			System.out.println("Memory Full: " + cpt);
			System.gc();
		}
		
		InputStream is = new FileInputStream(file);
		
		final HashMap<String, Integer> map = Indexer.getTermFrequencies(is, Const.NORMALIZER,Const.REMOVE_STOP_WORDS);
		String fileNameOfActualFile = file.getName();
		String fileNameConvertedInString = Integer.toString(Integer.parseInt(fileNameOfActualFile.substring(0,8)));
	
		for (final Map.Entry<String, Integer> entry : map.entrySet()){
			
			double tfidf = getTfIdfForOneWord(entry.getKey(), entry.getValue());
			addWordToInvertedFile(entry.getKey(), fileNameConvertedInString, tfidf);
			
		}
	}
	
	private void addWordToInvertedFile(String word, String fileName, double tfidf){
		TreeSet<String> s = res.get(word);
		String fileNamePlusTfIdf = fileName+":"+tfidf;
		if (s != null
				&& !res.get(word).contains(fileName)) {
			s.add(fileNamePlusTfIdf);
		} else {
			s = new TreeSet<String>();
			s.add(fileNamePlusTfIdf);
			//System.out.println(word+"    "+s);
			res.put(word, s);
		}
		
	}
	
	private double getTfIdfForOneWord(String word, int tf){
		double result = 0.0;
		double logtf = 0.0;
		double idf = 0.0;
		
		if (tf == 0) {
			logtf = 0D;
		} else {
			logtf = 1 + Math.log10(tf);
		}
		//TODO remplacer 9997 par document number
		idf = Math.log10((double) Const.NB_FILES_IN_CORPUS / Indexer.DOCUMENT_FREQUENCY.get(word));
		result = logtf * idf;
		return result;
	}

}
