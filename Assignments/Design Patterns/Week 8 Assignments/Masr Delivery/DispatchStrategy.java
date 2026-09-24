import java.util.List;

public interface DispatchStrategy {

    Rider chooseRider(Order order, List<Rider> riders) throws BusyRiderException;
}