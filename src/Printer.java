import java.util.Scanner;
import java.io.*;

public class Printer {
    private Scanner in = new Scanner(System.in);
    private Scanner fileInput;
    private File file;

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
     * Opens a given file.
     */
    public void openFile(String filename) throws FileNotFoundException {
        file = new File(filename);
        fileInput = new Scanner(file);
    }

    /** Saves all library data into a formatted JSON file */
    public void saveFile(String filename, Library library) throws IOException {
        FileWriter writer = new FileWriter(filename);

        writer.write("{\n");
        writer.write("  \"library\": {\n");

        // Write the books section
        writer.write("    \"books\": [\n");
        Book[] books = library.getBooks();
        int bookCount = library.getBookCount();
        for (int i = 0; i < bookCount; i++) {
            Book book = books[i];
            writer.write("      {\n");
            writer.write("        \"title\": \"" + book.getTitle() + "\",\n");
            writer.write("        \"author\": \"" + book.getAuthor() + "\",\n");
            writer.write("        \"ISBN\": \"" + book.getIsbn() + "\"\n");
            writer.write("      }" + (i < bookCount - 1 ? "," : "") + "\n");
        }
        writer.write("    ],\n");

        // Write the students section
        writer.write("    \"students\": [\n");
        Student[] students = library.getStudents();
        int studentCount = library.getStudentCount();
        for (int i = 0; i < studentCount; i++) {
            Student student = students[i];
            writer.write("      {\n");
            writer.write("        \"name\": \"" + student.getName() + "\",\n");
            writer.write("        \"ID\": " + student.getId() + ",\n");
            writer.write("        \"checkedOutBooks\": [\n");

            // Write each of the student’s checked out books.
            // In the Student class, the checkedOutBooks array is used sequentially so we can stop on the first null.
            Book[] checkedBooks = student.getCheckedOutBooks();
            for (int j = 0; j < checkedBooks.length; j++) {
                if (checkedBooks[j] == null) {
                    break;
                }
                writer.write("          {\n");
                writer.write("            \"title\": \"" + checkedBooks[j].getTitle() + "\",\n");
                writer.write("            \"checkOutDate\": null,\n");
                writer.write("            \"dueDate\": null,\n");
                writer.write("            \"returnedDate\": null\n");
                writer.write("          }" + ((j < checkedBooks.length - 1 && checkedBooks[j+1] != null) ? "," : "") + "\n");
            }
            writer.write("        ]\n");
            writer.write("      }" + (i < studentCount - 1 ? "," : "") + "\n");
        }
        writer.write("    ]\n");

        writer.write("  }\n");
        writer.write("}\n");
        writer.close();
    }
    
    /**
     * Closes the file.
     */
    public void closeFile() {
        fileInput.close();
        file = null;
        fileInput = null;
    }

    /**
     * Checks if the file is has another line to be read.
     */
    public boolean fileHasNextLine() {
        return (fileInput == null ? false : fileInput.hasNextLine());
    }

    /**
     * Reads the next line from the file.
     */
    public String fileGetNextLine() { // sequentially reads the next line from the file
        return (fileHasNextLine() ? fileInput.nextLine() : "");
    }

    /**
     * Outputs a string to the console.
     */
    public void output(String s) {
        System.out.println(CYAN + s);
    }

    /**
     * Takes a user input from the console.
     */
    public String input() {
        System.out.print(WHITE + "→ ");
        return in.nextLine();
    }

    /**
     * Takes a user input from the console and returns it as an integer.
     */
    public int inputInt() {
        System.out.print(WHITE + "→ ");
        String input = in.nextLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(CYAN + "Invalid input. Please enter a number.");
            return inputInt();
        }
    }

    public long inputLong() {
        System.out.print(WHITE + "→ ");
        String input = in.nextLine();
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println(CYAN + "Invalid input. Please enter a number.");
            return inputLong();
        }
    }
}
