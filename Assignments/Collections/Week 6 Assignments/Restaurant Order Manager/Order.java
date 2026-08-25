import java.util.ArrayList;

public class Order {

    private int orderId;
    private String customerName;
    private ArrayList<OrderItem> items;
    private double total;
    private OrderStatus status;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0;
        this.status = OrderStatus.PENDING;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
        calculateTotal();
    }

    public boolean removeItem(int itemId) {

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).getItem().getId() == itemId) {
                items.remove(i);
                calculateTotal();
                return true;
            }
        }

        return false;
    }

    public void calculateTotal() {

        total = 0;

        for (OrderItem orderItem : items) {
            total += orderItem.calculateSubtotal();
        }
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void displayOrder() {

        System.out.println("\n========== ORDER ==========");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customerName);
        System.out.println("Status: " + status);

        if (items.isEmpty()) {
            System.out.println("No items in this order.");
        } else {

            System.out.println("---------------------------");

            for (OrderItem orderItem : items) {

                System.out.println(
                        orderItem.getItem().getName() +
                                " x " +
                                orderItem.getQuantity() +
                                " = " +
                                orderItem.calculateSubtotal()
                );
            }
        }

        System.out.println("---------------------------");
        System.out.println("Total: " + total);
    }
}
