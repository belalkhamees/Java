package org.route.notification;

import org.route.model.Customer;

public interface NotificationSender {

    void send(Customer customer, String message);

    default boolean supports(Customer customer) {
        return true;
    }
}
