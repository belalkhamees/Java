
package org.route.console;

import org.route.cart.CartView;
import org.route.cart.LineView;
import org.route.cart.ShoppingCart;
import org.route.checkout.CheckoutService;
import org.route.common.Money;
import org.route.common.StoreException;
import org.route.model.Customer;
import org.route.model.Governorate;
import org.route.model.Order;
import org.route.model.Product;
import org.route.service.ProductService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StoreConsole {

    private final AnnotationConfigApplicationContext context;
    private final ProductService productService;
    private final CheckoutService checkoutService;

    private final Map<Integer, ShoppingCart> carts = new HashMap<>();
    private int nextCartNumber = 1;

    public StoreConsole(AnnotationConfigApplicationContext context) {
        this.context = context;
        this.productService = context.getBean(ProductService.class);
        this.checkoutService = context.getBean(CheckoutService.class);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n========== NILEMART ==========");
            System.out.println("1. Show products");
            System.out.println("2. Create a cart");
            System.out.println("3. Add a product to a cart");
            System.out.println("4. Show a cart");
            System.out.println("5. Check out a cart");
            System.out.println("0. Exit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> showProducts();
                    case "2" -> createCart(scanner);
                    case "3" -> addProduct(scanner);
                    case "4" -> showCart(scanner);
                    case "5" -> checkout(scanner);
                    case "0" -> {
                        System.out.println("\nThank you for shopping at NileMart!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (StoreException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showProducts() {
        System.out.println("\n========== PRODUCTS ==========");

        for (Product product : productService.findAll()) {
            System.out.println(
                    product.getSku() + " | "
                            + product.getName() + " | "
                            + product.getCategory() + " | "
                            + Money.format(product.getPrice()) + " EGP | "
                            + "Stock: " + product.getStockQuantity()
            );
        }
    }

    private void createCart(Scanner scanner) {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter customer phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter customer email (optional): ");
        String email = scanner.nextLine();

        if (email.isBlank()) {
            email = null;
        }

        Customer customer = new Customer(name, phone, email);

        ShoppingCart cart = context.getBean(ShoppingCart.class);

        int number = nextCartNumber++;

        cart.open(number, customer);

        carts.put(number, cart);

        System.out.println("Cart created successfully. Cart number: " + number);
    }

    private void addProduct(Scanner scanner) {
        int cartNumber = readInt(scanner, "Enter cart number: ");

        ShoppingCart cart = carts.get(cartNumber);

        if (cart == null) {
            throw new StoreException("Cart " + cartNumber + " was not found.");
        }

        System.out.print("Enter product SKU: ");
        String sku = scanner.nextLine();

        int quantity = readInt(scanner, "Enter quantity: ");

        cart.addItem(sku, quantity);

        System.out.println("Product added to cart successfully.");
    }

    private void showCart(Scanner scanner) {
        int cartNumber = readInt(scanner, "Enter cart number: ");

        ShoppingCart cart = carts.get(cartNumber);

        if (cart == null) {
            throw new StoreException("Cart " + cartNumber + " was not found.");
        }

        CartView view = cart.view();

        System.out.println("\n========== CART " + view.number() + " ==========");
        System.out.println("Customer: " + view.customer().fullName());

        if (view.empty()) {
            System.out.println("Cart is empty.");
            return;
        }

        for (LineView line : view.lines()) {
            System.out.println(
                    line.sku() + " | "
                            + line.name() + " | "
                            + "Quantity: " + line.quantity() + " | "
                            + Money.format(line.linePrice()) + " EGP"
            );
        }

        System.out.println("------------------------------");
        System.out.println("Total items: " + view.itemCount());
        System.out.println("Subtotal: " + Money.format(view.subtotal()) + " EGP");
    }

    private void checkout(Scanner scanner) {
        int cartNumber = readInt(scanner, "Enter cart number: ");

        ShoppingCart cart = carts.get(cartNumber);

        if (cart == null) {
            throw new StoreException("Cart " + cartNumber + " was not found.");
        }

        System.out.println("\nSelect governorate:");

        Governorate[] governorates = Governorate.values();

        for (int i = 0; i < governorates.length; i++) {
            System.out.println((i + 1) + ". " + governorates[i].getLabel());
        }

        int choice = readInt(scanner, "Choose governorate: ");

        if (choice < 1 || choice > governorates.length) {
            throw new StoreException("Invalid governorate selection.");
        }

        Governorate governorate = governorates[choice - 1];

        Order order = checkoutService.placeOrder(cart, governorate);

        System.out.println("\n========== ORDER CONFIRMED ==========");
        System.out.println("Order number: " + order.orderNumber());
        System.out.println("Customer: " + order.customer().fullName());
        System.out.println("Governorate: " + order.governorate().getLabel());
        System.out.println("Subtotal: " + Money.format(order.subtotal()) + " EGP");
        System.out.println("VAT: " + Money.format(order.vat()) + " EGP");
        System.out.println("Shipping: " + Money.format(order.shippingFee()) + " EGP");
        System.out.println("Total: " + Money.format(order.total()) + " EGP");
    }

    private int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);

        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new StoreException("Please enter a valid number.");
        }
    }
}
