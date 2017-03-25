package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;

/**
 * A simple TUI using the Console as input and output stream.
 * 
 * @author gereon
 *
 */
public class ViewTUI extends Observable implements Runnable {
	private static InputStream inStream = System.in;
	private static OutputStream outStream = System.out;
	private PrintWriter out = new PrintWriter(outStream);

	public ViewTUI() {

	}

	public String readString(String question) {
		// writeString(question);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
			antw = in.readLine();
		} catch (IOException e) {
			writeError(" Failed to read input");
		}

		return (antw == null) ? "" : antw;
	}

	public void writeString(String text) {
		out.println(text);
		out.flush();
	}

	public void writeError(String text) {
		System.err.println(text);
	}

	/**
	 * Checks the user input and acts accordingly. Passes on only valid input,
	 * thus not "" or null. informs the Controller (Observer)
	 */
	private void handleTerminalInput() {
		String input = readString("Input: ");
		if (!input.equals("") && input != null) {
			setChanged();
			notifyObservers(input);
		}
	}

	public String getAnswer(String s) {
		writeString(s);
		String a;
		do {
			a = readString("");
		} while (a == null || a.equals(""));
		return a;
	}

	public void run() {
		while (true) {
			handleTerminalInput();
		}
	}

	public void shutDown() {
		writeString("SHUTDOWN");
		out.close();
	}

}
