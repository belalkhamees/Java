import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Store {

    private List<Product> products;
    private Map<Integer, Product> productById;
    private Map<Integer, Order> orders;
    private Set<String> categories;
    private Queue<Order> shippingQueue;
    private Map<Integer, Order> deliveredOrders;
    private List<Review> reviews;

    public Store() {

        products = new ArrayList<>();
        productById = new HashMap<>();
        orders = new HashMap<>();
        categories = new HashSet<>();
        shippingQueue = new ArrayDeque<>();
        deliveredOrders = new LinkedHashMap<>();
        reviews = new ArrayList<>();
    }

    //  PRODUCTS

    public boolean addProduct(Product product) {

        if (productById.containsKey(product.getId())) {
            return false;
        }

        products.add(product);
        productById.put(product.getId(), product);
        categories.add(product.getCategory());

        return true;
    }


    public Optional<Product> findProductById(int id) {
        return Optional.ofNullable(productById.get(id));
    }

    public boolean removeProduct(int id) {

        Optional<Product> product = findProductById(id);

        if (product.isEmpty()) {
            return false;
        }

        products.remove(product.get());
        productById.remove(id);

        updateCategories();

        return true;
    }

    private void updateCategories() {

        categories.clear();

        products.stream()
                .map(Product::getCategory)
                .forEach(categories::add);
    }

    public void displayAllProducts() {

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }


        products.stream().forEach(System.out::println);

        System.out.println("--------------------");
    }

    public void displayProductById(int id) {

        Optional<Product> product = findProductById(id);

        if (product.isEmpty()) {

            System.out.println("Product not found.");

        } else {

            System.out.println(product.get());
        }
    }

    public void displayCategories() {

        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }


        categories.stream()
                .forEach(System.out::println);
    }

    public void displayProductsByPrice() {

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        List<Product> sortedProducts = new ArrayList<>(products);

        Collections.sort(sortedProducts);


        sortedProducts.forEach(System.out::println);
    }


    public List<Product> filterProducts(Predicate<Product> condition) {

        return products.stream().filter(condition).collect(Collectors.toList());
    }

    public void displayProductsByCategory(String category) {

        List<Product> filteredProducts =
                filterProducts(product -> product.getCategory().equalsIgnoreCase(category));

        if (filteredProducts.isEmpty()) {

            System.out.println("No products found in this category.");

        } else {

            filteredProducts.forEach(System.out::println);
        }
    }

    public void displayExpensiveProducts(double price) {

        List<Product> filteredProducts =
                filterProducts(product ->
                        product.getPrice() > price);

        if (filteredProducts.isEmpty()) {

            System.out.println("No products found.");

        } else {

            filteredProducts.forEach(System.out::println);
        }
    }

    public void removeOutOfStockProducts() {

        Iterator<Product> iterator = products.iterator();

        int removedCount = 0;

        while (iterator.hasNext()) {

            Product product = iterator.next();

            if (product.getStockQuantity() == 0) {

                int id = product.getId();

                iterator.remove();

                productById.remove(id);

                removedCount++;
            }
        }

        updateCategories();

        System.out.println(
                removedCount + " out-of-stock product(s) removed.");
    }

    // ORDERS

    public boolean createOrder(int orderId, String customerName) {

        if (orders.containsKey(orderId)) {
            return false;
        }

        Order order = new Order(orderId, customerName);

        orders.put(orderId, order);

        return true;
    }


    public Optional<Order> findOrderById(int orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public boolean addItemToOrder(int orderId, int productId, int quantity) {

        Optional<Order> orderOptional = findOrderById(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        Optional<Product> productOptional = findProductById(productId);

        if (productOptional.isEmpty()) {
            return false;
        }

        order.addItem(productOptional.get(), quantity);

        return true;
    }

    public boolean removeItemFromOrder(int orderId, int productId) {

        Optional<Order> orderOptional = findOrderById(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        return order.removeItem(productId);
    }

    // SHIPPING

    public boolean addOrderToShipping(int orderId) {

        Optional<Order> orderOptional = findOrderById(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        if (!order.hasItems()) {
            return false;
        }

        shippingQueue.offer(order);

        order.updateStatus(OrderStatus.SHIPPED);

        return true;
    }

    public boolean shipNextOrder() {

        if (shippingQueue.isEmpty()) {
            return false;
        }

        Order order = shippingQueue.peek();

        if (!order.hasItems()) {

            System.out.println("This order has no items and cannot be shipped.");

            return false;
        }

        shippingQueue.poll();

        order.updateStatus(OrderStatus.DELIVERED);

        deliveredOrders.put(order.getOrderId(), order);

        return true;
    }

    //  CANCEL ORDER

    public boolean cancelOrder(int orderId) {

        Optional<Order> orderOptional = findOrderById(orderId);

        if (orderOptional.isEmpty()) {
            return false;
        }

        Order order = orderOptional.get();

        if (order.getStatus() == OrderStatus.DELIVERED) {
            return false;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        if (order.getStatus() == OrderStatus.SHIPPED) {
            shippingQueue.remove(order);
        }

        order.updateStatus(OrderStatus.CANCELLED);

        return true;
    }

    // REVIEWS

    public boolean addReview(int productId, String customerName, String comment) {

        if (findProductById(productId).isEmpty()) {
            return false;
        }

        Review review = new Review(productId, customerName, comment);

        reviews.add(review);

        return true;
    }

    public void displayReviewsForProduct(int productId) {

        List<Review> productReviews =
                reviews.stream().filter(review -> review.getProductId() == productId).collect(Collectors.toList());

        if (productReviews.isEmpty()) {

            System.out.println(
                    "No reviews found for this product.");

        } else {

            productReviews.forEach(review -> {
                System.out.println(review);
                System.out.println("--------------------");
            });
        }
    }

    // ORDER SORTING

    public void displayOrdersByTotal() {

        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }

        List<Order> sortedOrders = new ArrayList<>(orders.values());

        Collections.sort(sortedOrders, new OrderTotalComparator());

        sortedOrders.forEach(System.out::println);
    }
}