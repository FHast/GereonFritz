package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import services.AccessPermissionService;
import services.BankAccountService;
import services.CustomerService;
import services.TransactionService;
import services.exceptions.BankLogicException;
import services.exceptions.InvalidParameterException;
import view.ViewTUI;

public class Administration implements Observer {
	private ViewTUI view;
	private static final String cmd = "";
	private static final String menuError = "Input unvalid. Please start a command with > " + cmd + " <";

	public Administration() {
		// User interface
		view = new ViewTUI();
		// menu
		menuMain();
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
		String[] items = new String[] { "Find bank accounts", "Create new bank account" };
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
				menuBaccFind();
				break;
			case cmd + "2":
				menuBaccCreate();
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuBaccFind() {
		String[] items = new String[] { "IBAN", "Bank account number", "Owner ID" };
		String input;
		do {
			input = view.getAnswer(getMenuText("BANK ACCOUNT SEARCH", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				String IBAN = view.getAnswer("Enter the IBAN...");
				try {
					ResultSet res = BankAccountService.getBankAccountByIBAN(IBAN);
					menuBaccInfo(res);
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			case cmd + "2":
				String baccID = view.getAnswer("Enter the bank account ID...");
				try {
					ResultSet res = BankAccountService.getBankAccountByID(Integer.parseInt(baccID));
					menuBaccInfo(res);
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			case cmd + "3":
				String ID = view.getAnswer("Enter the customer ID of the owner...");
				try {
					ResultSet res = BankAccountService.getBankAccountsByCustomer(Integer.parseInt(ID));
					menuBaccList(res);
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				} catch (NumberFormatException e) {
					view.writeError("Customer ID is invalid. ---");
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuBaccCreate() {

		String owner = "";
		String saldo = "";

		String input;
		do {
			String[] items = new String[] { "Owner ID: " + owner, "initial saldo: " + saldo, "ACCEPT", };
			input = view.getAnswer(getMenuText("BANK ACCOUNT CREATION", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				owner = view.getAnswer("Enter the Customer ID of the owner...");
				break;
			case cmd + "2":
				saldo = view.getAnswer("Enter the initial saldo [€.Cents]...");
				break;
			case cmd + "3":
				try {
					int ID = BankAccountService.addBankAccount(Integer.parseInt(owner), Double.parseDouble(saldo));
					ResultSet res = BankAccountService.getBankAccountByID(ID);
					menuBaccInfo(res);
					input = cmd + "0";
				} catch (NumberFormatException e) {
					view.writeError("Owner ID or saldo format invalid.");
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuBaccList(ResultSet res) {
		Integer ownerID = null;
		ArrayList<String> baccs = new ArrayList<>();
		ArrayList<Integer> baccIDs = new ArrayList<>();
		String baccInfo = "";
		try {
			while (res.next()) {
				ownerID = res.getInt(3);
				baccInfo = "";
				baccIDs.add(res.getInt(1));
				baccInfo += "IBAN: " + res.getString(4) + "\n Saldo: " + res.getDouble(2);
				baccs.add(baccInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] items = new String[baccs.size()];
		for (int i = 0; i < baccs.size(); i++) {
			items[i] = baccs.get(i);
		}

		String input;
		do {
			input = view.getAnswer(getMenuText("BANK ACCOUNTS OF CUSTOMER: " + ownerID, items));
			if (input.startsWith(cmd)) {
				if (input.equals(cmd + "0")) {
					break;
				} else if (input.equals(cmd + "exit")) {
					shutDown();
				} else {
					int index = Integer.parseInt(input.substring(cmd.length())) - 1;
					if (index < baccIDs.size()) {
						try {
							ResultSet bacc = BankAccountService.getBankAccountByID(baccIDs.get(index));
							menuBaccInfo(bacc);
						} catch (InvalidParameterException e) {
							e.printStackTrace();
						}
					} else {
						view.writeError(menuError);
					}
				}
			} else {
				view.writeError(menuError);
			}

		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuBaccPermissionList(ResultSet res, String IBAN) {
		ArrayList<String> customers = new ArrayList<>();
		ArrayList<Integer> custIDs = new ArrayList<>();
		String custInfo = "";
		try {
			while (res.next()) {
				custInfo = "";
				custIDs.add(res.getInt(1));
				ResultSet cust = CustomerService.getCustomerByID(res.getInt(1));
				custInfo += "Name: " + cust.getString(3) + ", " + cust.getString(2) + "\n ID: " + cust.getInt(1);
				customers.add(custInfo);
				cust.close();
			}
		} catch (SQLException | InvalidParameterException e) {
			e.printStackTrace();
		}
		String[] items = new String[customers.size()];
		for (int i = 0; i < customers.size(); i++) {
			items[i] = customers.get(i);
		}
		String input;
		do {
			input = view.getAnswer(getMenuText("CUSTOEMRS WITH PERMISSIONS TO: " + IBAN, items));
			if (input.startsWith(cmd)) {
				if (input.equals(cmd + "0")) {
					break;
				} else if (input.equals(cmd + "exit")) {
					shutDown();
				} else {
					int index = Integer.parseInt(input.substring(cmd.length())) - 1;
					if (index < custIDs.size()) {
						try {
							ResultSet cust = CustomerService.getCustomerByID(custIDs.get(index));
							menuCustInfo(cust);
						} catch (InvalidParameterException e) {
							e.printStackTrace();
						}
					} else {
						view.writeError(menuError);
					}
				}
			} else {
				view.writeError(menuError);
			}

		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuBaccInfo(ResultSet bacc) {
		String info = "\n----- BANK ACCOUNT INFO -----\n";
		try {
			bacc.next();
			info += "ID: " + bacc.getInt(1) + "\n";
			info += "IBAN: " + bacc.getString(4) + "\n";
			info += "Saldo: " + bacc.getDouble(2) + "\n";

			String ownerInfo = "";
			try {
				ResultSet owner = CustomerService.getCustomerByID(bacc.getInt(3));
				ownerInfo += "[" + owner.getInt(1) + "] " + owner.getString(3) + ", " + owner.getString(2);
			} catch (InvalidParameterException e1) {
				e1.printStackTrace();
			}

			info += "Owner: " + ownerInfo;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String[] items = new String[] { "Change details", "Delete account", "Show permissions", "Give permission",
				"Show Owner", "Transactions" };
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
					BankAccountService.removeBankAccount(bacc.getString(4));
					view.writeString("Success.");
					input = cmd + "0";
				} catch (BankLogicException e) {
					view.writeError(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case cmd + "3":
				try {
					ResultSet permissions = AccessPermissionService.getPermissionsByIBAN(bacc.getString(4));
					menuBaccPermissionList(permissions, bacc.getString(4));
				} catch (InvalidParameterException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				break;
			case cmd + "4":
				String custID = view.getAnswer("Enter the customer ID...");
				try {
					AccessPermissionService.addPermission(Integer.parseInt(custID), bacc.getString(4));
					view.writeString("Success.");
				} catch (NumberFormatException e1) {
					view.writeError("Customer ID format is invalid.");
				} catch (InvalidParameterException e1) {
					view.writeError(e1.getMessage());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				break;
			case cmd + "5":
				try {
					ResultSet res = CustomerService.getCustomerByID(bacc.getInt(3));
					menuCustInfo(res);
				} catch (InvalidParameterException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case cmd + "6":
				try {
					ResultSet transactions = TransactionService.getTransactionsByIBAN(bacc.getString(4));
					menuTransList(transactions, bacc.getString(4));
				} catch (InvalidParameterException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
				menuTransCreate();
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuTransCreate() {

		String senderIBAN = "";
		String receiverIBAN = "";
		String receiverName = "";
		String usage = "";
		String amount = "";

		String input;
		do {
			String[] items = new String[] { "Sender: " + senderIBAN, "Receiver: " + receiverIBAN,
					"Receiver name: " + receiverName, "Usage: " + usage, "Amount: " + amount, "ACCEPT", };
			input = view.getAnswer(getMenuText("NEW TRANSACTION", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				break;
			case cmd + "1":
				senderIBAN = view.getAnswer("Enter the senders' IBAN...");
				if (!BankAccountService.isBankAccountByIBAN(senderIBAN)) {
					view.writeError("IBAN is Invalid.");
					senderIBAN = "";
				}
				break;
			case cmd + "2":
				receiverIBAN = view.getAnswer("Enter the receivers' IBAN...");
				if (!BankAccountService.isBankAccountByIBAN(receiverIBAN)) {
					view.writeError("IBAN is Invalid.");
					receiverIBAN = "";
				}
				break;
			case cmd + "3":
				receiverName = view.getAnswer("Enter the name of the receiver...");
				break;
			case cmd + "4":
				usage = view.getAnswer("Enter the usage...");
				break;
			case cmd + "5":
				try {
					amount = "" + Double.parseDouble(view.getAnswer("Enter the amount..."));
				} catch (NumberFormatException e) {
					view.writeError("Entered amount format is invalid.");
				}
				break;
			case cmd + "6":
				try {
					TransactionService.transfer(senderIBAN, receiverIBAN, Double.parseDouble(amount), usage,
							receiverName);
					view.writeString("Success");
					input = cmd + "0";
				} catch (NumberFormatException e) {
					view.writeError("Amount format is invalid.");
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuTransList(ResultSet res, String IBAN) {
		ArrayList<String> transactions = new ArrayList<>();
		ArrayList<Integer> transIDs = new ArrayList<>();
		String transinfo = "";
		try {
			while (res.next()) {
				transinfo = "";
				transIDs.add(res.getInt(1));
				String datetime = res.getString(4);
				String[] dateparts = datetime.split("T")[0].split("-");
				String date = "" + dateparts[2] + "." + dateparts[1] + "." + dateparts[0].substring(2);
				if (res.getString(6).equals(IBAN)) {
				transinfo += "" + date + " Amount: -" + res.getDouble(2) + "\n Receiver: " + res.getString(7);
				} else {
					transinfo += "" + date + " Amount: +" + res.getDouble(2) + "\n Sender: " + res.getString(6);
				}
				transactions.add(transinfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] items = new String[transactions.size()];
		for (int i = 0; i < transactions.size(); i++) {
			items[i] = transactions.get(i);
		}

		String input;
		do {
			input = view.getAnswer(getMenuText("TRANSACTION HISTORY FOR: " + IBAN, items));
			if (input.startsWith(cmd)) {
				if (input.equals(cmd + "0")) {
					break;
				} else if (input.equals(cmd + "exit")) {
					shutDown();
				} else {
					// TODO
				}
			} else {
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
				String id = view.getAnswer("Enter the ID... ");
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
				String bsn = view.getAnswer("Enter the BSN... ");
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
				String email = view.getAnswer("Enter the BSN... ");
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
					input = cmd + "0";
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
					input = cmd + "0";
				} catch (InvalidParameterException | BankLogicException e) {
					view.writeError(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case cmd + "3":
				try {
					ResultSet permissions = AccessPermissionService.getPermissionsByCustomer(customer.getInt(1));
					menuCustPermissionList(permissions, customer.getInt(1));
				} catch (InvalidParameterException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				break;
			case cmd + "4":
				String IBAN = view.getAnswer("Enter the IBAN");
				try {
					AccessPermissionService.addPermission(customer.getInt(1), IBAN);
				} catch (InvalidParameterException e1) {
					view.writeError(e1.getMessage());
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				break;
			case cmd + "5":
				try {
					ResultSet res = BankAccountService.getBankAccountsByCustomer(customer.getInt(1));
					menuBaccList(res);
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuCustPermissionList(ResultSet res, int custID) {
		ArrayList<String> baccs = new ArrayList<>();
		ArrayList<Integer> baccIDs = new ArrayList<>();
		String baccInfo = "";
		try {
			while (res.next()) {
				baccInfo = "";
				baccIDs.add(res.getInt(2));
				ResultSet bacc = BankAccountService.getBankAccountByID(res.getInt(2));
				ResultSet owner = CustomerService.getCustomerByID(bacc.getInt(3));
				baccInfo += "IBAN: " + bacc.getString(4) + "\n Owner: " + owner.getString(3) + ", "
						+ owner.getString(2);
				baccs.add(baccInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
		String[] items = new String[baccs.size()];
		for (int i = 0; i < baccs.size(); i++) {
			items[i] = baccs.get(i);
		}

		String input;
		do {
			input = view.getAnswer(getMenuText("PERMISSIONS OF CUSTOMER: " + custID, items));
			if (input.startsWith(cmd)) {
				if (input.equals(cmd + "0")) {
					break;
				} else if (input.equals(cmd + "exit")) {
					shutDown();
				} else {
					int index = Integer.parseInt(input.substring(cmd.length())) - 1;
					if (index < baccIDs.size()) {
						try {
							ResultSet bacc = BankAccountService.getBankAccountByID(baccIDs.get(index));
							menuBaccInfo(bacc);
						} catch (InvalidParameterException e) {
							e.printStackTrace();
						}
					} else {
						view.writeError(menuError);
					}
				}
			} else {
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
