
package org.route;

import org.route.config.AppConfig;
import org.route.model.Customer;
import org.route.notification.NotificationService;
import org.route.service.ProductService;
import org.route.shipping.Governorate;
import org.route.shipping.ShippingQuoteService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Stage 2 test
            ProductService productService = context.getBean(ProductService.class);
            productService.findAll().forEach(System.out::println);

            // Stage 3: Shipping test
            ShippingQuoteService shippingService =
                    context.getBean(ShippingQuoteService.class);

            System.out.println("\n========== SHIPPING TEST ==========");

            System.out.println("Cairo: " +
                    shippingService.quote(
                            Governorate.CAIRO,
                            new BigDecimal("5000.00")
                    ));

            System.out.println("Matrouh: " +
                    shippingService.quote(
                            Governorate.MATROUH,
                            new BigDecimal("999999.00")
                    ));

            // Stage 3: Notifications test
            NotificationService notificationService =
                    context.getBean(NotificationService.class);

            Customer customer = new Customer(
                    "Mona",
                    "01012345678",
                    "mona@example.com"
            );

            System.out.println("\n========== NOTIFICATION TEST ==========");

            notificationService.sendToAll(
                    customer,
                    "Your order has been confirmed!"
            );

            // Stage 2: Bean definitions
            System.out.println("\n========== BEAN DEFINITIONS ==========");

            for (String beanName : context.getBeanDefinitionNames()) {
                System.out.println(beanName);
            }
        }
    }
}
