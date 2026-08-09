public class Bank {

    // Bank configuration values chosen for this system.
    private static final int MAX_CUSTOMERS = 100;
    private static final int MAX_ACCOUNTS = 200;

    private static final double MIN_SAVINGS_BALANCE = 500.0;
    private static final double MIN_CURRENT_BALANCE = 1000.0;
    private static final double MIN_FIXED_DEPOSIT_BALANCE = 5000.0;
    private static final double MIN_TRANSACTION_AMOUNT = 10.0;

    private final Customer[] customers;
    private final Account[] accounts;

    private int customerCount;
    private int accountCount;

    public Bank() {
        customers = new Customer[MAX_CUSTOMERS];
        accounts = new Account[MAX_ACCOUNTS];

        customerCount = 0;
        accountCount = 0;
    }

    public Customer findCustomerById(int id) {

        for (int i = 0; i < customerCount; i++) {

            if (customers[i].getId() == id) {
                return customers[i];
            }
        }

        return null;
    }

    public Customer findCustomerByNationalId(String nationalId) {

        for (int i = 0; i < customerCount; i++) {

            if (customers[i].getNationalId().equals(nationalId)) {
                return customers[i];
            }
        }

        return null;
    }

    public Account findAccountByNumber(int accountNumber) {

        for (int i = 0; i < accountCount; i++) {

            if (accounts[i].getAccountNumber() == accountNumber) {
                return accounts[i];
            }
        }

        return null;
    }


    public boolean registerCustomer(String name, String nationalId, String phone, CustomerTier tier) {

        if (customerCount >= customers.length) {
            return false;
        }

        if (findCustomerByNationalId(nationalId) != null) {
            return false;
        }

        Customer customer = new Customer(name, nationalId, phone, tier);

        customers[customerCount] = customer;
        customerCount++;

        return true;
    }

    public boolean openSavingsAccount(int customerId, double initialBalance, double annualInterestRate) {

        if (accountCount >= accounts.length) {
            return false;
        }

        if (initialBalance < MIN_SAVINGS_BALANCE) {
            return false;
        }

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            return false;
        }

        Account account =
                new SavingsAccount(
                        customer,
                        initialBalance,
                        annualInterestRate
                );

        accounts[accountCount] = account;
        accountCount++;

        customer.incrementAccountCount();

        return true;
    }

    public boolean openCurrentAccount(int customerId, double initialBalance, double overdraftLimit) {

        if (accountCount >= accounts.length) {
            return false;
        }

        if (initialBalance < MIN_CURRENT_BALANCE) {
            return false;
        }

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            return false;
        }

        Account account =
                new CurrentAccount(
                        customer,
                        initialBalance,
                        overdraftLimit
                );

        accounts[accountCount] = account;
        accountCount++;

        customer.incrementAccountCount();

        return true;
    }

    public boolean openFixedDepositAccount(int customerId, double initialBalance, double interestRate, int durationMonths) {

        if (accountCount >= accounts.length) {
            return false;
        }

        if (initialBalance < MIN_FIXED_DEPOSIT_BALANCE) {
            return false;
        }

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            return false;
        }

        Account account =
                new FixedDepositAccount(
                        customer,
                        initialBalance,
                        interestRate,
                        durationMonths
                );

        accounts[accountCount] = account;
        accountCount++;

        customer.incrementAccountCount();

        return true;
    }


    public boolean deposit(int accountNumber, double amount) {

        if (amount < MIN_TRANSACTION_AMOUNT) {
            return false;
        }

        Account account =
                findAccountByNumber(accountNumber);

        if (account == null) {
            return false;
        }

        return account.deposit(amount);
    }

    public boolean withdraw(int accountNumber, double amount) {

        if (amount < MIN_TRANSACTION_AMOUNT) {
            return false;
        }

        Account account =
                findAccountByNumber(accountNumber);

        if (account == null) {
            return false;
        }

        return account.withdraw(amount);
    }

    public boolean transfer(int sender, int receiver, double amount) {

        if (sender == receiver) {
            return false;
        }

        if (amount < MIN_TRANSACTION_AMOUNT) {
            return false;
        }

        Account senderAcc =
                findAccountByNumber(sender);

        Account receiverAcc =
                findAccountByNumber(receiver);

        if (senderAcc == null || receiverAcc == null) {
            return false;
        }

        if (!senderAcc.withdraw(amount)) {
            return false;
        }

        if (!receiverAcc.deposit(amount)) {

            senderAcc.restoreBalance(amount);

            return false;
        }

        return true;
    }

    public Account[] getCustomerAccounts(int customerId) {

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            return null;
        }

        int count = 0;

        for (int i = 0; i < accountCount; i++) {

            if (accounts[i].getOwner().getId() == customerId) {
                count++;
            }
        }

        Account[] result = new Account[count];

        int index = 0;

        for (int i = 0; i < accountCount; i++) {

            if (accounts[i].getOwner().getId() == customerId) {

                result[index] = accounts[i];
                index++;
            }
        }

        return result;
    }

    public Account[] getAllAccounts() {

        Account[] result = new Account[accountCount];

        for (int i = 0; i < accountCount; i++) {
            result[i] = accounts[i];
        }

        return result;
    }

    public double getCustomerTotalBalance(int customerId) {

        Account[] customerAccounts = getCustomerAccounts(customerId);

        if (customerAccounts == null) {
            return 0;
        }

        double total = 0;

        for (Account account : customerAccounts) {
            total += account.getBalance();
        }

        return total;
    }

    public double getTotalBalance(AccountType type) {

        Account[] matchingAccounts = getAccountsByType(type);

        double total = 0;

        for (Account account : matchingAccounts) {
            total += account.getBalance();
        }

        return total;
    }

    public Account[] getAccountsByType(AccountType type) {

        int count = 0;

        for (int i = 0; i < accountCount; i++) {

            if (type == AccountType.SAVINGS
                    && accounts[i] instanceof SavingsAccount) {
                count++;
            }
            else if (type == AccountType.CURRENT
                    && accounts[i] instanceof CurrentAccount) {
                count++;
            }
            else if (type == AccountType.FIXED_DEPOSIT
                    && accounts[i] instanceof FixedDepositAccount) {
                count++;
            }
        }

        Account[] result = new Account[count];

        int index = 0;

        for (int i = 0; i < accountCount; i++) {

            if (type == AccountType.SAVINGS
                    && accounts[i] instanceof SavingsAccount) {

                result[index] = accounts[i];
                index++;

            }
            else if (type == AccountType.CURRENT
                    && accounts[i] instanceof CurrentAccount) {

                result[index] = accounts[i];
                index++;

            }
            else if (type == AccountType.FIXED_DEPOSIT
                    && accounts[i] instanceof FixedDepositAccount) {

                result[index] = accounts[i];
                index++;
            }
        }

        return result;
    }

    public boolean closeAccount(int accountNumber) {

        Account account = findAccountByNumber(accountNumber);

        if (account == null) {
            return false;
        }

        if (!account.canClose()) {
            return false;
        }

        account.setStatus(AccountStatus.CLOSED);

        account.getOwner().decrementAccountCount();

        return true;
    }



}