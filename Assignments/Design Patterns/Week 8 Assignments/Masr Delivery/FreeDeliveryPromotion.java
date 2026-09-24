import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FreeDeliveryPromotion extends BasePromotion {

    public FreeDeliveryPromotion(
            String code,
            LocalDateTime expiry,
            BigDecimal minimumSubtotal,
            String district,
            boolean firstOrderOnly
    ) {
        super(
                code,
                expiry,
                minimumSubtotal,
                district,
                firstOrderOnly
        );
    }

    @Override
    public BigDecimal calculateDiscount(
            BigDecimal subtotal,
            BigDecimal deliveryFee,
            Customer customer,
            Address address,
            LocalDateTime now
    ) throws PromotionException {

        validate(subtotal, customer, address, now);

        return deliveryFee;
    }
}