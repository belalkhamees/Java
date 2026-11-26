package org.route;

import org.route.config.AppConfig;
import org.route.console.StoreConsole;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            StoreConsole console = new StoreConsole(context);

            console.run();
        }
    }
}