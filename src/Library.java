import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Library {
    private Book[] books = new Book[100];
    private int bookCount;
    private Student[] students = new Student[100];
    private int studentCount;

    public void addBook(String title, String author, int isbn) {
        if (bookCount < books.length) {
            books[bookCount++] = new Book(title, author, isbn);
        } else {
            System.out.println("Library is full. Cannot add more books.");
        }
    }

    public void removeBook(int isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getIsbn() == isbn) {
                books[i] = books[--bookCount]; // Replace with the last book
                books[bookCount] = null; // Clear the last position
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public Book findBookByIsbn(int isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getIsbn() == isbn) {
                return books[i];
            }
        }
        return null;
    }

    /**
     * Checks out a book for a student with the default number of days (14).
     */
    public void checkOutBook(int studentId, int isbn) {
        Book book = findBookByIsbn(isbn);
        if (book != null && !book.isCheckedOut() && !book.isLost()) {
            book.setCheckedOut(true);
            book.setStudentId(studentId);
            findStudentById(studentId).borrowBook(book);
            System.out.println("The book \"" + book.getTitle() + "\" has been checked out successfully. \nIt should be returned by " + todayPlus(14) + ".");
        } else if (book.isCheckedOut()) {
            System.out.println("Book is already checked out by " + findStudentById(studentId).getName() + ".");
        } else if (book.isLost()) {
            System.out.println("Book is marked as lost.");
        } else {
            System.out.println("Book not found.");
        }
    }

    /**
     * Checks out a book for a specified number of days.
     */
    public void checkOutBook(int studentId, int isbn, int days) {
        Book book = findBookByIsbn(isbn);
        if (book != null && !book.isCheckedOut() && !book.isLost()) {
            book.setCheckedOut(true);
            book.setStudentId(studentId);
            System.out.println("The book \"" + book.getTitle() + "\" has been checked out successfully. \nIt should be returned by " + todayPlus(days) + ".");
        } else if (book.isCheckedOut()) {
            System.out.println("Book is already checked out by " + findStudentById(studentId).getName() + ".");
        } else if (book.isLost()) {
            System.out.println("Book is marked as lost.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public void returnBook(int isbn) {
        Book book;
        while (true) {
            book = findBookByIsbn(isbn);
            if (book != null) {
                if (book.isCheckedOut()) {
                    book.setCheckedOut(false);
                    book.setStudentId(0);
                    System.out.println("The book \"" + book.getTitle() + "\" has been returned successfully.");
                    break;
                } else {
                    System.out.println("This book was not checked out.");
                    break;
                }
            } else {
                System.out.println("Book not found. Please input a valid ISBN or type -1 to cancel:");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter ISBN: ");
                isbn = scanner.nextInt();
                if (isbn == -1) {
                    System.out.println("Operation canceled.");
                    break;
                }
            scanner.close();
            }
        }
    }

    public void addStudent(String name, int id) {
        if (studentCount < students.length) {
            students[studentCount++] = new Student(name, id);
        } else {
            System.out.println("Library is full. Cannot add more students.");
        }
    }

    public void removeStudent(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                students[i] = students[--studentCount];
                students[studentCount] = null;
                return;
            }
        }
        System.out.println("Student not found.");
    }

    public Student findStudentById(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                return students[i];
            }
        }
        return null;
    }

    /**
     * Returns the current date plus a given number of days as a String in the format "yyyy-MM-dd".
     */
    public static String todayPlus(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
