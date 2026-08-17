import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Library library = new Library();

    public static void main(String[] args) {

        addInitialData();

        int choice;

        do {
            printMenu();
            choice = Validation.readMenuChoice(sc);

            switch (choice) {
                case 1:
                    library.viewCatalogue();
                    break;

                case 2:
                    registerMember();
                    break;

                case 3:
                    borrowItem();
                    break;

                case 4:
                    returnItem();
                    break;

                case 5:
                    renewLoan();
                    break;

                case 6:
                    searchItem();
                    break;

                case 7:
                    viewItemsByStatus();
                    break;

                case 8:
                    payOutstandingFine();
                    break;

                case 9:
                    library.viewMembers();
                    break;

                case 10:
                    library.printReport();
                    break;

                case 0:
                    System.out.println("\nThank you for using bayt al hekma library management system.");
                    break;

                default:
                    System.out.println("Invalid choice. Please choose from 0 to 10.");
            }

            System.out.println();

        } while (choice != 0);

        sc.close();
    }


    private static void printMenu() {
        System.out.println("========== " + LibraryItem.getLibraryName() + " ==========");
        System.out.println("1. View catalogue");
        System.out.println("2. Register member");
        System.out.println("3. Borrow item");
        System.out.println("4. Return item");
        System.out.println("5. Renew loan");
        System.out.println("6. Search item by ID");
        System.out.println("7. View items by status");
        System.out.println("8. Pay outstanding fines");
        System.out.println("9. View all members");
        System.out.println("10. Library report");
        System.out.println("0. Exit");
        System.out.println("======================================");
    }


    private static void registerMember() {
        String name = Validation.readName(sc, "Enter member name: ");
        int id = Validation.readPositiveInt(sc, "Enter membership ID: ");

        if (library.findMemberById(id) != null) {
            System.out.println("Membership ID already exists.");
            return;
        }

        MembershipType category = Validation.readMembershipType(sc);

        Member member = new Member(name, id, category);

        if (library.registerMember(member)) {
            System.out.println("Member registered successfully.");
        } else {
            System.out.println("Member could not be registered.");
        }
    }

    private static void borrowItem() {
        int itemId = Validation.readPositiveInt(sc, "Enter item ID: ");
        int memberId = Validation.readPositiveInt(sc, "Enter membership ID: ");

        library.lendItem(itemId, memberId);
    }

    private static void returnItem() {
        int itemId = Validation.readPositiveInt(sc, "Enter item ID: ");
        int daysOverdue = Validation.readNonNegativeInt(sc, "Enter days overdue: ");

        library.returnItem(itemId, daysOverdue);
    }

    private static void renewLoan() {
        int itemId = Validation.readPositiveInt(sc, "Enter item ID: ");

        library.renewItem(itemId);
    }

    private static void searchItem() {
        int itemId = Validation.readPositiveInt(sc, "Enter item ID: ");

        library.searchItem(itemId);
    }

    private static void viewItemsByStatus() {
        ItemStatus status = Validation.readItemStatus(sc);

        library.viewItemsByStatus(status);
    }

    private static void payOutstandingFine() {
        int memberId = Validation.readPositiveInt(sc, "Enter membership ID: ");
        double amount = Validation.readPositiveDouble(sc, "Enter payment amount: ");

        library.payFine(memberId, amount);
    }

    private static void addInitialData() {
        library.registerItem(new Book(1, "Clean Code", "Robert Martin", 464));
        library.registerItem(new Book(2, "Effective Java", "Joshua Bloch", 416));
        library.registerItem(new Magazine(3, "Java Monthly", 15));
        library.registerItem(new DVD(4, "Java Programming Course", 120));

        library.registerMember(new Member("Ahmed Mohamed", 1, MembershipType.STUDENT));
        library.registerMember(new Member("ali Mohamed", 2, MembershipType.STAFF));
        library.registerMember(new Member("belal khamees", 3, MembershipType.PUBLIC, 50.00));
    }
}
