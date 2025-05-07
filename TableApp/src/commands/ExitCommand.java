package commands;

public class ExitCommand implements Command {
    public void execute(String args) {
        System.out.println("Bye!");
        System.exit(0);
    }
}
