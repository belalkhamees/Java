public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public double calculateSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "Product: " + product.getName() + "\n" +
                "Quantity: " + quantity + "\n" +
                "Subtotal: " + calculateSubtotal();
    }
}