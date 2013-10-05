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
	
	private FrenchTokenizerAutomaton transducer;

	
	public FrenchTokenizer() {
		this.transducer = new FrenchTokenizerAutomaton();

	}
	
	public ArrayList<String> normalize(File file) throws IOException {
		String text = "";
		//lecture du fichier texte	
		InputStream ips=new FileInputStream(file); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String line;
		while ((line=br.readLine())!=null){
			text += line + " ";
		}
		br.close(); 
		return this.tokenize(text.toLowerCase());
	}

	
	/**
	 * This method drives the automaton execution over the stream of chars.
	 */
	public ArrayList<String> tokenize(String text) {
		char[] textContent = text.toCharArray();
		ArrayList<String> tokens = new ArrayList<String>();
		// Initialize the execution
		int begin = -1;
		transducer.reset();
		String word;
		// Run over the chars
		for(int i=0 ; i<textContent.length ; i++) {
			Signal s = transducer.feedChar( textContent[i] );
			switch(s) {
			case start_word:
				begin = i;
				break;
			case end_word:
				word = text.substring(begin, i);
				this.addToken(tokens, word);
				begin = -1;
				break;
			case end_word_prev:
				word = text.substring(begin, i-1);
				this.addToken(tokens, word);
				break;
			case switch_word:
				word = text.substring(begin, i);
				this.addToken(tokens, word);
				begin = i;
				break;
			case switch_word_prev:
				word = text.substring(begin, i-1);
				this.addToken(tokens, word);
				begin = i;
				break;
			case cancel_word:
				begin = -1;
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
	
	private ArrayList<String> addToken(ArrayList<String> list, String token) {
		list.add(token);			
		return list;
	}
	

	@Override
	public ArrayList<String> normalize(String text) {
		return this.tokenize(text);
	}
	
	public ArrayList<String> normalize(String fileName, boolean removeStopWords)
			throws IOException{
		String text = "";
		File file = new File(fileName);
		ArrayList<String> result = new ArrayList<String>();
		//lecture du fichier texte	
		InputStream ips=new FileInputStream(file); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String line;
		while ((line=br.readLine())!=null){
			text += line + " ";
		}
		br.close();
		result = this.tokenize(text.toLowerCase());
		if(removeStopWords == false){	
			return result;
		}
		ArrayList<String> stopWords = FrenchStemmer.getStopWords();
		result.removeAll(stopWords);
		return result;
		
	}
	public static void main(String[] args) {

	}

}
