package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import services.CustomerService;
import services.exceptions.BankLogicException;
import services.exceptions.InvalidParameterException;
import view.ViewTUI;

public class Administration implements Observer {
	private ViewTUI view;
	private static final String cmd = ".";
	private static final String menuError = "Input unvalid. Please start a command with > " + cmd + " <";
	

	public Administration() {
		// User interface
		view = new ViewTUI();
		// ((Observable) view).addObserver(this);
		// Thread viewThread = new Thread((Runnable) view);
		// viewThread.start();

		menuMain();

		// try {
		// viewThread.join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	public static void main(String[] args) {
		new Administration();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ViewTUI) {
			fromView((String) arg);
		}
	}

	private void fromView(String s) {

	}

	private void menuMain() {
		String[] items = new String[] { "Customers", "Bank accounts", "Transactions" };
		String input;
		do {
			input = view.getAnswer(getMenuText("MAIN MENU", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				shutDown();
				break;
			case cmd + "1":
				menuCustomers();
				break;
			case cmd + "2":
				menuBankaccounts();
				break;
			case cmd + "3":
				menuTransactions();
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuCustomers() {
		String[] items = new String[] { "Find a customer", "Create new Customer" };
		String input;
		do {
			input = view.getAnswer(getMenuText("CUSTOMER MENU", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				menuCustFind();
				break;
			case cmd + "2":
				menuCustCreate();
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuBankaccounts() {
		String[] items = new String[] { "Find a bank account", "Create new bank account" };
		String input;
		do {
			input = view.getAnswer(getMenuText("BANK ACCOUNT MENU", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				// TODO
				break;
			case cmd + "2":
				// TODO
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuTransactions() {
		String[] items = new String[] { "Create a transaction" };
		String input;
		do {
			input = view.getAnswer(getMenuText("BANK ACCOUNT MENU", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				// TODO
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuCustFind() {
		String[] items = new String[] { "ID", "BSN", "Email" };
		String input;
		do {
			input = view.getAnswer(getMenuText("SEARCHING", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				String id = view.getAnswer("Enter the ID: ");
				try {
					ResultSet res = CustomerService.getCustomerByID(Integer.parseInt(id));
					menuCustInfo(res);
				} catch (NumberFormatException e) {
					view.writeError("Given ID is invalid.");
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			case cmd + "2":
				String bsn = view.getAnswer("Enter the BSN: ");
				try {
					ResultSet res = CustomerService.getCustomerByBSN(Integer.parseInt(bsn));
					menuCustInfo(res);
				} catch (NumberFormatException e) {
					view.writeError("BSN format is invalid.");
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			case cmd + "3":
				String email = view.getAnswer("Enter the BSN: ");
				try {
					ResultSet res = CustomerService.getCustomerByEmail(email);
					menuCustInfo(res);
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuCustCreate() {

		String name = "";
		String surname = "";
		String dob = "";
		String bsn = "";
		String address = "";
		String phone = "";
		String email = "";

		String input;
		do {
			String[] items = new String[] { "Name: " + name, "Surname: " + surname, "Date of Birth: " + dob,
					"BSN: " + bsn, "Address: " + address, "Phone: " + phone, "Email: " + email, "ACCEPT", };
			input = view.getAnswer(getMenuText("CUSTOMER CREATION", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				name = view.getAnswer("Enter the name...");
				break;
			case cmd + "2":
				surname = view.getAnswer("Enter the surname...");
				break;
			case cmd + "3":
				dob = view.getAnswer("Enter the date of birth [dd.mm.yyyy]...");
				break;
			case cmd + "4":
				bsn = view.getAnswer("Enter the bsn...");
				break;
			case cmd + "5":
				address = view.getAnswer("Enter the address...");
				break;
			case cmd + "6":
				phone = view.getAnswer("Enter the phone number...");
				break;
			case cmd + "7":
				email = view.getAnswer("Enter the email address...");
				break;
			case cmd + "8":
				try {
					CustomerService.addCustomer(name, surname, dob, Integer.parseInt(bsn), address,
							Integer.parseInt(phone), email);
					ResultSet res = CustomerService.getCustomerByBSN(Integer.parseInt(bsn));
					menuCustInfo(res);
					input = "0";
				} catch (NumberFormatException e) {
					view.writeError("BSN or phone number format invalid.");
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuCustInfo(ResultSet customer) {
		String info = "\n----- CUSTOMER INFO -----\n";
		try {
			customer.next();
			info += "ID: " + customer.getInt(1) + "\n";
			info += "Name: " + customer.getString(2) + "\n";
			info += "Surname: " + customer.getString(3) + "\n";
			info += "Initials: " + customer.getString(4) + "\n";
			info += "Date of Birth: " + customer.getString(5) + "\n";
			info += "BSN: " + customer.getInt(6) + "\n";
			info += "Address: " + customer.getString(7) + "\n";
			info += "Phone: " + customer.getInt(8) + "\n";
			info += "Email: " + customer.getString(9) + "\n";
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String[] items = new String[] { "Change details", "Delete customer", "Show permissions", "Give permission",
				"Bank accounts" };
		String input;
		do {
			view.writeString(info);
			input = view.getAnswer(getMenuText("OPTIONS", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				view.writeError("Not yet supported");
				// TODO
				break;
			case cmd + "2":
				try {
					CustomerService.removeCustomer(customer.getInt(1));
					view.writeString("Success.");
					input = "0";
				} catch (InvalidParameterException | BankLogicException e) {
					view.writeError(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			case cmd + "3":
				view.writeError("Not yet supported");
				// TODO
				break;
			case cmd + "4":
				view.writeError("Not yet supported");
				// TODO
				break;
			case cmd + "5":
				view.writeError("Not yet supported");
				// TODO
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private String getMenuText(String title, String[] items) {
		String text = "----- " + title + " -----\n";
		text += "0 - Back\n";
		for (int i = 0; i < items.length; i++) {
			text += "" + (i + 1) + " - " + items[i] + "\n";
		}
		return text;
	}

	public void shutDown() {
		view.writeString("Shutting down...");
		// view.shutDown();
		System.exit(0);
	}
}
