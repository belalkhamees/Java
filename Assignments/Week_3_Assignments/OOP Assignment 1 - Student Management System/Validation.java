import java.util.Scanner;




public class Validation {

    // To Read -> id and number of students
    public static int readPositiveInt(Scanner sc , String message) {
        int number;

        while (true) {
            System.out.print(message);

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


    public static double readGrade(Scanner sc, String message) {
        double grade;

        while (true) {
            System.out.print(message);

            if (sc.hasNextDouble()) {
                grade = sc.nextDouble();

                if (grade >= 0 && grade <= 100) {
                    return grade;
                }
                else {
                    System.out.println("Invalid grade! Grade must be between 0 and 100.");
                }
            }
            else {
                System.out.println("Invalid input! Please enter a numeric value.");
                sc.next();
            }
        }
    }


    public static int readChoice(Scanner sc , String message) {
        int number;

        while (true) {
            System.out.print(message);

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


    public static String readName(Scanner sc, String message) {
        String name;
        boolean valid;

        while (true) {
            System.out.print(message);
            name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name Cant be Empty.");
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


}


