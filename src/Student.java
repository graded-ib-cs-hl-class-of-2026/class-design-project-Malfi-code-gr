import java.time.LocalDate;

public class Student {
    private String name;
    private int id;
    
    private LoanRecord[] loanRecords = new LoanRecord[100];
    private int loanRecordCount = 0;

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
            loanRecords[loanRecordCount++] = 
                new LoanRecord(book, loanPeriodDays);
        }
    }

    /** 
     * Load a historical record from JSON 
     */
    public void addLoanRecord(Book book,
                              LocalDate checkOut,
                              LocalDate due,
                              LocalDate returned) {
        if (loanRecordCount < loanRecords.length) {
            loanRecords[loanRecordCount++] = 
                new LoanRecord(book, checkOut, due, returned);
        }
    }

    /**
     * Returns an array containing only the active loan records.
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

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
