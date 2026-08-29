import java.util.Comparator;

public class OrderTotalComparator implements Comparator<Order> {

    @Override
    public int compare(Order order1, Order order2) {
        return Double.compare(order1.getTotal(), order2.getTotal());
    }
}