public class Book {
    private String title;
    private String author;
    private long isbn;
    private boolean isCheckedOut;
    private boolean isLost;
    private int studentId;
    
    /**
     * Constructor to initialize a Book object.
     */
    public Book(String title, String author, long isbn) {
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
    public long getIsbn() {
        return isbn;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public boolean isLost() {
        return isLost;
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
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * Override the toString method to return a string representation of the book.
     */
    @Override
    public String toString() {
        return "Title: '" + title + '\'' +
                ", Author: '" + author + '\'' +
                ", ISBN: " + isbn;
    }
}
