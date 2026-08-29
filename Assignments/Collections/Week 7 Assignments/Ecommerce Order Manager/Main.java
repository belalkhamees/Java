import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Store store = new Store();

        int choice;

        do {

            displayMenu();

            choice = Validation.readMenuChoice(sc);

            switch (choice) {

                case 1:
                    addProduct(sc, store);
                    break;

                case 2:
                    removeProduct(sc, store);
                    break;

                case 3:
                    store.displayAllProducts();
                    break;

                case 4:
                    searchProduct(sc, store);
                    break;

                case 5:
                    store.displayCategories();
                    break;

                case 6:
                    store.displayProductsByPrice();
                    break;

                case 7:
                    createOrder(sc, store);
                    break;

                case 8:
                    addItemToOrder(sc, store);
                    break;

                case 9:
                    removeItemFromOrder(sc, store);
                    break;

                case 10:
                    displayOrder(sc, store);
                    break;

                case 11:
                    addOrderToShipping(sc, store);
                    break;

                case 12:
                    shipNextOrder(store);
                    break;

                case 13:
                    cancelOrder(sc, store);
                    break;

                case 14:
                    searchOrder(sc, store);
                    break;

                case 15:
                    addReview(sc, store);
                    break;

                case 16:
                    showReviews(sc, store);
                    break;

                case 17:
                    store.removeOutOfStockProducts();
                    break;

                case 18:
                    store.displayOrdersByTotal();
                    break;

                case 19:

                    System.out.println("\nThank you for using E-Commerce Store Management System.");
                    break;
            }
        }while (choice != 19);

        sc.close();
    }

    public static void displayMenu() {

        System.out.println("\n========== E-Commerce Order & Inventory Manager ==========");

        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Display All Products");
        System.out.println("4. Search Product by ID");
        System.out.println("5. Show All Categories");
        System.out.println("6. Display Products Ordered by Price");
        System.out.println("7. Create Order");
        System.out.println("8. Add Item to Order");
        System.out.println("9. Remove Item from Order");
        System.out.println("10. Display Order");
        System.out.println("11. Add Order to the Shipping List");
        System.out.println("12. Ship Next Order");
        System.out.println("13. Cancel Order");
        System.out.println("14. Search Order by ID");
        System.out.println("15. Add Review to a Product");
        System.out.println("16. Show All Reviews for a Product");
        System.out.println("17. Remove Out-of-Stock Products");
        System.out.println("18. Display Orders Ordered by Total");
        System.out.println("19. Exit");

        System.out.println("==========================================================");
    }

    private static void addProduct(Scanner sc, Store store) {

        int id = Validation.readPositiveInt(sc, "Enter product ID: ");

        String name = Validation.readString(sc, "Enter product name: ");

        double price = Validation.readPositiveDouble(sc, "Enter product price: ");

        String category = Validation.readString(sc, "Enter product category: ");

        int stockQuantity = Validation.readNonNegativeInt(sc, "Enter stock quantity: ");

        Product product = new Product(id, name, price, category, stockQuantity);

        if (store.addProduct(product)) {

            System.out.println("Product added successfully.");

        } else {

            System.out.println("Product ID already exists.");
        }
    }

    private static void removeProduct(Scanner sc, Store store) {

        int id = Validation.readPositiveInt(sc, "Enter product ID: ");

        if (store.removeProduct(id)) {

            System.out.println("Product removed successfully.");

        } else {

            System.out.println("Product not found.");
        }
    }

    private static void searchProduct(Scanner sc, Store store) {

        int id = Validation.readPositiveInt(sc, "Enter product ID: ");

        store.displayProductById(id);
    }

    private static void createOrder(Scanner sc, Store store) {

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        String customerName = Validation.readString(sc, "Enter customer name: ");

        if (store.createOrder(orderId, customerName)) {

            System.out.println("Order created successfully.");

        } else {

            System.out.println("Order ID already exists.");
        }
    }

    private static void addItemToOrder(Scanner sc, Store store) {

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        Order order = store.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {

            System.out.println("Items cannot be added to this order.");
            return;
        }

        int productId = Validation.readPositiveInt(sc, "Enter product ID: ");

        if (store.findProductById(productId) == null) {

            System.out.println("Product not found.");
            return;
        }

        int quantity = Validation.readPositiveInt(sc, "Enter quantity: ");

        if (store.addItemToOrder(orderId, productId, quantity)) {

            System.out.println("Item added successfully.");

        } else {

            System.out.println("Could not add item.");
        }
    }

    private static void removeItemFromOrder(Scanner sc, Store store) {

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        Order order = store.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {

            System.out.println("Items cannot be removed from this order.");
            return;
        }

        int productId = Validation.readPositiveInt(sc, "Enter product ID: ");

        if (store.removeItemFromOrder(orderId, productId)) {

            System.out.println("Item removed successfully.");

        } else {

            System.out.println("Item not found in this order.");
        }
    }

    private static void displayOrder(Scanner sc, Store store) {

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        Order order = store.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");

        } else {

            order.displayOrder();
        }
    }


    private static void addOrderToShipping(Scanner sc, Store store) {

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        Order order = store.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {

            System.out.println("Only pending orders can be added to shipping.");
            return;
        }

        if (!order.hasItems()) {

            System.out.println("An order with no items cannot be placed in shipping.");
            return;
        }

        if (store.addOrderToShipping(orderId)) {

            System.out.println("Order added to shipping list.");

        } else {

            System.out.println("Order could not be added to shipping list.");
        }
    }

    private static void shipNextOrder(Store store) {

        if (store.shipNextOrder()) {

            System.out.println("Order delivered successfully.");

        } else {

            System.out.println("No order could be shipped.");
        }
    }

    private static void cancelOrder(Scanner sc, Store store) {

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        Order order = store.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");
            return;
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {

            System.out.println("Delivered orders cannot be cancelled.");
            return;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {

            System.out.println("Order is already cancelled.");
            return;
        }

        if (store.cancelOrder(orderId)) {

            System.out.println("Order cancelled successfully.");

        } else {

            System.out.println("Order could not be cancelled.");
        }
    }

    private static void searchOrder(Scanner sc, Store store) {

        int orderId = Validation.readPositiveInt(sc, "Enter order ID: ");

        Order order = store.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");

        } else {

            System.out.println(order);
        }
    }

    private static void addReview(Scanner sc, Store store) {

        int productId = Validation.readPositiveInt(sc, "Enter product ID: ");

        if (store.findProductById(productId) == null) {

            System.out.println("Product not found.");
            return;
        }

        String customerName = Validation.readString(sc, "Enter customer name: ");

        String comment = Validation.readString(sc, "Enter comment: ");

        if (store.addReview(productId, customerName, comment)) {

            System.out.println("Review added successfully.");

        } else {

            System.out.println("Review could not be added.");
        }
    }

    private static void showReviews(Scanner sc, Store store) {

        int productId = Validation.readPositiveInt(sc, "Enter product ID: ");

        if (store.findProductById(productId) == null) {

            System.out.println("Product not found.");
            return;
        }

        store.displayReviewsForProduct(productId);
    }
}