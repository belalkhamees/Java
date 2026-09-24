import java.math.BigDecimal;
import java.util.List;

public class ComboItem extends MenuItem {

    private final List<MenuItem> items;
    private final double discount;

    public ComboItem(
            String id,
            String name,
            String category,
            int preparationTime,
            List<MenuItem> items,
            double discount) {

        super(
                id,
                name,
                category,
                preparationTime);

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Combo must contain at least one item");
        }

        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException(
                    "Discount must be between 0 and 100");
        }

        this.items = List.copyOf(items);
        this.discount = discount;
    }

    @Override
    public BigDecimal calculatePrice(Number quantity) {

        int count = getQuantity(quantity);

        BigDecimal singleComboPrice =
                items.stream()
                        .map(item ->
                                item.calculatePrice(1))
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        BigDecimal discountAmount =
                singleComboPrice
                        .multiply(
                                BigDecimal.valueOf(discount))
                        .divide(
                                BigDecimal.valueOf(100));

        BigDecimal finalPrice =
                singleComboPrice
                        .subtract(discountAmount);

        return finalPrice.multiply(
                BigDecimal.valueOf(count));
    }

    @Override
    public boolean hasStock(Number quantity) {

        int count = getQuantity(quantity);

        for (MenuItem item : items) {

            if (!item.hasStock(count)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void removeStock(Number quantity)
            throws StockShortageException {

        int count = getQuantity(quantity);

        if (!hasStock(count)) {
            throw new StockShortageException(
                    "Not enough stock for combo: "
                            + getName());
        }

        for (MenuItem item : items) {
            item.removeStock(count);
        }
    }

    @Override
    public void addStock(Number quantity) {

        int count = getQuantity(quantity);

        for (MenuItem item : items) {
            item.addStock(count);
        }
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public double getDiscount() {
        return discount;
    }

    private int getQuantity(Number quantity) {

        if (quantity == null) {
            throw new IllegalArgumentException(
                    "Quantity cannot be null");
        }

        if (quantity instanceof Integer) {

            int count = quantity.intValue();

            if (count <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

            return count;
        }

        if (quantity instanceof BigDecimal) {

            BigDecimal value =
                    (BigDecimal) quantity;

            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

            try {
                return value.intValueExact();

            } catch (ArithmeticException e) {
                throw new IllegalArgumentException(
                        "Combo quantity must be a whole number");
            }
        }

        throw new IllegalArgumentException(
                "Quantity must be Integer or BigDecimal");
    }
}