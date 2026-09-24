import java.util.Objects;

public class Rider {

    private final int id;
    private final String name;
    private final VehicleType vehicleType;

    private String currentDistrict;
    private RiderStatus status;
    private int completedDeliveries;
    private Order currentOrder;

    public Rider(
            int id,
            String name,
            VehicleType vehicleType,
            String currentDistrict) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Rider ID must be positive");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Rider name cannot be empty");
        }

        if (vehicleType == null) {
            throw new IllegalArgumentException(
                    "Vehicle type cannot be null");
        }

        if (currentDistrict == null
                || currentDistrict.isBlank()) {

            throw new IllegalArgumentException(
                    "District cannot be empty");
        }

        this.id = id;
        this.name = name;
        this.vehicleType = vehicleType;
        this.currentDistrict = currentDistrict;
        this.status = RiderStatus.OFF_DUTY;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getCurrentDistrict() {
        return currentDistrict;
    }

    public RiderStatus getStatus() {
        return status;
    }

    public int getCompletedDeliveries() {
        return completedDeliveries;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentDistrict(String currentDistrict) {

        if (currentDistrict == null
                || currentDistrict.isBlank()) {

            throw new IllegalArgumentException(
                    "District cannot be empty");
        }

        this.currentDistrict = currentDistrict;
    }

    public void goOnDuty() {

        if (status == RiderStatus.BUSY) {
            throw new IllegalStateException(
                    "Rider is already delivering an order");
        }

        status = RiderStatus.AVAILABLE;
    }

    public void goOffDuty() {

        if (status == RiderStatus.BUSY) {
            throw new IllegalStateException(
                    "Rider cannot go off duty while delivering");
        }

        status = RiderStatus.OFF_DUTY;
    }

    public void assignOrder(Order order)
            throws BusyRiderException {

        if (order == null) {
            throw new IllegalArgumentException(
                    "Order cannot be null");
        }

        if (status != RiderStatus.AVAILABLE
                || currentOrder != null) {

            throw new BusyRiderException(
                    "Rider is not available");
        }

        currentOrder = order;
        status = RiderStatus.BUSY;
    }

    public void completeDelivery(String district) {

        if (currentOrder == null) {
            throw new IllegalStateException(
                    "Rider has no active order");
        }

        setCurrentDistrict(district);

        currentOrder = null;
        completedDeliveries++;
        status = RiderStatus.AVAILABLE;
    }

    public void releaseOrder() {

        currentOrder = null;

        if (status == RiderStatus.BUSY) {
            status = RiderStatus.AVAILABLE;
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Rider rider)) {
            return false;
        }

        return id == rider.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}