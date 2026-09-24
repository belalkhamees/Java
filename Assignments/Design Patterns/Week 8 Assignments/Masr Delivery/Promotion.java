import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Promotion {

    String getCode();

    BigDecimal calculateDiscount(
            BigDecimal subtotal,
            BigDecimal deliveryFee,
            Customer customer,
            Address address,
            LocalDateTime now)
            throws PromotionException;
}


