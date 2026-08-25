import java.util.Scanner;

public class Validation {

    public static int readMenuChoice(Scanner sc) {

        while (true) {

            System.out.print("Enter your choice: ");

            if (sc.hasNextInt()) {

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice >= 1 && choice <= 15) {
                    return choice;
                }

            } else {
                sc.nextLine();
            }

            System.out.println(
                    "Invalid choice. Enter a number from 1 to 15."
            );
        }
    }

    public static int readInt(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            if (sc.hasNextInt()) {
                return sc.nextInt();
            }

            System.out.println("Invalid input. Please enter a number.");
            sc.next();
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

    public static double readPositiveDouble(Scanner sc, String message) {

        while (true) {

            System.out.print(message);

            if (sc.hasNextDouble()) {

                double value = sc.nextDouble();

                if (value > 0) {
                    return value;
                }

                System.out.println("Value must be greater than 0.");

            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
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

