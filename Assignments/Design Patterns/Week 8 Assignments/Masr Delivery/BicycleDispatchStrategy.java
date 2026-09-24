import java.util.List;

public class BicycleDispatchStrategy
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
                    == VehicleType.BICYCLE
                    && rider.getStatus()
                    == RiderStatus.AVAILABLE
                    && distance <= 5) {

                return rider;
            }
        }

        throw new BusyRiderException(
                "No eligible bicycle rider is available"
        );
    }
}
