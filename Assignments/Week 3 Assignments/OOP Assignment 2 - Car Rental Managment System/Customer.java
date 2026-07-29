

public class Customer {

//Fields
private final String id;
private String name , phone , rentedCarId = "-1";
private double totalPaid = 0;
private int rentedDays = 0;
private static int customersCount = 0;

//--------------

    //Constructor
    public Customer(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;

        customersCount++;
    }

//--------------
    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRentedCarId(String rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    public void setRentedDays(int rentedDays) {

        if (rentedDays < 0)
            throw new IllegalArgumentException("Rental days cannot be negative.");

        this.rentedDays = rentedDays;
    }

    public void raiseTotalPaid(double paid) {

        if (paid < 0)
            throw new IllegalArgumentException("Paid amount cannot be negative.");

        totalPaid += paid;
    }

//------------------
    //Record and Clear Rentals

    public void recordRental(String rentedCarId, int rentedDays, double paid) {

        if (rentedCarId == null)
            throw new IllegalArgumentException("Invalid car ID.");

        if (rentedDays <= 0)
            throw new IllegalArgumentException("Rental days must be greater than zero.");

        if (paid < 0)
            throw new IllegalArgumentException("Invalid payment.");

        this.rentedCarId = rentedCarId;
        this.rentedDays = rentedDays;
        totalPaid += paid;
    }

    public void clearRental(){
        this.rentedCarId = "-1";
        this.rentedDays = 0;
    }

//------------------

    //Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRentedCarId() {
        return rentedCarId;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public int getRentedDays() {
        return rentedDays;
    }

    public static int getCustomersCount() {
        return customersCount;
    }


    public String getInfo() {
        return "\nID: " + id +
                "\nName: " + name +
                "\nPhone: " + phone +
                "\nRented Car ID: " + rentedCarId +
                "\nRented Days: " + rentedDays +
                "\nTotal Paid: " + totalPaid;
    }

}

