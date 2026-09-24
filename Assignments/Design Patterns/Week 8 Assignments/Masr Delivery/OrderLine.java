import java.math.BigDecimal;

public class OrderLine {

    private final MenuItem item;
    private final Number quantity;

    public OrderLine(
            MenuItem item,
            Number quantity) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Menu item cannot be null");
        }

        if (quantity == null) {
            throw new IllegalArgumentException(
                    "Quantity cannot be null");
        }

        if (quantity instanceof Integer) {

            if (quantity.intValue() <= 0) {
                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

        } else if (quantity instanceof BigDecimal) {

            if (((BigDecimal) quantity)
                    .compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

        } else {

            throw new IllegalArgumentException(
                    "Quantity must be Integer or BigDecimal");
        }

        this.item = item;
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return item.calculatePrice(quantity);
    }

    public MenuItem getItem() {
        return item;
    }

    public Number getQuantity() {
        return quantity;
    }
}