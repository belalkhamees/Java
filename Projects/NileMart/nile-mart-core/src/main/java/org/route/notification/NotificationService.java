package org.route.notification;

import lombok.RequiredArgsConstructor;
import org.route.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationSender> senders;

    public void sendToAll(Customer customer, String message) {
        senders.forEach(sender -> sender.send(customer, message));
    }
}