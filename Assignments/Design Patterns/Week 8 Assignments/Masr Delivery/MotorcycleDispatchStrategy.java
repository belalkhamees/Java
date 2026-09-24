import java.util.List;

public class MotorcycleDispatchStrategy
        implements DispatchStrategy {

    @Override
    public Rider chooseRider(
            Order order,
            List<Rider> riders
    ) throws BusyRiderException {

        double distance = PlatformConfig.getInstance()
                .getDistance(
                        order.getRestaurant().getDistrict(),
                        order.getDeliveryAddress().getDistrict()
                );

        for (Rider rider : riders) {

            if (rider.getVehicleType()
                    == VehicleType.MOTORCYCLE
                    && rider.getStatus()
                    == RiderStatus.AVAILABLE
                    && distance <= 20) {

                return rider;
            }
        }

        throw new BusyRiderException(
                "No eligible motorcycle rider is available"
        );
    }
}
