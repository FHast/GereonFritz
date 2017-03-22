package controller;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import view.ViewTUI;

public class Administration implements Observer {

	private static ViewTUI view;
	private Menu currentMenu = Menu.MAIN;
	private HashMap<String, Menu> menuLookup = new HashMap<>();

	public Administration() {
		// User interface
		view = new ViewTUI();
		((Observable) view).addObserver(this);
		Thread viewThread = new Thread((Runnable) view);
		viewThread.start();
		view.writeString(getMenu(Menu.MAIN));

		try {
			viewThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ViewTUI) {
			fromView((String) arg);
		}
	}

	public static void main(String[] args) {
		new Administration();
	}

	private void fromView(String input) {
		System.out.println("READ INPUT: " + input);

	}

	private String getMenu(String[] lines) {
		String info = "";

		info += "--- " + currentMenu + " --- \n";
		info += "0 - EXIT \n";
		for (int i = 0; i < lines.length; i++) {
			info += "" + (i + 1) + " - " + lines[i] + "\n";
		}
		return info;
	}

	private void shutdown() {
		System.out.println("Shutting down...");
		System.exit(0);
	}
}
