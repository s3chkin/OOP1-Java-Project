package commands;

import core.Spreadsheet;

/**
 * Команда за запазване на електронната таблица.
 */
public class SaveCommand implements Command {
    private Spreadsheet sheet;

    public SaveCommand(Spreadsheet sheet) {
        this.sheet = sheet;
    }

    public void execute(String args) {
        try {
            sheet.save();
            System.out.println("Saved.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
