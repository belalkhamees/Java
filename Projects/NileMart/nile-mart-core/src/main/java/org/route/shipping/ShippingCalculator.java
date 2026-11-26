package org.route.shipping;

import org.route.model.Governorate;

import java.math.BigDecimal;

public interface ShippingCalculator {

    BigDecimal calculate(Governorate governorate);
}