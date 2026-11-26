package org.route.checkout;

import jakarta.annotation.PreDestroy;
import org.route.common.Money;
import org.route.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class OrderRegistry {

    private final Clock clock;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final CopyOnWriteArrayList<Order> orders = new CopyOnWriteArrayList<>();

    public OrderRegistry(Clock clock) {
        this.clock = clock;
    }

    public String nextNumber() {
        return String.format("NM-%s-%05d",
                LocalDate.now(clock).format(DateTimeFormatter.BASIC_ISO_DATE),
                counter.incrementAndGet());
    }

    public void save(Order order) {
        orders.add(order);
    }

    public Optional<Order> findByNumber(String orderNumber) {
        return orders.stream()
                .filter(order -> order.orderNumber().equals(orderNumber))
                .findFirst();
    }

    public List<Order> findAll() {
        return orders.stream()
                .sorted(Comparator.comparing(Order::orderNumber).reversed())
                .toList();
    }

    public BigDecimal revenue() {
        return orders.stream()
                .map(Order::total)
                .reduce(Money.ZERO, BigDecimal::add);
    }

    @PreDestroy
    public void printEndOfDayReport() {
        System.out.printf(
                "End of day: %d orders, revenue %s EGP.%n",
                orders.size(),
                Money.format(revenue())
        );
    }
}