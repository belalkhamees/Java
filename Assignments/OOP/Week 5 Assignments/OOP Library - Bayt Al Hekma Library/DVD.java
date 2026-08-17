public class DVD extends LibraryItem {

    private final int runtime;

    public DVD(int id, String title, int runtime) {
        super(id, title);
        this.runtime = runtime;
    }

    public int getRuntime() {
        return runtime;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        if (daysOverdue <= 0) {
            return 0.0;
        }

        return daysOverdue * 15.00;
    }

    @Override
    public int getLoanPeriod() {
        return 3;
    }

    @Override
    public String getCategory() {
        return "DVD";
    }
}
