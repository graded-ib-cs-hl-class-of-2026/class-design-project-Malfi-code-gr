public class Book {
    private String title;
    private String author;
    private int isbn;
    private boolean isCheckedOut;
    private boolean isLost;
    private double stacksLocation;
    private int studentId;
    // private String dueDate;
    
    /**
     * Constructor to initialize a Book object.
     */
    public Book(String title, String author, int isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    public int getIsbn() {
        return isbn;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public boolean isLost() {
        return isLost;
    }

    public double getStacksLocation() {
        return stacksLocation;
    }

    public int getStudentId() {
        return studentId;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public void setCheckedOut(boolean isCheckedOut) {
        this.isCheckedOut = isCheckedOut;
    }

    public void setLost(boolean isLost) {
        this.isLost = isLost;
    }

    public void setStacksLocation(double stacksLocation) {
        this.stacksLocation = stacksLocation;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * Override the toString method to return a string representation of the book.
     */
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn=" + isbn +
                '}';
    }
}
