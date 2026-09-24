import java.util.HashMap;
import java.util.Map;

public class PlatformConfig {

    private static final PlatformConfig INSTANCE =
            new PlatformConfig();

    private final Map<String, Map<String, Double>> distances = new HashMap<>();

    private PlatformConfig() {
    }

    public static PlatformConfig getInstance() {
        return INSTANCE;
    }

    public void setDistance(
            String district1,
            String district2,
            double distance
    ) {
        if (district1 == null || district1.isBlank()
                || district2 == null || district2.isBlank()) {
            throw new IllegalArgumentException("Districts cannot be empty");
        }

        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }

        distances
                .computeIfAbsent(district1, key -> new HashMap<>())
                .put(district2, distance);

        distances
                .computeIfAbsent(district2, key -> new HashMap<>())
                .put(district1, distance);
    }

    public double getDistance(String district1, String district2) {
        if (district1.equalsIgnoreCase(district2)) {
            return 0;
        }

        Double distance = distances
                .getOrDefault(district1, new HashMap<>())
                .get(district2);

        if (distance == null) {
            distance = distances
                    .getOrDefault(district2, new HashMap<>())
                    .get(district1);
        }

        if (distance == null) {
            throw new IllegalArgumentException(
                    "Distance is not configured between "
                            + district1 + " and " + district2
            );
        }

        return distance;
    }
}