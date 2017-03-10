package SQL.test;

import java.io.FileNotFoundException;

import SQL.NoQueryFoundException;
import SQL.SQLfileReader;

public class fileReaderTest {

	public static void main(String[] args) {
		try {
			for (String s : SQLfileReader.getQueries("src/SQL/createDatabase.sql"))
			System.out.println(s);
		} catch (FileNotFoundException | NoQueryFoundException e) {
			e.printStackTrace();
		}
	}
}
