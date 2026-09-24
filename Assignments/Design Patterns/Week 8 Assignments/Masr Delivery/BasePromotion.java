import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class BasePromotion implements Promotion {

    private final String code;
    private final LocalDateTime expiry;
    private final BigDecimal minimumSubtotal;
    private final String district;
    private final boolean firstOrderOnly;

    protected BasePromotion(
            String code,
            LocalDateTime expiry,
            BigDecimal minimumSubtotal,
            String district,
            boolean firstOrderOnly
    ) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Promotion code cannot be empty");
        }

        if (expiry == null) {
            throw new IllegalArgumentException(
                    "Expiry cannot be null");
        }

        if (minimumSubtotal == null
                || minimumSubtotal.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Minimum subtotal cannot be negative");
        }

        this.code = code;
        this.expiry = expiry;
        this.minimumSubtotal = minimumSubtotal;
        this.district = district;
        this.firstOrderOnly = firstOrderOnly;
    }

    public String getCode() {
        return code;
    }

    protected void validate(
            BigDecimal subtotal,
            Customer customer,
            Address address,
            LocalDateTime now
    ) throws PromotionException {

        if (now == null) {
            throw new IllegalArgumentException(
                    "Time cannot be null");
        }

        if (!now.isBefore(expiry)) {
            throw new PromotionException(
                    "Promotion has expired");
        }

        if (subtotal.compareTo(minimumSubtotal) < 0) {
            throw new PromotionException(
                    "Minimum subtotal is "
                            + minimumSubtotal);
        }

        if (district != null
                && !district.equalsIgnoreCase(
                address.getDistrict())) {

            throw new PromotionException(
                    "Promotion is not available in this district");
        }

        if (firstOrderOnly
                && customer.getCompletedOrders() > 0) {

            throw new PromotionException(
                    "Promotion is only valid for the first order");
        }
    }
}
