package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
	
	/*final BufferedReader br = new BufferedReader(new InputStreamReader(
			new FileInputStream(f2)));/*/
	public static void main(String[] args){
		
	}
}
