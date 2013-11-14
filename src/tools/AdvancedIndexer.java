package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import expes.Utils;

public class AdvancedIndexer {

	private String pathATraiter = null;
	private Integer cpt = 0;
	private final TreeMap<String, TreeSet<String>> res = new TreeMap<String, TreeSet<String>>();
	private final TreeMap<String, Double> listDenominateur = new TreeMap<String, Double>();
	private int count = 0;
	private int countFichier = 1;

	public AdvancedIndexer(final String pathToCorpus) {
		this.pathATraiter = pathToCorpus;

	}

	public void run() throws IOException {
		System.out.println(new java.util.Date());

		if (Indexer.DOCUMENT_FREQUENCY.isEmpty()) {
			Indexer.DOCUMENT_FREQUENCY = Indexer.getDocumentFrequency(new File(
					this.pathATraiter));
		}

		this.traitement(new File(this.pathATraiter));
		System.out.println(new java.util.Date());

	}

	private void saveDenominateur(final TreeMap<String, Double> listDenominateur)
			throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				Const.WEIGHTFILETMP + this.countFichier + ".txt")));
		for (final Entry<String, Double> entry : listDenominateur.entrySet()) {
			String a = entry.getKey();
			a = a.split(".txt")[0];
			a = a.split(".html")[0];
			final int b = Integer.parseInt(a);
			writer.write(b + "\t" + entry.getValue() + "\n");

		}
		writer.close();
		listDenominateur.clear();
		System.gc();
		this.countFichier++;
	}

	private void traitement(final File dir) throws IOException {
		if (!IOManager.checkInDir(dir)) {
			throw new IOException();
			// return;
		}
		Const.CURRENT_NUMBER_OF_FILE = 0;
		this.traitementRec(dir);
		InvertedFile.saveInvertedFile(this.res,
				InvertedFile.generateInvertedFileName());
		//this.saveDenominateur(this.listDenominateur);

	}

	private boolean traitementRec(final File dir) throws IOException {

		final TreeSet<String> t = new TreeSet<String>(Arrays.asList(dir.list()));
		for (final String s : t) {
			final File f = new File(dir.getAbsolutePath() + File.separator + s);
			if (f.isFile()) {
				if (!f.getName().endsWith(Const.EXTENTION_KEEP)) {
					continue;
				}
				this.traiterFichier(f);
				Const.CURRENT_NUMBER_OF_FILE++;
				if (Const.CURRENT_NUMBER_OF_FILE > Const.MAX_NUMBER_OF_FILE) {
					System.out.println("reached max : "
							+ Const.MAX_NUMBER_OF_FILE + " files");
					return false;
				} else {
					//return true;
				}
			} else {
				if (this.traitementRec(f) == false) {
					return false;
				}
			}
		}
		if(listDenominateur.size()>0){
			this.saveDenominateur(this.listDenominateur);			
		}
		return true;
	}

	private void traiterFichier(final File file) throws IOException {

		double denominateur = 0.0;
		this.cpt++;
		if (this.cpt % 1000 == 0 && Utils.isMemoryFull(Main.RATIO_MEMORY)) {
			InvertedFile.saveInvertedFile(this.res,
					InvertedFile.generateInvertedFileName());
			this.res.clear();
			System.out.println("Memory Full: " + this.cpt);
			System.gc();
		}

		final InputStream is = new FileInputStream(file);

		final HashMap<String, Integer> map = Indexer.getTermFrequencies(is,
				Const.NORMALIZER, Const.REMOVE_STOP_WORDS);
		final String fileNameOfActualFile = file.getName();
		final String fileNameConvertedInString = Integer.toString(Integer
				.parseInt(fileNameOfActualFile.substring(0, 8)));

		for (final Map.Entry<String, Integer> entry : map.entrySet()) {
			final double tfidf = this.getTfIdfForOneWord(entry.getKey(),
					entry.getValue());
			denominateur += tfidf * tfidf;
			this.addWordToInvertedFile(entry.getKey(),
					fileNameConvertedInString, tfidf);
		}
		denominateur = Math.sqrt(denominateur);
		this.listDenominateur.put(fileNameOfActualFile, denominateur);
	}

	private void addWordToInvertedFile(final String word,
			final String fileName, final double tfidf) {
		TreeSet<String> s = this.res.get(word);
		final String fileNamePlusTfIdf = fileName + ":" + tfidf;
		if (s != null && !this.res.get(word).contains(fileName)) {
			s.add(fileNamePlusTfIdf);
		} else {
			s = new TreeSet<String>();
			s.add(fileNamePlusTfIdf);
			// System.out.println(word+"    "+s);
			this.res.put(word, s);
		}
	}

	private double getTfIdfForOneWord(final String word, final int tf) {
		double result = 0.0;
		double logtf = 0.0;
		double idf = 0.0;

		if (tf == 0) {
			logtf = 0D;
		} else {
			logtf = 1 + Math.log10(tf);
		}
		idf = Math.log10((double) Const.NB_FILES_IN_CORPUS
				/ Indexer.DOCUMENT_FREQUENCY.get(word));
		result = logtf * idf;
		
		return result;
	}

}
