import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class PercentagePromotion extends BasePromotion {

    private final double percentage;
    private final BigDecimal maximumDiscount;

    public PercentagePromotion(
            String code,
            LocalDateTime expiry,
            BigDecimal minimumSubtotal,
            String district,
            boolean firstOrderOnly,
            double percentage,
            BigDecimal maximumDiscount
    ) {
        super(
                code,
                expiry,
                minimumSubtotal,
                district,
                firstOrderOnly
        );

        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                    "Percentage must be between 0 and 100"
            );
        }

        if (maximumDiscount == null
                || maximumDiscount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Maximum discount cannot be negative"
            );
        }

        this.percentage = percentage;
        this.maximumDiscount = maximumDiscount;
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

        BigDecimal discount = subtotal
                .multiply(BigDecimal.valueOf(percentage))
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );

        return discount.min(maximumDiscount);
    }
}