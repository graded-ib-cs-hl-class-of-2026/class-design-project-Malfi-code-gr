import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Library {
    private Book[] books = new Book[100];
    private int bookCount;
    private Student[] students = new Student[100];
    private int studentCount;

    private static final String CYAN = "\u001B[36m";

    /**
     * Adds a new book to the library itself. (Book array)
     * @param title
     * @param author
     * @param isbn
     */
    public void addBook(String title, String author, long isbn) {
        if (bookCount < books.length) {
            books[bookCount++] = new Book(title, author, isbn);
        } else {
            System.out.println("Library is full. Cannot add more books.");
        }
    }

    /**
     * Removes a book from the library.
     * @param isbn
     */
    public void removeBook(long isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getIsbn() == isbn) {
                books[i] = books[--bookCount]; // Replace with the last book
                books[bookCount] = null; // Clear the last position
                return;
            }
        }
        System.out.println("Book not found.");
    }

    /**
     * Finds a book by its ISBN number.
     * @param isbn
     */
    public Book findBookByIsbn(long isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getIsbn() == isbn) {
                return books[i];
            }
        }
        return null;
    }

    /**
     * Finds a book by its title.
     * @param title
     */
    public Book findBookByTitle(String title) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getTitle().equalsIgnoreCase(title)) {
                return books[i];
            }
        }
        return null;
    }    
    
    /**
     * Checks out a book for a student with the default number of days (14).
     * @param studentId
     * @param isbn
     */
    public void checkOutBook(int studentId, long isbn) {
        Book book = findBookByIsbn(isbn);
        if (book != null && !book.isCheckedOut() && !book.isLost()) {
            book.setCheckedOut(true);
            book.setStudentId(studentId);
            findStudentById(studentId).borrowBook(book, 14);
            System.out.println(CYAN + "The book \"" + book.getTitle() + "\" has been checked out successfully by " + findStudentById(studentId).getName() + ". \nIt should be returned by " + todayPlus(14) + ".");
        } else if (book.isCheckedOut()) {
            System.out.println(CYAN + "Book is already checked out by " + findStudentById(studentId).getName() + ".");
        } else if (book.isLost()) {
            System.out.println(CYAN + "Book is marked as lost.");
        } else {
            System.out.println(CYAN + "Book not found.");
        }
    }

    /**
     * Checks out a book for a specified number of days.
     * @param studentId
     * @param isbn
     * @param days
     */
    public void checkOutBook(int studentId, long isbn, int days) {
        Book book = findBookByIsbn(isbn);
        if (book != null && !book.isCheckedOut() && !book.isLost()) {
            book.setCheckedOut(true);
            book.setStudentId(studentId);
            System.out.println(CYAN + "The book \"" + book.getTitle() + "\" has been checked out successfully. \nIt should be returned by " + todayPlus(days) + ".");
        } else if (book.isLost()) {
            System.out.println("Book is marked as lost.");
        } else {
            System.out.println("Book not found.");
        }
    }

    /**
     * Adds a student to the library.
     * @param name
     * @param id
     */
    public void addStudent(String name, int id) {
        if (studentCount < students.length) {
            students[studentCount++] = new Student(name, id);
        } else {
            System.out.println("Library is full. Cannot add more students.");
        }
    }

    /**
     * Removes a student from the library.
     * @param id
     */
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

    /**
     * Finds a student by their ID.
     * @param id
     * @return
     */
    public Student findStudentById(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                return students[i];
            }
        }
        return null;
    }

    /**
     * Finds a student by their name.
     * @param name
     * @return
     */
    public Student findStudentByName(String name) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getName().equalsIgnoreCase(name)) {
                return students[i];
            }
        }
        return null;
    }

    // getters
    public Book getBook(int i) {
        return books[i];
    }

    public int getBookCount() {
        return bookCount;
    }

    public Student[] getStudents() {
        return students;
    }

    public int getStudentCount() {
        return studentCount;
    }

    /**
     * Returns the current date plus a given number of days as a String in the format "yyyy-MM-dd".
     */
    public static String todayPlus(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
