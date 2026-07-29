

public class LuxuryCar extends Car{

//Fields
private final double insuranceFee;
private static final int MIN_RENT_DAYS = 3;    // minimum number of rental days

//--------------

    //Constructor
    public LuxuryCar(String id, String brand, String model, int year, double pricePerDay, double insuranceFee) {

        super(id, brand, model, year, pricePerDay);

        if (insuranceFee < 0)
            throw new IllegalArgumentException("Insurance fee cannot be negative!");

        this.insuranceFee = insuranceFee;
    }

    //Getters
    public static int getMinRentDays() {
        return MIN_RENT_DAYS;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    @Override
    public String getInfo(){
        return super.getInfo() +
                "\nInsurance Fee: " + insuranceFee;
    }

    //Operations
    @Override
    public double calculateRentalCost(int rentedDays){
        double cost = rentedDays * getPricePerDay() + insuranceFee;
        double tax = cost * Car.getTaxRate();
        return cost + tax;
    }




}

