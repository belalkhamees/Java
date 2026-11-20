package org.route.common;

import java.util.Optional;
import java.util.function.Supplier;

public final class Check {

    private Check() {}

    public static <T> T found(
            Optional<T> value,
            Supplier<? extends StoreException> error) {

        return value.orElseThrow(error);
    }

}