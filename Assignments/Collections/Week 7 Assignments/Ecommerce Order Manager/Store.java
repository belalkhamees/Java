import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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

    // PRODUCTS

    public boolean addProduct(Product product) {

        if (productById.containsKey(product.getId())) {
            return false;
        }

        products.add(product);

        productById.put(product.getId(), product);

        categories.add(product.getCategory());

        return true;
    }

    public Product findProductById(int id) {
        return productById.get(id);
    }

    public boolean removeProduct(int id) {

        if (!productById.containsKey(id)) {
            return false;
        }

        deleteProductEverywhere(id, null);

        return true;
    }

    private void deleteProductEverywhere(int id, Iterator<Product> iterator) {

        Product product = productById.remove(id);

        if (product == null) {
            return;
        }

        if (iterator != null) {
            iterator.remove();
        } else {
            products.remove(product);
        }

        updateCategories();
    }

    private void updateCategories() {

        categories.clear();

        for (Product product : products) {
            categories.add(product.getCategory());
        }
    }

    public void displayAllProducts() {

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (Product product : products) {

            System.out.println(product);
            System.out.println("--------------------");
        }
    }

    public void displayProductById(int id) {

        Product product = productById.get(id);

        if (product == null) {

            System.out.println("Product not found.");

        } else {

            System.out.println(product);
        }
    }

    public void displayCategories() {

        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }

        for (String category : categories) {
            System.out.println(category);
        }
    }

    public void displayProductsByPrice() {

        if (products.isEmpty()) {

            System.out.println("No products available.");
            return;
        }

        List<Product> sortedProducts = new ArrayList<>(products);

        Collections.sort(sortedProducts);

        for (Product product : sortedProducts) {

            System.out.println(product);
            System.out.println("--------------------");
        }
    }

    public void removeOutOfStockProducts() {

        Iterator<Product> iterator = products.iterator();

        int removedCount = 0;

        while (iterator.hasNext()) {

            Product product = iterator.next();

            if (product.getStockQuantity() == 0) {

                int id = product.getId();

                deleteProductEverywhere(id, iterator);

                removedCount++;
            }
        }

        System.out.println(removedCount + " out of stock products removed.");
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

    public Order findOrderById(int orderId) {
        return orders.get(orderId);
    }

    public boolean addItemToOrder(int orderId, int productId, int quantity) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        Product product = productById.get(productId);

        if (product == null) {
            return false;
        }

        order.addItem(product, quantity);

        return true;
    }

    public boolean removeItemFromOrder(int orderId, int productId) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        return order.removeItem(productId);
    }


    // SHIPPING

    public boolean addOrderToShipping(int orderId) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

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

    // CANCEL

    public boolean cancelOrder(int orderId) {

        Order order = orders.get(orderId);

        if (order == null) {
            return false;
        }

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

        if (!productById.containsKey(productId)) {
            return false;
        }

        Review review = new Review(productId, customerName, comment);

        reviews.add(review);

        return true;
    }

    public void displayReviewsForProduct(int productId) {

        boolean found = false;

        for (Review review : reviews) {

            if (review.getProductId() == productId) {

                System.out.println(review);
                System.out.println("--------------------");

                found = true;
            }
        }

        if (!found) {
            System.out.println("No reviews found for this product.");
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

        for (Order order : sortedOrders) {

            System.out.println(order);
            System.out.println("--------------------");
        }
    }
}













