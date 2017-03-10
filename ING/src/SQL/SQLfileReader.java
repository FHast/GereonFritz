package SQL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SQLfileReader {
	
	private static BufferedReader reader;
	
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
