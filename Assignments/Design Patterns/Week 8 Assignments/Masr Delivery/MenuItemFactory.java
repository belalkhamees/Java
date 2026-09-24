import java.math.BigDecimal;
import java.util.List;

public class MenuItemFactory {

    public static MenuItem create(
            String type,
            String id,
            String name,
            String category,
            BigDecimal price,
            int preparationTime,
            Number stock,
            List<MenuItem> comboItems,
            double discount) {

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(
                    "Menu item type cannot be empty");
        }

        return switch (type.toUpperCase()) {

            case "STANDARD" -> {

                if (!(stock instanceof Integer)) {
                    throw new IllegalArgumentException(
                            "Standard item stock must be Integer");
                }

                yield new StandardItem(
                        id,
                        name,
                        category,
                        price,
                        preparationTime,
                        stock.intValue());
            }

            case "WEIGHTED" -> {

                if (!(stock instanceof BigDecimal)) {
                    throw new IllegalArgumentException(
                            "Weighted item stock must be BigDecimal");
                }

                yield new WeightedItem(
                        id,
                        name,
                        category,
                        price,
                        preparationTime,
                        (BigDecimal) stock);
            }

            case "COMBO" -> new ComboItem(
                    id,
                    name,
                    category,
                    preparationTime,
                    comboItems,
                    discount);

            default -> throw new IllegalArgumentException(
                    "Unknown menu item type: " + type);
        };
    }
}