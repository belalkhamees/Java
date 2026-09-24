import java.math.BigDecimal;

public class WeightedItem extends MenuItem {

    private final BigDecimal pricePerKg;
    private BigDecimal stock;

    public WeightedItem(
            String id,
            String name,
            String category,
            BigDecimal pricePerKg,
            int preparationTime,
            BigDecimal stock) {

        super(
                id,
                name,
                category,
                preparationTime);

        if (pricePerKg == null
                || pricePerKg.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Price per kg must be greater than zero");
        }

        if (stock == null
                || stock.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Stock cannot be negative");
        }

        this.pricePerKg = pricePerKg;
        this.stock = stock;
    }

    @Override
    public BigDecimal calculatePrice(Number quantity) {

        BigDecimal weight =
                getQuantity(quantity);

        return pricePerKg.multiply(weight);
    }

    @Override
    public boolean hasStock(Number quantity) {

        BigDecimal weight =
                getQuantity(quantity);

        return stock.compareTo(weight) >= 0;
    }

    @Override
    public void removeStock(Number quantity)
            throws StockShortageException {

        BigDecimal weight =
                getQuantity(quantity);

        if (stock.compareTo(weight) < 0) {
            throw new StockShortageException(
                    "Not enough stock for " + getName());
        }

        stock = stock.subtract(weight);
    }

    @Override
    public void addStock(Number quantity) {

        BigDecimal weight =
                getQuantity(quantity);

        stock = stock.add(weight);
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    public BigDecimal getStock() {
        return stock;
    }

    private BigDecimal getQuantity(Number quantity) {

        if (quantity == null) {
            throw new IllegalArgumentException(
                    "Quantity cannot be null");
        }

        if (quantity instanceof BigDecimal) {

            BigDecimal weight =
                    (BigDecimal) quantity;

            if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

            return weight;
        }

        if (quantity instanceof Integer) {

            int value = quantity.intValue();

            if (value <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

            return BigDecimal.valueOf(value);
        }

        throw new IllegalArgumentException(
                "Quantity must be Integer or BigDecimal");
    }
}