package SQL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SQLfileReader {

	private static BufferedReader reader;

	/**
	 * Reads all Queries out of a SQL file and returns them as different Strings
	 * in an Array.
	 * 
	 * @param filepath
	 *            File path of SQL file to be read
	 * @return Array of all the different queries in the file
	 * @throws FileNotFoundException
	 *             if file path is incorrect
	 * @throws NoQueryFoundException
	 *             if the file does not contain any query
	 */
	public static String[] getQueries(String filepath) throws FileNotFoundException, NoQueryFoundException {
		String filedata = "";
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String result = reader.readLine();
			while (result != null) {
				filedata = filedata + result;
				result = reader.readLine();
			}
			String[] queries = filedata.split(";");
			return queries;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new NoQueryFoundException();
	}
}
