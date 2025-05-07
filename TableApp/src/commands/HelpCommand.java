package commands;

public class HelpCommand implements Command {
    public void execute(String args) {
        System.out.println("Commands: open, save, saveas, close, print, edit, help, exit");
    }
}