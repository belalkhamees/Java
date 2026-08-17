public class Magazine extends LibraryItem implements Renewable {

    private final int issueNumber;

    public Magazine(int id, String title, int issueNumber) {
        super(id, title);
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }

        return Math.min(daysOverdue * 3.00, 30.00);
    }

    @Override
    public int getLoanPeriod() {
        return 7;
    }

    @Override
    public String getCategory() {
        return "Magazine";
    }

    @Override
    public boolean renewLoan() {
        if (getStatus() != ItemStatus.ON_LOAN || getRenewalCount() >= getRenewalLimit()) {
            return false;
        }

        return recordRenewal();
    }

    @Override
    public int getRenewalLimit() {
        return 1;
    }
}
