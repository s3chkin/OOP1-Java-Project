package cli;

import commands.*;
import core.Spreadsheet;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработва и изпълнява потребителски команди.
 */
public class CommandProcessor {
    /**
     * Свързва името на всяка команда (като низ) със съответния Command обект
     */
    private Map<String, Command> commandMap = new HashMap<>();


    public CommandProcessor(Spreadsheet sheet) {
        commandMap.put("open", new OpenCommand(sheet));
        commandMap.put("save", new SaveCommand(sheet));
        commandMap.put("saveas", new SaveAsCommand(sheet));
        commandMap.put("print", new PrintCommand(sheet));
        commandMap.put("close", new CloseCommand(sheet));
        commandMap.put("edit", new EditCommand(sheet));
        commandMap.put("help", new HelpCommand());
        commandMap.put("exit", new ExitCommand());
    }

    /**
     * Обработва входния низ, извлича командата и аргументите,
     * намира съответния Command обект и го изпълнява.
     * Ако командата не е разпозната, извежда съобщение за грешка.
     */
    public void process(String input) {
        String[] parts = input.trim().split(" ", 2);
        String cmd = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";
        Command command = commandMap.get(cmd);
        if (command != null) {
            command.execute(args);
        } else {
            System.out.println("Unknown command: " + cmd);
        }
    }
}