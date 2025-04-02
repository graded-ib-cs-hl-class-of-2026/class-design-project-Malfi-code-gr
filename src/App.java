public class App {
    private Library myLibrary;
    private Printer printer = new Printer();

    public void start() {
        myLibrary = new Library();
        displayMenu();
    }

    public static void main(String[] args) throws Exception {
        new App().start();
    }
}
