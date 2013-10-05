package tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface de normalisation des mots
 * @author xtannier
 *
 */
public interface Normalizer {
	/**
	 * Renvoie la liste d'unités lexicales contenus dans le fichier
	 * spécifié, en appliquant une normalisation. Equivaut à {@code normalize(file, false)}. 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> normalize(File file) throws IOException;
	
	/**
	 * Renvoie la liste d'unités lexicales contenus dans le texte
	 * spécifié, en appliquant une normalisation. Equivaut à {@code normalize(text, false)}. 
	 * @param text
	 * @return
	 */
	public ArrayList<String> normalize(String text);
	
	/**
	 * Renvoie la liste d'unités lexicales contenus dans le fichier
	 * spécifié, en appliquant une normalisation. Equivaut à {@code normalize(text, false)}.
	 * avec suppression ou pas de mots vides
	 * @param fileName
	 * @param removeStopWords
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> normalize(String fileName, boolean removeStopWords)
			throws IOException;
}
