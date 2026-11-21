package org.route.model;

import org.route.common.StoreException;

public record Customer(String fullName, String phone, String email) {

    public Customer {

        if (fullName == null || fullName.isBlank()) {
            throw new StoreException("Customer name cannot be blank.");
        }

        if (phone == null || !phone.matches("01[0125][0-9]{8}")) {
            throw new StoreException("Invalid Egyptian phone number.");
        }

        if (email != null && email.isBlank()) {
            email = null;
        }

        if (email != null && !email.contains("@")) {
            throw new StoreException("Invalid customer email.");
        }
    }

}