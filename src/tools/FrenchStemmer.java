package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Classe de racinisation (stemming) des mots
 * en français.
 * Modification légère du package SnowBall
 * http://snowball.tartarus.org/download.php
 * @author xtannier
 *
 */
public class FrenchStemmer extends org.tartarus.snowball.ext.frenchStemmer implements Normalizer {

	private static short REPEAT = 1;
	
	public FrenchStemmer() {
	}

	@Override
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
		
		ArrayList<String> words = (new FrenchTokenizer()).tokenize(text.toLowerCase());
		ArrayList<String> result = new ArrayList<String>();
		for (String word : words) {
			// on ajoute le mot dans la liste s'il n'appartient pas ï¿œ la liste des mots-clï¿œs.
			// Idï¿œalement il faudrait utiliser une structure de donnï¿œes plus efficace que la liste,
			// mais ce n'est pas le sujet.
			this.setCurrent(word);
			for (int i = REPEAT; i != 0; i--) {
				this.stem();
			}
			result.add(this.getCurrent());
		}
		return result;
	}

	
	@Override
	public ArrayList<String> normalize(String text) {
		ArrayList<String> words = (new FrenchTokenizer()).tokenize(text.toLowerCase());
		return words;
	}
	
	public ArrayList<String> normalize(String fileName, boolean removeStopWords)
			throws IOException{
		ArrayList<String> result = new ArrayList<String>();
		File file = new File(fileName);
		
		ArrayList<String> stopWords = new ArrayList<String>();
		if(removeStopWords == true){
			stopWords = getStopWords();
		}
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
		
		ArrayList<String> words = (new FrenchTokenizer()).tokenize(text.toLowerCase());
		for (String word : words) {
			
			if(removeStopWords == true && stopWords.contains(word)){
				//System.out.println(word);
				continue;
			}else{
				
				this.setCurrent(word);
				for (int i = REPEAT; i != 0; i--) {
					this.stem();
				}
				result.add(this.getCurrent());
			}
		}
		return result;
	}
	
	public static ArrayList<String> getStopWords() throws IOException{
		ArrayList<String> stopWords = new ArrayList<String>();
		File file = new File("/net/k3/u/etudiant/mhadda1/IRI/stop-words.txt");
		InputStream ips=new FileInputStream(file); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String line;
		while ((line=br.readLine())!=null){
			stopWords.add(line);
		}
		br.close(); 
		return stopWords;
	}
	
	public static void main(String[] args) {
	}
}
