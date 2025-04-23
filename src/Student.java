import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

public class Student {
    private String name;
    private int id;
    
    private LoanRecord[] loanRecords = new LoanRecord[100];
    private int loanRecordCount = 0;
    private int activeLoanCount = 0;

    /**
     * Constructor to initialize a Student object.
     * @param name
     * @param id
     */
    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.loanRecords = new LoanRecord[100];
        this.loanRecordCount = 0;
    }

    /** 
     * For live check‑outs 
     */
    public void borrowBook(Book book, int loanPeriodDays) {
        if (loanRecordCount < loanRecords.length) {
            loanRecords[loanRecordCount++] = new LoanRecord(book, loanPeriodDays);
            activeLoanCount++;
            sortLoanRecords();
        }
    }

    /**
     * Returns a book to the library.
     * @param book
     */
    public void returnBook(Book book) {
        activeLoanCount--;
        for (int i = 0; i < loanRecordCount; i++) {
            if (loanRecords[i].getBook().equals(book)) {
                loanRecords[i].setReturnedDate(LocalDate.now());
                break;
            }
        }
    }

    /** 
     * Load a historical loan record from JSON 
     */
    public void addLoanRecord(Book book,
                              LocalDate checkOut,
                              LocalDate due,
                              LocalDate returned) {
        if (loanRecordCount < loanRecords.length) {
            loanRecords[loanRecordCount++] = new LoanRecord(book, checkOut, due, returned);
            sortLoanRecords();
            if (returned == null) {
                activeLoanCount++;
            }
        }
    }

    /**
     * Returns an array containing all loan records.
     */
    public LoanRecord[] getLoanRecords() {
        LoanRecord[] out = new LoanRecord[loanRecordCount];
        System.arraycopy(loanRecords, 0, out, 0, loanRecordCount);
        return out;
    }

    /**
     * Finds all loan records for a specific book title.
     */
    public LoanRecord[] findLoanRecordsByBook(String bookTitle) {
        // Use a temporary array to store matching records.
        LoanRecord[] temp = new LoanRecord[loanRecordCount];
        int count = 0;
        for (int i = 0; i < loanRecordCount; i++) {
            if (loanRecords[i].getBook().getTitle().equalsIgnoreCase(bookTitle)) {
                temp[count++] = loanRecords[i];
            }
        }
        // Return an array sized exactly to the number of matching records.
        LoanRecord[] results = new LoanRecord[count];
        for (int i = 0; i < count; i++) {
            results[i] = temp[i];
        }
        return results;
    }

    /** 
     * After every insertion, keep loanRecords[0..loanRecordCount) sorted by checkOutDate 
     */
    // Learned from https://www.geeksforgeeks.org/arrays-sort-in-java/ and https://www.baeldung.com/java-8-comparator-comparing
    private void sortLoanRecords() { 
        Arrays.sort(loanRecords, 0, loanRecordCount,
            Comparator.comparing(LoanRecord::getCheckOutDate).reversed());
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getLoanCount() {
        return loanRecordCount;
    }

    public int getActiveLoanCount() {
        return activeLoanCount;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
