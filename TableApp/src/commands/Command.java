package commands;

/**
 * Интерфейс за команди.
 */
public interface Command {

    /**
     * Изпълнява командата със зададени аргументи.
     */
    void execute(String args);
}
