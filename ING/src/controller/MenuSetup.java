package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuSetup {
	private HashMap<String, Integer> menuLookup = new HashMap<>();
	private HashMap<Integer,ArrayList<String>> menuitems = new HashMap<>();
	private BufferedReader reader;
	private String filepath = "src/controller/menus.txt";
	
	/** what comes next?
	 * 0 - ID
	 * 1 - title
	 * 2 - menu item
	 */
	private int currentstate = 0;
	
	public MenuSetup() {
		readFile();
	}
	
	public void readFile() {
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line = reader.readLine();
			ArrayList<String> currentmenu = new ArrayList<>();
			int currentID = -1;
			while (line != null) {
				if (currentstate == 0) {
					currentmenu.clear();
					currentID = Integer.parseInt(line);
					currentstate = 1;
				} else if (currentstate == 1) {
					currentmenu.add(line);
					currentstate = 2;
				} else if (currentstate == 2) {
					if (line.startsWith("/")) {
						menuitems.put(currentID, currentmenu);
						currentstate = 0;
					} else {
						String[] words = line.split(";");
						currentmenu.add(words[0]);
						menuLookup.put(words[0], Integer.parseInt(words[1]));
					}
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
