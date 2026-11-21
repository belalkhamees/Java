package org.route.notification;

import org.route.model.Customer;

public abstract class AbstractNotificationSender implements NotificationSender {

    @Override
    public final void send(Customer customer, String message) {

        if (supports(customer)) {
            System.out.println(
                    "[" + channel() + "] to " + address(customer) + ": " + message
            );
        }
    }

    protected abstract String channel();

    protected abstract String address(Customer customer);
}