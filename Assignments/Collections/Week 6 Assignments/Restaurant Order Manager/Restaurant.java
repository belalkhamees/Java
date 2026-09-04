import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Predicate;

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

        if (searchMenuItem(item.getId()).isPresent()) {
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

    public Optional<MenuItem> searchMenuItem(int id) {

        Predicate<MenuItem> byId = item -> item.getId() == id;

        return menu.stream().filter(byId).findFirst();
    }

    // ORDERS

    public boolean createOrder(Order order) {

        if (orders.containsKey(order.getOrderId())) {
            return false;
        }

        orders.put(order.getOrderId(), order);

        return true;
    }

    public Optional<Order> searchOrder(int orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }
    public boolean addItemToOrder(int orderId, int menuItemId, int quantity) {

        Optional<Order> orderOptional = searchOrder(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        Optional<MenuItem> menuItemOptional =
                searchMenuItem(menuItemId);

        if (menuItemOptional.isEmpty()) {
            return false;
        }

        MenuItem menuItem = menuItemOptional.get();

        OrderItem orderItem = new OrderItem(menuItem, quantity);

        order.addItem(orderItem);

        return true;
    }

    public boolean removeItemFromOrder(int orderId, int menuItemId) {

        Optional<Order> orderOptional = searchOrder(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        return order.removeItem(menuItemId);
    }

    // KITCHEN

    public boolean addOrderToKitchen(int orderId) {

        Optional<Order> orderOptional = searchOrder(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

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

        Optional<Order> orderOptional = searchOrder(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

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

        completedOrders.values().forEach(Order::displayOrder);
    }
}
