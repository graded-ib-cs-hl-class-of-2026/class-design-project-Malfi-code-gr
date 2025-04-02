public class Student {
    private String name;
    private int id;
    private Book[] checkedOutBooks;
    private int checkedOutCount;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public borrowBook(int isbn) {
        Book book = findBookByIsbn(isbn);
        if (book != null && !book.isCheckedOut()) {
            book.setCheckedOut(true);
            book.setStudentId(this.id);
            checkedOutBooks[checkedOutCount++] = book;
            System.out.println("Book with ISBN " + book.getIsbn() + " checked out successfully.");
        } else {
            System.out.println("Book is not available for checkout.");
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Book[] getCheckedOutBooks() {
        return checkedOutBooks;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
