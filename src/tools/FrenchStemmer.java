package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Classe de racinisation (stemming) des mots en français. Modification légère
 * du package SnowBall http://snowball.tartarus.org/download.php
 * 
 * @author xtannier
 * 
 */
public class FrenchStemmer extends org.tartarus.snowball.ext.frenchStemmer
		implements Normalizer {

	private static short REPEAT = 1;

	public FrenchStemmer() {
	}

	@Override
	public ArrayList<String> normalize(final File file) throws IOException {
		String text = "";
		// lecture du fichier texte
		final InputStream ips = new FileInputStream(file);
		final InputStreamReader ipsr = new InputStreamReader(ips);
		final BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			text += line + " ";
		}
		br.close();

		final ArrayList<String> words = new FrenchTokenizer().tokenize(text
				.toLowerCase());
		final ArrayList<String> result = new ArrayList<String>();
		for (final String word : words) {
			// on ajoute le mot dans la liste s'il n'appartient pas ï¿œ la liste
			// des mots-clï¿œs.
			// Idï¿œalement il faudrait utiliser une structure de donnï¿œes plus
			// efficace que la liste,
			// mais ce n'est pas le sujet.
			this.setCurrent(word);
			for (int i = FrenchStemmer.REPEAT; i != 0; i--) {
				this.stem();
			}
			result.add(this.getCurrent());
		}
		return result;
	}

	@Override
	public ArrayList<String> normalize(final String text) {
		final ArrayList<String> words = new FrenchTokenizer().tokenize(text
				.toLowerCase());
		return words;
	}

	@Override
	public ArrayList<String> normalize(final String fileName,
			final boolean removeStopWords, String pathToStopWords) throws IOException {
		final ArrayList<String> result = new ArrayList<String>();
		final File file = new File(fileName);

		ArrayList<String> stopWords = new ArrayList<String>();
		if (removeStopWords == true) {
			stopWords = FrenchStemmer.getStopWords(pathToStopWords);
		}
		String text = "";
		// lecture du fichier texte
		final InputStream ips = new FileInputStream(file);
		final InputStreamReader ipsr = new InputStreamReader(ips);
		final BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			text += line + " ";
		}
		br.close();

		final ArrayList<String> words = new FrenchTokenizer().tokenize(text
				.toLowerCase());
		for (final String word : words) {

			if (removeStopWords == true && stopWords.contains(word)) {
				// System.out.println(word);
				continue;
			} else {
				this.setCurrent(word);
				for (int i = FrenchStemmer.REPEAT; i != 0; i--) {
					this.stem();
				}
				result.add(this.getCurrent());
			}
		}
		return result;
	}

	public static ArrayList<String> getStopWords(String pathToStopWords) throws IOException {
		final ArrayList<String> stopWords = new ArrayList<String>();
		final File file = new File(
				pathToStopWords);
		final InputStream ips = new FileInputStream(file);
		final InputStreamReader ipsr = new InputStreamReader(ips);
		final BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			stopWords.add(line);
		}
		br.close();
		return stopWords;
	}

	public static void main(final String[] args) {
	}
}
