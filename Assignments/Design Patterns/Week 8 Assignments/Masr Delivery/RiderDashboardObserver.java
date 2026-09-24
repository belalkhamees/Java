public class RiderDashboardObserver
        implements OrderObserver {

    @Override
    public void update(Order order) {

        if (order.getStatus() == OrderStatus.ASSIGNED
                || order.getStatus()
                == OrderStatus.OUT_FOR_DELIVERY) {

            System.out.println(
                    "Rider dashboard updated for order "
                            + order.getId());
        }
    }
}