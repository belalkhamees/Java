import java.util.Objects;
import java.util.Scanner;


public class Validation {

    // Read a positive number from the user.
    public static int readPositiveInt(Scanner sc , String msg) {
        int number;

        while (true) {
            System.out.print(msg);

            if (sc.hasNextInt()) {
                number = sc.nextInt();

                if (number > 0) {
                    return number;
                }
                else {
                    System.out.println("Invalid Input! Number must be greater than zero.");
                }
            }
            else {
                System.out.println("Invalid Input! Please Enter a Valid Integer.");
                sc.next(); // this to remove the invalid input from the buffer
            }
        }
    }

    public static double readPositiveDouble(Scanner sc , String msg) {
        double number;

        while (true) {
            System.out.print(msg);

            if (sc.hasNextDouble()) {
                number = sc.nextDouble();

                if (number > 0) {
                    return number;
                }
                else {
                    System.out.println("Invalid Input! Number must be greater than zero.");
                }
            }
            else {
                System.out.println("Invalid Input! Please Enter a Valid Number.");
                sc.next();
            }
        }
    }

//--------------------
    // Read a positive number or zero from the user
    public static int readChoice(Scanner sc , String msg) {
        int number;

        while (true) {
            System.out.print(msg);

            if (sc.hasNextInt()) {
                number = sc.nextInt();

                if (number >= 0) {
                    return number;
                }
                else {
                    System.out.println("Invalid Input! Please Enter a Number From The Menu.");
                }
            }
            else {
                System.out.println("Invalid Input! Please Enter a Valid Integer.");
                sc.next();
            }
        }
    }

//--------------------
    // Read a unique id from the user.
    public static String readUniqueId(Scanner sc, String msg, Car[] cars) {
        while (true) {
           System.out.print(msg);

           String id = sc.next();

           boolean duplicated = false;

           for (Car car : cars) {
               if (car != null && car.getId().equals(id)) {
                   System.out.println("This id already exists! Please enter a unique id.");
                   duplicated = true;
                   break;
               }
           }

           if (!duplicated) {
               return id;
           }
        }
    }

    public static String readUniqueId(Scanner sc , String msg , Customer[] customers){
        while (true) {
            System.out.print(msg);

            String id = sc.next();

            boolean duplicate = false;

            for (Customer customer : customers) {
                if (customer != null && customer.getId().equals(id)) {
                    System.out.println("This id already exists! Please enter a unique id.");
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                return id;
            }
        }

    }

//--------------------
    // Read a car (brand or model) and year from the user (type = brand or model)
    public static String readType(Scanner sc, String msg) {
        String type;
        boolean valid;

        while (true) {
            System.out.print("Enter Car " + msg +": ");

            type = sc.nextLine().trim();

            if (type.isEmpty()) {
                System.out.println(msg + " can't be empty!");
                continue;
            }

            valid = true;

            for (int i = 0; i < type.length(); i++) {
                char ch = type.charAt(i);

                if (!Character.isLetterOrDigit(ch) && ch != ' ' && ch != '-') {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                return type;
            }

            System.out.println("Invalid input! Only letters, Numbers, Spaces, and Dash Symbol(-) are allowed.");
        }
    }

    public static int readYear(Scanner sc, String msg) {
        int year;

        while (true) {
            System.out.print("Enter Car " + msg + ": ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid year.");
                sc.next();
                continue;
            }

            year = sc.nextInt();

            if (year >= 1990 && year <= 2026) {
                return year;
            }

            System.out.println("Invalid year! Manufacturing year must be between 1990 and 2026.");
        }
    }

//--------------------
    // Read customer's name and phone from the user
    public static String readName(Scanner sc, String msg) {
        String name;
        boolean valid;

        while (true) {
            System.out.print(msg);
            name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name Cant be Empty!");
                continue;
            }

            valid = true;

            for (int i = 0; i < name.length(); i++) {
                char ch = name.charAt(i);

                if (!Character.isLetter(ch) && ch != ' ') {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                return name;
            }

            System.out.println("Invalid Name! Please Enter letters only.");

        }
    }

    public static String readPhoneNumber(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String phone = sc.next();

            if (phone.length() != 11) {
                System.out.println("Invalid Input! Phone Number Must be 11 Digits.");
                continue;
            }

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

            System.out.println("Invalid Input! Phone Number Must Contain Digits Only.");
        }
    }


}


