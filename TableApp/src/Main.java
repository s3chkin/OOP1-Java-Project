import cli.CommandProcessor;
import core.Spreadsheet;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Spreadsheet spreadsheet = new Spreadsheet();
        CommandProcessor processor = new CommandProcessor(spreadsheet);

        System.out.println("Spreadsheet CLI ready. Type 'help'.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            processor.process(line);
        }
    }
}