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

            System.out.println("Invalid input. Please enter a positive integer.");
        }
    }

    public static int readNonNegativeInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                sc.nextLine();

                if (value >= 0) {
                    return value;
                }
            } else {
                sc.nextLine();
            }

            System.out.println("Invalid input. Please enter a non-negative integer.");
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

            System.out.println("Invalid input. Please enter a positive amount.");
        }
    }

    public static int readMenuChoice(Scanner sc) {
        while (true) {
            System.out.print("Enter your choice: ");

            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice >= 0 && choice <= 10) {
                    return choice;
                }
            } else {
                sc.nextLine();
            }

            System.out.println("Invalid choice. Enter a number from 0 to 10.");
        }
    }

    public static String readName(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();

            if (value.matches("[A-Za-z ]+") && !value.isEmpty()) {
                return value;
            }

            System.out.println("Invalid name. Use letters and spaces only.");
        }
    }

    public static String readNonEmptyString(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    public static MembershipType readMembershipType(Scanner sc) {
        while (true) {
            System.out.println("1. Student (25% waiver)");
            System.out.println("2. Staff (10% waiver)");
            System.out.println("3. Public (No waiver)");

            int choice = readPositiveInt(sc, "Choose membership category: ");

            switch (choice) {
                case 1:
                    return MembershipType.STUDENT;
                case 2:
                    return MembershipType.STAFF;
                case 3:
                    return MembershipType.PUBLIC;
                default:
                    System.out.println("Invalid category.");
            }
        }
    }

    public static ItemStatus readItemStatus(Scanner sc) {
        while (true) {
            System.out.println("1. AVAILABLE");
            System.out.println("2. ON_LOAN");
            System.out.println("3. RESERVED");
            System.out.println("4. LOST");

            int choice = readPositiveInt(sc, "Choose status: ");

            switch (choice) {
                case 1:
                    return ItemStatus.AVAILABLE;
                case 2:
                    return ItemStatus.ON_LOAN;
                case 3:
                    return ItemStatus.RESERVED;
                case 4:
                    return ItemStatus.LOST;
                default:
                    System.out.println("Invalid status.");
            }
        }
    }
}
