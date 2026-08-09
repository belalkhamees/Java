import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Bank bank = new Bank();


    public static void displayWelcomeBanner() {
        System.out.print("""
                       +------------------------------------------+
                       |         Welcome to AL MANARA BANK        |
                       +------------------------------------------+
                       """);
    }

    // MENU
    // =========================================================

    private static void printMenu() {

        System.out.println("\n========== AL MANARA BANK ==========");
        System.out.println("1. Register New Customer");
        System.out.println("2. Open New Account");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Transfer Between Accounts");
        System.out.println("6. Display Customer Accounts");
        System.out.println("7. Display All Branch Accounts");
        System.out.println("8. Search Account by Number");
        System.out.println("9. Search Accounts by Type");
        System.out.println("10. Close an Account");
        System.out.println("0. Exit");
        System.out.println("====================================");
    }


    // 1. REGISTER CUSTOMER
    // =========================================================

    private static void registerCustomer() {

        System.out.println("\n--- Register New Customer ---");


        String name =
                Validation.readName(sc, "Enter full name: ");

        String nationalId =
                Validation.readNationalId(sc, "Enter national ID: ");

        String phone =
                Validation.readPhoneNumber(sc, "Enter phone number (optional): ");

        CustomerTier tier =
                Validation.readCustomerTier(sc);

        boolean success =
                bank.registerCustomer(name, nationalId, phone, tier);

        if (!success) {

            System.out.println(
                    "Customer registration failed."
            );

            System.out.println(
                    "The national ID may already exist " +
                            "or customer storage may be full."
            );

            return;
        }

        Customer customer = bank.findCustomerByNationalId(nationalId);

        System.out.println(
                "\nCustomer registered successfully."
        );

        System.out.println(customer);
    }


    // 2. OPEN ACCOUNT
    // =========================================================

    private static void openAccount() {

        System.out.println("\n--- Open New Account ---");

        int customerId = Validation.readPositiveInt(sc, "Enter customer ID: ");

        Customer customer = bank.findCustomerById(customerId);

        if (customer == null) {

            System.out.println("Customer not found.");
            return;
        }

        AccountType type = Validation.readAccountType(sc);

        double initialBalance = Validation.readPositiveDouble(sc, "Enter initial balance: ");

        boolean success = false;

        switch (type) {

            case SAVINGS:

                double annualInterestRate =
                        Validation.readPositiveDouble(sc, "Enter annual interest rate: ");

                success =
                        bank.openSavingsAccount(customerId, initialBalance, annualInterestRate);

                break;

            case CURRENT:

                double overdraftLimit =
                        Validation.readPositiveDouble(sc, "Enter overdraft limit: ");

                success =
                        bank.openCurrentAccount(customerId, initialBalance, overdraftLimit);

                break;

            case FIXED_DEPOSIT:

                double interestRate =
                        Validation.readPositiveDouble(sc, "Enter interest rate: ");

                int durationMonths =
                        Validation.readPositiveInt(sc, "Enter duration in months: ");

                success =
                        bank.openFixedDepositAccount(customerId, initialBalance, interestRate, durationMonths);

                break;
        }

        if (!success) {

            System.out.println(
                    "Account could not be opened."
            );

            System.out.println(
                    "Check the opening balance and account information."
            );

            return;
        }

        Account[] customerAccounts =
                bank.getCustomerAccounts(customerId);

        Account newAccount =
                customerAccounts[customerAccounts.length - 1];

        System.out.println("\nAccount opened successfully.");

        System.out.println(
                "Account Number: " + newAccount.getAccountNumber()
        );

        System.out.println(
                "Account Type: " + type
        );

        System.out.println(
                "Owner: " + customer.getName()
        );

        System.out.println(
                "Balance: " + newAccount.getBalance()
        );
    }


    // 3. DEPOSIT MONEY
    // =========================================================

    private static void depositMoney() {

        System.out.println("\n--- Deposit Money ---");

        int accountNumber =
                Validation.readPositiveInt(sc, "Enter account number: ");

        double amount =
                Validation.readPositiveDouble(sc, "Enter deposit amount: ");

        Account account =
                bank.findAccountByNumber(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        boolean success =
                bank.deposit(accountNumber, amount);

        if (success) {

            System.out.println("Deposit successful.");

            System.out.println(
                    "New Balance: " + account.getBalance()
            );

        } else {

            System.out.println("Deposit rejected.");

            System.out.println(
                    "The account may be frozen or closed, " +
                            "or the amount may be below the minimum transaction amount."
            );
        }
    }


    // 4. WITHDRAW MONEY
    // =========================================================

    private static void withdrawMoney() {

        System.out.println("\n--- Withdraw Money ---");

        int accountNumber =
                Validation.readPositiveInt(sc, "Enter account number: ");

        double amount =
                Validation.readPositiveDouble(sc, "Enter withdrawal amount: ");

        Account account =
                bank.findAccountByNumber(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        boolean success =
                bank.withdraw(accountNumber, amount);

        if (success) {

            System.out.println("Withdrawal successful.");

            System.out.println(
                    "New Balance: " + account.getBalance()
            );

            return;
        }

        System.out.println("Withdrawal rejected.");

        if (account.getStatus() != AccountStatus.ACTIVE) {

            System.out.println(
                    "Transactions are not allowed on a " +
                            account.getStatus() + " account."
            );

        } else if (account instanceof FixedDepositAccount) {

            FixedDepositAccount fixed =
                    (FixedDepositAccount) account;

            if (!fixed.isMatured()) {

                System.out.println(
                        "Fixed deposit has not matured."
                );

                System.out.println(
                        "Remaining months: " + fixed.getRemainingMonths()
                );

            } else {

                System.out.println(
                        "Withdrawal exceeds the available balance."
                );
            }

        } else if (account instanceof SavingsAccount) {

            System.out.println(
                    "Savings account cannot have a negative balance."
            );

        } else if (account instanceof CurrentAccount) {

            CurrentAccount current =
                    (CurrentAccount) account;

            System.out.println(
                    "Withdrawal exceeds the allowed overdraft limit of "
                            + current.getOverdraftLimit()
            );
        }
    }


    // 5. TRANSFER BETWEEN ACCOUNTS
    // =========================================================

    private static void transferMoney() {

        System.out.println("\n--- Transfer Between Accounts ---");

        int sender =
                Validation.readPositiveInt(
                        sc,
                        "Enter sender account number: "
                );

        int receiver =
                Validation.readPositiveInt(
                        sc,
                        "Enter receiver account number: "
                );

        double amount =
                Validation.readPositiveDouble(
                        sc,
                        "Enter transfer amount: "
                );

        Account senderAccount =
                bank.findAccountByNumber(sender);

        Account receiverAccount =
                bank.findAccountByNumber(receiver);

        if (senderAccount == null) {

            System.out.println("sender account not found.");
            return;
        }

        if (receiverAccount == null) {

            System.out.println("receiver account not found.");
            return;
        }

        if (sender == receiver) {

            System.out.println(
                    "sender and receiver accounts must be different."
            );

            return;
        }

        boolean success =
                bank.transfer(sender, receiver, amount);

        if (success) {

            System.out.println("Transfer successful.");

            System.out.println(
                    "sender New Balance: " + senderAccount.getBalance()
            );

            System.out.println(
                    "receiver New Balance: " +
                            receiverAccount.getBalance()
            );

        } else {

            System.out.println("Transfer failed.");
            System.out.println("No funds were lost.");
        }
    }


    // 6. DISPLAY CUSTOMER ACCOUNTS
    // =========================================================

    private static void displayCustomerAccounts() {

        System.out.println("\n--- Display Customer Accounts ---");

        int customerId =
                Validation.readPositiveInt(sc, "Enter customer ID: ");

        Customer customer =
                bank.findCustomerById(customerId);

        if (customer == null) {

            System.out.println("Customer not found.");
            return;
        }

        Account[] accounts =
                bank.getCustomerAccounts(customerId);

        System.out.println("\n" + customer);

        if (accounts.length == 0) {

            System.out.println(
                    "This customer has no accounts."
            );

            return;
        }

        System.out.println("\nAccounts:");

        for (Account account : accounts) {

            System.out.println(
                    "\n----------------------------"
            );

            System.out.println(account);
        }

        System.out.println(
                "\nCombined Balance: " +
                        bank.getCustomerTotalBalance(customerId)
        );
    }


    // 7. DISPLAY ALL BRANCH ACCOUNTS
    // =========================================================

    private static void displayAllBranchAccounts() {

        System.out.println("\n--- All Branch Accounts ---");

        Account[] accounts =
                bank.getAllAccounts();

        if (accounts.length == 0) {

            System.out.println(
                    "There are no accounts in the branch."
            );

            return;
        }

        for (Account account : accounts) {

            System.out.println(
                    "\n----------------------------"
            );

            System.out.println(
                    "Account Number: " +
                            account.getAccountNumber()
            );

            System.out.println(
                    "Owner: " +
                            account.getOwner().getName()
            );

            System.out.println(
                    "Account Type: " +
                            account.getClass().getSimpleName()
            );

            System.out.println(
                    "Balance: " +
                            account.getBalance()
            );

            System.out.println(
                    "Status: " +
                            account.getStatus()
            );

            System.out.println(
                    "Transaction Count: " +
                            account.getTransactionCount()
            );
        }
    }


    // 8. SEARCH ACCOUNT BY NUMBER
    // =========================================================

    private static void searchAccountByNumber() {

        System.out.println("\n--- Search Account By Number ---");

        int accountNumber =
                Validation.readPositiveInt(
                        sc,
                        "Enter account number: "
                );

        Account account =
                bank.findAccountByNumber(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        System.out.println("\nAccount found:");

        System.out.println(
                "----------------------------"
        );

        System.out.println(account);
    }


    // 9. SEARCH ACCOUNTS BY TYPE
    // =========================================================

    private static void searchAccountsByType() {

        System.out.println("\n--- Search Accounts By Type ---");

        AccountType type =
                Validation.readAccountType(sc);

        Account[] accounts =
                bank.getAccountsByType(type);

        if (accounts.length == 0) {

            System.out.println(
                    "No accounts found for type: " + type
            );

            return;
        }

        for (Account account : accounts) {

            System.out.println(
                    "\n----------------------------"
            );

            System.out.println(account);
        }

        System.out.println(
                "\nNumber of Matching Accounts: " +
                        accounts.length
        );

        System.out.println(
                "Combined Balance: " +
                        bank.getTotalBalance(type)
        );
    }


    // 10. CLOSE ACCOUNT
    // =========================================================

    private static void closeAccount() {

        System.out.println("\n--- Close Account ---");

        int accountNumber =
                Validation.readPositiveInt(
                        sc,
                        "Enter account number: "
                );

        Account account =
                bank.findAccountByNumber(accountNumber);

        if (account == null) {

            System.out.println("Account not found.");
            return;
        }

        if (account.getStatus() == AccountStatus.CLOSED) {

            System.out.println("Account is already closed.");
            return;
        }

        if (account.getBalance() != 0) {

            System.out.println("Account cannot be closed.");

            System.out.println(
                    "Balance must be exactly 0."
            );

            return;
        }

        if (account instanceof FixedDepositAccount) {

            FixedDepositAccount fixed =
                    (FixedDepositAccount) account;

            if (!fixed.isMatured()) {

                System.out.println(
                        "Account cannot be closed."
                );

                System.out.println(
                        "Fixed deposit has not matured."
                );

                System.out.println(
                        "Remaining months: " +
                                fixed.getRemainingMonths()
                );

                return;
            }
        }

        boolean success =
                bank.closeAccount(accountNumber);

        if (success) {

            System.out.println(
                    "Account closed successfully."
            );

        } else {

            System.out.println(
                    "Account could not be closed."
            );
        }
    }


    // MAIN
    // =========================================================

    public static void main(String[] args) {

        displayWelcomeBanner();


        int choice;

        do {

            printMenu();

            choice = Validation.readChoice(sc, "Choose an option: ");

            switch (choice) {

                case 1 -> registerCustomer();

                case 2 -> openAccount();

                case 3 -> depositMoney();

                case 4 -> withdrawMoney();

                case 5 -> transferMoney();

                case 6 -> displayCustomerAccounts();

                case 7 -> displayAllBranchAccounts();

                case 8 -> searchAccountByNumber();

                case 9 -> searchAccountsByType();

                case 10 -> closeAccount();

                case 0 -> System.out.println("\nThank you for using Al Manara Bank System.");

                default -> System.out.println("Invalid menu option.");
            }

        } while (choice != 0);

        sc.close();
    }
}