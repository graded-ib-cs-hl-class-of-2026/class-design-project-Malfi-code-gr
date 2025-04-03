public class Student {
    private String name;
    private int id;
    private Book[] checkedOutBooks;
    private int checkedOutCount;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void borrowBook(Book book) {
        checkedOutBooks[checkedOutCount++] = book;
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
