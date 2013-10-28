package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOManager {

	// public static BufferedReader BR = null;
	// public static BufferedWriter BW = null;

	/**
	 * Check if a directory is ok to be read
	 * 
	 * @param inDir
	 *            the input
	 * @return true if it's ok
	 */
	public static boolean checkInDir(final File inDir) {
		if (!inDir.exists() || !inDir.isDirectory() || !inDir.canRead()) {
			System.err.println("***ERROR : Indexer - getWeightFiles: inDir "
					+ inDir);
			if (!inDir.exists()) {
				System.err.println(" doesn't exist");
			}
			if (!inDir.isDirectory()) {
				System.err.println(" is not a directory");
			}
			if (!inDir.canRead()) {
				System.err.println(" can't be read");

			}
			return false;
		}
		return true;
	}

	/**
	 * Check if an output directory is ok
	 * 
	 * @param outDir
	 *            the output
	 * @return true if it's ok
	 * @throws IOException
	 */
	public static boolean checkOutDir(final File outDir) throws IOException {
		if (!outDir.exists()) {
			System.out.println(outDir.getAbsolutePath());
			outDir.mkdir();
		}
		// we test if the outDir exists again in cast we can't create it.
		if (!outDir.exists() || !outDir.isDirectory() || !outDir.canWrite()) {
			System.err.print("***ERROR : IOManager - checkOutDir: outDir "
					+ outDir);
			if (!outDir.exists()) {
				System.err.print(" doesn't exist");
			}
			if (!outDir.isDirectory()) {
				System.err.print(" is not a directory");
			}
			if (!outDir.canWrite()) {
				System.err.print(" can't write");

			}
			System.err.println();
			throw new IOException();
			// return false;
		}
		return true;
	}

	/**
	 * Create and check a new file to write in
	 * 
	 * @param fnm
	 *            the file to create
	 * @return the file to use
	 * @throws IOException
	 */
	public static File createWriteFile(final String fnm) throws IOException {
		// We can analyse... Output creation
		final File out = new File(fnm);
		if (!out.exists()) {
			out.createNewFile();
		}
		// Check we can write
		if (!out.canWrite()) {
			System.err
					.println("*** ERROR : IOManager - createWriteFile: can't write in new outFile (out):"
							+ out.getName());
			throw new IOException();
			// return null;
		}
		return out;
	}

	/**
	 * Create and check a new directory to write in
	 * 
	 * @param fnm
	 *            the directory name
	 * @return the directory to use
	 * @throws IOException
	 */
	public static File createWriteDir(final String fnm) throws IOException {
		// We can analyse... Output creation
		final File out = new File(fnm);
		if (!out.exists()) {
			out.mkdir();
		}
		// Check we can write
		if (!out.canRead() || !out.canWrite()) {
			System.err
					.println("*** ERROR : IOManager - createWriteDir: can't write in new outFile (out):"
							+ out.getName());
			throw new IOException();
			// return null;
		}
		return out;
	}

	/**
	 * Calculate nb of files in this directory
	 * 
	 * Suppose that inDir is checked.
	 * 
	 * @param inDir
	 *            the in dir
	 * @return nb of files in the direcotry and sub-directories
	 */
	private static int countDocumentRecursively(final File inDir) {
		int cpt = 0;
		if (!inDir.exists()) {
			System.err.println("IOManager, countDocumentRecusively : File "
					+ inDir.getAbsolutePath() + " don't exists");
			return -1;
		}

		for (final File f : inDir.listFiles()) {
			if (f.isDirectory()) {
				// System.out.println(f.getAbsolutePath());
				// cpt++;
				cpt += IOManager.countDocumentRecursively(f);
			} else {
				if (!f.getName().endsWith(Const.EXTENTION_KEEP)) {
					continue;
				}
				// System.out.print("+");
				cpt++;
			}
		}
		return cpt;
	}

	/**
	 * Get the number of files of the directory "directory"
	 * 
	 * This function use a file. If the number of file is already added to the
	 * file, we return it. Otherwise, we calculate it and add it to the file
	 * before we return it.
	 * 
	 * Suppose that "directory" is checked
	 * 
	 * @param directory
	 *            the directory to consider
	 * @return the number of files in the directory
	 * @throws IOException
	 * 
	 */
	public static int getNbFiles(final File directory) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(Const.PATH_TO_NBFILES)));
		String line = br.readLine();
		String words[];
		while (line != null) {
			words = line.split("\t");
			if (words[0].equals(directory.getAbsolutePath())) {
				br.close();
				return Integer.parseInt(words[1]);
			}
			line = br.readLine();
		}
		br.close();
		// no results, we have to calculate it
		final int nbDocs = IOManager.countDocumentRecursively(directory);
		// final PrintWriter pw = new PrintWriter(new
		// File(Const.PATH_TO_NBFILES));
		final BufferedWriter bw = new BufferedWriter(new FileWriter(
				Const.PATH_TO_NBFILES, true));
		bw.write(directory.getAbsolutePath() + "\t" + nbDocs + "\n");
		bw.close();
		return nbDocs;
	}

	/**
	 * nom de fichier .poid => chemin vers le fichier du corpus
	 * 
	 * @param fileName
	 * @return
	 */
	public static String returnFilePathFromPoid(final String fileName) {
		// the main directory is PATH_TO_CORPUS
		// calculate file name
		System.out.println(fileName);
		final String[] fileNameTokens = fileName.split("\\.");
		System.out.println(fileName.split("\\.").length);
		final String realFileName = fileNameTokens[0] + "." + fileNameTokens[1];

		final String mainFolder = fileNameTokens[0].substring(0, 4);
		System.out.println("MainFolder: " + mainFolder);
		final String folder = fileNameTokens[0].substring(0, 6);
		System.out.println("Folder: " + folder);
		System.out.println("File: " + realFileName);
		// System.out.println("Path: " + Const.PATH_TO_CORPUS + mainFolder
		// + File.separator + folder + File.separator + realFileName);

		return Const.PATH_TO_CORPUS + mainFolder + File.separator + folder
				+ File.separator + realFileName;
	}

	public static BufferedReader returnBufferedReader(final File f)
			throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(f)));
	}

}
