import java.time.LocalDateTime;

public class AuditObserver
        implements OrderObserver {

    @Override
    public void update(Order order) {

        System.out.println(
                "AUDIT "
                        + LocalDateTime.now()
                        + " Order "
                        + order.getId()
                        + " -> "
                        + order.getStatus());
    }
}