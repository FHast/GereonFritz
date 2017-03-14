DROP TABLE IF EXISTS "AccessPermissions";
CREATE TABLE "AccessPermissions" ( 
	`CustomerID` INTEGER NOT NULL, 
	`BankAccountID` INTEGER NOT NULL, 
	PRIMARY KEY(`CustomerID`,`BankAccountID`), 
	FOREIGN KEY(`CustomerID`) REFERENCES `CustomerAccounts`(`CustomerAccountID`), 
	FOREIGN KEY(`BankAccountID`) REFERENCES `BankAccounts`(`BankAccountID`) 
);
DROP TABLE IF EXISTS "BankAccounts";
CREATE TABLE "BankAccounts" ( 
	`BankAccountID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, 
	`Saldo` REAL, `MainCustomerID` INTEGER NOT NULL, 
	`IBAN` TEXT NOT NULL UNIQUE, 
	FOREIGN KEY(`MainCustomerID`) REFERENCES `CustomerAccounts`(`CustomerAccountID`) 
);
DROP TABLE IF EXISTS "CustomerAccounts";
CREATE TABLE "CustomerAccounts" (
	`CustomerAccountID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Name`	TEXT NOT NULL,
	`Surname`	TEXT NOT NULL,
	`Initials`	TEXT NOT NULL,
	`DOB`	NUMERIC NOT NULL,
	`BSN`	INTEGER NOT NULL UNIQUE,
	`Address`	TEXT NOT NULL,
	`Phone`	INTEGER NOT NULL,
	`Email`	TEXT NOT NULL UNIQUE
);
DROP TABLE IF EXISTS "PinCards";
CREATE TABLE "PinCards" (
	`PinCardID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`ExpirationDate`	NUMERIC NOT NULL,
	`PIN`	INTEGER NOT NULL,
	`AccessPermissionID`	INTEGER NOT NULL UNIQUE,
	FOREIGN KEY(`AccessPermissionID`) REFERENCES AccessPermissions ( AccessPermissionID )
);
DROP TABLE IF EXISTS "Transactions";
CREATE TABLE `Transactions` (
	`TransactionID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`Amount`	REAL NOT NULL,
	`Usage`	TEXT,
	`TransactionTime`	NUMERIC NOT NULL,
	`ReceiverName`	TEXT,
	`SenderIBAN`	TEXT NOT NULL,
	`ReceiverIBAN`	TEXT NOT NULL
)