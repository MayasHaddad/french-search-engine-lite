/**
 * This class inherits Searcher
 * It implements a search of query on an exploded index
 * Example :
 * (Simple index) uniqueIndex.txt (contains) 
 * [abc file1]
 * [bcg file2]
 * (Exploded index is) ab.txt and bc.txt (containing)
 * ab.txt : [abc file1]
 * bc.txt : [bcg file2] 
 */
// THIS CLASS AND ITS SUB CLASSES ARE DEPRECATED AVOID USING IT!
package abstractClasses;

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
 * @author mhadda1
 *
 */
public abstract class SimpleIndexSearcher extends Searcher {
	public static Map<String, TreeSet<String>> getContainingFilesOfThisQuery(
			final ArrayList<String> queryNormalized,
			final File invertedFile) throws
			  IOException {
			 
			  final Map<String, TreeSet<String>> filesContainingQueryWords = new
			  HashMap<String, TreeSet<String>>();
			  
			  // lecture du fichier texte 
			  final InputStream ips = new FileInputStream(invertedFile);
			  final InputStreamReader ipsr = new
			  InputStreamReader(ips); final BufferedReader br = new BufferedReader(ipsr); 
			  String line; while ((line = br.readLine()) != null)
			  { 
				  if (queryNormalized.contains(line.split("\t")[0])) { 
					  // the current word is in the query // store the files containing the word 
					  final  ArrayList<String> filenamesArrayList = new ArrayList<String>(); for
			  (final String filename : line.split("\t")[2].split(",")) {
			  filenamesArrayList.add(filename); }
			 
			  final TreeSet<String> filenamesTreeSet = new TreeSet<String>(
			  filenamesArrayList); filesContainingQueryWords.put(line.split("\t")[0],
			  filenamesTreeSet); Searcher.DOCUMENT_FRENQUENCIES_QUERY_WORDS.put(
			  line.split("\t")[0], Integer.parseInt(line.split("\t")[1])); } }
			  br.close(); return filesContainingQueryWords; }
}
