import java.math.BigDecimal;

public class StandardItem extends MenuItem {

    private final BigDecimal price;
    private int stock;

    public StandardItem(
            String id,
            String name,
            String category,
            BigDecimal price,
            int preparationTime,
            int stock) {

        super(
                id,
                name,
                category,
                preparationTime);

        if (price == null
                || price.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Price must be greater than zero");
        }

        if (stock < 0) {
            throw new IllegalArgumentException(
                    "Stock cannot be negative");
        }

        this.price = price;
        this.stock = stock;
    }

    @Override
    public BigDecimal calculatePrice(Number quantity) {

        int count = getQuantity(quantity);

        return price.multiply(
                BigDecimal.valueOf(count));
    }

    @Override
    public boolean hasStock(Number quantity) {

        return stock >= getQuantity(quantity);
    }

    @Override
    public void removeStock(Number quantity)
            throws StockShortageException {

        int count = getQuantity(quantity);

        if (stock < count) {
            throw new StockShortageException(
                    "Not enough stock for " + getName());
        }

        stock -= count;
    }

    @Override
    public void addStock(Number quantity) {

        stock += getQuantity(quantity);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
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
                        "Standard item quantity must be a whole number");
            }
        }

        throw new IllegalArgumentException(
                "Quantity must be Integer or BigDecimal");
    }
}