import java.util.*;

public class Restaurant {

    private final int id;
    private final String name;
    private final String district;

    private final Set<String> cuisines =
            new LinkedHashSet<>();

    private final Map<String, MenuItem> menu =
            new LinkedHashMap<>();

    private double rating;
    private boolean open;

    public Restaurant(
            int id,
            String name,
            String district,
            double rating) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Restaurant ID must be positive");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Restaurant name cannot be empty");
        }

        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException(
                    "Restaurant district cannot be empty");
        }

        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 0 and 5");
        }

        this.id = id;
        this.name = name;
        this.district = district;
        this.rating = rating;
        this.open = true;
    }

    public void addCuisine(String cuisine) {

        if (cuisine == null || cuisine.isBlank()) {
            throw new IllegalArgumentException(
                    "Cuisine cannot be empty");
        }

        cuisines.add(cuisine);
    }

    public void addItem(MenuItem item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Menu item cannot be null");
        }

        if (menu.containsKey(item.getId())) {
            throw new IllegalArgumentException(
                    "Menu item ID already exists");
        }

        menu.put(item.getId(), item);
    }

    public void removeItem(String itemId) {
        menu.remove(itemId);
    }

    public MenuItem getItem(String itemId) {
        return menu.get(itemId);
    }

    public void toggleItemAvailability(String itemId) {

        MenuItem item = getItem(itemId);

        if (item == null) {
            throw new IllegalArgumentException(
                    "Menu item not found");
        }

        item.setAvailable(!item.isAvailable());
    }

    public void checkOpen()
            throws RestaurantClosedException {

        if (!open) {
            throw new RestaurantClosedException(
                    "Restaurant is closed");
        }
    }

    public void toggleOpen() {
        open = !open;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDistrict() {
        return district;
    }

    public double getRating() {
        return rating;
    }

    public boolean isOpen() {
        return open;
    }

    public Set<String> getCuisines() {
        return Collections.unmodifiableSet(cuisines);
    }

    public List<MenuItem> getMenu() {
        return List.copyOf(menu.values());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Restaurant restaurant)) {
            return false;
        }

        return id == restaurant.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}