import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeliverySystem {

    private final Map<Integer, Customer> customers =
            new HashMap<>();

    private final Map<Integer, Restaurant> restaurants =
            new HashMap<>();

    private final Map<Integer, Rider> riders =
            new HashMap<>();

    private final Map<Integer, Order> orders =
            new HashMap<>();

    private final Map<String, Promotion> promotions =
            new HashMap<>();

    private final List<OrderObserver> observers =
            new ArrayList<>();

    private int nextCustomerId = 1;
    private int nextRestaurantId = 1;
    private int nextRiderId = 1;
    private int nextOrderId = 1;

    private final PlatformConfig config =
            PlatformConfig.getInstance();

    /* ================= CUSTOMER ================= */

    public Customer addCustomer(
            String name,
            String mobile,
            BigDecimal wallet) {

        if (customers.values().stream()
                .anyMatch(c -> c.getMobile().equals(mobile))) {

            throw new IllegalArgumentException(
                    "Mobile already exists");
        }

        Customer customer =
                new Customer(
                        nextCustomerId++,
                        name,
                        mobile,
                        wallet);

        customers.put(
                customer.getId(),
                customer);

        return customer;
    }

    public Customer getCustomer(int id) {

        Customer customer = customers.get(id);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found");
        }

        return customer;
    }

    /* ================= RESTAURANT ================= */

    public Restaurant addRestaurant(
            String name,
            String district,
            double rating,
            String... cuisines) {

        Restaurant restaurant =
                new Restaurant(
                        nextRestaurantId++,
                        name,
                        district,
                        rating);

        for (String cuisine : cuisines) {
            restaurant.addCuisine(cuisine);
        }

        restaurants.put(
                restaurant.getId(),
                restaurant);

        return restaurant;
    }

    public void removeRestaurant(int id) {

        if (!restaurants.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Restaurant not found");
        }

        restaurants.remove(id);
    }

    public Restaurant getRestaurant(int id) {

        Restaurant restaurant =
                restaurants.get(id);

        if (restaurant == null) {
            throw new IllegalArgumentException(
                    "Restaurant not found");
        }

        return restaurant;
    }

    /* ================= RIDER ================= */

    public Rider addRider(
            String name,
            VehicleType vehicle,
            String district) {

        Rider rider =
                new Rider(
                        nextRiderId++,
                        name,
                        vehicle,
                        district);

        riders.put(
                rider.getId(),
                rider);

        return rider;
    }

    public Rider getRider(int id) {

        Rider rider = riders.get(id);

        if (rider == null) {
            throw new IllegalArgumentException(
                    "Rider not found");
        }

        return rider;
    }

    public void riderGoOnDuty(int riderId) {

        getRider(riderId).goOnDuty();
    }

    public void riderGoOffDuty(int riderId) {

        getRider(riderId).goOffDuty();
    }

    /* ================= SEARCH ================= */

    public List<Restaurant> searchRestaurants(
            Customer customer,
            String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Search text cannot be empty");
        }

        customer.addSearch(text);

        String search =
                text.toLowerCase();

        return restaurants.values()
                .stream()
                .filter(r ->
                        r.getName()
                                .toLowerCase()
                                .contains(search)
                                || r.getCuisines()
                                .stream()
                                .anyMatch(c ->
                                        c.toLowerCase()
                                                .contains(search)))
                .sorted(
                        Comparator
                                .comparingDouble(
                                        Restaurant::getRating)
                                .reversed()
                                .thenComparing(
                                        Restaurant::getName))
                .toList();
    }

    public List<Restaurant> searchRestaurants(
            Predicate<Restaurant> condition) {

        return restaurants.values()
                .stream()
                .filter(condition)
                .sorted(
                        Comparator
                                .comparingDouble(
                                        Restaurant::getRating)
                                .reversed()
                                .thenComparing(
                                        Restaurant::getName))
                .toList();
    }

    /* ================= ORDER ================= */

    public Order placeOrder(OrderBuilder builder)
            throws RestaurantClosedException,
            UnavailableItemException,
            StockShortageException,
            PromotionException {

        if (builder == null) {
            throw new IllegalArgumentException(
                    "Order builder cannot be null");
        }

        int orderId = nextOrderId;

        builder.setId(orderId);

        Order order = builder.build();

        Customer customer =
                order.getCustomer();

        Restaurant restaurant =
                order.getRestaurant();

        restaurant.checkOpen();

        if (!customer.getAddresses()
                .contains(order.getDeliveryAddress())) {

            throw new IllegalArgumentException(
                    "Address does not belong to customer");
        }

        BigDecimal subtotal =
                BigDecimal.ZERO;

        for (OrderLine line : order.getLines()) {

            MenuItem item =
                    line.getItem();

            if (!item.isAvailable()) {

                throw new UnavailableItemException(
                        "Item is unavailable: "
                                + item.getName());
            }

            if (!item.hasStock(
                    line.getQuantity())) {

                throw new StockShortageException(
                        "Not enough stock for: "
                                + item.getName());
            }

            subtotal =
                    subtotal.add(
                            line.getPrice());
        }

        BigDecimal deliveryFee =
                calculateDeliveryFee(
                        restaurant.getDistrict(),
                        order.getDeliveryAddress()
                                .getDistrict(),
                        customer.getLoyaltyTier());

        BigDecimal serviceFee =
                subtotal
                        .multiply(BigDecimal.TEN)
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP);

        BigDecimal promotionDiscount =
                BigDecimal.ZERO;

        if (order.getPromotion() != null) {

            promotionDiscount =
                    order.getPromotion()
                            .calculateDiscount(
                                    subtotal,
                                    deliveryFee,
                                    customer,
                                    order.getDeliveryAddress(),
                                    LocalDateTime.now());
        }

        BigDecimal total =
                subtotal
                        .add(deliveryFee)
                        .add(serviceFee)
                        .subtract(promotionDiscount);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        order.setPriceDetails(
                subtotal,
                deliveryFee,
                serviceFee,
                promotionDiscount,
                total);

        for (OrderLine line : order.getLines()) {

            line.getItem()
                    .removeStock(
                            line.getQuantity());
        }

        orders.put(
                order.getId(),
                order);

        nextOrderId++;

        return order;
    }

    private BigDecimal calculateDeliveryFee(
            String restaurantDistrict,
            String customerDistrict,
            LoyaltyTier tier) {

        double distance =
                config.getDistance(
                        restaurantDistrict,
                        customerDistrict);

        BigDecimal fee =
                BigDecimal.valueOf(15);

        if (distance > 3) {

            fee = fee.add(
                    BigDecimal.valueOf(
                                    distance - 3)
                            .multiply(
                                    BigDecimal.valueOf(3)));
        }

        if (tier == LoyaltyTier.SILVER) {

            fee = fee.multiply(
                    BigDecimal.valueOf(0.90));

        } else if (tier == LoyaltyTier.GOLD) {

            fee = BigDecimal.ZERO;
        }

        return fee.setScale(
                2,
                RoundingMode.HALF_UP);
    }

    /* ================= PAYMENT ================= */

    public void payOrder(int orderId) throws InsufficientWalletException {

        Order order =
                getOrder(orderId);

        if (order.isPaid()) {
            throw new IllegalStateException(
                    "Order is already paid");
        }

        order.getCustomer()
                .deductWallet(
                        order.getTotal());

        order.markAsPaid();
    }

    /* ================= RESTAURANT ORDER FLOW ================= */

    public void acceptOrder(int orderId) throws IllegalOrderTransitionException {

        Order order =
                getOrder(orderId);

        order.changeStatus(
                OrderStatus.ACCEPTED);

        notifyObservers(order);
    }

    public void rejectOrder(int orderId) throws IllegalOrderTransitionException {

        cancelOrder(orderId);
    }

    public void markPreparing(int orderId) throws IllegalOrderTransitionException {

        changeOrderStatus(
                orderId,
                OrderStatus.PREPARING);
    }

    public void markReady(int orderId) throws IllegalOrderTransitionException {

        changeOrderStatus(
                orderId,
                OrderStatus.READY);
    }

    public List<Order> getRestaurantOrders(int restaurantId) {

        Restaurant restaurant =
                getRestaurant(restaurantId);

        return orders.values()
                .stream()
                .filter(o ->
                        o.getRestaurant()
                                .equals(restaurant))
                .sorted(
                        Comparator
                                .comparing(
                                        Order::getPlacedAt)
                                .reversed())
                .toList();
    }

    public List<Order> getPendingOrders(int restaurantId) {

        Restaurant restaurant =
                getRestaurant(restaurantId);

        return orders.values()
                .stream()
                .filter(o ->
                        o.getRestaurant()
                                .equals(restaurant))
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.PLACED)
                .sorted(
                        Comparator
                                .comparing(
                                        Order::getPlacedAt))
                .toList();
    }

    public List<Order> getTodayOrders(
            int restaurantId) {

        Restaurant restaurant =
                getRestaurant(restaurantId);

        LocalDate today =
                LocalDate.now();

        return orders.values()
                .stream()
                .filter(o ->
                        o.getRestaurant()
                                .equals(restaurant))
                .filter(o ->
                        o.getPlacedAt()
                                .toLocalDate()
                                .equals(today))
                .sorted(
                        Comparator
                                .comparing(
                                        Order::getPlacedAt)
                                .reversed())
                .toList();
    }

    public BigDecimal restaurantTodayRevenue(
            int restaurantId) {

        return getTodayOrders(restaurantId)
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.DELIVERED)
                .map(Order::getTotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
    }

    /* ================= MENU MANAGEMENT ================= */

    public void toggleItemAvailability(
            int restaurantId,
            String itemId) {

        getRestaurant(restaurantId)
                .toggleItemAvailability(itemId);
    }

    public void addMenuItem(
            int restaurantId,
            MenuItem item) {

        getRestaurant(restaurantId)
                .addItem(item);
    }

    public void removeMenuItem(
            int restaurantId,
            String itemId) {

        getRestaurant(restaurantId)
                .removeItem(itemId);
    }

    public void addStock(
            int restaurantId,
            String itemId,
            Number quantity) {

        Restaurant restaurant =
                getRestaurant(restaurantId);

        MenuItem item =
                restaurant.getItem(itemId);

        item.addStock(quantity);
    }

    /* ================= STATUS ================= */

    public void changeOrderStatus(
            int orderId,
            OrderStatus newStatus)
            throws IllegalOrderTransitionException {

        Order order =
                getOrder(orderId);

        order.changeStatus(newStatus);

        if (newStatus == OrderStatus.DELIVERED) {

            order.getCustomer()
                    .orderCompleted();

            if (order.getRider() != null) {

                order.getRider()
                        .completeDelivery(
                                order.getDeliveryAddress()
                                        .getDistrict());
            }
        }

        notifyObservers(order);
    }

    public void markPickedUp(int orderId) throws IllegalOrderTransitionException {

        Order order =
                getOrder(orderId);

        if (order.getRider() == null) {

            throw new IllegalOrderTransitionException(
                    "Order has no assigned rider");
        }

        changeOrderStatus(
                orderId,
                OrderStatus.OUT_FOR_DELIVERY);
    }

    public void markDelivered(int orderId)
            throws IllegalOrderTransitionException {

        changeOrderStatus(
                orderId,
                OrderStatus.DELIVERED);
    }

    /* ================= DISPATCH ================= */

    public Order getNextReadyOrder() {

        return orders.values()
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.READY)
                .sorted(
                        Comparator
                                .comparing(
                                        (Order o) ->
                                                o.getCustomer()
                                                        .getLoyaltyTier()
                                                        == LoyaltyTier.GOLD)
                                .reversed()
                                .thenComparing(
                                        Order::getPlacedAt))
                .findFirst()
                .orElse(null);
    }

    public void assignRider(
            int orderId,
            DispatchStrategy strategy)
            throws BusyRiderException,
            IllegalOrderTransitionException {

        Order order = getOrder(orderId);

        if (order.getStatus() != OrderStatus.READY) {
            throw new IllegalOrderTransitionException(
                    "Only READY orders can be assigned");
        }

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Dispatch strategy cannot be null");
        }

        Rider rider = strategy.chooseRider(
                order,
                new ArrayList<>(riders.values()));

        if (rider == null) {
            throw new BusyRiderException(
                    "No available rider found");
        }

        rider.assignOrder(order);

        order.setRider(rider);

        order.changeStatus(OrderStatus.ASSIGNED);

        notifyObservers(order);
    }

    /* ================= CANCELLATION ================= */

    public void cancelOrder(int orderId) throws IllegalOrderTransitionException {

        Order order =
                getOrder(orderId);

        if (order.getStatus()
                == OrderStatus.OUT_FOR_DELIVERY
                || order.getStatus()
                == OrderStatus.DELIVERED
                || order.getStatus()
                == OrderStatus.CANCELLED) {

            throw new IllegalOrderTransitionException(
                    "Order cannot be cancelled from status "
                            + order.getStatus());
        }

        order.changeStatus(
                OrderStatus.CANCELLED);

        if (order.getRider() != null) {

            order.getRider()
                    .releaseOrder();
        }

        if (order.isPaid()) {

            order.getCustomer()
                    .addWallet(
                            order.getTotal());
        }

        for (OrderLine line :
                order.getLines()) {

            line.getItem()
                    .addStock(
                            line.getQuantity());
        }

        notifyObservers(order);
    }

    /* ================= PROMOTIONS ================= */

    public void addPromotion(Promotion promotion) {

        if (promotion == null) {
            throw new IllegalArgumentException(
                    "Promotion cannot be null");
        }

        String code =
                promotion.getCode()
                        .toLowerCase();

        if (promotions.containsKey(code)) {

            throw new IllegalArgumentException(
                    "Promotion code already exists");
        }

        promotions.put(
                code,
                promotion);
    }

    public Promotion getPromotion(String code) {

        if (code == null || code.isBlank()) {
            return null;
        }

        return promotions.get(
                code.toLowerCase());
    }

    /* ================= OBSERVER ================= */

    public void addObserver(OrderObserver observer) {

        if (observer == null) {
            throw new IllegalArgumentException(
                    "Observer cannot be null");
        }

        observers.add(observer);
    }

    private void notifyObservers(Order order) {

        for (OrderObserver observer :
                observers) {

            observer.update(order);
        }
    }

    /* ================= REPORTS ================= */

    public BigDecimal totalRevenue(LocalDate from, LocalDate to) {

        return orders.values()
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.DELIVERED)
                .filter(o -> {

                    LocalDate date =
                            o.getPlacedAt()
                                    .toLocalDate();

                    return !date.isBefore(from)
                            && !date.isAfter(to);
                })
                .map(Order::getTotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
    }

    public List<Restaurant> topFiveRestaurants(int year, int month) {

        return orders.values()
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.DELIVERED)
                .filter(o -> {

                    LocalDateTime date =
                            o.getPlacedAt();

                    return date.getYear() == year
                            && date.getMonthValue()
                            == month;
                })
                .collect(
                        Collectors.groupingBy(
                                Order::getRestaurant,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Order::getTotal,
                                        BigDecimal::add)))
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<Restaurant, BigDecimal>
                                        comparingByValue()
                                .reversed()
                                .thenComparing(
                                        e -> e.getKey()
                                                .getName()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Map<String, Double> averageOrderValueByDistrict() {

        return orders.values()
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.DELIVERED)
                .collect(
                        Collectors.groupingBy(
                                o -> o.getDeliveryAddress()
                                        .getDistrict(),
                                Collectors.averagingDouble(
                                        o -> o.getTotal()
                                                .doubleValue())));
    }

    public List<Restaurant> highRatedRestaurants() {

        return restaurants.values()
                .stream()
                .filter(r ->
                        r.getRating() > 4.5)
                .filter(r ->
                        orders.values()
                                .stream()
                                .filter(o ->
                                        o.getRestaurant()
                                                .equals(r))
                                .filter(o ->
                                        o.getStatus()
                                                == OrderStatus.DELIVERED)
                                .count() >= 20)
                .toList();
    }

    public Map<OrderStatus, Long> ordersByStatus() {

        return orders.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Order::getStatus,
                                Collectors.counting()));
    }

    public Map<Rider, Integer> riderCompletedDeliveries() {

        return riders.values()
                .stream()
                .collect(
                        Collectors.toMap(
                                r -> r,
                                Rider::getCompletedDeliveries));
    }

    public Map<Rider, Double> riderAverageDeliveryDuration() {

        return orders.values()
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.DELIVERED)
                .filter(o ->
                        o.getRider() != null)
                .collect(
                        Collectors.groupingBy(
                                Order::getRider,
                                Collectors.averagingLong(
                                        o -> o.getDeliveryDuration()
                                                .toMinutes())));
    }

    public String mostFrequentlyOrderedItem() {

        return orders.values()
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.DELIVERED)
                .flatMap(o ->
                        o.getLines().stream())
                .collect(
                        Collectors.groupingBy(
                                line -> line.getItem()
                                        .getName(),
                                Collectors.summingDouble(
                                        line -> line.getQuantity()
                                                .doubleValue())))
                .entrySet()
                .stream()
                .max(
                        Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No orders");
    }

    public List<Order> customerOrderHistory(Customer customer) {

        return orders.values()
                .stream()
                .filter(o ->
                        o.getCustomer()
                                .equals(customer))
                .sorted(
                        Comparator
                                .comparing(
                                        Order::getPlacedAt)
                                .reversed())
                .toList();
    }

    public BigDecimal customerTotalSpent(Customer customer) {

        return customerOrderHistory(customer)
                .stream()
                .filter(o ->
                        o.getStatus()
                                == OrderStatus.DELIVERED)
                .map(Order::getTotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
    }

    public int peakOrderingHour() {

        return orders.values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                o -> o.getPlacedAt()
                                        .getHour(),
                                Collectors.counting()))
                .entrySet()
                .stream()
                .max(
                        Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);
    }

    public List<Customer> customersNotOrderedLast30Days() {

        LocalDateTime cutoff =
                LocalDateTime.now()
                        .minusDays(30);

        return customers.values()
                .stream()
                .filter(customer ->
                        orders.values()
                                .stream()
                                .filter(o ->
                                        o.getCustomer()
                                                .equals(customer))
                                .noneMatch(o ->
                                        o.getPlacedAt()
                                                .isAfter(cutoff)))
                .toList();
    }

    /* ================= GETTERS ================= */

    public Order getOrder(int id) {

        Order order =
                orders.get(id);

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order not found");
        }

        return order;
    }

    public List<Restaurant> getRestaurants() {
        return List.copyOf(
                restaurants.values());
    }

    public List<Customer> getCustomers() {
        return List.copyOf(
                customers.values());
    }

    public List<Rider> getRiders() {
        return List.copyOf(
                riders.values());
    }

    public List<Order> getOrders() {
        return List.copyOf(
                orders.values());
    }
}