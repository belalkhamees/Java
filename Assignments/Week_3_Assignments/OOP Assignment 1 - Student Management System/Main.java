import java.util.Scanner;


public class Main {

    public static void printMenu(){
        System.out.println("\n======== Main Menu =======\n" +
                "1. Display Students.\n" +
                "2. Calculate Average Grade.\n" +
                "3. Find Highest Grade.\n" +
                "4. Search Student by ID.\n" +
                "5. Count Passed and Failed Students.\n" +
                "6. Sort Students by Grade.\n" +
                "7. Modify Student Grades\n" +
                "0. Exit.\n" +
                "=======================");
    }


    public static void fillStudentsData(Scanner sc, Student[] students) {

        System.out.println("Enter Students Information");

        for (int i = 0; i < students.length; i++) {

            System.out.println("\n(Student " + (i + 1) + ")");

            int id = Validation.readPositiveInt(sc, "Enter ID: ");
            sc.nextLine();

            String name = Validation.readName(sc , "Enter Name: ");

            double[] grades = new double[2];

            for (int j = 0; j < grades.length; j++) {
                grades[j] = Validation.readGrade(sc, "Enter Subject " + (j + 1) + " Grade: " );
            }

            students[i] = new Student(id, name, grades);
        }
    }


    public static void displayStudents(Student[] students) {

        System.out.println("\n===== Students List =====");

        for (Student student : students) {
            System.out.println(student.getInfo());
            System.out.println("-------------------------");
        }
    }


    public static float calculateAverageGrade(Student[] students) {

        float sum = 0;

        for (Student student : students) {
            sum += student.calculateFinalGrade();
        }

        return sum / students.length;
    }


    public static Student findHighestGrade(Student[] students) {

        Student highestStudent = students[0];

        for (int i = 1; i < students.length; i++) {
            if (students[i].calculateFinalGrade() > highestStudent.calculateFinalGrade()) {
                highestStudent = students[i];
            }
        }

        return highestStudent;
    }


    public static Student searchStudentById(Student[] students, int id) {

        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }

        return null;
    }


    public static int countPassedStudents(Student[] students) {

        int count = 0;

        for (Student student : students) {
            if (student.calculateFinalGrade() >= 60) {
                count++;
            }
        }

        return count;
    }


    public static int countFailedStudents(Student[] students) {

        int count = 0;

        for (Student student : students) {
            if (student.calculateFinalGrade() < 60) {
                count++;
            }
        }

        return count;
    }


    public static void sortStudentsByGrade(Student[] students) {

        for (int i = 0; i < students.length - 1; i++) {

            for (int j = 0; j < students.length - 1 - i; j++) {

                if (students[j].calculateFinalGrade() < students[j + 1].calculateFinalGrade()) {

                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
    }


    public static void modifyStudentGrades(Scanner sc, Student[] students) {

        int id = Validation.readPositiveInt(sc, "Enter Student ID: ");

        Student student = searchStudentById(students, id);

        if (student != null) {

            double[] grades = new double[2];

            for (int i = 0; i < grades.length; i++) {
                grades[i] = Validation.readGrade(sc,"Enter New Grade Of Subject " + (i + 1) + ":");
            }

            student.setGrades(grades);

            System.out.println("Grades updated successfully.");

        }
        else {
            System.out.println("Student not found.");
        }
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numberOfStudents = Validation.readPositiveInt(sc , "Enter The Number Of Students: ");

        Student[] students = new Student[numberOfStudents];

        fillStudentsData(sc, students);


        int choice;
        do{
            printMenu();

            choice = Validation.readChoice(sc ,"Enter Your Choice: ");

            switch (choice){
                case 1 -> displayStudents(students);

                case 2 -> System.out.println("The Students Average Grade is: " + calculateAverageGrade(students));

                case 3 -> {
                    Student highestStudent = findHighestGrade(students);
                    System.out.println("Student with the Highest Grade is:");
                    System.out.println(highestStudent.getInfo());
                }

                case 4 -> {
                    int id = Validation.readPositiveInt(sc, "Enter Student ID: ");

                    Student student = searchStudentById(students, id);

                    if (student != null) {
                        System.out.println(student.getInfo());
                    }
                    else {
                        System.out.println("Student not Found.");
                    }
                }

                case 5 -> {
                    System.out.println("Passed Students: " + countPassedStudents(students));
                    System.out.println("Failed Students: " + countFailedStudents(students));
                }

                case 6 -> {
                    sortStudentsByGrade(students);
                    System.out.println("Students sorted by Grade Successfully.");
                    displayStudents(students);
                }

                case 7 -> modifyStudentGrades(sc, students);

                case 0 -> System.out.println("Exit.");

                default -> System.out.println("Invalid Choice.");

            }


        }while(choice != 0);

        sc.close();
    }
}

