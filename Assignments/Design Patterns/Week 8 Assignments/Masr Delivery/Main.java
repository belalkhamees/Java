import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final DeliverySystem system =
            new DeliverySystem();

    public static void main(String[] args) {

        setupExampleData();

        while (true) {

            System.out.println();
            System.out.println("===== MASR DELIVERY =====");
            System.out.println("1. Customer");
            System.out.println("2. Restaurant");
            System.out.println("3. Rider");
            System.out.println("4. Admin");
            System.out.println("0. Exit");
            System.out.println("-------------------------");

            int choice = readInt("Choose: ");

            try {

                switch (choice) {

                    case 1:
                        customerMenu();
                        break;

                    case 2:
                        restaurantMenu();
                        break;

                    case 3:
                        riderMenu();
                        break;

                    case 4:
                        adminMenu();
                        break;

                    case 0:
                        System.out.println("\nThank You For Using Masr Delivery System.");
                        return;

                    default:
                        System.out.println(
                                "Invalid choice.");
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage());
            }
        }
    }

    /* ================= CUSTOMER MENU ================= */

    private static void customerMenu() {

        Customer customer =
                chooseCustomer();

        if (customer == null) {
            return;
        }

        while (true) {

            System.out.println();
            System.out.println("===== CUSTOMER =====");
            System.out.println("1. Browse restaurants");
            System.out.println("2. Search restaurants");
            System.out.println("3. View restaurant menu");
            System.out.println("4. Place order");
            System.out.println("5. Pay order");
            System.out.println("6. Track order");
            System.out.println("7. Cancel order");
            System.out.println("8. Order history");
            System.out.println("9. Wallet balance");
            System.out.println("0. Back");
            System.out.println("-------------------");

            int choice = readInt("Choose: ");

            try {

                switch (choice) {

                    case 1:
                        browseRestaurants();
                        break;

                    case 2:
                        searchRestaurants(customer);
                        break;

                    case 3:
                        viewRestaurantMenu();
                        break;

                    case 4:
                        placeOrder(customer);
                        break;

                    case 5:
                        payOrder(customer);
                        break;

                    case 6:
                        trackOrder();
                        break;

                    case 7:
                        cancelOrder(customer);
                        break;

                    case 8:
                        showOrderHistory(customer);
                        break;

                    case 9:
                        System.out.println(
                                "Wallet: "
                                        + customer.getWallet());
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println(
                                "Invalid choice.");
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage());
            }
        }
    }

    private static void browseRestaurants() {

        List<Restaurant> restaurants =
                system.searchRestaurants(
                        r -> r.isOpen());

        if (restaurants.isEmpty()) {

            System.out.println(
                    "No open restaurants found.");

            return;
        }

        for (Restaurant restaurant :
                restaurants) {

            System.out.println(
                    restaurant.getId()
                            + ". "
                            + restaurant.getName()
                            + " | Rating: "
                            + restaurant.getRating()
                            + " | District: "
                            + restaurant.getDistrict());
        }
    }

    private static void searchRestaurants(
            Customer customer) {

        System.out.print("Search: ");
        String text = scanner.nextLine();

        List<Restaurant> restaurants =
                system.searchRestaurants(
                        customer,
                        text);

        if (restaurants.isEmpty()) {

            System.out.println(
                    "Nothing found.");

            return;
        }

        for (Restaurant restaurant :
                restaurants) {

            System.out.println(
                    restaurant.getId()
                            + ". "
                            + restaurant.getName()
                            + " | Rating: "
                            + restaurant.getRating());
        }
    }

    private static void viewRestaurantMenu() {

        Restaurant restaurant =
                chooseRestaurant();

        if (restaurant == null) {
            return;
        }

        System.out.println();
        System.out.println(
                "===== "
                        + restaurant.getName()
                        + " MENU =====");

        List<MenuItem> menu =
                restaurant.getMenu();

        if (menu.isEmpty()) {

            System.out.println(
                    "No menu items.");

            return;
        }

        for (MenuItem item : menu) {

            System.out.println(
                    item.getId()
                            + " | "
                            + item.getName()
                            + " | "
                            + item.getCategory()
                            + " | "
                            + getItemPrice(item)
                            + " | "
                            + (item.isAvailable()
                            ? "Available"
                            : "Unavailable"));
        }
    }

    private static void placeOrder(
            Customer customer)
            throws Exception {

        Restaurant restaurant =
                chooseRestaurant();

        if (restaurant == null) {
            return;
        }

        restaurant.checkOpen();

        Address address =
                chooseAddress(customer);

        if (address == null) {
            System.out.println(
                    "Customer has no address.");

            return;
        }

        OrderBuilder builder =
                new OrderBuilder()
                        .setCustomer(customer)
                        .setRestaurant(restaurant)
                        .setDeliveryAddress(address);

        while (true) {

            String availableItemIds =
                    restaurant.getMenu()
                            .stream()
                            .map(MenuItem::getId)
                            .collect(Collectors.joining(", "));

            System.out.print(
                    "Enter item ID ("
                            + availableItemIds
                            + ") or 0 to finish: ");

            String itemId =
                    scanner.nextLine().trim().toUpperCase();

            if (itemId.equals("0")) {
                break;
            }

            MenuItem item =
                    restaurant.getItem(itemId);

            if (item == null) {

                System.out.println(
                        "Item not found.");

                continue;
            }

            if (!item.isAvailable()) {

                System.out.println(
                        "Item is unavailable.");

                continue;
            }

            Number quantity;

            if (item instanceof WeightedItem) {

                quantity =
                        readBigDecimal(
                                "Weight in kg: ");

            } else {

                quantity =
                        readInt(
                                "Quantity: ");
            }

            try {

                OrderLine line =
                        new OrderLine(
                                item,
                                quantity);

                builder.addLine(line);

            } catch (IllegalArgumentException e) {

                System.out.println(
                        e.getMessage());
            }
        }

        System.out.print(
                "Promotion code (press Enter to skip): ");

        String code =
                scanner.nextLine();

        if (!code.isBlank()) {

            Promotion promotion =
                    system.getPromotion(code);

            if (promotion == null) {

                System.out.println(
                        "Promotion not found.");

                return;
            }

            builder.setPromotion(promotion);
        }

        System.out.print(
                "Delivery notes (optional): ");

        String notes =
                scanner.nextLine();

        if (!notes.isBlank()) {
            builder.setDeliveryNotes(notes);
        }

        Order order =
                system.placeOrder(builder);

        System.out.println();
        System.out.println(
                "Order created successfully.");

        printOrderDetails(order);
    }

    private static void payOrder(
            Customer customer)
            throws InsufficientWalletException {

        int orderId =
                readInt("Order ID: ");

        Order order =
                system.getOrder(orderId);

        if (!order.getCustomer()
                .equals(customer)) {

            System.out.println(
                    "This order does not belong to you.");

            return;
        }

        system.payOrder(orderId);

        System.out.println(
                "Payment successful.");

        System.out.println(
                "Remaining wallet: "
                        + customer.getWallet());
    }

    private static void trackOrder() {

        int orderId =
                readInt("Order ID: ");

        Order order =
                system.getOrder(orderId);

        System.out.println(
                "Order status: "
                        + order.getStatus());

        System.out.println(
                "Placed at: "
                        + order.getPlacedAt());

        System.out.println(
                "Elapsed time: "
                        + java.time.Duration
                        .between(
                                order.getPlacedAt(),
                                java.time.LocalDateTime.now())
                        .toMinutes()
                        + " minutes");
    }

    private static void cancelOrder(
            Customer customer)
            throws IllegalOrderTransitionException {

        int orderId =
                readInt("Order ID: ");

        Order order =
                system.getOrder(orderId);

        if (!order.getCustomer()
                .equals(customer)) {

            System.out.println(
                    "This order does not belong to you.");

            return;
        }

        system.cancelOrder(orderId);

        System.out.println(
                "Order cancelled.");

        System.out.println(
                "Wallet: "
                        + customer.getWallet());
    }

    private static void showOrderHistory(
            Customer customer) {

        List<Order> orders =
                system.customerOrderHistory(
                        customer);

        if (orders.isEmpty()) {

            System.out.println(
                    "No orders found.");

            return;
        }

        for (Order order : orders) {

            System.out.println(
                    "Order ID: "
                            + order.getId()
                            + " | Status: "
                            + order.getStatus()
                            + " | Total: "
                            + order.getTotal());
        }

        System.out.println(
                "Lifetime total spent: "
                        + system.customerTotalSpent(
                        customer));
    }

    /* ================= RESTAURANT MENU ================= */

    private static void restaurantMenu() {

        Restaurant restaurant =
                chooseRestaurant();

        if (restaurant == null) {
            return;
        }

        while (true) {

            System.out.println();
            System.out.println(
                    "===== RESTAURANT =====");
            System.out.println(
                    "Restaurant: "
                            + restaurant.getName());
            System.out.println(
                    "Open: "
                            + restaurant.isOpen());

            System.out.println("1. Toggle open/closed");
            System.out.println("2. View menu");
            System.out.println("3. Add menu item");
            System.out.println("4. Remove menu item");
            System.out.println("5. Accept order");
            System.out.println("6. Reject order");
            System.out.println("7. Mark preparing");
            System.out.println("8. Mark ready");
            System.out.println(
                    "9. Toggle item availability");
            System.out.println("10. Adjust stock");
            System.out.println(
                    "11. View today's orders");
            System.out.println(
                    "12. View today's revenue");
            System.out.println("0. Back");
            System.out.println("-------------------");

            int choice =
                    readInt("Choose: ");

            try {

                switch (choice) {

                    case 1:
                        restaurant.toggleOpen();

                        System.out.println(
                                "Restaurant is now "
                                        + (restaurant.isOpen()
                                        ? "open."
                                        : "closed."));
                        break;

                    case 2:
                        printMenu(restaurant);
                        break;

                    case 3:
                        addMenuItem(restaurant);
                        break;

                    case 4:
                        removeMenuItem(restaurant);
                        break;

                    case 5:
                        acceptRestaurantOrder(
                                restaurant);
                        break;

                    case 6:
                        rejectRestaurantOrder(
                                restaurant);
                        break;

                    case 7:
                        changeRestaurantOrderStatus(
                                restaurant,
                                OrderStatus.PREPARING);
                        break;

                    case 8:
                        changeRestaurantOrderStatus(
                                restaurant,
                                OrderStatus.READY);
                        break;

                    case 9:
                        toggleItemAvailability(
                                restaurant);
                        break;

                    case 10:
                        adjustStock(restaurant);
                        break;

                    case 11:
                        showTodaysOrders(
                                restaurant);
                        break;

                    case 12:
                        showTodaysRevenue(
                                restaurant);
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println(
                                "Invalid choice.");
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu(
            Restaurant restaurant) {

        List<MenuItem> menu =
                restaurant.getMenu();

        if (menu.isEmpty()) {

            System.out.println(
                    "No menu items.");

            return;
        }

        for (MenuItem item : menu) {

            System.out.println(
                    item.getId()
                            + " | "
                            + item.getName()
                            + " | "
                            + item.getCategory()
                            + " | Price: "
                            + getItemPrice(item)
                            + " | "
                            + (item.isAvailable()
                            ? "Available"
                            : "Unavailable"));
        }
    }

    private static void addMenuItem(
            Restaurant restaurant) {

        System.out.println(
                "1. Standard");
        System.out.println(
                "2. Weighted");
        System.out.println(
                "3. Combo");

        int type =
                readInt("Type: ");

        System.out.print("ID: ");
        String id =
                scanner.nextLine();

        System.out.print("Name: ");
        String name =
                scanner.nextLine();

        System.out.print("Category: ");
        String category =
                scanner.nextLine();

        int preparationTime =
                readInt(
                        "Preparation time: ");

        MenuItem item;

        if (type == 1) {

            BigDecimal price =
                    readBigDecimal("Price: ");

            int stock =
                    readInt("Stock: ");

            item =
                    MenuItemFactory.create(
                            "STANDARD",
                            id,
                            name,
                            category,
                            price,
                            preparationTime,
                            stock,
                            null,
                            0);

        } else if (type == 2) {

            BigDecimal price =
                    readBigDecimal(
                            "Price per kg: ");

            BigDecimal stock =
                    readBigDecimal(
                            "Stock in kg: ");

            item =
                    MenuItemFactory.create(
                            "WEIGHTED",
                            id,
                            name,
                            category,
                            price,
                            preparationTime,
                            stock,
                            null,
                            0);

        } else if (type == 3) {

            System.out.println(
                    "Enter component item IDs.");

            List<MenuItem> components =
                    new java.util.ArrayList<>();

            while (true) {

                System.out.print(
                        "Component ID "
                                + "(0 to finish): ");

                String componentId =
                        scanner.nextLine();

                if (componentId.equals("0")) {
                    break;
                }

                MenuItem component =
                        restaurant.getItem(
                                componentId);

                if (component == null) {

                    System.out.println(
                            "Item not found.");

                    continue;
                }

                components.add(component);
            }

            double discount =
                    readDouble(
                            "Discount percentage: ");

            item =
                    MenuItemFactory.create(
                            "COMBO",
                            id,
                            name,
                            category,
                            BigDecimal.ZERO,
                            preparationTime,
                            null,
                            components,
                            discount);

        } else {

            System.out.println(
                    "Invalid item type.");

            return;
        }

        system.addMenuItem(
                restaurant.getId(),
                item);

        System.out.println(
                "Menu item added.");
    }

    private static void removeMenuItem(
            Restaurant restaurant) {

        System.out.print("Item ID: ");
        String itemId =
                scanner.nextLine();

        system.removeMenuItem(
                restaurant.getId(),
                itemId);

        System.out.println(
                "Menu item removed.");
    }

    private static void acceptRestaurantOrder(
            Restaurant restaurant)
            throws IllegalOrderTransitionException {

        int orderId =
                choosePendingOrder(
                        restaurant);

        if (orderId == -1) {
            return;
        }

        system.acceptOrder(orderId);

        System.out.println(
                "Order accepted.");
    }

    private static void rejectRestaurantOrder(
            Restaurant restaurant)
            throws IllegalOrderTransitionException {

        int orderId =
                choosePendingOrder(
                        restaurant);

        if (orderId == -1) {
            return;
        }

        system.rejectOrder(orderId);

        System.out.println(
                "Order rejected.");
    }

    private static void changeRestaurantOrderStatus(
            Restaurant restaurant,
            OrderStatus status)
            throws IllegalOrderTransitionException {

        int orderId =
                chooseRestaurantOrder(
                        restaurant);

        if (orderId == -1) {
            return;
        }

        system.changeOrderStatus(
                orderId,
                status);

        System.out.println(
                "Order status changed to "
                        + status);
    }

    private static void toggleItemAvailability(
            Restaurant restaurant) {

        System.out.print("Item ID: ");
        String itemId =
                scanner.nextLine();

        system.toggleItemAvailability(
                restaurant.getId(),
                itemId);

        System.out.println(
                "Item availability changed.");
    }

    private static void adjustStock(
            Restaurant restaurant) {

        System.out.print("Item ID: ");
        String itemId =
                scanner.nextLine();

        MenuItem item =
                restaurant.getItem(itemId);

        if (item == null) {

            System.out.println(
                    "Item not found.");

            return;
        }

        Number quantity;

        if (item instanceof WeightedItem) {

            quantity =
                    readBigDecimal(
                            "Quantity to add: ");

        } else {

            quantity =
                    readInt(
                            "Quantity to add: ");
        }

        system.addStock(
                restaurant.getId(),
                itemId,
                quantity);

        System.out.println(
                "Stock updated.");
    }

    private static void showTodaysOrders(
            Restaurant restaurant) {

        var orders =
                system.getTodayOrders(
                        restaurant.getId());

        if (orders.isEmpty()) {

            System.out.println(
                    "No orders today.");

            return;
        }

        for (Order order : orders) {

            System.out.println(
                    "Order ID: "
                            + order.getId()
                            + " | Status: "
                            + order.getStatus()
                            + " | Total: "
                            + order.getTotal());
        }
    }

    private static void showTodaysRevenue(
            Restaurant restaurant) {

        BigDecimal revenue =
                system.restaurantTodayRevenue(
                        restaurant.getId());

        System.out.println(
                "Today's revenue: "
                        + revenue);
    }

    /* ================= RIDER MENU ================= */

    private static void riderMenu() {

        Rider rider =
                chooseRider();

        if (rider == null) {
            return;
        }

        while (true) {

            System.out.println();
            System.out.println("===== RIDER =====");
            System.out.println(
                    "Rider: " + rider.getName());
            System.out.println(
                    "Status: " + rider.getStatus());

            System.out.println("1. Go on duty");
            System.out.println("2. Go off duty");
            System.out.println(
                    "3. View assigned order");
            System.out.println("4. Mark picked up");
            System.out.println("5. Mark delivered");
            System.out.println("6. Delivery stats");
            System.out.println("0. Back");
            System.out.println("-------------------");

            int choice =
                    readInt("Choose: ");

            try {

                switch (choice) {

                    case 1:
                        system.riderGoOnDuty(
                                rider.getId());

                        System.out.println(
                                "Rider is now on duty.");
                        break;

                    case 2:
                        system.riderGoOffDuty(
                                rider.getId());

                        System.out.println(
                                "Rider is now off duty.");
                        break;

                    case 3:
                        showAssignedOrder(rider);
                        break;

                    case 4:
                        markPickedUp(rider);
                        break;

                    case 5:
                        markDelivered(rider);
                        break;

                    case 6:
                        showRiderStats(rider);
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println(
                                "Invalid choice.");
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage());
            }
        }
    }

    private static void showAssignedOrder(
            Rider rider) {

        Order order =
                rider.getCurrentOrder();

        if (order == null) {

            System.out.println(
                    "No assigned order.");

            return;
        }

        printOrderDetails(order);
    }

    private static void markPickedUp(
            Rider rider)
            throws IllegalOrderTransitionException {

        Order order =
                rider.getCurrentOrder();

        if (order == null) {

            System.out.println(
                    "No assigned order.");

            return;
        }

        system.markPickedUp(
                order.getId());

        System.out.println(
                "Order picked up.");
    }

    private static void markDelivered(
            Rider rider)
            throws IllegalOrderTransitionException {

        Order order =
                rider.getCurrentOrder();

        if (order == null) {

            System.out.println(
                    "No assigned order.");

            return;
        }

        system.markDelivered(
                order.getId());

        System.out.println(
                "Order delivered.");
    }

    private static void showRiderStats(
            Rider rider) {

        System.out.println(
                "Completed deliveries: "
                        + rider.getCompletedDeliveries());

        var averages =
                system.riderAverageDeliveryDuration();

        Double average =
                averages.get(rider);

        System.out.println(
                "Average delivery duration: "
                        + (average == null
                        ? 0
                        : average)
                        + " minutes");
    }

    /* ================= ADMIN MENU ================= */

    private static void adminMenu() {

        while (true) {

            System.out.println();
            System.out.println("===== ADMIN =====");
            System.out.println("1. Add customer");
            System.out.println("2. Add restaurant");
            System.out.println("3. Add rider");
            System.out.println("4. Remove restaurant");
            System.out.println("5. Assign rider");
            System.out.println("6. Orders by status");
            System.out.println("7. Peak ordering hour");
            System.out.println("8. Most ordered item");
            System.out.println("9. Customer total spent");
            System.out.println("10. Total revenue");
            System.out.println("11. Top five restaurants");
            System.out.println(
                    "12. Average order value by district");
            System.out.println(
                    "13. High rated restaurants");
            System.out.println(
                    "14. Rider statistics");
            System.out.println(
                    "15. Customers without order");
            System.out.println("0. Back");
            System.out.println("-------------------");

            int choice =
                    readInt("Choose: ");

            try {

                switch (choice) {

                    case 1:
                        addCustomer();
                        break;

                    case 2:
                        addRestaurant();
                        break;

                    case 3:
                        addRider();
                        break;

                    case 4:
                        removeRestaurant();
                        break;

                    case 5:
                        assignRider();
                        break;

                    case 6:
                        showOrdersByStatus();
                        break;

                    case 7:
                        System.out.println(
                                "Peak hour: "
                                        + system.peakOrderingHour());
                        break;

                    case 8:
                        System.out.println(
                                "Most ordered item: "
                                        + system.mostFrequentlyOrderedItem());
                        break;

                    case 9:
                        showCustomerTotalSpent();
                        break;

                    case 10:
                        showTotalRevenue();
                        break;

                    case 11:
                        showTopFiveRestaurants();
                        break;

                    case 12:
                        showAverageOrderValue();
                        break;

                    case 13:
                        showHighRatedRestaurants();
                        break;

                    case 14:
                        showAllRiderStats();
                        break;

                    case 15:
                        showCustomersWithoutOrders();
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println(
                                "Invalid choice.");
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage());
            }
        }
    }

    private static void addCustomer() {

        System.out.print("Name: ");
        String name =
                scanner.nextLine();

        System.out.print("Mobile: ");
        String mobile =
                scanner.nextLine();

        BigDecimal wallet =
                readBigDecimal(
                        "Initial wallet: ");

        Customer customer =
                system.addCustomer(
                        name,
                        mobile,
                        wallet);

        System.out.println(
                "Customer added.");
        System.out.println(
                "Customer ID: "
                        + customer.getId());

        System.out.print(
                "Add address now? (Y/N): ");

        String answer =
                scanner.nextLine();

        if (answer.equalsIgnoreCase("Y")) {

            System.out.print(
                    "District: ");

            String district =
                    scanner.nextLine();

            System.out.print(
                    "Address details: ");

            String details =
                    scanner.nextLine();

            customer.addAddress(
                    new Address(
                            district,
                            details));

            System.out.println(
                    "Address added.");
        }
    }

    private static void addRestaurant() {

        System.out.print("Name: ");
        String name =
                scanner.nextLine();

        System.out.print("District: ");
        String district =
                scanner.nextLine();

        double rating =
                readDouble("Rating: ");

        System.out.print(
                "Cuisine: ");

        String cuisine =
                scanner.nextLine();

        Restaurant restaurant =
                system.addRestaurant(
                        name,
                        district,
                        rating,
                        cuisine);

        System.out.println(
                "Restaurant added.");
        System.out.println(
                "Restaurant ID: "
                        + restaurant.getId());
    }

    private static void addRider() {

        System.out.print("Name: ");
        String name =
                scanner.nextLine();

        System.out.println(
                "1. Motorcycle");
        System.out.println(
                "2. Bicycle");
        System.out.println(
                "3. Car");

        int choice =
                readInt("Vehicle: ");

        VehicleType vehicle;

        switch (choice) {

            case 1:
                vehicle =
                        VehicleType.MOTORCYCLE;
                break;

            case 2:
                vehicle =
                        VehicleType.BICYCLE;
                break;

            case 3:
                vehicle =
                        VehicleType.CAR;
                break;

            default:
                System.out.println(
                        "Invalid vehicle.");
                return;
        }

        System.out.print(
                "District: ");

        String district =
                scanner.nextLine();

        Rider rider =
                system.addRider(
                        name,
                        vehicle,
                        district);

        System.out.println(
                "Rider added.");
        System.out.println(
                "Rider ID: "
                        + rider.getId());
    }

    private static void removeRestaurant() {

        int id =
                readInt("Restaurant ID: ");

        system.removeRestaurant(id);

        System.out.println(
                "Restaurant removed.");
    }

    private static void assignRider()
            throws BusyRiderException,
            IllegalOrderTransitionException {

        int orderId =
                readInt("Order ID: ");

        System.out.println(
                "1. Motorcycle");
        System.out.println(
                "2. Bicycle");

        int choice =
                readInt("Strategy: ");

        DispatchStrategy strategy;

        if (choice == 1) {

            strategy =
                    new MotorcycleDispatchStrategy();

        } else if (choice == 2) {

            strategy =
                    new BicycleDispatchStrategy();

        } else {

            System.out.println(
                    "Invalid strategy.");

            return;
        }

        system.assignRider(
                orderId,
                strategy);

        System.out.println(
                "Rider assigned.");
    }

    private static void showOrdersByStatus() {

        var result =
                system.ordersByStatus();

        if (result.isEmpty()) {

            System.out.println(
                    "No orders.");

            return;
        }

        result.forEach(
                (status, count) ->
                        System.out.println(
                                status + ": " + count));
    }

    private static void showCustomerTotalSpent() {

        Customer customer =
                chooseCustomer();

        if (customer == null) {
            return;
        }

        System.out.println(
                "Total spent: "
                        + system.customerTotalSpent(
                        customer));
    }

    private static void showTotalRevenue() {

        LocalDate from =
                readDate("From date (yyyy-MM-dd): ");

        LocalDate to =
                readDate("To date (yyyy-MM-dd): ");

        System.out.println(
                "Total revenue: "
                        + system.totalRevenue(
                        from,
                        to));
    }

    private static void showTopFiveRestaurants() {

        int year =
                readInt("Year: ");

        int month =
                readInt("Month: ");

        List<Restaurant> restaurants =
                system.topFiveRestaurants(
                        year,
                        month);

        if (restaurants.isEmpty()) {

            System.out.println(
                    "No restaurants found.");

            return;
        }

        for (Restaurant restaurant :
                restaurants) {

            System.out.println(
                    restaurant.getName());
        }
    }

    private static void showAverageOrderValue() {

        var result =
                system.averageOrderValueByDistrict();

        if (result.isEmpty()) {

            System.out.println(
                    "No completed orders.");

            return;
        }

        result.forEach(
                (district, average) ->
                        System.out.println(
                                district
                                        + ": "
                                        + average));
    }

    private static void showHighRatedRestaurants() {

        List<Restaurant> restaurants =
                system.highRatedRestaurants();

        if (restaurants.isEmpty()) {

            System.out.println(
                    "No restaurants found.");

            return;
        }

        for (Restaurant restaurant :
                restaurants) {

            System.out.println(
                    restaurant.getName()
                            + " | Rating: "
                            + restaurant.getRating());
        }
    }

    private static void showAllRiderStats() {

        var deliveries =
                system.riderCompletedDeliveries();

        var averages =
                system.riderAverageDeliveryDuration();

        for (Rider rider :
                system.getRiders()) {

            System.out.println(
                    rider.getName()
                            + " | Deliveries: "
                            + deliveries.get(rider)
                            + " | Average duration: "
                            + averages.getOrDefault(
                            rider,
                            0.0)
                            + " minutes");
        }
    }

    private static void showCustomersWithoutOrders() {

        List<Customer> customers =
                system.customersNotOrderedLast30Days();

        if (customers.isEmpty()) {

            System.out.println(
                    "No customers found.");

            return;
        }

        for (Customer customer :
                customers) {

            System.out.println(
                    customer.getId()
                            + " | "
                            + customer.getName());
        }
    }

    /* ================= HELPERS ================= */

    private static Customer chooseCustomer() {

        List<Customer> customers =
                system.getCustomers();

        if (customers.isEmpty()) {

            System.out.println(
                    "No customers available.");

            return null;
        }

        System.out.println(
                "===== CUSTOMERS =====");

        for (Customer customer :
                customers) {

            System.out.println(
                    customer.getId()
                            + ". "
                            + customer.getName()
                            );
        }

        int id =
                readInt("Customer ID: ");

        try {
            return system.getCustomer(id);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    e.getMessage());
            return null;
        }
    }

    private static Restaurant chooseRestaurant() {

        List<Restaurant> restaurants =
                system.getRestaurants();

        if (restaurants.isEmpty()) {

            System.out.println(
                    "No restaurants available.");

            return null;
        }

        System.out.println(
                "===== RESTAURANTS =====");

        for (Restaurant restaurant :
                restaurants) {

            System.out.println(
                    restaurant.getId()
                            + ". "
                            + restaurant.getName()
                            + " | "
                            + restaurant.getDistrict()
                            + " | Rating: "
                            + restaurant.getRating());
        }

        int id =
                readInt("Restaurant ID: ");

        try {
            return system.getRestaurant(id);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    e.getMessage());
            return null;
        }
    }

    private static Rider chooseRider() {

        List<Rider> riders =
                system.getRiders();

        if (riders.isEmpty()) {

            System.out.println(
                    "No riders available.");

            return null;
        }

        System.out.println(
                "===== RIDERS =====");

        for (Rider rider :
                riders) {

            System.out.println(
                    rider.getId()
                            + ". "
                            + rider.getName()
                            + " | "
                            + rider.getVehicleType()
                            + " | "
                            + rider.getStatus());
        }

        int id =
                readInt("Rider ID: ");

        try {
            return system.getRider(id);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    e.getMessage());
            return null;
        }
    }

    private static Address chooseAddress(
            Customer customer) {

        List<Address> addresses =
                customer.getAddresses();

        if (addresses.isEmpty()) {
            return null;
        }

        System.out.println(
                "===== ADDRESSES =====");

        for (int i = 0;
             i < addresses.size();
             i++) {

            Address address =
                    addresses.get(i);

            System.out.println(
                    (i + 1)
                            + ". "
                            + address.getDistrict()
                            + " | "
                            + address.getDetails());
        }

        int choice =
                readInt("Address: ");

        if (choice < 1
                || choice > addresses.size()) {

            System.out.println(
                    "Invalid address.");

            return null;
        }

        return addresses.get(choice - 1);
    }

    private static int choosePendingOrder(
            Restaurant restaurant) {

        List<Order> orders =
                system.getPendingOrders(
                        restaurant.getId());

        if (orders.isEmpty()) {

            System.out.println(
                    "No pending orders.");

            return -1;
        }

        for (Order order : orders) {

            System.out.println(
                    "Order ID: "
                            + order.getId()
                            + " | Customer: "
                            + order.getCustomer()
                            .getName()
                            + " | Total: "
                            + order.getTotal());
        }

        return readInt("Order ID: ");
    }

    private static int chooseRestaurantOrder(
            Restaurant restaurant) {

        List<Order> orders =
                system.getRestaurantOrders(
                        restaurant.getId());

        if (orders.isEmpty()) {

            System.out.println(
                    "No orders.");

            return -1;
        }

        for (Order order : orders) {

            System.out.println(
                    "Order ID: "
                            + order.getId()
                            + " | Status: "
                            + order.getStatus()
                            + " | Total: "
                            + order.getTotal());
        }

        return readInt("Order ID: ");
    }

    private static void printOrderDetails(Order order) {

        System.out.println();
        System.out.println("----- ORDER Receipt -----");

        System.out.println(
                "ID: " + order.getId());

        System.out.println(
                "Restaurant: "
                        + order.getRestaurant().getName());

        System.out.println(
                "Status: " + order.getStatus());

        System.out.println();
        System.out.println("--------- ITEMS --------");

        for (OrderLine line : order.getLines()) {

            System.out.println(
                    line.getItem().getName()
                            + " | Quantity: "
                            + line.getQuantity()
                            + " | Price: "
                            + line.getPrice());
        }

        System.out.println();
        System.out.println(
                "Subtotal: " + order.getSubtotal());

        System.out.println(
                "Delivery fee: "
                        + order.getDeliveryFee());

        System.out.println(
                "Service fee: "
                        + order.getServiceFee());

        System.out.println(
                "Promotion discount: "
                        + order.getPromotionDiscount());

        System.out.println(
                "Total: " + order.getTotal());

        System.out.println(
                "Paid: " + order.isPaid());
    }
    private static String getItemPrice(
            MenuItem item) {

        if (item instanceof StandardItem) {

            return ((StandardItem) item)
                    .getPrice()
                    .toString();

        } else if (item instanceof WeightedItem) {

            return ((WeightedItem) item)
                    .getPricePerKg()
                    + " / kg";

        } else {

            return item.calculatePrice(1)
                    .toString();
        }
    }

    private static void setupExampleData() {

        PlatformConfig.getInstance()
                .setDistance(
                        "Faisal",
                        "Maadi",
                        12);

        Restaurant restaurant =
                system.addRestaurant(
                        "Masr Kitchen",
                        "Faisal",
                        4.7,
                        "Egyptian");

        restaurant.addItem(
                new StandardItem(
                        "S1",
                        "Koshary",
                        "Main",
                        BigDecimal.valueOf(60),
                        15,
                        20));

        restaurant.addItem(
                new StandardItem(
                        "S2",
                        "Molokhia",
                        "Main",
                        BigDecimal.valueOf(80),
                        20,
                        15));

        restaurant.addItem(
                new WeightedItem(
                        "W1",
                        "Grilled Meat",
                        "Main",
                        BigDecimal.valueOf(350),
                        30,
                        BigDecimal.valueOf(10)));

        Customer customer =
                system.addCustomer(
                        "Belal",
                        "01012345678",
                        BigDecimal.valueOf(1000));

        customer.addAddress(
                new Address(
                        "Maadi",
                        "Street 10"));

        system.addRider(
                "Ahmed",
                VehicleType.MOTORCYCLE,
                "Faisal");

    }

    /* ================= INPUT ================= */

    private static int readInt(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }

    private static double readDouble(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                return Double.parseDouble(
                        scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }

    private static BigDecimal readBigDecimal(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                BigDecimal value =
                        new BigDecimal(
                                scanner.nextLine());

                if (value.compareTo(
                        BigDecimal.ZERO) <= 0) {

                    System.out.println(
                            "Value must be greater than zero.");

                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }

    private static LocalDate readDate(
            String message) {

        while (true) {

            try {

                System.out.print(message);

                return LocalDate.parse(
                        scanner.nextLine());

            } catch (Exception e) {

                System.out.println(
                        "Use yyyy-MM-dd.");
            }
        }
    }
}
