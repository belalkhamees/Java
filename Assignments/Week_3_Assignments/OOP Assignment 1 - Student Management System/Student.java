import java.util.Arrays;

public class Student {

    private int id;
    private String name;
    private double[] grades = new double[2];

    
    public Student(int id, String name, double[] grades) {
        this.id = id;
        this.name = name;
        setGrades(grades);;
    }


    public void setGrades(double[] grades) {
        if (grades.length == 2) {
            this.grades = grades;
        }
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public double[] getGrades() {
        return grades.clone();
    }


    public String getGradeStatus(){
        double finalGradeNumber = calculateFinalGrade();

        return  finalGradeNumber >= 90 ? "Excellent" :
                finalGradeNumber >= 75 ? "Very Good" :
                finalGradeNumber >= 60 ? "Pass" : "Fail";
    }


    public double calculateFinalGrade(){
        double sum = 0;
        for(double grade : grades){
            sum += grade;
        }
        return sum / grades.length;
    }


    public String getInfo() {
        return  "ID: " + id +
                "\nName: " + name +
                 "\nSubject 1 Grade: " + grades[0] +
                 "\nSubject 2 Grade: " + grades[1] +
                "\nFinal Grade: " + calculateFinalGrade() +
                "\nGrade Status: " + getGradeStatus();

    }



}

