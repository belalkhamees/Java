
package org.route.common;

public class NotFoundException extends StoreException {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public int status() {
        return 404;
    }
}
