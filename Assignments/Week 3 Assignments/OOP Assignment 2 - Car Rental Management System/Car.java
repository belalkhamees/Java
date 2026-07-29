

public class Car {

//Fields
private final String id , brand , model;
private final int year;
private double pricePerDay;
private boolean available = true;
private static final double TAX_RATE = 0.14;
private static int carsCount = 0;

//--------------

    //Constructor
    public Car(String id, String brand, String model, int year, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = validateYear(year);
        setPricePerDay(pricePerDay);

        carsCount++;
    }

//--------------
    //Setters
    public void setPricePerDay(double pricePerDay) {

    if (pricePerDay <= 0)
        throw new IllegalArgumentException("Price per day must be greater than zero.");

    this.pricePerDay = pricePerDay;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    //Validate year in constructor because it has no setter
    private int validateYear(int year) {
        if (year < 1990 || year > 2026)
            throw new IllegalArgumentException("Manufacturing year must be between 1990 and 2026." );

        return year;
    }


//--------------
    //Operations
    public double calculateRentalCost(int rentedDays){
        double cost = rentedDays * pricePerDay;
        double tax = cost * TAX_RATE;
        return cost + tax;
    }

    //Availability Operations
    public void markAsRented() {
        available = false;
    }

    public void returnCar() {
        available = true;
    }

    public boolean isAvailable() {
        return available;
    }

//-----------------
    //Getters
    public String getId() {
    return id;
}

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public static int getCarsCount() {
        return carsCount;
    }

    public static double getTaxRate(){
        return TAX_RATE;
    }


    public String getInfo() {
        return "ID: " + id +
                "\nBrand: " + brand +
                "\nModel: " + model +
                "\nYear: " + year +
                "\nPrice Per Day: " + pricePerDay +
                "\nIs Available: " + available ;
    }

}

