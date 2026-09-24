public class CustomerNotificationObserver
        implements OrderObserver {

    @Override
    public void update(Order order) {

        System.out.println(
                "Customer notification: Order "
                        + order.getId()
                        + " is now "
                        + order.getStatus());
    }
}