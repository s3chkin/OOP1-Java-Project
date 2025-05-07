package commands;

import core.Spreadsheet;

public class PrintCommand implements Command {
    private Spreadsheet sheet;
    public PrintCommand(Spreadsheet sheet) {
        this.sheet = sheet;
    }
    public void execute(String args) {
        sheet.print();
    }
}