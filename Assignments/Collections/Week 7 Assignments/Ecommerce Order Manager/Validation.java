import java.util.Scanner;

public class Validation {

    public static int readMenuChoice(Scanner sc) {

        while (true) {

            System.out.print("Enter your choice: ");

            if (sc.hasNextInt()) {

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice >= 1 && choice <= 19) {
                    return choice;
                }

                System.out.println("Please enter a number from 1 to 19.");

            } else {

                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public static int readInt(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            if (sc.hasNextInt()) {

                int value = sc.nextInt();
                sc.nextLine();

                return value;

            } else {

                System.out.println("Invalid input. Please enter an integer.");
                sc.nextLine();
            }
        }
    }

    public static int readPositiveInt(Scanner sc, String message) {

        while (true) {

            int value = readInt(sc, message);

            if (value > 0) {
                return value;
            }

            System.out.println("Value must be greater than 0.");
        }
    }

    public static int readNonNegativeInt(Scanner sc, String message) {

        while (true) {

            int value = readInt(sc, message);

            if (value >= 0) {
                return value;
            }

            System.out.println("Value cannot be negative.");
        }
    }

    public static double readDouble(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            if (sc.hasNextDouble()) {

                double value = sc.nextDouble();
                sc.nextLine();

                return value;

            } else {

                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
        }
    }

    public static double readPositiveDouble(Scanner sc, String message) {

        while (true) {

            double value = readDouble(sc, message);

            if (value > 0) {
                return value;
            }

            System.out.println("Value must be greater than 0.");
        }
    }

    public static String readString(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }
}