public class Review {

    private int productId;
    private String customerName;
    private String comment;

    public Review(int productId, String customerName, String comment) {
        this.productId = productId;
        this.customerName = customerName;
        this.comment = comment;
    }

    public int getProductId() {
        return productId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId + "\n" +
                "Customer Name: " + customerName + "\n" +
                "Comment: " + comment;
    }
}