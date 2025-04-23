import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class App {
    private Library myLibrary;
    private Printer printer = new Printer();

    // ANSI color codes
    private static final String WHITE = "\u001B[37m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String LIGHT_BLUE = "\u001B[94m";

    public void start() {
        myLibrary = readLibraryData("library.json");
        displayTitle();
        displayMenu();
    }

    public Library readLibraryData(String filename) {
        Library library = new Library();
    
        try {
            printer.openFile(filename);
    
            String str = "";
            while (printer.fileHasNextLine()) {
                str = str + printer.fileGetNextLine() + "\n";
            }
            printer.closeFile();
            String json = str.toString();
    
            Pattern bookPattern = Pattern.compile(
                // This looks really scary but it's really not.
                // Basically, this is a pattern which the reader will try to match against the JSON string. Below is a key for interpreting the jargon.
                // Curly bracket = \\{ 
                // Any amount of whitespace = \\s*
                // Any string value in between quotes = \"([^\"]+)\"
                // Any number in between quotes = \"(\\d+)\"
                // The string "title" = \"title\"
                // The string "author" = \"author\"
                // The string "ISBN" = \"ISBN\"
                "\\{\\s*\"title\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"author\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"ISBN\"\\s*:\\s*\"(\\d+)\"\\s*\\}"
            );
            Matcher bookMatcher = bookPattern.matcher(json);
            while (bookMatcher.find()) {
                String title = bookMatcher.group(1);
                String author = bookMatcher.group(2);
                long isbn = Long.parseLong(bookMatcher.group(3));
                library.addBook(title, author, isbn);
            }

            Pattern studentPattern = Pattern.compile(
                // Same thing as above but it looks for a different thing
                "\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"ID\"\\s*:\\s*(\\d+)\\s*,\\s*\"checkedOutBooks\"\\s*:\\s*\\[(.*?)\\]\\s*\\}",
                Pattern.DOTALL
            );
            Matcher studentMatcher = studentPattern.matcher(json);
            while (studentMatcher.find()) {
                String studentName = studentMatcher.group(1);
                int studentId = Integer.parseInt(studentMatcher.group(2));

                library.addStudent(studentName, studentId);
                Student student = library.findStudentById(studentId);
                String booksBlock = studentMatcher.group(3);

                // captures title, checkout, due, returned (or null)
                Pattern recordPattern = Pattern.compile(
                    "\\{\\s*\"title\"\\s*:\\s*\"([^\"]+)\"\\s*," +
                    "\\s*\"checkOutDate\"\\s*:\\s*\"(\\d{4}-\\d{2}-\\d{2})\"\\s*," +
                    "\\s*\"dueDate\"\\s*:\\s*\"(\\d{4}-\\d{2}-\\d{2})\"\\s*," +
                    "\\s*\"returnedDate\"\\s*:\\s*(?:\"(\\d{4}-\\d{2}-\\d{2})\"|(null))" +
                    "\\s*\\}",
                    Pattern.DOTALL
                );

                Matcher recM = recordPattern.matcher(booksBlock);
                while (recM.find()) {
                    String title = recM.group(1);
                    LocalDate checkout = LocalDate.parse(recM.group(2));
                    LocalDate due = LocalDate.parse(recM.group(3));
                    String retString = recM.group(4);
                
                    LocalDate returned;
                    if (retString != null) {
                        returned = LocalDate.parse(retString);
                    } else {
                        returned = null;
                    }
                
                    Book book = library.findBookByTitle(title);
                    if (book != null) {
                        book.setCheckedOut(returned == null);
                        book.setStudentId(studentId);
                        student.addLoanRecord(book, checkout, due, returned);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            printer.output("Error: File not found.");
            e.printStackTrace();
        }
    
        return library;
    }    

    public void displayTitle() { 
        printer.output("\n" +
                        "████████╗██╗░░██╗███████╗  ██╗░░░░░██╗██████╗░██████╗░░█████╗░██████╗░██╗░░░██╗\n"+
                        "╚══██╔══╝██║░░██║██╔════╝  ██║░░░░░██║██╔══██╗██╔══██╗██╔══██╗██╔══██╗╚██╗░██╔╝\n"+
                        "░░░██║░░░███████║█████╗░░  ██║░░░░░██║██████╦╝██████╔╝███████║██████╔╝░╚████╔╝░\n"+
                        "░░░██║░░░██╔══██║██╔══╝░░  ██║░░░░░██║██╔══██╗██╔══██╗██╔══██║██╔══██╗░░╚██╔╝░░\n"+
                        "░░░██║░░░██║░░██║███████╗  ███████╗██║██████╦╝██║░░██║██║░░██║██║░░██║░░░██║░░░\n"+
                        "░░░╚═╝░░░╚═╝░░╚═╝╚══════╝  ╚══════╝╚═╝╚═════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░");
    }

    public void displayMenu() {
        printer.output("\n╭───────────────────────────────────────────────────╮");
        printer.output("│[1] Check Out Book                                 │");
        printer.output("│[2] Return Book                                    │");
        printer.output("│[3] Search Book                                    │");
        printer.output("│[4] List All Books                                 │");
        printer.output("│[5] Add Book                                       │");
        printer.output("│[6] Remove Book                                    │");
        printer.output("│[7] Mark Book Lost                                 │");
        printer.output("│[8] Add Student                                    │");
        printer.output("│[9] Remove Student                                 │");
        printer.output("│[10] Search Student                                │");
        printer.output("│[11] View Student Records                          │");
        printer.output("│[12] Exit The Library®                             │");
        printer.output("╰───────────────────────────────────────────────────╯");

        int choice = printer.inputInt();
        if (choice == 1) { // Check Out Book
            boolean cancel = false;
            int studentId;
            while (true) {
                printer.output("Enter student ID (or -1 to cancel):");
                studentId = printer.inputInt();
                if (studentId == -1) {
                    printer.output("Returning to the main menu.");
                    cancel = true;
                    break;
                }
                if (myLibrary.findStudentById(studentId) != null) {
                    break;
                } else {
                    printer.output("Student not found. Please try again or press -1 to go back.");
                }
            }
            if (!cancel) {
                printer.output("Enter ISBN of the book (or -1 to cancel):");
                long isbn = printer.inputLong();
                if (isbn == -1) {
                    printer.output("Returning to the main menu.");
                } else {
                    Book book = myLibrary.findBookByIsbn(isbn);
                    while (book == null) {
                        printer.output("Book not found. Please input a valid ISBN or type -1 to cancel:");
                        isbn = printer.inputLong();
                        if (isbn == -1) {
                            printer.output("Returning to the main menu.");
                            break;
                        }
                        book = myLibrary.findBookByIsbn(isbn);
                    }
                    printer.output("Please enter the number of days before returning. Enter nothing for default (14)");
                    String input = printer.input();
                    int days = 0;
                    if (!input.isEmpty()) {
                        try {
                            days = Integer.parseInt(input);
                            myLibrary.checkOutBook(studentId, isbn, days);
                        } catch (NumberFormatException e) {
                            printer.output("Invalid input. Defaulting to 14 days.");
                            myLibrary.checkOutBook(studentId, isbn);
                        }
                    } else {
                        myLibrary.checkOutBook(studentId, isbn);
                    }
                }
            }        
        } else if (choice == 2) { // Return Book
            printer.output("Enter ISBN of the book you'd like to return (or -1 to cancel):");
            long isbn = printer.inputLong();
            if (isbn == -1) {
                printer.output("Returning to the main menu.");
            } else {
                Book book = myLibrary.findBookByIsbn(isbn);
                while (book == null) {
                    printer.output("Book not found. Please input a valid ISBN or type -1 to cancel:");
                    isbn = printer.inputLong();
                    if (isbn == -1) {
                        printer.output("Returning to the main menu.");
                        break;
                    }
                    book = myLibrary.findBookByIsbn(isbn);
                }
                if (isbn != -1 && book != null) {
                    if (book.isCheckedOut()) {
                        book.setCheckedOut(false);
                        myLibrary.findStudentById(book.getStudentId()).returnBook(book);
                        book.setStudentId(0);
                        printer.output("The book \"" + book.getTitle() + "\" has been returned successfully.");
                    } else {
                        printer.output("This book was not checked out.");
                    }
                }
            }
        } else if (choice == 3) { // Search Book
            while (true) {
                printer.output("Search by: [1] ISBN, [2] Name (or -1 to cancel):");
                int searchType = printer.inputInt();
                if (searchType == -1) {
                    printer.output("Returning to the main menu.");
                    break; // exit choice 3
                }
                if (searchType == 1) { // ISBN
                    while (true) {
                        printer.output("Enter the ISBN of the book you'd like to find (or type -1 to cancel):");
                        long isbn = printer.inputLong();
                        if (isbn == -1) {
                            printer.output("Returning to the main menu.");
                            break;
                        }
                        Book book = myLibrary.findBookByIsbn(isbn);
                        if (book == null) {
                            printer.output("Book not found. Please try again or type -1 to cancel.");
                            continue;
                        }
                        if (book.isCheckedOut()) {
                            printer.output("Title: " + RED + book.getTitle() +
                                           CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                                           CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                                           CYAN + ", Checked out by: " + YELLOW +
                                           myLibrary.findStudentById(book.getStudentId()).getName());
                        } else if (book.isLost()) {
                            printer.output("Title: " + RED + book.getTitle() +
                                           CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                                           CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                                           CYAN + ", Status: " + RED + "Lost");
                        } else {
                            printer.output("Title: " + RED + book.getTitle() +
                                           CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                                           CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                                           CYAN + ", Status: " + GREEN + "Available");
                        }
                        break; // Name flow
                    }
                    break; // searchType loop
                }
                if (searchType == 2) { // Name
                    while (true) {
                        printer.output("Enter the name of the book you'd like to find (or type -1 to cancel):");
                        String bookName = printer.input();
                        if (bookName.equals("-1")) {
                            printer.output("Returning to the main menu.");
                            break;
                        }
                        Book book = myLibrary.findBookByTitle(bookName);
                        if (book == null) {
                            printer.output("No books found with the name \"" + bookName + "\".");
                            continue;
                        }
                        if (book.isCheckedOut()) {
                            printer.output("Title: " + RED + book.getTitle() +
                                           CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                                           CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                                           CYAN + ", Checked out by: " + YELLOW +
                                           myLibrary.findStudentById(book.getStudentId()).getName());
                        } else if (book.isLost()) {
                            printer.output("Title: " + RED + book.getTitle() +
                                           CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                                           CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                                           CYAN + ", Status: " + RED + "Lost");
                        } else {
                            printer.output("Title: " + RED + book.getTitle() +
                                           CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                                           CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                                           CYAN + ", Status: " + GREEN + "Available");
                        }
                        break; // Name flow
                    }
                    break; // searchType loop
                }
                printer.output("Invalid input. Please enter 1, 2, or -1 to cancel.");
            }
        } else if (choice == 4) { // List All Books
            printer.output("List of all books in the library:\n-=-=-=-=-=-=-=-=-=-=-=-=-");
            for (int i = 0; i < myLibrary.getBookCount(); i++) {
                Book book = myLibrary.getBooks()[i];
                if (book.isCheckedOut()) {
                    printer.output("Title: " + RED + book.getTitle() + 
                    CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                    CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                    CYAN + ", Checked out by: " + YELLOW + myLibrary.findStudentById(book.getStudentId()).getName());
                } else if (book.isLost()) {
                    printer.output("Title: " + RED + book.getTitle() + 
                    CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                    CYAN + ", ISBN: " + GREEN + book.getIsbn() + 
                    CYAN + ", Status: " + RED + "Lost");
                } else {
                    printer.output("Title: " + RED + book.getTitle() + 
                    CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                    CYAN + ", ISBN: " + GREEN + book.getIsbn() + 
                    CYAN + ", Status: " + GREEN + "Available");
                }
            }
            printer.output("-=-=-=-=-=-=-=-=-=-=-=-=-");
        } else if (choice == 5) { // Add Book
            boolean cancelAdd = false;
            while (!cancelAdd) {
                String bookTitle;
                while (true) {
                    printer.output("Enter book title (or type -1 to cancel):");
                    bookTitle = printer.input().trim();
                    if (bookTitle.equals("-1")) {
                        printer.output("Returning to the main menu.");
                        cancelAdd = true;
                        break;
                    }
                    if (!bookTitle.isEmpty()) {
                        break;
                    }
                    printer.output("Invalid input. Title cannot be empty. Please try again.");
                }
                if (cancelAdd) break;
        
                String bookAuthor;
                while (true) {
                    printer.output("Enter book author (or type -1 to cancel):");
                    bookAuthor = printer.input().trim();
                    if (bookAuthor.equals("-1")) {
                        printer.output("Returning to the main menu.");
                        cancelAdd = true;
                        break;
                    }
                    if (!bookAuthor.isEmpty()) {
                        break;
                    }
                    printer.output("Invalid input. Author cannot be empty. Please try again.");
                }
                if (cancelAdd) break;

                long bookIsbn;
                while (true) {
                    printer.output("Enter book ISBN-13 (13 digits without dashes, or type -1 to cancel):");
                    bookIsbn = printer.inputLong();
                    if (bookIsbn == -1) {
                        printer.output("Returning to the main menu.");
                        cancelAdd = true;
                        break;
                    }
                    String s = String.valueOf(bookIsbn);
                    if (s.length() != 13) {
                        printer.output("Invalid input. ISBN must be a 13-digit number without dashes. Please try again.");
                        continue;
                    }
                    boolean exists = false;
                    for (int i = 0; i < myLibrary.getBookCount(); i++) {
                        if (myLibrary.getBooks()[i].getIsbn() == bookIsbn) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        printer.output("A book with this ISBN already exists. Please enter a different ISBN.");
                        continue;
                    }
                    break;
                }
                if (cancelAdd) break;

                myLibrary.addBook(bookTitle, bookAuthor, bookIsbn);
                printer.output("Book \"" + bookTitle + "\" by " + bookAuthor + " has been added successfully.");
                break;
            }
        }
        else if (choice == 6) { // Remove Book
            printer.output("Enter ISBN of the book you'd like to remove (or -1 to cancel):");
            long isbn = printer.inputLong();
            if (isbn == -1) {
                printer.output("Returning to the main menu.");
            } else {
                Book book = myLibrary.findBookByIsbn(isbn);
                if (book != null) {
                    myLibrary.removeBook(isbn);
                    printer.output("The book \"" + book.getTitle() + "\" has been removed successfully.");
                } else {
                    printer.output("Book not found. Please try again or type -1 to cancel.");
                }
            }
        } else if (choice == 7) { // Mark Lost
            while (true) {
                printer.output("Enter ISBN of the book you'd like to mark as lost (or -1 to cancel):");
                long isbn = printer.inputLong();
                if (isbn == -1) {
                    printer.output("Returning to the main menu.");
                    break;
                }
                Book book = myLibrary.findBookByIsbn(isbn);
                if (book != null) {
                    book.setLost(true);
                    printer.output("The book \"" + book.getTitle() + "\" has been marked as lost.");
                    break;
                } else {
                    printer.output("Book not found. Please try again or type -1 to cancel.");
                }
            }
        } else if (choice == 8) { // Add Student
            printer.output("Enter student's name (or type -1 to cancel):");
            String studentName = printer.input();
            if (studentName.equals("-1")) {
                printer.output("Returning to the main menu.");
            } else {
                printer.output("Enter student ID (or -1 to cancel):");
                int studentId = printer.inputInt();
                if (studentId == -1) {
                    printer.output("Returning to the main menu.");
                } else {
                    if (myLibrary.findStudentById(studentId) != null) {
                        printer.output("A student with this ID already exists. Returning to the main menu.");
                    } else {
                        myLibrary.addStudent(studentName, studentId);
                        printer.output("Student " + studentName + " (ID: " + studentId + ") has been added successfully.");
                    }
                }
            }
        } else if (choice == 9) { // Remove Student
            while (true) {
                printer.output("Enter student ID of the student you'd like to remove (or -1 to cancel):");
                int studentId = printer.inputInt();
                if (studentId == -1) {
                    printer.output("Returning to the main menu.");
                    break;
                }
                Student student = myLibrary.findStudentById(studentId);
                if (student != null) {
                    myLibrary.removeStudent(studentId);
                    printer.output("The student " + student.getName() + " has been removed successfully.");
                    break;
                } else {
                    printer.output("Student not found. Please try again or type -1 to cancel.");
                }
            }
        } else if (choice == 10) { // Search Student
            while (true) {
                printer.output("Search student by: [1] ID, [2] Name (or -1 to cancel):");
                int searchType = printer.inputInt();
                if (searchType == -1) {
                    printer.output("Returning to the main menu.");
                    break;
                }
                if (searchType == 1) { // ID
                    while (true) {
                        printer.output("Enter student ID (or -1 to cancel):");
                        int studentId = printer.inputInt();
                        if (studentId == -1) {
                            printer.output("Returning to the main menu.");
                            break;
                        }
                        Student student = myLibrary.findStudentById(studentId);
                        if (student != null) {
                            printer.output("Student ID: " + WHITE + student.getId() +
                                           CYAN + ", Name: " + GREEN + student.getName() +
                                           CYAN + ", Active Loans: " + RED + student.getActiveLoanCount());
                            break;
                        } else {
                            printer.output("Student not found. Please try again or type -1 to cancel.");
                        }
                    }
                    break;
                }
                if (searchType == 2) { // Name
                    while (true) {
                        printer.output("Enter student name (or type -1 to cancel):");
                        String studentName = printer.input();
                        if (studentName.equals("-1")) {
                            printer.output("Returning to the main menu.");
                            break;
                        }
                        Student student = myLibrary.findStudentByName(studentName);
                        if (student != null) {
                            printer.output("Student ID: " + WHITE + student.getId() +
                                           CYAN + ", Name: " + GREEN + student.getName() +
                                           CYAN + ", Active Loans: " + RED + student.getActiveLoanCount());
                            break;
                        } else {
                            printer.output("Student not found. Please try again or type -1 to cancel.");
                        }
                    }
                    break;
                }
                printer.output("Invalid input. Please enter 1, 2, or -1 to cancel.");
            }
        } else if (choice == 11) { // View Student Records
            while (true) {
                printer.output("Enter student ID (or -1 to cancel):");
                int studentId = printer.inputInt();
                if (studentId == -1) {
                    printer.output("Returning to the main menu.");
                    break;
                }
                Student student = myLibrary.findStudentById(studentId);
                if (student != null) {
                    printer.output("Student ID: " + WHITE + student.getId() + CYAN + ", Name: " + GREEN + student.getName() +
                    CYAN + ", Loan Count: " + RED + student.getLoanCount());
                    for (int j = 0; j < student.getLoanCount(); j++) {
                        LoanRecord record = student.getLoanRecords()[j];
                        if (record != null) {
                            // current time has passed due date (LocalTime)
                            if (record.getDueDate().isBefore(LocalDate.now()) && record.getReturnedDate() == null) {
                                printer.output(RED + "OVERDUE  " + CYAN + "││  \"" + YELLOW + record.getBook().getTitle() + CYAN +
                                "\", Check Out Date: " + record.getCheckOutDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + CYAN +
                                ", Due Date: " + WHITE + record.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + CYAN +
                                ", Returned Date: " + RED + (record.getReturnedDate() != null ? record.getReturnedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "Not returned"));
                            } else if (record.getReturnedDate() == null) {
                                printer.output(LIGHT_BLUE + "ACTIVE" + CYAN + "   ││ \"" + YELLOW + record.getBook().getTitle() + CYAN +
                                "\", Check Out Date: " + record.getCheckOutDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + CYAN +
                                ", Due Date: " + WHITE + record.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + CYAN +
                                ", Returned Date: " + GREEN + (record.getReturnedDate() != null ? record.getReturnedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "Not returned"));
                            } else {
                                printer.output(GREEN + "RETURNED " + CYAN + "││ \"" + YELLOW + record.getBook().getTitle() + CYAN +
                                "\", Check Out Date: " + record.getCheckOutDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + CYAN +
                                ", Due Date: " + WHITE + record.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + CYAN +
                                ", Returned Date: " + GREEN + (record.getReturnedDate() != null ? record.getReturnedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "Not returned"));
                            }
                        }
                    }
                    break;
                } else {
                    printer.output("Student not found. Please try again or type -1 to cancel.");
                }
            }
        } else if (choice == 12) { // Exit
            while (true) {
                printer.output("Would you like to save? (Y/N or -1 to cancel):");
                String response = printer.input();
                if (response.equalsIgnoreCase("Y")) {
                    try {
                        printer.saveFile("library.json", myLibrary);
                        printer.output("Library data saved successfully.");
                        System.exit(0);
                    } catch (IOException e) {
                        printer.output("Error saving library data: " + e.getMessage());
                    }
                    break;
                } else if (response.equalsIgnoreCase("N")) {
                    printer.output("Library data not saved.");
                    printer.output("Thank you for using The Library®. Goodbye!");
                    System.exit(0);
                } else if (response.equals("-1")) {
                    printer.output("Exit operation canceled.");
                    break;
                } else {
                    printer.output("Invalid input. Please enter Y, N, or -1 to cancel.");
                }
            }
        } else {
            printer.output("Invalid choice. Please try again.");
        }
        // set timeout for 1.5 seconds before displaying menu
        try {
            Thread.sleep(1500);
            displayMenu();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new App().start();
    }
}
