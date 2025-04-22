import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Stores the details for one checkout event
 */
public class LoanRecord {
    private Book book;
    private LocalDate checkOutDate;
    private LocalDate dueDate;
    private LocalDate returnedDate; // remains null until the book is returned

    public LoanRecord(Book book, LocalDate checkOutDate, LocalDate dueDate, LocalDate returnedDate) {
        this.book = book;
        this.checkOutDate = checkOutDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    public LoanRecord(Book book, int loanPeriodDays) {
        this(book, LocalDate.now(), LocalDate.now().plusDays(loanPeriodDays), null);
    }
    
    // getters
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public LocalDate getReturnedDate() { 
        return returnedDate;
    }
    public Book getBook() {
        return book;
    }
    
    public long getDaysGone() {
        LocalDate end;
        if (returnedDate != null) { 
            end = returnedDate;
        } else {
            end = LocalDate.now();
        }
        return ChronoUnit.DAYS.between(checkOutDate, end);
    }

    // setters
    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }
}
