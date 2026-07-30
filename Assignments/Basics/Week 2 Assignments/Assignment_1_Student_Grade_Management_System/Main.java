import java.util.Scanner;


public class Main {

    public static void fillStudentsData(Scanner sc, String[] student,float[] subject1,float[] subject2,float[] subject3){

        System.out.println("Enter The Name Of The 5 Students And Their Grades:");

        for (int i = 0; i < student.length; i++) {

            System.out.print("Name " + (i + 1) + ": ");
            student[i] = sc.next();

            System.out.print("Subject 1 Grade: ");
            subject1[i] = gradeInput(sc);

            System.out.print("Subject 2 Grade: ");
            subject2[i] = gradeInput(sc);

            System.out.print("Subject 3 Grade: ");
            subject3[i] = gradeInput(sc);
        }
    }

    private static float gradeInput (Scanner sc){
        float studentGrade = sc.nextFloat();

        while(studentGrade < 0 || studentGrade > 100){
            System.out.println("Invalid Grade! Grade must be between 0 and 100.");
            System.out.print("Enter Grade Again:");
            studentGrade = sc.nextFloat();
        }
        return studentGrade;
    }

//-------------
    public static void printMenu(){
        System.out.println("======== Main Menu =======\n" +
                "1. Show All Students names.\n" +
                "2. Show all Students grades in each subject.\n" +
                "3. Search Student by name.\n" +
                "4. Count Passed Students.\n" +
                "5. Average for each subject.\n"+
                "6. Highest grade in each subject.\n"+
                "7. Show Letter grades.\n"+
                "0. Exit.\n" +
                "=======================");
    }

//-------------
    public static void printStudentsNames(String[] student){
        System.out.println("Students Names:");
     for(int i = 0; i< student.length; i++){
         System.out.println(student[i]);
     }
    }

//-------------
    public static void printStudentsGrades(String[] student, float[] subject1, float[] subject2, float[] subject3) {
           System.out.printf("%-15s | %-10s | %-10s | %-10s%n",
            "Student Name", "Subject 1", "Subject 2", "Subject 3");

       for (int i = 0; i < student.length; i++) {
           System.out.printf("%-15s | %-10.2f | %-10.2f | %-10.2f%n",
                student[i],
                subject1[i],
                subject2[i],
                subject3[i]);
       }
   }

//-------------
    public static int searchStudent(String[] student , String name){

       for(int i = 0; i < student.length; i++){
           if(student[i].equalsIgnoreCase(name)){
               return i;
           }
       }
       return -1;
    }

//--------------
    public static void printStudentInfo(String[] student, int index, float[] subject1 ,float[] subject2 ,float[] subject3){

        System.out.println("Student Name: " + student[index]);
        System.out.println("Subject 1 Grade: " + subject1[index]);
        System.out.println("Subject 2 Grade: " + subject2[index]);
        System.out.println("Subject 3 Grade: " + subject3[index]);

    }

//--------------
    public static int countPassedStudents(float[] subject1 ,float[] subject2 ,float[] subject3){
        int count = 0;
        for(int i = 0; i < subject1.length; i++){
            if(subject1[i] >= 50 && subject2[i] >= 50 && subject3[i] >= 50) count++;
        }
        return count;
    }

//--------------
    private static char gradeLetter(float gradeNumber){
        return  gradeNumber >= 85 ? 'A' :
                gradeNumber >= 75 ? 'B' :
                gradeNumber >= 65 ? 'C' :
                gradeNumber >= 50 ? 'D' : 'F';
    }


    public static void printLetterGrades(String[] student, float[] subject1 ,float[] subject2 ,float[] subject3) {
        System.out.printf("%-15s | %-10s | %-10s | %-10s%n",
                    "Student Name", "Subject 1", "Subject 2", "Subject 3");

        for (int i = 0; i < student.length; i++) {
            System.out.printf("%-15s | %-10c | %-10c | %-10c%n",
                        student[i],
                        gradeLetter(subject1[i]),
                        gradeLetter(subject2[i]),
                        gradeLetter(subject3[i]));

            }

    }

//--------------
    private static float averageForSubject(float[] subject){
        float sum = 0;

        for(int i = 0; i < subject.length; i++){
            sum += subject[i];
        }
        return sum / subject.length;
    }

    public static void printAverage(float[] subject1, float[] subject2, float[] subject3){

        System.out.println("The average for each subject is:");
        System.out.println("Subject1: " + averageForSubject(subject1));
        System.out.println("Subject2: " + averageForSubject(subject2));
        System.out.println("Subject3: " + averageForSubject(subject3));
    }

//--------------
    private static float highestGradeInSubjects(float[] subject){
        float highest = subject[0];
        for(int i = 1; i < subject.length; i++){
            if(subject[i] > highest) {
                highest = subject[i];
            }
        }
        return highest;
    }

    public static void printHighestGrade(float[] subject1, float[] subject2, float[] subject3){

        System.out.println("Highest grade in each subject:");
        System.out.println("Subject1: " + highestGradeInSubjects(subject1));
        System.out.println("Subject2: " + highestGradeInSubjects(subject2));
        System.out.println("Subject3: " + highestGradeInSubjects(subject3));
    }

//---------------


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String[] student = new String[5];
        float[] subject1 = new float[5];
        float[] subject2 = new float[5];
        float[] subject3 = new float[5];


        fillStudentsData(sc, student, subject1, subject2, subject3);


        int choice;
        do{
            printMenu();

            System.out.print("Enter Your Choice: ");
            choice = sc.nextInt();

            switch (choice){
                case 1 -> printStudentsNames(student);

                case 2 -> printStudentsGrades(student,subject1,subject2,subject3);

                case 3 -> {
                    System.out.print("Enter The Student's Name: ");
                    String name = sc.next();
                    int index = searchStudent(student, name);
                    if(index != -1){
                        printStudentInfo(student, index, subject1 , subject2 ,subject3);
                    }
                    else{
                        System.out.println("Student Not Found!");
                    }
                }

                case 4 -> {
                    int count = countPassedStudents(subject1 , subject2 , subject3);
                    System.out.println("The Number Of Passed Students is: " + count);
                }

                case 5 -> printAverage(subject1,subject2,subject3);


                case 6 -> printHighestGrade(subject1,subject2,subject3);

                case 7 -> printLetterGrades(student,subject1,subject2,subject3);

                case 0 -> System.out.println("Exit.");

                default -> System.out.println("Invalid Choice.");

            }

            if(choice == 0) break;

        }while(true);

        sc.close();
    }
}

