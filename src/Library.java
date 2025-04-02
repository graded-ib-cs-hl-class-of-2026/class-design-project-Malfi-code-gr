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

    public void checkOutBook(String studentId, String isbn) {
        // 1. Get book
        // 2. Get student
        // 3. Make sure book is available
        // 4. Tell book to check itself out
        // 5. Tell student to record the checkout
    }

    public void returnBook(String studentId, String isbn) {
        // 1. Get book
        // 2. Get student
        // 3. Make sure book is checked out
        // 4. Tell book to check itself in
        // 5. Tell student to remove the book from its currently checked out list
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
                students[i] = students[--studentCount]; // Replace with the last student
                students[studentCount] = null; // Clear the last position
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
}
