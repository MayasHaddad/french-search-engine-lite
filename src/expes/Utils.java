package expes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Utils {

	final static Runtime runtime = Runtime.getRuntime();
	private static HashMap<Integer, Long> startTimes = new HashMap<Integer, Long>();
	private static int chronoId = 0;

	public static int cpt = 0;
	final static long MAX_MEMORY = Math.min(1073741824L * 2L,
			Utils.runtime.maxMemory());

	/**
	 * Returns a String representation of memory information
	 * 
	 * @return a String representation of memory information
	 */
	public static String memoryInfo() {
		final long maxMemory = Utils.runtime.maxMemory();
		final long allocatedMemory = Utils.runtime.totalMemory();
		final long freeMemory = Utils.runtime.freeMemory();
		return "  free memory: " + freeMemory / 1024.0 / 1024.0
				+ "\n  allocated memory: " + allocatedMemory / 1024.0 / 1024.0
				+ "\n  max memory: " + maxMemory / 1024.0 / 1024.0
				+ "\n  total free memory: "
				+ (freeMemory + maxMemory - allocatedMemory) / 1024.0 / 1024.0;
	}

	/**
	 * Returns used memory (in bytes)
	 * 
	 * @return used memory (in bytes)
	 */
	public static long getUsedMemory() {
		final long allocatedMemory = Utils.runtime.totalMemory();
		final long freeMemory = Utils.runtime.freeMemory();
		return allocatedMemory - freeMemory;
	}

	/**
	 * Returns <code>true</code> if the memory is 90% full, <code>false</code>
	 * otherwise
	 * 
	 * @return <code>true</code> if the memory is 90% full, <code>false</code>
	 *         otherwise
	 */
	public static boolean isMemoryFull() {
		return Utils.isMemoryFull(0.9);
	}

	/**
	 * Return <code>true</code> if the ratio of the memory is full.
	 * 
	 * @param ratio
	 *            the ratio we mustn't exceed
	 * @return <code>true</code> if the ratio of the memory is full.
	 */
	public static boolean isMemoryFull(final double ratio) {

		// final long maxMemory = Math.min(1073741824,
		// Utils.runtime.maxMemory()); // Utils.runtime.maxMemory();
		// System.out.println(maxMemory);
		final long allocatedMemory = Utils.runtime.totalMemory();
		final long freeMemory = Utils.runtime.freeMemory();
		// final double r = (allocatedMemory - freeMemory) / (double) maxMemory
		// * 1073741824 / maxMemory;
		final double r = (allocatedMemory - freeMemory)
				/ (double) Utils.MAX_MEMORY;
		if (Utils.cpt++ % 100 == 0) {
			System.out.println((double) allocatedMemory / 1073741824 + "\t"
					+ (double) freeMemory / 1073741824 + "\t"
					+ (double) Utils.MAX_MEMORY / 1073741824 + "\t"
					+ (double) Utils.runtime.maxMemory() / 1073741824 + "\t"
					+ r + "\t" + ratio);
		}
		if (r > ratio) {
			System.out.println("BOUM : " + (allocatedMemory - freeMemory)
					/ (double) 1073741824 + "GB");
			System.out.println((double) allocatedMemory / 1073741824 + "\t"
					+ (double) freeMemory / 1073741824 + "\t"
					+ (double) Utils.MAX_MEMORY / 1073741824 + "\t"
					+ (double) Utils.runtime.maxMemory() / 1073741824 + "\t"
					+ r + "\t" + ratio);
		}
		return r > ratio;
	}

	/**
	 * Waits until a key has been pressed.
	 */
	public static void waitKeyPressed() {
		try {
			System.out.println("\nPress a key to continue... \n");
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the chrono.
	 * 
	 * @return the identifier of the chrono
	 */
	public static int startChrono() {
		Utils.startTimes.put(++Utils.chronoId, System.currentTimeMillis());
		return Utils.chronoId;
	}

	/**
	 * Ends the chrono and returns the time in seconds since last start
	 * 
	 * @param chronoId
	 *            the identifier of the chrono to end.
	 * @return the time in seconds since last start of the identified chrono
	 */
	public static double endChrono(final int chronoId) {
		final long endTime = System.currentTimeMillis();
		final long elapsed = endTime - Utils.startTimes.get(chronoId);
		return elapsed / 1000.0;
	}

	/**
	 * Ends the chrono and returns a String representation of the time since
	 * last start
	 * 
	 * @param chronoId
	 *            the identifier of the chrono to end.
	 * @return a String representation of the time since last start
	 */
	public static String formatEndChrono(final int chronoId) {
		final long endTime = System.currentTimeMillis();
		final long elapsed = endTime - Utils.startTimes.get(chronoId);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(new Date(elapsed));
	}

	/**
	 * Returns a String representation of the current date and time
	 * 
	 * @return a String representation of the current date and time
	 */
	public static String logTime() {
		final Calendar now = Calendar.getInstance();
		final int hh = now.get(Calendar.HOUR_OF_DAY);
		final int mm = now.get(Calendar.MINUTE);
		final int ss = now.get(Calendar.SECOND);
		final int mois = now.get(Calendar.MONTH) + 1;
		final int jour = now.get(Calendar.DAY_OF_MONTH);
		final int annee = now.get(Calendar.YEAR);
		return jour + " / " + mois + " / " + annee + "   " + hh + ":" + mm
				+ ":" + ss + "\n";
	}
}
