package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
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

	private static Integer cpt = 0;
	private static TreeMap<String, TreeSet<String>> res = new TreeMap<String, TreeSet<String>>();

	/**
	 * Analyse l'ensemble des fichiers d'un repertoire et collecte, pour chaque
	 * mot, la liste des fichiers dans lesquels ce mot apparait.
	 * 
	 * @param dirName
	 * @param normalizer
	 * @param removeStopWords
	 * @return nothing relevant
	 */
	public static void calculateInvertedFile(final File dir,
			final Normalizer normalizer, final boolean removeStopWords)
			throws IOException {
		// the results <words, <docsName>>
		// contains the words <words>
		final TreeSet<String> occurences = new TreeSet<String>();
		if (dir.exists() && dir.canRead() && dir.isDirectory()) {
			Iterator it;
			TreeSet<String> listFiles;
			List<String> mots;
			for (final File f : dir.listFiles()) {
				// check memory
				if (Utils.isMemoryFull(Main.RATIO_MEMORY)) {
					InvertedFile.saveInvertedFile(InvertedFile.res,
							InvertedFile.generateInvertedFileName());
					InvertedFile.res.clear();
					occurences.clear();
					System.out.println("Memory Full: " + InvertedFile.cpt);
					final Runtime r = Runtime.getRuntime();
					r.gc();
				}
				// recursively...
				if (f.isDirectory()) {
					InvertedFile.calculateInvertedFile(f, normalizer,
							removeStopWords);
					// for (final Map.Entry<String, TreeSet<String>> invertFile
					// : invertFiles
					// .entrySet()) {
					// if (!InvertedFile.res.containsKey(invertFile.getKey())) {
					// InvertedFile.res.put(invertFile.getKey(),
					// invertFile.getValue());
					// } else {
					// final TreeSet<String> tmpSet = invertFile
					// .getValue();
					// it = tmpSet.iterator();
					// while (it.hasNext()) {
					// final TreeSet<String> tmp = InvertedFile.res
					// .get(invertFile.getKey());
					// tmp.add((String) it.next());
					// }
					// }
					// }
					continue;
				} // otherwise, this is a file, work on it
				if (!f.getName().endsWith(".poid")) {
					continue;
				}
				occurences.clear();
				mots = normalizer.normalize(f.getAbsolutePath(),
						removeStopWords, Const.PATH_TO_STOP_WORDS);
				for (final String word : mots) {
					occurences.add(word);
				}
				// put all the words into the tree
				it = occurences.iterator();
				while (it.hasNext()) {
					final String key = (String) it.next();
					final TreeSet<String> s = InvertedFile.res.get(key);
					if (s != null
							&& !InvertedFile.res.get(key).contains(f.getName())) {
						listFiles = InvertedFile.res.get(key);
						listFiles.add(f.getName());
						// res.put(key, listFiles);
					} else {
						listFiles = new TreeSet<String>();
						listFiles.add(f.getName());
						InvertedFile.res.put(key, listFiles);
					}
				}
			}
			InvertedFile.saveInvertedFile(InvertedFile.res,
					InvertedFile.generateInvertedFileName());
		}
		// return InvertedFile.res;
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
		return new File(Const.PATH_TO_INVERTED_FILE_FROM_INDEXER + File.separator
				+ InvertedFile.cpt.toString());
	}
}
