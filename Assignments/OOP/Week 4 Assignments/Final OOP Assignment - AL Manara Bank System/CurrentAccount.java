public class CurrentAccount extends Account {

    private final double overdraftLimit;

    public CurrentAccount(Customer owner, double balance, double overdraftLimit) {
        super(owner, balance);

        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public boolean isUsingOverdraft() {
        return balance < 0;
    }

    @Override
    public boolean withdraw(double amount) {

        if (!canTransact()) {
            return false;
        }

        if (balance - amount < -overdraftLimit) {
            return false;
        }

        balance -= amount;
        transactionCount++;

        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nOverdraft Limit: " + overdraftLimit +
                "\nUsing Overdraft: " + isUsingOverdraft();
    }
}