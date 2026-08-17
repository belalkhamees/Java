public class Library {

    private static final int MAX_ITEMS = 100;
    private static final int MAX_MEMBERS = 100;

    private final LibraryItem[] catalogue;
    private final Member[] members;

    private int itemCount;
    private int memberCount;

    public Library() {
        catalogue = new LibraryItem[MAX_ITEMS];
        members = new Member[MAX_MEMBERS];
        itemCount = 0;
        memberCount = 0;
    }

    public boolean registerItem(LibraryItem item) {
        if (item == null || itemCount >= MAX_ITEMS || findItemById(item.getId()) != null) {
            return false;
        }

        catalogue[itemCount] = item;
        itemCount++;
        LibraryItem.incrementTotalCatalogued();

        return true;
    }

    public boolean registerMember(Member member) {
        if (member == null || memberCount >= MAX_MEMBERS ||
                findMemberById(member.getMembershipId()) != null) {
            return false;
        }

        members[memberCount] = member;
        memberCount++;

        return true;
    }

    public LibraryItem findItemById(int id) {
        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getId() == id) {
                return catalogue[i];
            }
        }

        return null;
    }

    public Member findMemberById(int id) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getMembershipId() == id) {
                return members[i];
            }
        }

        return null;
    }

    public void viewCatalogue() {
        if (itemCount == 0) {
            System.out.println("Catalogue is empty.");
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            catalogue[i].display();
        }
    }

    public void viewItemsByStatus(ItemStatus status) {
        boolean found = false;

        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getStatus() == status) {
                catalogue[i].display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No items found with status " + status + ".");
        }
    }

    public void viewMembers() {
        if (memberCount == 0) {
            System.out.println("No members registered.");
            return;
        }

        for (int i = 0; i < memberCount; i++) {
            members[i].printDetails();
        }
    }

    public int getItemsOnLoan() {
        int count = 0;

        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getStatus() == ItemStatus.ON_LOAN) {
                count++;
            }
        }

        return count;
    }

    public double getLoanRate() {
        if (itemCount == 0) {
            return 0.0;
        }

        return (getItemsOnLoan() * 100.0) / itemCount;
    }

    public double getTotalOutstanding() {
        double total = 0.0;

        for (int i = 0; i < memberCount; i++) {
            total += members[i].getBalance();
        }

        return total;
    }

    public double getProjectedFines(int daysOverdue) {
        double total = 0.0;

        for (int i = 0; i < itemCount; i++) {
            if (catalogue[i].getStatus() == ItemStatus.ON_LOAN) {
                total += catalogue[i].calculateFine(daysOverdue);
            }
        }

        return total;
    }

    public void lendItem(int itemId, int membershipId) {
        LibraryItem item = findItemById(itemId);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        Member member = findMemberById(membershipId);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (item.getStatus() != ItemStatus.AVAILABLE) {
            System.out.println("Item is not available for borrowing.");
            return;
        }

        if (!member.canBorrow()) {
            System.out.println("Member is not eligible to borrow.");
            System.out.println("A member may hold fewer than 3 items and owe no more than 100.00 EGP.");
            return;
        }

        if (item.lendTo(member)) {
            member.recordBorrowing();

            System.out.println("Item borrowed successfully.");
            System.out.println("Loan period: " + item.getLoanPeriod() + " days.");
        }
    }

    public void returnItem(int itemId, int daysOverdue) {
        LibraryItem item = findItemById(itemId);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (item.getStatus() != ItemStatus.ON_LOAN) {
            System.out.println("Item is not currently on loan.");
            return;
        }

        if (daysOverdue < 0) {
            System.out.println("Days overdue cannot be negative.");
            return;
        }

        String borrowerName = item.getBorrowerName();
        Member member = findMemberByName(borrowerName);

        if (member == null) {
            System.out.println("Borrowing member could not be found.");
            return;
        }

        double baseFine = item.calculateFine(daysOverdue);

        if (daysOverdue == 0) {
            baseFine = 0.0;
        }

        double waiver = baseFine * member.getCategory().getWaiverRate();
        double waivedFine = baseFine - waiver;
        double administrativeCharge = daysOverdue > 0 ? LibraryItem.getAdministrativeCharge() : 0.0;
        double totalCharge = waivedFine + administrativeCharge;

        if (totalCharge > 0) {
            member.chargeFine(totalCharge);
        }

        member.recordReturn();
        item.takeBack();

        System.out.println("Item returned successfully.");
        System.out.println("Base fine: " + baseFine + " EGP");
        System.out.println("Waiver: " + waiver + " EGP");
        System.out.println("Fine after waiver: " + waivedFine + " EGP");
        System.out.println("Administrative charge: " + administrativeCharge + " EGP");
        System.out.println("Total charged: " + totalCharge + " EGP");
        System.out.println("Member new balance: " + member.getBalance() + " EGP");
    }

    private Member findMemberByName(String name) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getName().equals(name)) {
                return members[i];
            }
        }

        return null;
    }

    public void renewItem(int itemId) {
        LibraryItem item = findItemById(itemId);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (!(item instanceof Renewable)) {
            System.out.println("This item type cannot be renewed.");
            return;
        }

        Renewable renewable = (Renewable) item;

        if (renewable.renewLoan()) {
            int remaining = renewable.getRenewalLimit() - item.getRenewalCount();

            System.out.println("Loan renewed successfully.");
            System.out.println("Renewals used: " + item.getRenewalCount());
            System.out.println("Renewals remaining: " + remaining);
        }
        else {
            System.out.println("Renewal failed.");
            System.out.println("The item must be on loan and must not have reached its renewal limit.");

            int remaining = renewable.getRenewalLimit() - item.getRenewalCount();

            if (remaining == 0) {
                System.out.println("No renewals remaining.");
            }
        }
    }

    public void searchItem(int id) {
        LibraryItem item = findItemById(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        item.display();
    }

    public void payFine(int membershipId, double amount) {
        Member member = findMemberById(membershipId);

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (member.payFine(amount)) {
            System.out.println("Payment successful. New balance: " + member.getBalance() + " EGP");
        } else {
            if (amount <= 0) {
                System.out.println("Payment must be positive.");
            } else {
                System.out.println("Payment cannot exceed the balance owed.");
            }
        }
    }

    public void printReport() {
        System.out.println("\n========== LIBRARY REPORT ==========");
        System.out.println("Library: " + LibraryItem.getLibraryName());
        System.out.println("Catalogue size: " + itemCount);
        System.out.println("Items ever catalogued: " + LibraryItem.getTotalCatalogued());
        System.out.println("Items on loan: " + getItemsOnLoan());
        System.out.println("Loan rate: " + getLoanRate() + " %");
        System.out.println("Total outstanding: " + getTotalOutstanding() + " EGP");
        System.out.println("Projected fines for 5 days overdue: " + getProjectedFines(5) + " EGP");
        System.out.println("====================================");
    }
}
