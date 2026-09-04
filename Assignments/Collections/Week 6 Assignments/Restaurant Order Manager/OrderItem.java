public record OrderItem(MenuItem item, int quantity) {

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double calculateSubtotal() {
        return item.getPrice() * quantity;
    }
}