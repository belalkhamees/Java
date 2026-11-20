package org.route;

import org.route.repository.InMemoryProductRepository;
import org.route.service.PricingService;
import org.route.service.ProductService;
import org.route.service.PromotionalPricingService;
import org.route.service.StandardPricingService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ManualWiring {


    void main() {

        InMemoryProductRepository repo = new InMemoryProductRepository();
        repo.loadProducts();

        PricingService standardPricing = new StandardPricingService();

        ProductService standardService = new ProductService(repo,standardPricing);

        System.out.println("Standard price:\n");
        System.out.println(standardService.getPrice("ACC-CHG-20W",2) + "\n");


        PricingService promotionalPricing = new PromotionalPricingService();
        ProductService promotionalService = new ProductService(repo,promotionalPricing);

        System.out.println("promotional price:\n");
        System.out.println(promotionalService.getPrice("ACC-CHG-20W",2) + "\n");


        Set<Category> categories = promotionalService.groupByCategory().keySet();
        System.out.println("categories:\n");
        System.out.println(categories);

        System.out.println("Out-of-stock products:\n");
        System.out.println(promotionalService.search(p -> p.getStockQuantity() == 0));

        Product a = Product.builder()
                .sku("TEST-001")
                .name("Product A")
                .price(new BigDecimal("100.00"))
                .category(Category.ELECTRONICS)
                .stockQuantity(10)
                .build();

        Product b = Product.builder()
                .sku("TEST-001")
                .name("Product B")
                .price(new BigDecimal("200.00"))
                .category(Category.BOOKS)
                .stockQuantity(5)
                .build();


        HashSet<Product> set = new HashSet<>();
        set.add(a);
        set.add(b);

        System.out.println("\nEquals test:\n");
        System.out.println(a.equals(b));
        System.out.println("HashSet size: " + set.size());

    }

}

/*

Standard price:
1300.00

Promotional price:
1040.00

Categories:
[ELECTRONICS, FASHION, HOME_KITCHEN, BEAUTY, BOOKS]

Out-of-stock products:
[Product(sku=BEA-SUN-50, name=Sun Cream SPF50, ...)]

Equals test:
true
HashSet size: 1

 */