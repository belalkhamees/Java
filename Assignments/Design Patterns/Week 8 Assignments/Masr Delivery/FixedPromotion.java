import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FixedPromotion extends BasePromotion {

    private final BigDecimal amount;

    public FixedPromotion(
            String code,
            LocalDateTime expiry,
            BigDecimal minimumSubtotal,
            String district,
            boolean firstOrderOnly,
            BigDecimal amount
    ) {
        super(
                code,
                expiry,
                minimumSubtotal,
                district,
                firstOrderOnly
        );

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Discount amount cannot be negative"
            );
        }

        this.amount = amount;
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

        return amount.min(subtotal);
    }
}