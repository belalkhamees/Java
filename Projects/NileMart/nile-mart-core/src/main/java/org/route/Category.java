package org.route;

public enum Category {

    ELECTRONICS("Electronics"),
    FASHION("Fashion"),
    HOME_KITCHEN("Home & Kitchen"),
    BEAUTY("Beauty"),
    BOOKS("Books");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}








