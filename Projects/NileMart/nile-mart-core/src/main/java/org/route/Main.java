package org.route;

import org.route.config.AppConfig;
import org.route.service.ProductService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            ProductService productService = context.getBean(ProductService.class);

            productService.findAll().forEach(System.out::println);

            System.out.println("\n========== BEAN DEFINITIONS ==========");

            for (String beanName : context.getBeanDefinitionNames()) {
                System.out.println(beanName);
            }
        }
    }
}