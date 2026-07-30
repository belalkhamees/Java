import java.util.Scanner;


public class Main {

    public static int rowInput(Scanner sc){
        System.out.print("Rows: ");
        int row = sc.nextInt();
        while (row < 1){
            System.out.println("Rows number must be greater than zero, Try again.");
            System.out.print("Rows:");
            row = sc.nextInt();
        }
        return row;
    }

    public static int columnInput(Scanner sc){
        System.out.print("Columns: ");

        int column = sc.nextInt();
        while (column < 1){
            System.out.println("Columns number must be greater than zero, Try again.");
            System.out.print("Columns:");
            column = sc.nextInt();
        }
        return column;
    }

//-----------------------------
    public static String[] moviesInput(Scanner sc){
        System.out.print("Enter number of Movies: ");
        int moviesNumber = sc.nextInt();

        while (moviesNumber <= 0) {
            System.out.print("Movies number must be greater than zero.\n" + "Enter again: ");
            moviesNumber = sc.nextInt();
        }

        sc.nextLine();  // to avoid \0 in buffer

        String[] movieNames = new String[moviesNumber];
        System.out.println("Enter Movies Names: ");

        for(int i = 0; i < moviesNumber; i++){
            System.out.print(i + 1 + ". ");
            movieNames[i] = sc.nextLine();
        }

        return movieNames;
    }

//-----------------------------
    public static void initializeSeatsAsAvailable(char[][] seats){
        for(int i = 0; i < seats.length; i++){
            for(int j = 0; j < seats[i].length; j++){
                seats[i][j] = 'O';
            }
        }

    }

    public static void printSeats(char[][] seats){
        for(int i = 0; i < seats.length; i++){
            for(int j = 0; j < seats[i].length; j++){
                System.out.print(seats[i][j] + " ");
            }
            System.out.println();
        }
    }

//-----------------------------
    public static void printMenu(){
        System.out.println("======== Main Menu =======\n" +
                "1. Display Seats.\n" +
                "2. Book Seat.\n" +
                "3. Cancel Booking.\n" +
                "4. Show all movies.\n" +
                "5. Show number of available and booked seats.\n"+
                "0. Exit.\n" +
                "=======================");
    }

//-----------------------------
    private static boolean isValidSeatNo(int row, int column, char[][] seats) {
    return row >= 1 && row <= seats.length &&
            column >= 1 && column <= seats[0].length;
    }

//-----------------------------
    private static boolean tryBookingSeat(char[][] seats, int row, int column){

        char status = seats[row][column];
        if(status == 'O'){
            seats[row][column] = 'X';
            return true;
        }
        else{
            return false;
        }

    }

    public static boolean bookSeatInput(Scanner sc , char[][] seats){
        System.out.print("Enter Seat Number: ");
        int seat = sc.nextInt();

        int row = seat / 10;
        int column = seat % 10;

        boolean validSeat = isValidSeatNo(row , column ,seats);
        if(!validSeat){
            System.out.println("Invalid Seat.");
            return false;
        }

        boolean result = tryBookingSeat(seats,row -1 ,column -1);
                                        // I subtract 1 because user dosent know that array is 0 based
        if(result){
            System.out.println("The seat has been booked successfully.");
            return true;
        }
        else{
            System.out.println("Invalid booking, This seat is reserved before!");
            return false;
        }
    }

//-------------------------------
    private static boolean tryCancelingSeat(char[][] seats, int row, int column){

        char status = seats[row][column];
        if(status == 'X'){
            seats[row][column] = 'O';
            return true;
        }
        else{
            return false;
        }

    }

    public static boolean cancelBooking(Scanner sc , char[][] seats){
        System.out.print("Enter Seat Number: ");
        int seat = sc.nextInt();

        int row = seat / 10;
        int column = seat % 10;

        boolean validSeat = isValidSeatNo(row , column ,seats);
        if(!validSeat){
            System.out.println("Invalid Seat.");
            return false;
        }

        boolean result = tryCancelingSeat(seats,row -1 ,column -1);
                                        // I subtract 1 because user dosent know that array is 0 based
        if(result){
            System.out.println("The seat booking was canceled successfully.");
            return true;
        }
        else{
            System.out.println("Invalid canceling, This seat is already available!");
            return false;
        }


    }

//-------------------------------
    public static void printMovies(String[] movieNames){
        System.out.println("Movie Names:");
        for (int i = 0; i < movieNames.length; i++){
            System.out.println((i + 1) + ". " + movieNames[i]);        }
    }

//-------------------------------


    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of rows & columns that will represent the seats:");

        int rows = rowInput(sc);
        int columns = columnInput(sc);
        char[][] seats = new char[rows][columns];

        initializeSeatsAsAvailable(seats);

        String[] movieNames = moviesInput(sc);


        int availableSeats = rows * columns;
        int bookedSeats = 0;

        int totalSeats = rows * columns;
        int exceededOccupancyRate = totalSeats * 80 / 100;


        int choice;
        do{
            printMenu();

            System.out.print("Enter Your Choice: ");
            choice = sc.nextInt();

            switch (choice){

                case 1 -> printSeats(seats);

                case 2 -> {
                   if( bookSeatInput(sc, seats) ){
                       bookedSeats++;
                       availableSeats--;
                   }
                   if(bookedSeats > exceededOccupancyRate){
                       System.out.println("The seats are almost full!");
                   }
                }

                case 3 -> {
                   if( cancelBooking(sc, seats) ){
                       bookedSeats--;
                       availableSeats++;
                   }
                }

                case 4 -> printMovies(movieNames);

                case 5 -> System.out.println("Available Seats: " + availableSeats + "\nBooked Seats: " + bookedSeats);

                case 0 -> System.out.println("Goodbye.");
                default -> System.out.println("Invalid Choice.");

            }

        }while(choice != 0);



    }
}

