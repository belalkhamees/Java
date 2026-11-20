
package org.route.service;

import org.route.Product;
import java.math.BigDecimal;

public interface PricingService {

    BigDecimal calculatePrice(Product product, int quantity);

}
