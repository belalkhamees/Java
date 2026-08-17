public abstract class LibraryItem {

    private final int id;
    private final String title;
    private ItemStatus status;
    private String borrowerName;
    private int renewalCount;

    private static final String LIBRARY_NAME = "Bayt Al Hekma Library";
    private static final double ADMINISTRATIVE_CHARGE = 10.00;
    private static int totalCatalogued = 0;

    public LibraryItem(int id, String title) {
        this.id = id;
        this.title = title;
        this.status = ItemStatus.AVAILABLE;
        this.borrowerName = "";
        this.renewalCount = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public static String getLibraryName() {
        return LIBRARY_NAME;
    }

    public static double getAdministrativeCharge() {
        return ADMINISTRATIVE_CHARGE;
    }

    public static int getTotalCatalogued() {
        return totalCatalogued;
    }

    public static void incrementTotalCatalogued() {
        totalCatalogued++;
    }

    public abstract double calculateFine(int daysOverdue);

    public abstract int getLoanPeriod();

    public abstract String getCategory();

    public boolean reserve() {
        if (status != ItemStatus.AVAILABLE) {
            return false;
        }

        status = ItemStatus.RESERVED;
        return true;
    }

    public boolean markLost() {
        if (status == ItemStatus.LOST) {
            return false;
        }

        status = ItemStatus.LOST;
        return true;
    }

    public boolean bringBack() {
        if (status != ItemStatus.RESERVED && status != ItemStatus.LOST) {
            return false;
        }

        status = ItemStatus.AVAILABLE;
        borrowerName = "";
        renewalCount = 0;
        return true;
    }

    public boolean lendTo(Member member) {
        if (status != ItemStatus.AVAILABLE || member == null) {
            return false;
        }

        status = ItemStatus.ON_LOAN;
        borrowerName = member.getName();
        renewalCount = 0;
        return true;
    }

    public final boolean takeBack() {
        if (status != ItemStatus.ON_LOAN) {
            return false;
        }

        status = ItemStatus.AVAILABLE;
        borrowerName = "";
        renewalCount = 0;
        return true;
    }

    protected final boolean recordRenewal() {
        renewalCount++;
        return true;
    }

    public void display() {
        System.out.println("ID: " + id + " | Category: " + getCategory() +
                " | Title: " + title + " | State: " + status +
                " | Borrower: " + (borrowerName.isEmpty() ? "None" : borrowerName) +
                " | Loan Period: " + getLoanPeriod() + " days | One-Day Fine: " + calculateFine(1) + " EGP");
    }
}
