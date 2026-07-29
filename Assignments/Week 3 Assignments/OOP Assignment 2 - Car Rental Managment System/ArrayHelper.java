import java.util.Objects;
import java.util.Scanner;


public class ArrayHelper {

    //To find first empty position in array to store new element
    public static int findFirstEmptyPos(Car[] cars){
        int index = -1;

        for (int i = 0; i < cars.length; i++) {
            if (cars[i] == null) {
                index = i;
                break;
            }
        }
        return index;

    }

    public static int findFirstEmptyPos(Customer[] customers){
        int index = -1;

        for (int i = 0; i < customers.length; i++) {
            if (customers[i] == null) {
                index = i;
                break;
            }
        }
        return index;

    }

//-------------------
    //To return the index of car in array if exists
    public static int findMostExCar(Car[] cars){
    int carIndex = -1;
    double highestPrice = 0;

    for(int i = 0; i < cars.length; i++){
        if(cars[i] != null && cars[i].getPricePerDay() > highestPrice){
            highestPrice = cars[i].getPricePerDay();
            carIndex = i;
        }
    }
    return carIndex;
}

    public static int findCarId(Scanner sc, Car[] cars){
        String id;
        int index = -1;
        System.out.print("Enter Car Id: ");

        id = sc.next();

        for(int i = 0; i < cars.length; i++){
            if(cars[i] != null && cars[i].getId().equals(id)){
                index = i;
                return index;
            }
        }

        return index;
    }

    public static boolean isBrandExists(Car[] cars, String brand){

        for(Car car : cars){
            if(car != null && car.getBrand().equalsIgnoreCase(brand)){
                return true;
            }
        }
        return false;
    }

//-------------------
    //To return the index of customer in array if exists
    public static int findCustomerById(Scanner sc, Customer[] customers){
        String id;
        int index = -1;
        System.out.print("Enter Customer Id: ");

        id = sc.next();

        for(int i = 0; i < customers.length; i++){
            if(customers[i] != null && customers[i].getId().equals(id)){
                index = i;
                return index;
            }
        }

        return index;
    }


}
