package commands;

import core.Spreadsheet;
/**
 * Команда за редактиране на стойност в електронната таблица.
 */
public class EditCommand implements Command {
    private Spreadsheet sheet;
    public EditCommand(Spreadsheet sheet) {
        this.sheet = sheet;
    }
    public void execute(String args) {
        try {
            String[] parts = args.trim().split(" ", 3);
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            String val = parts[2];
            sheet.edit(row, col, val);
            System.out.println("Cell updated.");
        } catch (Exception e) {
            System.out.println("Invalid input: row col \"value\"");
        }
    }
}