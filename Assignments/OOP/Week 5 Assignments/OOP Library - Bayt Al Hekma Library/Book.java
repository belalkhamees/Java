public class Book extends LibraryItem implements Renewable {

    private final String author;
    private final int pageCount;

    public Book(int id, String title, String author, int pageCount) {
        super(id, title);
        this.author = author;
        this.pageCount = pageCount;
    }

    public String getAuthor() {
        return author;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }

        return daysOverdue * 5.00;
    }

    @Override
    public int getLoanPeriod() {
        return 14;
    }

    @Override
    public String getCategory() {
        return "Book";
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
        return 2;
    }
}
