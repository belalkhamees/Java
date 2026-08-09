public abstract class Account {

    private static int nextAccountNumber = 1;

    private final int accountNumber;
    private final Customer owner;

    protected double balance;
    protected AccountStatus status;
    protected int transactionCount;

    public Account(Customer owner, double balance) {
        this.accountNumber = nextAccountNumber++;
        this.owner = owner;
        this.balance = balance;
        this.status = AccountStatus.ACTIVE;
        this.transactionCount = 0;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    protected boolean canTransact() {
        return status == AccountStatus.ACTIVE;
    }

    public boolean deposit(double amount) {

        if (!canTransact()) {
            return false;
        }

        balance += amount;
        transactionCount++;

        return true;
    }

    protected void restoreBalance(double amount) {
        balance += amount;
        transactionCount--;
    }

    public abstract boolean withdraw(double amount);

    public boolean canClose() {

        if (status == AccountStatus.CLOSED) {
            return false;
        }

        if (balance != 0) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {

        return "Account Number: " + accountNumber +
                "\nAccount Type: " + getClass().getSimpleName() +
                "\nOwner: " + owner.getName() +
                "\nBalance: " + balance +
                "\nStatus: " + status +
                "\nTransaction Count: " + transactionCount;
    }
}