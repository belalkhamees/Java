package org.route.model;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.stream.Collectors;

public enum Zone {

    GREATER_CAIRO("50.00", true),
    ALEX_DELTA("65.00", true),
    CANAL("70.00", true),
    UPPER_EGYPT("85.00", true),
    REMOTE("120.00", false);

    private final BigDecimal fee;
    private final boolean freeShippingEligible;

    Zone(String fee, boolean freeShippingEligible) {
        this.fee = new BigDecimal(fee);
        this.freeShippingEligible = freeShippingEligible;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public boolean isFreeShippingEligible() {
        return freeShippingEligible;
    }

    public EnumSet<Governorate> governorates() {
        return java.util.Arrays.stream(Governorate.values())
                .filter(governorate -> governorate.getZone() == this)
                .collect(Collectors.toCollection(
                        () -> EnumSet.noneOf(Governorate.class)
                ));
    }
}