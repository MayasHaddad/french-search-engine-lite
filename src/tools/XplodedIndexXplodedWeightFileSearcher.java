package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import abstractClasses.XplodedIndexSearcher;

public class XplodedIndexXplodedWeightFileSearcher extends XplodedIndexSearcher{
	public  static String getFirstWord(String line, String splitStr){
		return line.split(splitStr)[0];
	}
	/*final BufferedReader br = new BufferedReader(new InputStreamReader(
			new FileInputStream(f2)));
	public String getLineStartingWith(String word, BufferedReader br){
		// The file content is alphabetically increasingly ordered
		String line = br.readLine();
		while(line != null && this.getFirstWord(line, "\t")){
			
		}
	}*/
	public static void main(String[] args){
		System.out.println(XplodedIndexXplodedWeightFileSearcher.getFirstWord("coucou	hello	yess","\t"));
	}
}
