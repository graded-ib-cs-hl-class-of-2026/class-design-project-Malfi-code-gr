import java.util.Scanner;
import java.io.*;

public class Printer {
    private Scanner in = new Scanner(System.in);
    private Scanner fileInput;
    private File file;

    /**
     * Opens a given file.
     */
    public void openFile(String filename) throws FileNotFoundException {
        file = new File(filename);
        fileInput = new Scanner(file);
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
        System.out.println(s);
    }

    /**
     * Takes a user input from the console.
     */
    public String input() {
        return in.nextLine();
    }

    /**
     * Takes a user input from the console and returns it as an integer.
     */
    public int inputInt() {
        String input = in.nextLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return inputInt();
        }
    }
}
