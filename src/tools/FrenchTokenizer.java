package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import tools.FrenchTokenizerAutomaton.Signal;

public class FrenchTokenizer implements Normalizer {

	private final FrenchTokenizerAutomaton transducer;

	public FrenchTokenizer() {
		this.transducer = new FrenchTokenizerAutomaton();

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
		return this.tokenize(text.toLowerCase());
	}

	/**
	 * This method drives the automaton execution over the stream of chars.
	 */
	public ArrayList<String> tokenize(final String text) {
		final char[] textContent = text.toCharArray();
		final ArrayList<String> tokens = new ArrayList<String>();
		// Initialize the execution
		int begin = -1;
		this.transducer.reset();
		String word;
		// Run over the chars
		for (int i = 0; i < textContent.length; i++) {
			final Signal s = this.transducer.feedChar(textContent[i]);
			switch (s) {
			case start_word:
				begin = i;
				break;
			case end_word:
				word = text.substring(begin, i);
				this.addToken(tokens, word);
				begin = -1;
				break;
			case end_word_prev:
				word = text.substring(begin, i - 1);
				this.addToken(tokens, word);
				break;
			case switch_word:
				word = text.substring(begin, i);
				this.addToken(tokens, word);
				begin = i;
				break;
			case switch_word_prev:
				word = text.substring(begin, i - 1);
				this.addToken(tokens, word);
				begin = i;
				break;
			case cancel_word:
				begin = -1;
				break;
			default:
				break;
			}
		}
		// Add the last one
		if (begin != -1) {
			word = text.substring(begin, text.length());
			this.addToken(tokens, word);
		}

		return tokens;
	}

	private ArrayList<String> addToken(final ArrayList<String> list,
			final String token) {
		list.add(token);
		return list;
	}

	@Override
	public ArrayList<String> normalize(final String text) {
		return this.tokenize(text);
	}
	
	public ArrayList<String> normalize(InputStream fileInputStream, boolean removeStopWords, String pathToStopWords)
			throws IOException{
		String text = "";
		ArrayList<String> result = new ArrayList<String>();
		// lecture du fichier texte
		final InputStreamReader ipsr = new InputStreamReader(fileInputStream);
		final BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			text += line + " ";
		}
		br.close();
		result = this.tokenize(text.toLowerCase());
		if (removeStopWords == false) {
			return result;
		}
		final ArrayList<String> stopWords = FrenchStemmer.getStopWords(pathToStopWords);
		result.removeAll(stopWords);
		return result;
	}
	
	@Override
	public ArrayList<String> normalize(final String fileName,
			final boolean removeStopWords, String pathToStopWords) throws IOException {
		
		final File file = new File(fileName);
		final InputStream ips = new FileInputStream(file);
		
		return normalize(ips, removeStopWords, pathToStopWords);

	}

	public static void main(final String[] args) {

	}

}
