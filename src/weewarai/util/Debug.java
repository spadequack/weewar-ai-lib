package weewarai.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Class for debugging and logging. If debug is turned on, things are printed to
 * standard output. If logging is turned on, things are printed to the logging
 * file. Having one on does not affect the other and is not necessary for the
 * other to work. Make sure to close the logging writer.
 * 
 * @author spadequack
 */
public class Debug {

	private static boolean isOn = false;
	private static int numIndents = 0;
	public static final int NUM_SPACES_PER_INDENT = 2;

	private static boolean isLoggerOn = false;
	private static PrintWriter writer;
	private static String logFileName;

	public Debug(String logFileName) {
		Debug.logFileName = logFileName;
		try {
			// create the printwriter with automatic line flushing and append
			writer = new PrintWriter(new FileWriter(Debug.logFileName, true),
					true);
			writer.println("");
			writer.println("");
			writer.println("----------------- " + new Date()
					+ " -----------------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void finish() {
		writer.flush();
		writer.close();
	}

	public static boolean isLoggerOn() {
		return isLoggerOn;
	}

	public static void setLoggerOn(boolean isLoggerOn) {
		Debug.isLoggerOn = isLoggerOn;
	}

	public static String getLogFileName() {
		return logFileName;
	}

	public static void setLogFileName(String logFileName) {
		Debug.logFileName = logFileName;
	}

	public static int getNumIndents() {
		return numIndents;
	}

	public static void indent() {
		numIndents++;
	}

	public static void dedent() {
		numIndents--;
	}

	public static void setOn(boolean isOn) {
		Debug.isOn = isOn;
	}

	public static boolean isOn() {
		return isOn;
	}

	public static void print(String s) {
		// sadly System.out and PrintWriter have no ancestors in common.
		// PrintStream could be used instead of PrintWriter but is not
		// recommended...
		if (isOn) {
			for (int i = 0; i < numIndents * NUM_SPACES_PER_INDENT; i++) {
				System.out.print(" ");
			}
			System.out.println(s);
		}
		if (isLoggerOn) {
			for (int i = 0; i < numIndents * NUM_SPACES_PER_INDENT; i++) {
				writer.print(" ");
			}
			writer.println(s);
		}
	}

}
