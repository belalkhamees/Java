public class SavingsAccount extends Account {

    private final double annualInterestRate;
    private int monthlyWithdrawalCount;

    public SavingsAccount(Customer owner, double balance, double annualInterestRate) {
        super(owner, balance);

        this.annualInterestRate = annualInterestRate;
        this.monthlyWithdrawalCount = 0;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public int getMonthlyWithdrawalCount() {
        return monthlyWithdrawalCount;
    }

    @Override
    public boolean withdraw(double amount) {

        if (!canTransact()) {
            return false;
        }

        if (balance - amount < 0) {
            return false;
        }

        balance -= amount;
        transactionCount++;
        monthlyWithdrawalCount++;

        return true;
    }

    @Override
    protected void restoreBalance(double amount) {

        super.restoreBalance(amount);
        monthlyWithdrawalCount--;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nAnnual Interest Rate: " + annualInterestRate +
                "\nMonthly Withdrawal Count: " + monthlyWithdrawalCount;
    }
}