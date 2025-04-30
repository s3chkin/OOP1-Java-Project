public class OpenCommand implements Command {
    private Spreadsheet sheet;

    public OpenCommand(Spreadsheet sheet) {
        this.sheet = sheet;
    }

    public void execute(String args) {
        try {
            sheet.loadFromFile(args);
            System.out.println("Opened: " + args);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
