package org.route.shipping;

import org.route.model.Governorate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingQuoteService {

    private final ShippingCalculator zoneCalculator;
    private final ShippingCalculator freeCalculator;
    private final BigDecimal freeShippingThreshold;

    public ShippingQuoteService(
            ShippingCalculator zoneCalculator,
            @Qualifier("freeShipping") ShippingCalculator freeCalculator,
            @Value("${store.free-shipping-threshold}") BigDecimal freeShippingThreshold) {

        this.zoneCalculator = zoneCalculator;
        this.freeCalculator = freeCalculator;
        this.freeShippingThreshold = freeShippingThreshold;
    }

    public BigDecimal quote(Governorate governorate, BigDecimal subtotal) {

        if (governorate.getZone().isFreeShippingEligible()
                && subtotal.compareTo(freeShippingThreshold) >= 0) {

            return freeCalculator.calculate(governorate);
        }

        return zoneCalculator.calculate(governorate);
    }
}