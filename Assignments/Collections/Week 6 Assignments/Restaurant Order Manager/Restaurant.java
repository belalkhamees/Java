import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Restaurant {

    private ArrayList<MenuItem> menu;
    private LinkedList<Order> kitchenQueue;
    private HashMap<Integer, Order> orders;
    private LinkedHashMap<Integer, Order> completedOrders;

    public Restaurant() {
        menu = new ArrayList<>();
        kitchenQueue = new LinkedList<>();
        orders = new HashMap<>();
        completedOrders = new LinkedHashMap<>();
    }

    // MENU

    public boolean addMenuItem(MenuItem item) {

        if (searchMenuItem(item.getId()) != null) {
            return false;
        }

        menu.add(item);
        return true;
    }

    public boolean removeMenuItem(int id) {

        for (int i = 0; i < menu.size(); i++) {

            if (menu.get(i).getId() == id) {
                menu.remove(i);
                return true;
            }
        }

        return false;
    }

    public void displayMenu() {

        if (menu.isEmpty()) {
            System.out.println("Menu is empty.");
            return;
        }

        System.out.println("\n========== MENU ==========");

        for (MenuItem item : menu) {
            System.out.println(item);
            System.out.println("---------------------------");
        }
    }

    public MenuItem searchMenuItem(int id) {

        for (MenuItem item : menu) {

            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    // ORDERS

    public boolean createOrder(Order order) {

        if (orders.containsKey(order.getOrderId())) {
            return false;
        }

        orders.put(order.getOrderId(), order);

        return true;
    }

    public Order searchOrder(int orderId) {
        return orders.get(orderId);
    }

    public boolean addItemToOrder(int orderId, int menuItemId, int quantity) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        MenuItem menuItem = searchMenuItem(menuItemId);

        if (menuItem == null) {
            return false;
        }

        OrderItem orderItem = new OrderItem(menuItem, quantity);

        order.addItem(orderItem);

        return true;
    }

    public boolean removeItemFromOrder(int orderId, int menuItemId) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        return order.removeItem(menuItemId);
    }

    // KITCHEN

    public boolean addOrderToKitchen(int orderId) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        kitchenQueue.add(order);

        order.updateStatus(OrderStatus.IN_KITCHEN);

        return true;
    }

    public boolean processNextOrder() {

        if (kitchenQueue.isEmpty()) {
            return false;
        }

        Order order = kitchenQueue.removeFirst();

        order.updateStatus(OrderStatus.COMPLETED);

        completedOrders.put(order.getOrderId(), order);

        return true;
    }

    // CANCEL ORDER

    public boolean cancelOrder(int orderId) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.IN_KITCHEN) {
            return false;
        }

        order.updateStatus(OrderStatus.CANCELLED);

        return true;
    }

    // DISPLAY

    public void displayCompletedOrders() {

        if (completedOrders.isEmpty()) {
            System.out.println("No completed orders.");
            return;
        }

        System.out.println("\n===== COMPLETED ORDERS =====");

        for (Order order : completedOrders.values()) {
            order.displayOrder();
        }
    }
}
