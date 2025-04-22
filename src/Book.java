public class Book {
    private String title;
    private String author;
    private long isbn;
    private boolean isCheckedOut;
    private boolean isLost;
    private double stacksLocation;
    private int studentId;
    private String dueDate;

    // private String dueDate;
    
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
        return "Title: '" + title + '\'' +
                ", Author: '" + author + '\'' +
                ", ISBN: " + isbn;
    }
}
