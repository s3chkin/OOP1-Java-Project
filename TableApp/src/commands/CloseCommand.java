package commands;

import core.Spreadsheet;

/**
 * Команда за затваряне на таблицата.
 */
public class CloseCommand implements Command {
    private Spreadsheet sheet;

    public CloseCommand(Spreadsheet sheet) {
        this.sheet = sheet;
    }

    public void execute(String args) {
        sheet.close();
        System.out.println("Closed.");
    }
}
