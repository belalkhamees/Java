public class FixedDepositAccount extends Account {

    private final double interestRate;
    private final int durationMonths;
    private int elapsedMonths;

    public FixedDepositAccount(Customer owner, double balance,
                               double interestRate, int durationMonths) {
        super(owner, balance);
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.elapsedMonths = 0;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public int getElapsedMonths() {
        return elapsedMonths;
    }

    public boolean isMatured() {
        return elapsedMonths >= durationMonths;
    }

    public int getRemainingMonths() {
        if (isMatured()) {
            return 0;
        }

        return durationMonths - elapsedMonths;
    }

    public void advanceMonth() {
        if (elapsedMonths < durationMonths) {
            elapsedMonths++;
        }
    }

    @Override
    public boolean canClose() {

        if (!super.canClose()) {
            return false;
        }

        return isMatured();
    }

    @Override
    public boolean withdraw(double amount) {

        if (!canTransact()) {
            return false;
        }

        if (!isMatured()) {
            return false;
        }

        if (balance - amount < 0) {
            return false;
        }

        balance -= amount;
        transactionCount++;

        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\nInterest Rate: " + interestRate +
                "\nDuration Months: " + durationMonths +
                "\nElapsed Months: " + elapsedMonths +
                "\nMatured: " + isMatured() +
                "\nRemaining Months: " + getRemainingMonths();
    }
}