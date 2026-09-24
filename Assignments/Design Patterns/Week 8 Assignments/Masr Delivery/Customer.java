import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer {

    private final int id;
    private final String name;
    private final String mobile;

    private final List<Address> addresses = new ArrayList<>();
    private final List<String> searches = new ArrayList<>();

    private BigDecimal wallet;

    private int completedOrders;

    public Customer(
            int id,
            String name,
            String mobile,
            BigDecimal wallet) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }

        if (!isValidEgyptianMobile(mobile)) {
            throw new IllegalArgumentException(
                    "Mobile must be 11 digits and start with 010, 011, 012, or 015");
        }

        if (wallet == null || wallet.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Wallet balance cannot be negative");
        }

        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.wallet = wallet;
    }

    private boolean isValidEgyptianMobile(String mobile) {

        return mobile != null
                && mobile.matches("01[0125]\\d{8}");
    }

    public void addAddress(Address address) {

        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }

        if (!addresses.contains(address)) {
            addresses.add(address);
        }
    }

    public void addSearch(String search) {

        if (search == null || search.isBlank()) {
            return;
        }

        searches.removeIf(old -> old.equalsIgnoreCase(search));

        searches.add(0, search);

        if (searches.size() > 5) {
            searches.remove(5);
        }
    }

    public void deductWallet(BigDecimal amount)
            throws InsufficientWalletException {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Amount cannot be negative");
        }

        if (wallet.compareTo(amount) < 0) {
            throw new InsufficientWalletException(
                    "Insufficient wallet balance");
        }

        wallet = wallet.subtract(amount);
    }

    public void addWallet(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Amount cannot be negative");
        }

        wallet = wallet.add(amount);
    }

    public void orderCompleted() {
        completedOrders++;
    }

    public LoyaltyTier getLoyaltyTier() {

        if (completedOrders >= 30) {
            return LoyaltyTier.GOLD;
        }

        if (completedOrders >= 10) {
            return LoyaltyTier.SILVER;
        }

        return LoyaltyTier.BRONZE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public List<String> getSearches() {
        return Collections.unmodifiableList(searches);
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Customer customer)) {
            return false;
        }

        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}