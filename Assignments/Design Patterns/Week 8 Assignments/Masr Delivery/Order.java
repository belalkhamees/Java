import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final int id;
    private final Customer customer;
    private final Restaurant restaurant;
    private final Address deliveryAddress;
    private final List<OrderLine> lines;
    private final LocalDateTime placedAt;

    private OrderStatus status;
    private Rider rider;

    private LocalDateTime outForDeliveryAt;
    private LocalDateTime deliveredAt;

    private Promotion promotion;
    private String deliveryNotes;

    private BigDecimal subtotal;
    private BigDecimal deliveryFee;
    private BigDecimal serviceFee;
    private BigDecimal promotionDiscount;
    private BigDecimal total;

    private boolean paid;

    public Order(
            int id,
            Customer customer,
            Restaurant restaurant,
            Address deliveryAddress,
            List<OrderLine> lines) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Order ID must be positive");
        }

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null");
        }

        if (restaurant == null) {
            throw new IllegalArgumentException(
                    "Restaurant cannot be null");
        }

        if (deliveryAddress == null) {
            throw new IllegalArgumentException(
                    "Delivery address cannot be null");
        }

        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order must contain at least one line");
        }

        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.lines = List.copyOf(lines);

        this.placedAt = LocalDateTime.now();
        this.status = OrderStatus.PLACED;

        this.subtotal = BigDecimal.ZERO;
        this.deliveryFee = BigDecimal.ZERO;
        this.serviceFee = BigDecimal.ZERO;
        this.promotionDiscount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;

        this.paid = false;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Rider getRider() {
        return rider;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public BigDecimal getPromotionDiscount() {
        return promotionDiscount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getOutForDeliveryAt() {
        return outForDeliveryAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public boolean isPaid() {
        return paid;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setPriceDetails(
            BigDecimal subtotal,
            BigDecimal deliveryFee,
            BigDecimal serviceFee,
            BigDecimal promotionDiscount,
            BigDecimal total) {

        if (subtotal == null
                || subtotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Invalid subtotal");
        }

        if (deliveryFee == null
                || deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Invalid delivery fee");
        }

        if (serviceFee == null
                || serviceFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Invalid service fee");
        }

        if (promotionDiscount == null
                || promotionDiscount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Invalid promotion discount");
        }

        if (total == null
                || total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Invalid total");
        }

        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.serviceFee = serviceFee;
        this.promotionDiscount = promotionDiscount;
        this.total = total;
    }

    public void setRider(Rider rider) {

        if (rider == null) {
            throw new IllegalArgumentException(
                    "Rider cannot be null");
        }

        this.rider = rider;
    }

    public void markAsPaid() {

        if (paid) {
            throw new IllegalStateException(
                    "Order is already paid");
        }

        paid = true;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public void setDeliveryNotes(String deliveryNotes) {

        if (deliveryNotes != null
                && deliveryNotes.isBlank()) {
            throw new IllegalArgumentException(
                    "Delivery notes cannot be blank");
        }

        this.deliveryNotes = deliveryNotes;
    }

    public void changeStatus(OrderStatus newStatus)
            throws IllegalOrderTransitionException {

        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "Order status cannot be null");
        }

        if (newStatus == OrderStatus.ACCEPTED
                && !paid) {

            throw new IllegalOrderTransitionException(
                    "Order must be paid before it can be accepted");
        }

        if (!isLegalTransition(status, newStatus)) {

            throw new IllegalOrderTransitionException(
                    "Cannot change order from "
                            + status
                            + " to "
                            + newStatus);
        }

        status = newStatus;

        if (newStatus == OrderStatus.OUT_FOR_DELIVERY) {
            outForDeliveryAt = LocalDateTime.now();
        }

        if (newStatus == OrderStatus.DELIVERED) {
            deliveredAt = LocalDateTime.now();
        }
    }

    private boolean isLegalTransition(
            OrderStatus current,
            OrderStatus next) {

        if (current == OrderStatus.DELIVERED
                || current == OrderStatus.CANCELLED) {
            return false;
        }

        if (next == OrderStatus.CANCELLED) {
            return current != OrderStatus.OUT_FOR_DELIVERY;
        }

        return switch (current) {

            case PLACED ->
                    next == OrderStatus.ACCEPTED;

            case ACCEPTED ->
                    next == OrderStatus.PREPARING;

            case PREPARING ->
                    next == OrderStatus.READY;

            case READY ->
                    next == OrderStatus.ASSIGNED;

            case ASSIGNED ->
                    next == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY ->
                    next == OrderStatus.DELIVERED;

            case DELIVERED, CANCELLED ->
                    false;
        };
    }

    public Duration getDeliveryDuration() {

        if (outForDeliveryAt == null
                || deliveredAt == null) {
            return Duration.ZERO;
        }

        return Duration.between(
                outForDeliveryAt,
                deliveredAt);
    }

    public Duration getElapsedTime() {

        return Duration.between(
                placedAt,
                LocalDateTime.now());
    }
}