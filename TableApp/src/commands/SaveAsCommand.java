package commands;

import core.Spreadsheet;

public class SaveAsCommand implements Command {
    private Spreadsheet sheet;
    public SaveAsCommand(Spreadsheet sheet) {
        this.sheet = sheet;
    }
    public void execute(String args) {
        try {
            sheet.saveAs(args);
            System.out.println("Saved as: " + args);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
