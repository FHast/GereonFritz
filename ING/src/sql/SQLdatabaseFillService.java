package sql;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import modules.exceptions.InvalidParamValueException;
import services.BankAccountService;
import services.CustomerService;

public class SQLdatabaseFillService {

	public static final String SOURCEPATH = "src/SQL/";

	public static final void main(String[] args) {
		fillCustomerAccounts();
		fillBankAccounts();
	}

	public static void fillCustomerAccounts() {
		String[] name = new String[50];
		String[] surname = new String[50];
		// theoretical initials, created by addCustomer method
		String[] DOB = new String[50];
		int[] BSN = new int[50];
		String[] address = new String[50];
		int[] phone = new int[50];
		String[] email = new String[50];

		// Fill Arrays
		try {
			BufferedReader reader = new BufferedReader(new FileReader(SOURCEPATH + "randomNames"));
			int i = 0;
			while (reader.ready()) {
				String fullname = reader.readLine();
				name[i] = fullname.split(" ")[0];
				surname[i] = fullname.split(" ")[1];
				i++;
			}
			reader.close();

			reader = new BufferedReader(new FileReader(SOURCEPATH + "randomAddress"));
			i = 0;
			while (reader.ready()) {
				address[i] = reader.readLine();
				i++;
			}
			reader.close();

			reader = new BufferedReader(new FileReader(SOURCEPATH + "randomEmail"));
			i = 0;
			while (reader.ready()) {
				email[i] = reader.readLine();
				i++;
			}
			reader.close();

			for (i = 0; i < 50; i++) {
				DOB[i] = "" + ((int) (Math.random() * 31) + 1) + "." + ((int) (Math.random() * 12) + 1) + "."
						+ (1917 + (int) (Math.random() * 100));
				BSN[i] = (int) (Math.random() * 1000000000);
				phone[i] = 100000000 + (int) (Math.random() * 1000000000);
			}

			for (i = 0; i < 50; i++) {
				CustomerService.addCustomer(name[i], surname[i], DOB[i], BSN[i], address[i], phone[i], email[i]);
			}

		} catch (FileNotFoundException e) {
			System.err.println("Sourcepath '" + SOURCEPATH + "' seems invalid. ");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParamValueException e) {
			e.printStackTrace();
		}

	}

	public static void fillBankAccounts() {
		for (int i = 0; i < 100; i++) {
			int mainCustomer = (int) (Math.random() * 50) + 1;
			int startsaldo = (int) (Math.random() * 10000 );
			try {
				BankAccountService.addBankAccount(mainCustomer, startsaldo);
			} catch (InvalidParamValueException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void fillAccessPermissions() {
		
	}
}
