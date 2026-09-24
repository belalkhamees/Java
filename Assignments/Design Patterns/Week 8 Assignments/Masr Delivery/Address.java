import java.util.Objects;

public class Address {

    private final String district;
    private final String details;

    public Address(String district, String details) {
        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("District cannot be empty");
        }

        if (details == null || details.isBlank()) {
            throw new IllegalArgumentException("Address details cannot be empty");
        }

        this.district = district;
        this.details = details;
    }

    public String getDistrict() {
        return district;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address address)) {
            return false;
        }

        return district.equalsIgnoreCase(address.district)
                && details.equalsIgnoreCase(address.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(district.toLowerCase(), details.toLowerCase());
    }

    @Override
    public String toString() {
        return district + " - " + details;
    }
}