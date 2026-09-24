public abstract class MenuItem {

    private final String id;
    private final String name;
    private final String category;
    private final int preparationTime;
    private boolean available;

    public MenuItem(
            String id,
            String name,
            String category,
            int preparationTime) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Menu item ID cannot be empty");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Menu item name cannot be empty");
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Menu item category cannot be empty");
        }

        if (preparationTime <= 0) {
            throw new IllegalArgumentException(
                    "Preparation time must be greater than zero");
        }

        this.id = id;
        this.name = name;
        this.category = category;
        this.preparationTime = preparationTime;
        this.available = true;
    }

    public abstract java.math.BigDecimal calculatePrice(
            Number quantity);

    public abstract boolean hasStock(Number quantity);

    public abstract void removeStock(Number quantity)
            throws StockShortageException;

    public abstract void addStock(Number quantity);

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}