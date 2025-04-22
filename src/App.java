import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.*;

public class App {
    private Library myLibrary;
    private Printer printer = new Printer();

    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String WHITE = "\u001B[37m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
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
                String checkedOutBooksContent = studentMatcher.group(3);
    
                library.addStudent(studentName, studentId);
                Student student = library.findStudentById(studentId);
    
                Pattern titlePattern = Pattern.compile("\"title\"\\s*:\\s*\"([^\"]+)\"");
                Matcher titleMatcher = titlePattern.matcher(checkedOutBooksContent);
                while (titleMatcher.find()) {
                    String bookTitle = titleMatcher.group(1);
                    Book book = library.findBookByTitle(bookTitle);
                    if (book != null) {
                        book.setCheckedOut(true);
                        book.setStudentId(studentId);
                        student.borrowBook(book);
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
        printer.output("│[3] Search Book by ISBN                            │");
        printer.output("│[4] Search Book by Name                            │");
        printer.output("│[5] List All Books                                 │");
        printer.output("│[6] Add Book                                       │");
        printer.output("│[7] Remove Book                                    │");
        printer.output("│[8] Add Student                                    │");
        printer.output("│[9] Remove Student                                 │");
        printer.output("│[10] Exit The Library®                             │");
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
                        book.setStudentId(0);
                        printer.output("The book \"" + book.getTitle() + "\" has been returned successfully.");
                    } else {
                        printer.output("This book was not checked out.");
                    }
                }
            }
        } else if (choice == 3) { // Search Book by ISBN
            boolean cancelSearch = false;
            while (!cancelSearch) {
                printer.output("Enter the ISBN of the book you'd like to find (or type -1 to cancel):");
                long isbn = printer.inputLong();
                if (isbn == -1) {
                    printer.output("Returning to the main menu.");
                    break;
                }
                Book book = myLibrary.findBookByIsbn(isbn);
                if (book != null) {
                    if (book.isCheckedOut()) {
                        printer.output("The book \"" + book.getTitle() + "\" is currently checked out by " +
                                        myLibrary.findStudentById(book.getStudentId()).getName() + ".");
                    } else if (book.isLost()) {
                        printer.output("The book \"" + book.getTitle() + "\" is marked as lost.");
                    } else {
                        printer.output("The book \"" + book.getTitle() + "\" is available. Would you like to check it out? (Y/N)");
                        String response = printer.input();
                        if (response.equalsIgnoreCase("Y")) {
                            boolean cancelThisCheckout = false;
                            int studentId;
                            while (true) {
                                printer.output("Enter student ID (or -1 to cancel):");
                                studentId = printer.inputInt();
                                if (studentId == -1) {
                                    printer.output("Returning to the main menu.");
                                    cancelThisCheckout = true;
                                    break;
                                }
                                if (myLibrary.findStudentById(studentId) != null) {
                                    break;
                                } else {
                                    printer.output("Student not found. Please try again or press -1 to go back.");
                                }
                            }

                            if (cancelThisCheckout) {
                                cancelSearch = true;
                                break;
                            }
                            int days;
                            while (true) {
                                printer.output("Please enter the number of days before returning or press Enter for default (14)");
                                String line = printer.input();
                                if (line.isEmpty()) {
                                    days = 14;
                                    break;
                                }
                                try {
                                    days = Integer.parseInt(line);
                                    if (days > 0) {
                                        break;
                                    }
                                    printer.output("Please enter a positive number or just hit Enter.");
                                } catch (NumberFormatException e) {
                                    printer.output("Invalid input. Please enter a positive number or just hit Enter.");
                                }
                            }

                            // now you have a guaranteed positive `days` (or 14), so:
                            myLibrary.checkOutBook(studentId, isbn, days);
                            break;
                        } else {
                            printer.output("Returning to the main menu.");
                            break;
                        }
                    }
                } else {
                    printer.output("Book not found. Please try again or type -1 to cancel.");
                }
            }
        } else if (choice == 4) { // Search Book by Name
            while (true) {
                printer.output("Enter the name of the book you'd like to find (or type -1 to cancel):");
                String bookName = printer.input();
                if (bookName.equals("-1")) {
                    printer.output("Returning to the main menu.");
                    break;
                } else {
                    Book book = myLibrary.findBookByTitle(bookName);
                    if (book == null) {
                        printer.output("No books found with the name \"" + bookName + "\".");
                        // ask for input again
                    } else {
                        if (book.isCheckedOut()) {
                            printer.output("Title: " + RED + book.getTitle() + 
                            CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                            CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                            CYAN + ", Checked out by: " + MAGENTA + myLibrary.findStudentById(book.getStudentId()).getName());
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
                        break;
                    }
                }
            }
        } else if (choice == 5) { // List All Books
            printer.output("List of all books in the library:\n-=-=-=-=-=-=-=-=-=-=-=-=-");
            for (int i = 0; i < myLibrary.getBookCount(); i++) {
                Book book = myLibrary.getBooks()[i];
                if (book.isCheckedOut()) {
                    printer.output("Title: " + RED + book.getTitle() + 
                    CYAN + ", Author: " + LIGHT_BLUE + book.getAuthor() +
                    CYAN + ", ISBN: " + GREEN + book.getIsbn() +
                    CYAN + ", Checked out by: " + MAGENTA + myLibrary.findStudentById(book.getStudentId()).getName());
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
        } else if (choice == 6) { // Add Book
            while (true) {
                printer.output("Enter book title (or type -1 to cancel):");
                String bookTitle = printer.input().trim();
                if (bookTitle.equals("-1")) {
                    printer.output("Returning to the main menu.");
                    break;
                } else if (bookTitle.isEmpty()) {
                    printer.output("Invalid input. Title cannot be empty. Please try again.");
                    continue;
                }

                printer.output("Enter book author (or type -1 to cancel):");
                String bookAuthor = printer.input().trim();
                if (bookAuthor.equals("-1")) {
                    printer.output("Returning to the main menu.");
                    break;
                } else if (bookAuthor.isEmpty()) {
                    printer.output("Invalid input. Author cannot be empty. Please try again.");
                    continue;
                }

                printer.output("Enter book ISBN-13 (13 digits without dashes, or type -1 to cancel):");
                long bookIsbn = printer.inputLong();
                if (bookIsbn == -1) {
                    printer.output("Returning to the main menu.");
                    break;
                } else if (String.valueOf(bookIsbn).length() != 13) {
                    printer.output("Invalid input. ISBN must be a 13-digit number without dashes. Please try again.");
                    continue;
                }

                boolean isbnExists = false;
                for (int i = 0; i < myLibrary.getBookCount(); i++) {
                    if (myLibrary.getBooks()[i].getIsbn() == bookIsbn) {
                        isbnExists = true;
                        break;
                    }
                }

                if (isbnExists) {
                    printer.output("A book with this ISBN already exists in the library. Please try again.");
                } else {
                    myLibrary.addBook(bookTitle, bookAuthor, bookIsbn);
                    printer.output("Book \"" + bookTitle + "\" by " + bookAuthor + " has been added successfully.");
                    break;
                }
            }
        } else if (choice == 7) { // Remove Book
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
        } else if (choice == 10) { // Exit
            printer.output("Would you like to save? (Y/N)");
            while (true) {
                printer.output("Would you like to save? (Y/N or -1 to cancel):");
                String response = printer.input();
                if (response.equalsIgnoreCase("Y")) {
                    try {
                        printer.saveFile("library.json", myLibrary);
                        printer.output("Library data saved successfully.");
                    } catch (IOException e) {
                        printer.output("Error saving library data: " + e.getMessage());
                    }
                    break;
                } else if (response.equalsIgnoreCase("N")) {
                    printer.output("Library data not saved.");
                    printer.output("Thank you for using The Library®. Goodbye!");
                    System.exit(0);
                } else if (response.equals("-1")) {
                    printer.output("Save operation canceled.");
                    break;
                } else {
                    printer.output("Invalid input. Please enter Y, N, or -1 to cancel.");
                }
            }
        } else {
            printer.output("Invalid choice. Please try again.");
        }
        // set timeout for 2 seconds before displaying menu
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
