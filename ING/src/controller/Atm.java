package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import services.BankAccountService;
import services.PinCardService;
import services.TransactionService;
import services.exceptions.InvalidParameterException;
import view.ViewTUI;

public class Atm implements Observer {
	private ViewTUI view;
	private int atmAccountID;
	private static final String cmd = "";
	private static final String menuError = "Input unvalid. Please start a command with > " + cmd + " <";

	public Atm() {
		// User interface
		view = new ViewTUI();
		atmAccountID = 1;
		startScreen();
	}

	public static void main(String[] args) {
		new Atm();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ViewTUI) {
			fromView((String) arg);
		}
	}

	private void fromView(String s) {

	}

	private void startScreen() {
		String[] items = new String[] { "PinCard ID" };
		String input;
		do {
			input = view.getAnswer(getMenuText("INJECT CARD", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				int pinCardID = Integer.parseInt(view.getAnswer("Enter the PinCard ID..."));
				try {
					PinCardService.getPinCardByID(pinCardID);
					menuLogIn(pinCardID);
				} catch(InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "exit"));
	}

	private void menuLogIn(int pinCardID) {
		String[] items = new String[] { "Eject card", "Pin" };
		String input;
		do {
			input = view.getAnswer(getMenuText("LOG IN", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				startScreen();
				break;
			case cmd + "1":
				int pin = Integer.parseInt(view.getAnswer("Enter the Pin..."));				
				try {
					ResultSet res = PinCardService.isCorrectPin(pinCardID, pin);
					menuMain(res);
				} catch (InvalidParameterException e) {
					view.writeError(e.getMessage());
				}
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}

	private void menuMain(ResultSet res) {
		String[] items = new String[] { "Eject card", "Make transaction", "Last transactions", "Withdraw money" };
		double saldo = 0;
		try {
			saldo = (BankAccountService.getBankAccountByID(res.getInt(5))).getDouble(2);
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String input;
		do {
			input = view.getAnswer(getMenuText("MAIN MENU " + saldo + "€", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				startScreen();
				break;
			case cmd + "1":
				menuMakeTransaction(res);
				break;
			case cmd + "2":
				menuLastTransactions(res);
				break;
			case cmd + "3":
				menuWithdraw(res);
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "0") && !input.equals(cmd + "exit"));
	}
	
	private void menuMakeTransaction(ResultSet res) {

		String senderIBAN = "";
		try {
			senderIBAN = (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4);
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String receiverIBAN = "";
		String receiverName = "";
		String usage = "";
		String amount = "";

		String input;
		do {
			String[] items = new String[] { "Eject card", "Back", "Sender: " + senderIBAN, "Receiver: " + receiverIBAN,
					"Receiver name: " + receiverName, "Usage: " + usage, "Amount: " + amount, "ACCEPT", };
			input = view.getAnswer(getMenuText("NEW TRANSACTION", items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				startScreen();
				break;
			case cmd + "1":
				break;
			case cmd + "2":
				break;
			case cmd + "3":
				receiverIBAN = view.getAnswer("Enter the receivers' IBAN...");
				if (!BankAccountService.isBankAccountByIBAN(receiverIBAN)) {
					view.writeError("IBAN is Invalid.");
					receiverIBAN = "";
				}
				break;
			case cmd + "4":
				receiverName = view.getAnswer("Enter the name of the receiver...");
				break;
			case cmd + "5":
				usage = view.getAnswer("Enter the usage...");
				break;
			case cmd + "6":
				try {
					amount = "" + Double.parseDouble(view.getAnswer("Enter the amount..."));
				} catch (NumberFormatException e) {
					view.writeError("Entered amount format is invalid.");
				}
				break;
			case cmd + "7":
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
		} while (!input.equals(cmd + "1") && !input.equals(cmd + "exit"));
	}

	private void menuLastTransactions(ResultSet pinRes) {
		String IBAN = "";
		try {
			IBAN = (BankAccountService.getBankAccountByID(pinRes.getInt(5))).getString(4);
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet res = null;
		try {
			res = TransactionService.getTransactionsByIBAN((BankAccountService.getBankAccountByID(pinRes.getInt(5))).getString(4));
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		String[] items = new String[transactions.size() + 2];
		items[0] = "Eject card";
		items[1] = "Back";
		for (int i = 0; i < transactions.size(); i++) {
			items[i + 2] = transactions.get(i);
		}

		String input;
		do {
			input = view.getAnswer(getMenuText("TRANSACTION HISTORY FOR: " + IBAN, items));
			switch (input) {
			case cmd + "exit":
				shutDown();
				break;
			case cmd + "0":
				startScreen();
				break;
			case cmd + "1":
				break;
			default:
				view.writeError(menuError);
			}
		} while (!input.equals(cmd + "1") && !input.equals(cmd + "exit"));
	}

	private void menuWithdraw(ResultSet res) {
		int[] amount = new int[] { 20, 50, 70, 100, 150, 200, 300};
		String input;
		String[] items = new String[] {  "Eject card", "Back", amount[0] + "€",amount[1] + "€", amount[2] + "€", amount[3] + "€", amount[4] + "€", amount[5] + "€", amount[6] + "€", "Another amount" };
		input = view.getAnswer(getMenuText("WITHDRAW MONEY", items));
		switch (input) {
		case cmd + "exit":
			shutDown();
			break;
		case cmd + "0":
			startScreen();
			break;
		case cmd + "1":
			break;
		case cmd + "2":
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), amount[0], "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		case cmd + "3":
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), amount[1], "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		case cmd + "4":
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), amount[2], "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		case cmd + "5":
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), amount[3], "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		case cmd + "6":
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), amount[4], "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		case cmd + "7":
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), amount[5], "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		case cmd + "8":
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), amount[6], "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		case cmd + "9":
			int freeAmount = 0;
			try {
				freeAmount = Integer.parseInt(view.getAnswer("Enter the amount..."));
			} catch(NumberFormatException e) {
				menuWithdraw(res);
			}
			try {
			TransactionService.transfer( (BankAccountService.getBankAccountByID(res.getInt(5))).getString(4), (BankAccountService.getBankAccountByID(atmAccountID)).getString(4), freeAmount, "WITHDRAW", "Bank");
			} catch(InvalidParameterException e) {
				view.writeError(e.getMessage());
			} catch(SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			view.writeError(menuError);
		}
	}

	private String getMenuText(String title, String[] items) {
		String text = "----- " + title + " -----\n";
		for (int i = 0; i < items.length; i++) {
			text += "" + (i) + " - " + items[i] + "\n";
		}
		return text;
	}

	public void shutDown() {
		view.writeString("Shutting down...");
		// view.shutDown();
		System.exit(0);
	}
}
