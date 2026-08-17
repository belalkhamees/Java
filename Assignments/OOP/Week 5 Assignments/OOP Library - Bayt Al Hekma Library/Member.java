public class Member {

    private final int membershipId;
    private String name;
    private final MembershipType category;
    private double balance;
    private int itemsHeld;

    public Member(String name, int membershipId, MembershipType category) {

        this(name, membershipId, category, 0.0);
    }

    public Member(String name, int membershipId, MembershipType category, double balance) {
        this.name = name;
        this.membershipId = membershipId;
        this.category = category;
        this.balance = balance;
        this.itemsHeld = 0;
    }

    public String getName() {
        return name;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public MembershipType getCategory() {
        return category;
    }

    public double getBalance() {
        return balance;
    }

    public int getItemsHeld() {
        return itemsHeld;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean chargeFine(double amount) {
        if (amount <= 0) {
            return false;
        }

        balance += amount;
        return true;
    }

    public boolean payFine(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }

        balance -= amount;
        return true;
    }

    public boolean canBorrow() {
        return itemsHeld < 3 && balance <= 100.00;
    }

    public void recordBorrowing() {
        itemsHeld++;
    }

    public boolean recordReturn() {
        if (itemsHeld <= 0) {
            return false;
        }

        itemsHeld--;
        return true;
    }

    public void printDetails() {
        System.out.println("Name: " + name + " | ID: " + membershipId +
                " | Category: " + category + " | Items Held: " + itemsHeld + " | Balance: " + balance + " EGP");
    }
}
