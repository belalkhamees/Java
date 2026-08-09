import java.util.Scanner;

public class Validation {

    public static int readPositiveInt(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            if (sc.hasNextInt()) {

                int value = sc.nextInt();
                sc.nextLine();

                if (value > 0) {
                    return value;
                }

            } else {
                sc.nextLine();
            }

            System.out.println("Please enter a positive integer.");
        }
    }


    public static double readPositiveDouble(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            if (sc.hasNextDouble()) {

                double value = sc.nextDouble();
                sc.nextLine();

                if (value > 0) {
                    return value;
                }

            } else {
                sc.nextLine();
            }

            System.out.println("Please enter a positive number.");
        }
    }


    public static int readChoice(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            if (sc.hasNextInt()) {

                int number = sc.nextInt();
                sc.nextLine();

                if (number >= 0) {
                    return number;
                }

                System.out.println(
                        "Invalid Input! Please Enter a Number From The Menu."
                );

            } else {

                System.out.println(
                        "Invalid Input! Please Enter a Valid Integer."
                );

                sc.nextLine();
            }
        }
    }


    public static String readName(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            String name = sc.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println("Name cannot be empty.");
        }
    }


    public static String readNationalId(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            String nationalId = sc.nextLine().trim();

            if (!nationalId.isEmpty()) {
                return nationalId;
            }

            System.out.println("National ID cannot be empty.");
        }
    }


    public static String readPhoneNumber(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            String phone = sc.nextLine().trim();

            // Phone number is optional
            if (phone.isEmpty()) {
                return "";
            }

            if (phone.length() >= 7 && phone.length() <= 15) {

                boolean valid = true;

                for (int i = 0; i < phone.length(); i++) {

                    if (!Character.isDigit(phone.charAt(i))) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    return phone;
                }
            }

            System.out.println(
                    "Phone number must contain only digits " +
                            "and be between 7 and 15 digits."
            );
        }
    }


    public static CustomerTier readCustomerTier(Scanner sc) {

        while (true) {

            System.out.println("\n1. STANDARD");
            System.out.println("2. SILVER");
            System.out.println("3. GOLD");

            int choice = readPositiveInt(sc, "Choose customer tier: ");

            switch (choice) {

                case 1 -> {
                    return CustomerTier.STANDARD;
                }

                case 2 -> {
                    return CustomerTier.SILVER;
                }

                case 3 -> {
                    return CustomerTier.GOLD;
                }

                default -> {
                    System.out.println(
                            "Invalid choice. Please choose 1, 2, or 3."
                    );
                }
            }
        }
    }

    public static AccountType readAccountType(Scanner sc) {

        while (true) {

            System.out.println("\n1. SAVINGS");
            System.out.println("2. CURRENT");
            System.out.println("3. FIXED DEPOSIT");

            int choice = readPositiveInt(sc, "Choose account type: ");

            switch (choice) {

                case 1 -> {
                    return AccountType.SAVINGS;
                }

                case 2 -> {
                    return AccountType.CURRENT;
                }

                case 3 -> {
                    return AccountType.FIXED_DEPOSIT;
                }

                default -> {
                    System.out.println(
                            "Invalid choice. Please choose 1, 2, or 3."
                    );
                }
            }
        }
    }

}