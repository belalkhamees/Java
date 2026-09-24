import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {

    private Customer customer;
    private Restaurant restaurant;
    private Address deliveryAddress;
    private final List<OrderLine> lines = new ArrayList<>();
    private Promotion promotion;
    private String deliveryNotes;
    private int id;


    public OrderBuilder setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Order ID must be positive");
        }

        this.id = id;
        return this;
    }

    public OrderBuilder setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public OrderBuilder setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public OrderBuilder setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public OrderBuilder addLine(OrderLine line) {
        if (line == null) {
            throw new IllegalArgumentException("Order line cannot be null");
        }

        lines.add(line);
        return this;
    }

    public OrderBuilder setPromotion(Promotion promotion) {
        this.promotion = promotion;
        return this;
    }

    public OrderBuilder setDeliveryNotes(String deliveryNotes) {
        if (deliveryNotes != null && deliveryNotes.isBlank()) {
            throw new IllegalArgumentException(
                    "Delivery notes cannot be blank"
            );
        }

        this.deliveryNotes = deliveryNotes;
        return this;
    }

    public Order build() {

        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant is required");
        }

        if (deliveryAddress == null) {
            throw new IllegalArgumentException(
                    "Delivery address is required"
            );
        }

        if (lines.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order must contain at least one line"
            );
        }

        Order order = new Order(
                id,
                customer,
                restaurant,
                deliveryAddress,
                lines
        );

        order.setPromotion(promotion);
        order.setDeliveryNotes(deliveryNotes);

        return order;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getDeliveryNotes() {
        return deliveryNotes;
    }
}