package core;

/**
 * Клас, който управлява показването на електронната таблица.
 * Отговаря за форматирането и визуализацията.
 */
public class DisplayManager {
    private TableManager tableManager;

    public DisplayManager(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    /**
     * Отпечатва съдържанието на таблицата в конзолата.
     * Форматира изхода в табличен вид, подравнен по най-дългия ред.
     */
    public void print() {
        int height = tableManager.getActualHeight();
        int width = tableManager.getActualWidth();

        Row currentRow = tableManager.getFirstRow();
        for (int r = 0; r < height; r++) {
            StringBuilder sb = new StringBuilder();
            Cell currentCell = currentRow != null ? currentRow.getFirstCell() : null;
            for (int c = 0; c < width; c++) {
                String displayValue = "";
                if (currentCell != null) {
                    displayValue = currentCell.getDisplay();
                }
                sb.append(String.format("%-15s|", displayValue));
                currentCell = currentCell != null ? currentCell.getNextCell() : null;
            }
            System.out.println(sb);
            currentRow = currentRow != null ? currentRow.getNextRow() : null;
        }
    }

    /**
     * Връща форматиран низ за показване на таблицата.
     */
    public String getFormattedTable() {
        StringBuilder sb = new StringBuilder();
        int height = tableManager.getActualHeight();
        int width = tableManager.getActualWidth();

        Row currentRow = tableManager.getFirstRow();
        for (int r = 0; r < height; r++) {
            Cell currentCell = currentRow != null ? currentRow.getFirstCell() : null;
            for (int c = 0; c < width; c++) {
                String displayValue = "";
                if (currentCell != null) {
                    displayValue = currentCell.getDisplay();
                }
                sb.append(String.format("%-15s|", displayValue));
                currentCell = currentCell != null ? currentCell.getNextCell() : null;
            }
            sb.append("\n");
            currentRow = currentRow != null ? currentRow.getNextRow() : null;
        }
        return sb.toString();
    }
}
