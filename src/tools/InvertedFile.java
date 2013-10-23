package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import expes.Utils;

/**
 * Generate, print and save the index file <word, listOfDocs>
 * 
 * @author vvanhec
 * 
 */
public class InvertedFile {

	public static final String INVERTED_FILE_DIR = "";
	private static Integer cpt = 0;

	/**
	 * Analyse l'ensemble des fichiers d'un repertoire et collecte, pour chaque
	 * mot, la liste des fichiers dans lesquels ce mot apparait.
	 * 
	 * @param dirName
	 * @param normalizer
	 * @param removeStopWords
	 * @return
	 */
	public static TreeMap<String, TreeSet<String>> getInvertedFile(
			final File dir, final Normalizer normalizer,
			final boolean removeStopWords) throws IOException {
		// the results <words, <docsName>>
		final TreeMap<String, TreeSet<String>> res = new TreeMap<String, TreeSet<String>>();
		// contains the words <words>
		final TreeSet<String> occurences = new TreeSet<String>();
		if (dir.exists() && dir.canRead() && dir.isDirectory()) {
			Iterator it;
			TreeSet<String> listFiles;
			ArrayList<String> mots;
			for (final File f : dir.listFiles()) {
				// check memory
				if (Utils.isMemoryFull(Main.RATIO_MEMORY)) {
					InvertedFile.saveInvertedFile(res,
							InvertedFile.generateInvertedFileName());
					res.clear();
					System.out.println("Memory Full: " + InvertedFile.cpt);
				}
				// recursively...
				if (f.isDirectory()) {
					final TreeMap<String, TreeSet<String>> invertFiles = InvertedFile
							.getInvertedFile(f, normalizer, removeStopWords);
					for (final Map.Entry<String, TreeSet<String>> invertFile : invertFiles
							.entrySet()) {
						if (!res.containsKey(invertFile.getKey())) {
							res.put(invertFile.getKey(), invertFile.getValue());
						} else {
							final TreeSet<String> tmpSet = invertFile
									.getValue();
							it = tmpSet.iterator();
							while (it.hasNext()) {
								final TreeSet<String> tmp = res.get(invertFile
										.getKey());
								tmp.add((String) it.next());
							}
						}
					}
					continue;
				} // otherwise, this is a file, work on it
				if (!f.getName().endsWith(Indexer.EXTENTION_KEEP)) {
					continue;
				}
				occurences.clear();
				mots = normalizer.normalize(f.getAbsolutePath(),
						removeStopWords, Indexer.PATH_TO_STOP_WORDS);
				for (final String word : mots) {
					occurences.add(word);
				}
				// put all the words into the tree
				it = occurences.iterator();
				while (it.hasNext()) {
					final String key = (String) it.next();
					if (res.containsKey(key)
							&& !res.get(key).contains(f.getName())) {
						listFiles = res.get(key);
						listFiles.add(f.getName());
						// res.put(key, listFiles);
					} else {
						listFiles = new TreeSet<String>();
						listFiles.add(f.getName());
						res.put(key, listFiles);
					}
				}
			}
		}
		return res;
	}

	/**
	 * Print the inverted file (syntax = word doc1,doc2,doc3...)
	 * 
	 * @param invertedFile
	 */
	public static void printInvertedFile(
			final TreeMap<String, TreeSet<String>> invertedFile) {
		for (final Map.Entry<String, TreeSet<String>> i : invertedFile
				.entrySet()) {
			System.out.print(i.getKey() + "=[");
			for (final String s : i.getValue()) {
				System.out.print(s + ", ");
			}
			System.out.println("]");
		}
	}

	/**
	 * Sauvegarde un fichier de l'index au format suivant : mot frequence
	 * doc1,doc2,doc3
	 * 
	 * @param invertedFile
	 * @param outFile
	 */
	public static void saveInvertedFile(
			final TreeMap<String, TreeSet<String>> invertedFile,
			final File outFile) throws IOException {
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		if (outFile.canRead() && outFile.isFile()) {
			final OutputStream os = new FileOutputStream(outFile);
			final OutputStreamWriter osr = new OutputStreamWriter(os);
			final BufferedWriter bw = new BufferedWriter(osr);
			Iterator it;
			int size, cpt;

			for (final Map.Entry<String, TreeSet<String>> data : invertedFile
					.entrySet()) {
				size = data.getValue().size();
				bw.append(data.getKey() + "\t" + size + "\t");
				it = data.getValue().iterator();
				cpt = 0;
				while (it.hasNext()) {
					cpt++;
					bw.append((String) it.next());
					if (cpt != size) {
						bw.append(",");
					}
				}
				bw.append('\n');
			}
			bw.close();
		}
	}

	private static File generateInvertedFileName() {
		InvertedFile.cpt++;
		return new File(InvertedFile.INVERTED_FILE_DIR + File.separator
				+ InvertedFile.cpt.toString());
	}
}
