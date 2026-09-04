import java.util.Scanner;
import java.util.Optional;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Restaurant restaurant = new Restaurant();

    public static void main(String[] args) {

        int choice;

        do {

            displayMenu();

            choice = Validation.readMenuChoice(sc);

            switch (choice) {

                case 1:
                    addMenuItem();
                    break;

                case 2:
                    removeMenuItem();
                    break;

                case 3:
                    restaurant.displayMenu();
                    break;

                case 4:
                    searchMenuItem();
                    break;

                case 5:
                    createOrder();
                    break;

                case 6:
                    addItemToOrder();
                    break;

                case 7:
                    removeItemFromOrder();
                    break;

                case 8:
                    displayOrder();
                    break;

                case 9:
                    addOrderToKitchen();
                    break;

                case 10:
                    processNextOrder();
                    break;

                case 11:
                    searchOrder();
                    break;

                case 12:
                    checkOrderStatus();
                    break;

                case 13:
                    restaurant.displayCompletedOrders();
                    break;

                case 14:
                    cancelOrder();
                    break;

                case 15:
                    System.out.println("\nThank you for using Restaurant Order Manager.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 15);

        sc.close();
    }

    private static void displayMenu() {

        System.out.println("\n=================================");
        System.out.println("     RESTAURANT ORDER MANAGER");
        System.out.println("=================================");
        System.out.println("1. Add Menu Item");
        System.out.println("2. Remove Menu Item");
        System.out.println("3. Display Menu");
        System.out.println("4. Search Menu Item");
        System.out.println("5. Create Order");
        System.out.println("6. Add Item to Order");
        System.out.println("7. Remove Item from Order");
        System.out.println("8. Display Order");
        System.out.println("9. Add Order to Kitchen Queue");
        System.out.println("10. Process Next Order");
        System.out.println("11. Search Order");
        System.out.println("12. Check Order Status");
        System.out.println("13. Display Completed Orders");
        System.out.println("14. Cancel Order");
        System.out.println("15. Exit");
        System.out.println("=================================");
    }

    private static void addMenuItem() {

        System.out.println("\n===== Add Menu Item =====");

        int id = Validation.readPositiveInt(sc, "Enter item ID: ");

        sc.nextLine();

        String name = Validation.readString(sc, "Enter item name: ");

        double price = Validation.readPositiveDouble(sc, "Enter item price: ");

        sc.nextLine();

        String category = Validation.readString(sc, "Enter item category: ");

        MenuItem item = new MenuItem(id, name, price, category);

        if (restaurant.addMenuItem(item)) {
            System.out.println("Menu item added successfully.");
        }
        else {
            System.out.println("Menu item ID already exists.");
        }
    }

    private static void removeMenuItem() {

        System.out.println("\n===== Remove Menu Item =====");

        int id = Validation.readPositiveInt(sc, "Enter item ID: ");

        sc.nextLine();

        if (restaurant.removeMenuItem(id)) {
            System.out.println("Menu item removed successfully.");
        }
        else {
            System.out.println("Menu item not found.");
        }
    }

    private static void searchMenuItem() {

        System.out.println("\n===== Search Menu Item =====");

        int id = Validation.readPositiveInt(sc, "Enter item ID: ");

        sc.nextLine();

        Optional<MenuItem> item = restaurant.searchMenuItem(id);

        if (item.isPresent()) {
            System.out.println(item.get());
        }
        else {
            System.out.println("Menu item not found.");
        }

    }

    private static void createOrder() {

        System.out.println("\n===== Create Order =====");

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        sc.nextLine();

        String customerName = Validation.readString(sc, "Enter customer name: ");

        Order order = new Order(orderId, customerName);

        if (restaurant.createOrder(order)) {

            System.out.println("Order created successfully.");

            System.out.println(
                    "Order status: " + order.getStatus()
            );

        }
        else {
            System.out.println("Order ID already exists.");
        }
    }

    private static void addItemToOrder() {

        System.out.println("\n===== Add Item to Order =====");

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        int menuItemId = Validation.readPositiveInt(sc, "Enter menu item ID: ");

        int quantity = Validation.readPositiveInt(sc, "Enter quantity: ");

        sc.nextLine();

        Optional<Order> orderOptional =
                restaurant.searchOrder(orderId);

        if (orderOptional.isEmpty()) {
            System.out.println(
                    "Order not found."
            );
            return;
        }

        Order order = orderOptional.get();

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED) {

            System.out.println("Completed or cancelled orders cannot be modified.");

            return;
        }

        Optional<MenuItem> itemOptional = restaurant.searchMenuItem(menuItemId);

        if (itemOptional.isEmpty()) {
            System.out.println("Menu item not found.");
            return;
        }

        if (restaurant.addItemToOrder(orderId, menuItemId, quantity)) {

            System.out.println(
                    "Item added to order successfully."
            );

        }
        else {

            System.out.println(
                    "Could not add item to order."
            );
        }
    }

    private static void removeItemFromOrder() {

        System.out.println(
                "\n===== Remove Item from Order ====="
        );

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        int menuItemId = Validation.readPositiveInt(sc, "Enter menu item ID: ");

        sc.nextLine();

        Optional<Order> orderOptional =
                restaurant.searchOrder(orderId);

        if (orderOptional.isEmpty()) {
            System.out.println("Order not found.");
            return;
        }

        Order order = orderOptional.get();

        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED) {

            System.out.println("Completed or cancelled orders cannot be modified.");

            return;
        }

        if (restaurant.removeItemFromOrder(orderId, menuItemId)) {

            System.out.println("Item removed from order successfully.");

        }
        else {
            System.out.println("Item not found in order.");
        }
    }

    private static void displayOrder() {

        System.out.println("\n===== Display Order =====");

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        sc.nextLine();

        Optional<Order> orderOptional =
                restaurant.searchOrder(orderId);

        if (orderOptional.isPresent()) {
            orderOptional.get().displayOrder();
        }
        else {
            System.out.println(
                    "Order not found."
            );
        }
    }

    private static void addOrderToKitchen() {

        System.out.println("\n===== Add Order to Kitchen =====");

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        sc.nextLine();

        Optional<Order> orderOptional = restaurant.searchOrder(orderId);

        if (orderOptional.isEmpty()) {

            System.out.println("Order not found.");

            return;
        }

        Order order = orderOptional.get();

        if (order.getStatus() == OrderStatus.IN_KITCHEN) {

            System.out.println("Order is already in the kitchen queue.");

            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {

            System.out.println(
                    "Order is already completed."
            );

            return;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {

            System.out.println("Cancelled order cannot be added to the kitchen.");

            return;
        }

        if (restaurant.addOrderToKitchen(orderId)) {

            System.out.println("Order added to kitchen queue.");

            System.out.println("Order status: " + order.getStatus());

        } else {

            System.out.println(
                    "Order could not be added to kitchen."
            );
        }
    }

    private static void processNextOrder() {

        System.out.println("\n===== Process Next Order =====");

        if (restaurant.processNextOrder()) {

            System.out.println("Next order processed successfully.");

        } else {

            System.out.println("Kitchen queue is empty.");
        }
    }

    private static void searchOrder() {

        System.out.println("\n===== Search Order =====");

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        sc.nextLine();

        Optional<Order> orderOptional = restaurant.searchOrder(orderId);

        if (orderOptional.isPresent()) {
            orderOptional.get().displayOrder();
        }
        else {
            System.out.println(
                    "Order not found."
            );
        }
    }

    private static void checkOrderStatus() {

        System.out.println("\n===== Check Order Status =====");

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        sc.nextLine();

        Optional<Order> orderOptional =
                restaurant.searchOrder(orderId);

        if (orderOptional.isPresent()) {

            System.out.println("Order Status: " + orderOptional.get().getStatus());
        }
        else {

            System.out.println("Order not found.");
        }
    }

    private static void cancelOrder() {

        System.out.println("\n===== Cancel Order =====");

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        sc.nextLine();

        Optional<Order> orderOptional =
                restaurant.searchOrder(orderId);

        if (orderOptional.isEmpty()) {

            System.out.println("Order not found.");

            return;
        }

        Order order = orderOptional.get();

        if (order.getStatus() == OrderStatus.IN_KITCHEN) {

            System.out.println("Order is already in the kitchen and cannot be cancelled.");

            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {

            System.out.println("Completed order cannot be cancelled.");

            return;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {

            System.out.println("Order is already cancelled.");

            return;
        }

        if (restaurant.cancelOrder(orderId)) {

            System.out.println("Order cancelled successfully.");

            System.out.println("Order status: " + order.getStatus());

        } else {

            System.out.println("Order could not be cancelled.");
        }
    }
}
