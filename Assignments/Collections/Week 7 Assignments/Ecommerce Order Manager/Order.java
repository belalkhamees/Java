import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orderId;
    private String customerName;
    private List<CartItem> items;
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

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void addItem(Product product, int quantity) {

        for (CartItem item : items) {

            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                calculateTotal();
                return;
            }
        }

        items.add(new CartItem(product, quantity));

        calculateTotal();
    }

    public boolean removeItem(int productId) {

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).getProduct().getId() == productId) {

                items.remove(i);

                calculateTotal();

                return true;
            }
        }

        return false;
    }

    public void calculateTotal() {

        total = 0;

        for (CartItem item : items) {
            total += item.calculateSubtotal();
        }
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void displayOrder() {

        System.out.println("\nOrder ID: " + orderId);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Status: " + status);

        System.out.println("Items:");

        if (items.isEmpty()) {

            System.out.println("No items in this order.");

        } else {

            for (CartItem item : items) {
                System.out.println(item);
                System.out.println("--------------------");
            }
        }

        System.out.println("Total: " + total);
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + "\n" +
                "Customer Name: " + customerName + "\n" +
                "Status: " + status + "\n" +
                "Total: " + total;
    }
}