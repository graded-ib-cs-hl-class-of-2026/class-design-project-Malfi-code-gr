public class App {
    private Library myLibrary;
    private Printer printer = new Printer();

    public void start() {
        myLibrary = readLibraryData("library.json");
        displayMenu();
    }

    public Library readLibraryData(String filename) {
        Library library = new Library();
        // JSON parsing logic to read library data from the file
        return library;
    }

    public void displayMenu() {
        printer.output("Welcome to The Library®. Please select an option:");
        printer.output("[1] Check Out Book");
        printer.output("[2] Return Book");
        printer.output("[3] Search Book");
        printer.output("[4] Add Book");
        printer.output("[5] Remove Book");
        printer.output("[6] Add Student");
        printer.output("[7] Remove Student");
        printer.output("[8] Exit");


        int choice = printer.inputInt();
        if (choice == 1) {
            int studentId;
            while (true) {
                printer.output("Enter student ID:");
                studentId = printer.inputInt();
                if (myLibrary.findStudentById(studentId) != null) {
                    break;
                } else {
                    printer.output("Student not found. Please try again.");
                }
            }
            printer.output("Enter ISBN of the book:");
            int isbn = printer.inputInt();
            printer.output("Please enter the number of days before returning. Enter nothing for default (14)");
            String input = printer.input();
            int days = 0;
            if (!input.isEmpty()) {
                try {
                    days = Integer.parseInt(input);
                    myLibrary.checkOutBook(studentId, isbn, days);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Defaulting to 14 days.");
                    myLibrary.checkOutBook(studentId, isbn);
                }
            } else {
                myLibrary.checkOutBook(studentId, isbn);
            }
        } else if (choice == 2) {
            System.out.println("Enter ISBN of the book you'd like to return:");
            int isbn = printer.inputInt();
            myLibrary.returnBook(isbn);
        } else if (choice == 3) {
            Book book;
            while (true) {
                printer.output("Enter the ISBN of the book you'd like to find:");
                int isbn = printer.inputInt();
                book = myLibrary.findBookByIsbn(isbn);
                if (book != null) {
                    if (book.isCheckedOut()) {
                        printer.output("The book \"" + book.getTitle() + "\" is currently checked out by " + book.getStudentId() + ".");
                    } else if (book.isLost()) {
                        printer.output("The book \"" + book.getTitle() + "\" is marked as lost.");
                    } else {
                        printer.output("The book \"" + book.getTitle() + "\" is available.");
                    }
                } else {
                    printer.output("Book not found. Please try again or type -1 to cancel.");
                    int cancel = printer.inputInt();
                    if (cancel == -1) {
                        break;
                    }
                }
            }
        } else if (choice == 4) {
            // addBook();
        } else if (choice == 5) {
            // removeBook();
        } else if (choice == 6) {
            // addStudent();
        } else if (choice == 7) {
            // removeStudent();
        } else if (choice == 8) {
            System.out.println("Thank you for using The Library®. Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
        displayMenu();
    }

    public static void main(String[] args) throws Exception {
        new App().start();
    }
}
