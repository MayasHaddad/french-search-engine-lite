package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * 
 * @author sbazin10 Fusionne les fichiers inverses générés par l'indexer.
 */
public class InvertedFileMerger {

	private final File directorySource;
	private int marqueur = 1;
	private String pathSource;

	/**
	 * 
	 * @param pathToDirectorySource
	 *            Path du repertoire contenant les inverted files générés par
	 *            l'indexer.
	 **/

	public InvertedFileMerger(String pathSource) {
		this.pathSource = pathSource;
		this.directorySource = new File(
				this.pathSource);
	}

	/**
	 * La méthode à lancer, qui démarre le merge
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {

		final String[] fileNames = this.directorySource.list();
		this.merge(fileNames);
	}

	/**
	 * Fusionne récursivement deux a deux tous les fichiers d'un repertoire,
	 * supprime les fichiers sources, et relance la fusion sur les fichiers
	 * resultats Quand il ne reste plus que un seul fichier, le découpe
	 * 
	 * @param fileNames
	 *            FileName du répertoire d'ou on va merger les fichiers
	 * @throws IOException
	 */

	private void merge(final String[] fileNames) throws IOException {

		// S'il n'ya qu'un seul fichier dans le repertoire, on lui applique un
		// traitement pour le découper, par exemple
		// alphabétiquement ou alors comme on veut.
		if (fileNames.length == 1) {
			// cette méthode peut etre interchangée pour créer des resultats
			// différents
			this.splitInvertedFileResultWithTwoLetters(fileNames[0]);

			final File file = new File(fileNames[0]);
			file.delete();
		} else {
			for (int i = 0; i < fileNames.length; i = i + 2) {
				if (i >= fileNames.length - 1) {
				} else {
					final File fileA = new File(
							this.pathSource
									+ fileNames[i]);
					final File fileB = new File(
							this.pathSource
									+ fileNames[i + 1]);
					final File fileResultat = new File(
							this.pathSource
									+ "FileResultat" + this.marqueur + ".txt");

					this.mergeInvertedFiles(fileA, fileB, fileResultat);

					fileA.delete();
					fileB.delete();

					this.marqueur++;
				}
			}
			final String[] newFileNames = this.directorySource.list();
			this.merge(newFileNames);
		}
	}

	/**
	 * Split un fichier inverse en 26 fichiers inverse, un par lettres de
	 * l'alphabet (+ 1 fichier pour les chiffres et 1 fichier pour les
	 * caractères bizarres)
	 * 
	 * @param fileName
	 * @throws IOException
	 */

	private void splitInvertedFileResult(final String fileName)
			throws IOException {
		final File file = new File(Const.PATH_TO_INVERTED_FILE_FROM_INDEXER
				+ fileName);
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		final BufferedWriter trucDeMerde = new BufferedWriter(new FileWriter(
				Const.PATH_TO_INVERTED_FILE_FROM_MERGER + "trucDeMerde.txt"));
		final BufferedWriter chiffre = new BufferedWriter(new FileWriter(
				Const.PATH_TO_INVERTED_FILE_FROM_MERGER + "chiffre.txt"));

		BufferedWriter writer = new BufferedWriter(new FileWriter(
				Const.PATH_TO_INVERTED_FILE_FROM_MERGER + "a.txt"));
		String currentString = "a";

		String mot = reader.readLine();
		while (mot != null) {
			final String firstLetter = mot.substring(0, 1).toLowerCase();
			if (Const.LETTRES.contains(firstLetter)) {
				if (currentString.equals(firstLetter)) {
					writer.write(mot + "\n");
				} else {
					writer.close();
					currentString = firstLetter;
					writer = new BufferedWriter(new FileWriter(
							Const.PATH_TO_INVERTED_FILE_FROM_MERGER
									+ currentString + ".txt"));
					writer.write(mot + "\n");
				}
			} else if (Const.CHIFFRES.contains(firstLetter)) {
				chiffre.write(mot + "\n");
			} else {
				trucDeMerde.write(mot + "\n");
			}

			mot = reader.readLine();
		}

		reader.close();
		trucDeMerde.close();
		chiffre.close();
		writer.close();
	}

	/**
	 * Découpe un fichier inverse en de nombreux fichiers inverses, ordonnées
	 * par les deux premières lettres des mots qu'ils contiennent
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	private void splitInvertedFileResultWithTwoLetters(final String fileName)
			throws IOException {
		final File file = new File(this.pathSource
				+ fileName);
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		final BufferedWriter trucDeMerde = new BufferedWriter(new FileWriter(
				Const.PATH_TO_INVERTED_FILE_FROM_MERGER + "otherCharacter.txt"));
		final BufferedWriter chiffre = new BufferedWriter(new FileWriter(
				Const.PATH_TO_INVERTED_FILE_FROM_MERGER + "chiffre.txt"));

		BufferedWriter writer = new BufferedWriter(new FileWriter(
				Const.PATH_TO_INVERTED_FILE_FROM_MERGER + "aa.txt"));
		String currentString = "aa";

		String mot = reader.readLine();
		while (mot != null) {
			final String firstLetter = mot.substring(0, 1).toLowerCase();
			final String secondLetter = mot.substring(1, 2).toLowerCase();
			final String twoFirstLetters = mot.substring(0, 2).toLowerCase();

			if (Const.LETTRES.contains(firstLetter)) {

				if (currentString.equals(twoFirstLetters)) {
					writer.write(mot + "\n");
				} else {
					if (Const.LETTRES.contains(secondLetter)) {

						writer.close();
						currentString = twoFirstLetters;
						writer = new BufferedWriter(new FileWriter(
								Const.PATH_TO_INVERTED_FILE_FROM_MERGER
										+ currentString + ".txt"));
						writer.write(mot + "\n");
					} else {
						trucDeMerde.write(mot + "\n");
					}
				}
			} else if (Const.CHIFFRES.contains(firstLetter)) {
				chiffre.write(mot + "\n");
			} else {
				trucDeMerde.write(mot + "\n");
			}
			mot = reader.readLine();
		}

		reader.close();
		trucDeMerde.close();
		chiffre.close();
		writer.close();
	}

	/**
	 * 
	 * @param invertedFile1
	 *            Premier fichier inverse à fusionner
	 * @param invertedFile2
	 *            Second fichier inverse à fusionner
	 * @param mergedInvertedFile
	 *            Fichier inverse resultat de la fusion des deux
	 * @throws IOException
	 */
	private void mergeInvertedFiles(final File invertedFile1,
			final File invertedFile2, final File mergedInvertedFile)
			throws IOException {
		final BufferedReader readerA = new BufferedReader(new FileReader(
				invertedFile1));
		final BufferedReader readerB = new BufferedReader(new FileReader(
				invertedFile2));
		final BufferedWriter writer = new BufferedWriter(new FileWriter(
				mergedInvertedFile));

		try {
			String motA = readerA.readLine();
			String motB = readerB.readLine();

			while (motA != null && motB != null) {
				// On split les lignes courantes selon la tabulation
				final String[] wordFreqDocsA = motA.split("\t");
				final String[] wordFreqDocsB = motB.split("\t");
				if (wordFreqDocsA[0].equals(wordFreqDocsB[0])) {
					final String[] documentListA = wordFreqDocsA[2].split(",");
					final String[] documentListB = wordFreqDocsB[2].split(",");

					final TreeSet<String> documentListResult = new TreeSet<String>();
					for (final String s : documentListB) {
						documentListResult.add(s);
					}
					for (final String s : documentListA) {
						documentListResult.add(s);
					}
					
					if(wordFreqDocsA[1].equals("")){
						motA = readerA.readLine();
						continue;
					}
					if(wordFreqDocsB[1].equals("")){
						motB = readerB.readLine();
						continue;
					}

					/*System.out.print("f: " + invertedFile1.getName() + ", w:"
							+ wordFreqDocsA[0] + ", d: " + wordFreqDocsA[1]
							+ "files : "+ wordFreqDocsA[2]+ "\t");
					System.out.print("f: " + invertedFile2.getName() + ", w:"
							+ wordFreqDocsB[0] + ", d: " + wordFreqDocsB[1] + "files : "+ wordFreqDocsB[2]									
							+ "\n");*/
					final int a = Integer.parseInt(wordFreqDocsA[1])
							+ Integer.parseInt(wordFreqDocsB[1]);
					writer.write(wordFreqDocsA[0] + "\t" + a + "\t");

					// Ecriture de la liste des documents
					final Iterator it = documentListResult.iterator();
					final int size = documentListResult.size();
					int cpt = 0;
					while (it.hasNext()) {
						cpt++;
						writer.append((String) it.next());
						if (cpt != size) {
							writer.append(",");
						}
					}
					writer.append('\n');

					motA = readerA.readLine();
					motB = readerB.readLine();
				} else if (wordFreqDocsA[0].compareTo(wordFreqDocsB[0]) < 0) {
					writer.write(motA + "\n");
					motA = readerA.readLine();
				} else if (wordFreqDocsA[0].compareTo(wordFreqDocsB[0]) > 0) {
					writer.write(motB + "\n");
					motB = readerB.readLine();
				}
			}

			// Si le document A est vide on Ã©crit la fin du document B
			if (motA == null) {
				while (motB != null) {
					writer.write(motB + "\n");
					motB = readerB.readLine();
				}
			}

			// Si le document B est vide on Ã©crit la fin du document A
			if (motB == null) {
				while (motA != null) {
					writer.write(motA + "\n");
					motA = readerA.readLine();
				}
			}
		} finally {
			writer.close();
			readerA.close();
			readerB.close();
		}
	}

}
