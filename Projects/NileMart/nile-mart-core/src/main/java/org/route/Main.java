
package org.route;

import org.route.cart.ShoppingCart;
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

            Customer ahmed = new Customer(
                    "Ahmed Ali",
                    "01012345678",
                    "ahmed@gmail.com"
            );

            Customer mona = new Customer(
                    "Mona Hassan",
                    "01112345678",
                    "mona@gmail.com"
            );

            ShoppingCart ahmedCart = context.getBean(ShoppingCart.class);
            ShoppingCart monaCart = context.getBean(ShoppingCart.class);

            ahmedCart.open(1, ahmed);
            monaCart.open(2, mona);

            // Ahmed's cart
            ahmedCart.addItem("ACC-CHG-20W", 2);
            ahmedCart.addItem("HOM-PAN-28", 1);

            // Mona's cart
            monaCart.addItem("ACC-BUDS-6", 1);

            System.out.println("Ahmed's cart:");
            System.out.println(ahmedCart.view());

            System.out.println("Mona's cart:");
            System.out.println(monaCart.view());

        }
    }
}
