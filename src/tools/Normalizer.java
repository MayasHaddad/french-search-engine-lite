package tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface de normalisation des mots
 * 
 * @author xtannier
 * 
 */
public interface Normalizer {
	/**
	 * Give lexical units in the file. Equivalent {@code normalize(file, false)}
	 * 
	 * @param file
	 *            input file
	 * @return <unit token>
	 * @throws IOException
	 */
	public ArrayList<String> normalize(File file) throws IOException;

	/**
	 * Give lexical units in the file. Equivalent {@code normalize(file, false)}
	 * 
	 * @param text
	 *            input Text
	 * @return <unit token>
	 */
	public ArrayList<String> normalize(String text);

	/**
	 * Give lexical units in the file. Equivalent {@code normalize(file, false)}
	 * . It can or not supress stop words
	 * 
	 * @param fileName
	 *            input file name
	 * @param removeStopWords
	 *            true : remove too usual words
	 * @return <unit token>
	 * @throws IOException
	 */
	public ArrayList<String> normalize(String fileName, boolean removeStopWords)
			throws IOException;
}
