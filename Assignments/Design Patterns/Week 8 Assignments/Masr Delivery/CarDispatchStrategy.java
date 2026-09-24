import java.util.List;

public class CarDispatchStrategy
        implements DispatchStrategy {

    @Override
    public Rider chooseRider(
            Order order,
            List<Rider> riders
    ) throws BusyRiderException {

        for (Rider rider : riders) {

            if (rider.getVehicleType() == VehicleType.CAR
                    && rider.getStatus() == RiderStatus.AVAILABLE) {

                return rider;
            }
        }

        throw new BusyRiderException(
                "No eligible car rider is available"
        );
    }





}