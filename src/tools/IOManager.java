package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class IOManager {

	public static BufferedReader BR = null;
	public static BufferedWriter BW = null;

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
	 * Suppose that inDir is checked.
	 * 
	 * @param inDir
	 *            the in dir
	 * @return nb of files in the direcotry and sub-directories
	 */
	public static int countDocumentRecursively(final File inDir) {
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
				if (!f.getName().endsWith(Indexer.EXTENTION_KEEP)) {
					continue;
				}
				// System.out.print("+");
				cpt++;
			}
		}
		return cpt;
	}
}
