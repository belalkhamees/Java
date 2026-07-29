import java.util.Scanner;



public class Main {

    //Class fields
    private static final int MAX_CARS = 20, MAX_CUSTOMERS = 20;
    private static double officeTotalIncome = 0;
    private static int carsCount = 0,  customersCount = 0 ;
    private static int rentedCars = 0;

//-------------------------------------------------------------
    //Class methods
    
    public static void displayWelcomeBanner() {
        System.out.print("""
                           +------------------------------------------+
                           |    Welcome to SPEEDWAY RENTALS SYSTEM    |
                           |      MANAGE CARS - MANAGE CUSTOMERS      |
                           +------------------------------------------+
                           """);
    }

    public static void displayMenu(){
      System.out.println("\n======================================== \n" +
                           "         SPEEDWAY RENTALS SYSTEM       " +
                           "\n======================================== \n" +
                           "1. Add Regular Car \n" +
                           "2. Add Luxury Car \n" +
                           "3. Add Customer \n" +
                           "4. Display All Cars \n" +
                           "5. Display Available Cars \n" +
                           "6. Rent a Car \n" +
                           "7. Return a Car \n" +
                           "8. Search Car by ID \n" +
                           "9. Search Car by Brand \n" +
                           "10. Search Customer By ID \n" +
                           "11. Display All Customers \n" +
                           "0. Exit \n" +
                           "========================================");
    }

//----------------------

    public static void addRegularCar(Scanner sc, Car[] cars) {

        int firstEmptyIndex = ArrayHelper.findFirstEmptyPos(cars);

        if(firstEmptyIndex == -1){
            System.out.println("You Can't Add New Car! The Cars Capacity is Full.");
            return;
        }

        System.out.println("\n-Enter Car Information-");

        String id = Validation.readUniqueId(sc ,"Enter Car ID: " , cars);
        sc.nextLine();

        String brand = Validation.readType(sc , "Brand");

        String model = Validation.readType(sc,"Model");

        int year = Validation.readYear(sc , "Year");

        double pricePerDay = Validation.readPositiveDouble(sc , "Enter Price Per Day: ");

        cars[firstEmptyIndex] = new Car(id , brand , model , year , pricePerDay);

        System.out.println("\nCar added successfully.\n" + "Car id: " + id);

        carsCount++;
    }

    public static void addLuxuryCar(Scanner sc, Car[] cars){
        int firstEmptyIndex = ArrayHelper.findFirstEmptyPos(cars);

        if(firstEmptyIndex == -1){
            System.out.println("You Can't Add New Luxury Car! The Cars Capacity is Full.");
            return;
        }

        System.out.println("\n-Enter Luxury Car Information-");

        String id = Validation.readUniqueId(sc ,"Enter Car ID: " , cars);
        sc.nextLine();

        String brand = Validation.readType(sc , "Brand");

        String model = Validation.readType(sc,"Model");

        int year = Validation.readYear(sc , "Year");

        double pricePerDay = Validation.readPositiveDouble(sc , "Enter Price Per Day: ");

        double insuranceFee = Validation.readPositiveDouble(sc , "Enter Insurance Fee: ");

        cars[firstEmptyIndex] = new LuxuryCar(id , brand , model , year , pricePerDay, insuranceFee);

        System.out.println("\nLuxury Car was added successfully.\n" + "Luxury car id: " + id);

        carsCount++;
    }

//---------------------

    public static void addCustomer(Scanner sc, Customer[] customers) {

        int firstEmptyIndex = ArrayHelper.findFirstEmptyPos(customers);

        if(firstEmptyIndex == -1){
            System.out.println("You Can't Add New Customer! The Customers Capacity is Full.");
            return;
        }

        System.out.println("\n-Enter Customer Information-");

        String id = Validation.readUniqueId(sc ,"Enter Customer ID: " , customers);
        sc.nextLine();

        String name = Validation.readName(sc , "Enter Customer Name: ");

        String phoneNumber = Validation.readPhoneNumber(sc , "Enter Customer Phone Number: ");

        customers[firstEmptyIndex] = new Customer(id , name , phoneNumber);

        System.out.println("----------------------------------------");
        System.out.println("\nCustomer was added successfully.\n" + "Customer name: " + name + "\nCustomer id: " + id);

        customersCount++;
    }

//---------------------

    public static void displayCars(Car[] cars){
        if(carsCount == 0){
            System.out.println("The Fleet Of Cars is Empty!");
            return;
        }
        System.out.println( "\n========= CAR INFORMATION =========" );



        for(int i = 0; i < carsCount; i++){
            System.out.println(cars[i].getInfo());
            System.out.println("---------------------------");
        }
    }

    public static void displayAvailableCars(Car[] cars){
        int count = 0;
        System.out.println("\n========== Available Cars ==========");

        for(int i = 0; i < carsCount; i++){
            if(cars[i].isAvailable()) {
                System.out.println(cars[i].getInfo());
                System.out.println("----------------------------");
                count++;
            }
        }
        if(count == 0) {
            System.out.println("No available cars found!");
            return;
        }

        System.out.println("Total Count of available cars: " + count);
    }

//--------------------

    public static void rentCar(Customer[] customers ,Car[] cars ,Scanner sc){

            //Customer Validation
            int customerIndex = ArrayHelper.findCustomerById(sc ,customers);
            if(customerIndex == -1){
                System.out.println("Invalid Input! The customer id is not found.");
                return;
            }
            Customer customer = customers[customerIndex];
            if(!customer.getRentedCarId().equals("-1")){
                System.out.println("Failed Operation! This Customer already hold a Car.");
                return;
            }

            //Car Validation
            int carIndex = ArrayHelper.findCarId(sc , cars);
            if(carIndex == -1){
               System.out.println("Invalid Input! The Car Id is Not Found.");
               return;
            }
            Car car = cars[carIndex];

            if(!car.isAvailable()){
                System.out.println("Failed Operation! This Car is Not Available.");
                return;
            }

            int rentedDays = Validation.readPositiveInt(sc , "Enter Number Of Rented Days: ");

            if(car instanceof LuxuryCar){
                if(rentedDays < LuxuryCar.getMinRentDays()) {
                  System.out.println("Failed Operation! The Number Of Days is Less Than The Minimum Rental Period Of Luxury Car.");
                  return;
                }
            }

            double rentalCost = car.calculateRentalCost(rentedDays);

            car.markAsRented();

            customer.recordRental(car.getId(), rentedDays, rentalCost);

            officeTotalIncome += rentalCost;
            rentedCars++;

            printRentalReceipt(customer, car, rentedDays, rentalCost);

    }

    public static void printRentalReceipt(Customer customer, Car car, int rentedDays, double rentalCost) {
        System.out.println("""
            \n============ RENTAL RECEIPT =============
            """ +
                "Customer Name: " + customer.getName() +
                "\nCar Brand: " + car.getBrand() +
                "\nCar Model: " + car.getModel() +
                "\nNumber Of Rented Days: " + rentedDays +
                "\nFinal Rental Cost (Including Tax): " + rentalCost +
                "\n-----------------------------------------");
    }

//--------------------

    public static void returnCar(Customer[] customers, Car[] cars, Scanner sc){

        //Customer Validation
        int customerIndex = ArrayHelper.findCustomerById(sc ,customers);
        if(customerIndex == -1){
            System.out.println("Invalid Input! The customer id is not found.");
            return;
        }

        Customer customer = customers[customerIndex];
        String rentedCarId = customer.getRentedCarId();

        if(rentedCarId.equals("-1")){
            System.out.println("Failed Operation! This customer has no rented car.");
            return;
        }

        Car returnedCar = null;
        for (int i = 0; i < carsCount; i++) {
            if ( cars[i].getId().equals(rentedCarId) ) {
                returnedCar = cars[i];
                cars[i].returnCar();
                break;
            }
        }

        customer.clearRental();
        rentedCars--;

        if(returnedCar != null){
            printReturnMessage(returnedCar);
        }

    }

    public static void printReturnMessage(Car car) {
        System.out.println("\nCar returned successfully.\n" +
                "Returned Car: " + car.getBrand() + " " + car.getModel());
    }

//--------------------

    public static void searchCarById(Car[] cars , Scanner sc){
        sc.nextLine();

        int index = ArrayHelper.findCarId(sc , cars);
        if(index == -1) {
            System.out.println("Car not found!");
            return;
        }
        System.out.println("\n========= CAR INFORMATION  =========");
        System.out.println(cars[index].getInfo());
        System.out.println("------------------------------------");
    }

    public static void searchCarByBrand(Car[] cars , Scanner sc){
        int brandCount = 0;

        sc.nextLine();
        String brand = Validation.readType(sc ,"Brand");

        if(!ArrayHelper.isBrandExists(cars, brand)){
            System.out.println("No car of that brand exists!");
            return;
        }
        else{
            System.out.println("\n========== " + brand.toUpperCase() + " CARS ==========");
        }

        for(int i = 0; i < carsCount; i++){
            if(cars[i].getBrand().equalsIgnoreCase(brand)){
                System.out.println(cars[i].getInfo());
                brandCount++;
                System.out.println("----------------------------");
            }
        }

        System.out.println("Number of matching cars: " + brandCount);
    }

//-------------------

    public static void searchCustomerById(Customer[] customers, Scanner sc) {

        if(customersCount == 0){
            System.out.println("No customers found!");
            return;
        }

        int index = ArrayHelper.findCustomerById(sc, customers);
        if(index == -1) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println(
                "\n======== CUSTOMER INFORMATION  ========" +
                customers[index].getInfo());
        System.out.println("--------------------------------------");
    }

    public static void displayCustomers(Customer[] customers, Car[] cars){

        if(customersCount == 0){
            System.out.println("No customers found!");
            return;
        }

        System.out.println(
                "\n====== CUSTOMER INFORMATION ======" );

        for(int i = 0; i < customersCount; i++){
            System.out.println("Customer " + (i + 1) + ": " + customers[i].getName());
            if(customers[i].getRentedCarId().equals("-1")){
                System.out.println("Current Car: " + "None");
            }
            else{
                for(int j = 0; j < carsCount; j++){
                    if(cars[j].getId().equals(customers[i].getRentedCarId())){
                        System.out.println("Current Car: " + cars[j].getBrand() + " " + cars[j].getModel());
                        break;
                    }
                }
            }
            System.out.println("-------------------------------");
        }

    }

//-------------------

    public static double calcAverageDailyPrice(Car[] cars){
        double sum = 0;

        for(int i = 0; i < carsCount; i++){
            sum += cars[i].getPricePerDay();
        }

        if(sum == 0) return sum;
        return sum / carsCount;
    }

    public static String returnMostExCar(Car[] cars){
        int carIndex = ArrayHelper.findMostExCar(cars);
        String msg = "None";
        if(carIndex != -1){
            msg = cars[carIndex].getBrand() + " " + cars[carIndex].getModel();
        }
        return msg;
    }

//-------------------

    public static void displayGoodbyeMessage() {
        System.out.println(
            "\n+--------------------------------------+" +
            "\n|         THANK YOU FOR USING          |" +
            "\n|       SPEEDWAY RENTALS SYSTEM        |" +
            "\n|          HAVE A GREAT DAY!           |" +
            "\n+--------------------------------------+" );
    }

    public static void displayFinalStatistics(Car[] cars){

        System.out.println(
                "\n========== OFFICE STATISTICS ==========" +
                "\nTotal Cars: " + carsCount +
                "\nRented Cars: " + rentedCars +
                "\nMost Expensive Car: " + returnMostExCar(cars) +
                "\nCustomers: " + customersCount +
                "\nAverage Daily Price: " + calcAverageDailyPrice(cars) +
                "\nTotal Income: " + officeTotalIncome +
                "\n---------------------------------------" );
    }


//------------------------------------------------------------------


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        displayWelcomeBanner();

        Car[] cars = new Car[MAX_CARS];
        Customer[] customers = new Customer[MAX_CUSTOMERS];


        int choice;
        do{
            displayMenu();

            choice = Validation.readChoice(sc ,"Enter Your Choice: ");

            switch (choice) {

               case 1 -> addRegularCar(sc , cars);

               case 2 -> addLuxuryCar(sc , cars);

               case 3 -> addCustomer(sc , customers);

               case 4 -> displayCars(cars);

               case 5 -> displayAvailableCars(cars);

               case 6 -> rentCar(customers, cars, sc);

               case 7 -> returnCar(customers, cars, sc);

               case 8 -> searchCarById(cars ,sc);

               case 9 -> searchCarByBrand(cars ,sc);

               case 10 -> searchCustomerById(customers, sc);

               case 11 -> displayCustomers(customers, cars);

               case 0 -> {
                   displayGoodbyeMessage();
                   displayFinalStatistics(cars);
               }

               default -> System.out.println("Invalid Choice.");


            }

            }while(choice != 0);

        sc.close();


    }


}

