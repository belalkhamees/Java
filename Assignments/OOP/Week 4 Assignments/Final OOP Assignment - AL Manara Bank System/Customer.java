public class Customer {

    private static int nextId = 1;

    private final int id;
    private final String name;
    private final String nationalId;
    private  String phone;
    private final CustomerTier tier;

    private int accountCount;

    public Customer(String name, String nationalId, String phone, CustomerTier tier) {
        this.id = nextId++;
        this.name = name;
        this.nationalId = nationalId;
        this.phone = phone;
        this.tier = tier;
        this.accountCount = 0;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhone() {
        return phone;
    }

    public CustomerTier getTier() {
        return tier;
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void incrementAccountCount() {
        accountCount++;
    }

    public void decrementAccountCount() {
        if (accountCount > 0) {
            accountCount--;
        }
    }

    @Override
    public String toString() {
        return "Customer ID: " + id +
                "\nName: " + name +
                "\nNational ID: " + nationalId +
                "\nPhone: " + phone +
                "\nTier: " + tier +
                "\nOpen Accounts: " + accountCount;
    }
}