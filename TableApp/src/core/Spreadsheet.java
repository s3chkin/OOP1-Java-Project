package core;

import java.io.IOException;

/**
 * Основен клас, който координира работата на електронната таблица.
 * Сега има по-малко отговорности - само координира другите мениджъри.
 */
public class Spreadsheet {
    private TableManager tableManager;
    private FileManager fileManager;
    private DisplayManager displayManager;

    /**
     * Конструктор, който инициализира мениджърите.
     */
    public Spreadsheet() {
        this.tableManager = new TableManager();
        this.fileManager = new FileManager(tableManager);
        this.displayManager = new DisplayManager(tableManager);
    }

    /**
     * Зарежда таблица от файл.
     */
    public void loadFromFile(String filePath) throws IOException {
        fileManager.loadFromFile(filePath);
    }

    /**
     * Записва текущата таблица във файла, от който е заредена.
     */
    public void save() throws IOException {
        fileManager.save();
    }

    /**
     * Записва текущата таблица в нов файл.
     */
    public void saveAs(String filePath) throws IOException {
        fileManager.saveAs(filePath);
    }

    /**
     * Отпечатва съдържанието на таблицата в конзолата.
     */
    public void print() {
        displayManager.print();
    }

    /**
     * Изчиства цялата таблица и забравя името на текущия файл.
     */
    public void close() {
        tableManager.clear();
        fileManager.clearCurrentFile();
    }

    /**
     * Редактира съдържанието на една клетка.
     */
    public void edit(int row, int col, String value) {
        int rowIdx = row - 1;
        int colIdx = col - 1;

        ensureCellCapacity(rowIdx, colIdx);
        Row currentRow = tableManager.getRow(rowIdx);
        Cell newCell = Cell.createCell(value, this);
        Cell prev = null;
        Cell current = currentRow.getFirstCell();
        int i = 0;
        while (current != null && i < colIdx) {
            prev = current;
            current = current.getNextCell();
            i++;
        }
        if (i == colIdx) {
            if (prev == null) {
                newCell.setNextCell(current != null ? current.getNextCell() : null);
                currentRow.setFirstCell(newCell);
            } else {
                newCell.setNextCell(current != null ? current.getNextCell() : null);
                prev.setNextCell(newCell);
            }
        } else {
            if (prev == null) {
                currentRow.setFirstCell(newCell);
            } else {
                prev.setNextCell(newCell);
            }
        }
    }

    /**
     * Връща клетка по координати (ред и колона, започващи от 1).
     */
    public Cell getCell(int row, int col) {
        int rowIdx = row - 1;
        int colIdx = col - 1;
        if (rowIdx < 0 || colIdx < 0 || rowIdx >= tableManager.getActualHeight()) {
            return null;
        }
        Row currentRow = tableManager.getRow(rowIdx);
        return currentRow.getCell(colIdx);
    }

    /** Осигурява съществуването на достатъчно редове в таблицата. */
    private void ensureRowCapacity(int rowIdx) {
        if (rowIdx >= tableManager.getActualHeight()) {
            tableManager.addRow();
        }
    }

    /** Осигурява съществуването на клетка на даден ред и колона. */
    private void ensureCellCapacity(int rowIdx, int colIdx) {
        ensureRowCapacity(rowIdx);

        Row currentRow = tableManager.getRow(rowIdx);
        if (currentRow.getFirstCell() == null) {
            currentRow.setFirstCell(new StringCell(""));
        } else if (colIdx >= tableManager.getActualWidth()) {
            Cell currentCell = currentRow.getFirstCell();
            while (currentCell.getNextCell() != null) {
                currentCell = currentCell.getNextCell();
            }
            currentCell.setNextCell(new StringCell(""));
        }
    }

    // Добавя нов ред в края
    public void addRow() {
        tableManager.addRow();
    }

    // Връща ред по индекс (0-базиран)
    public Row getRow(int index) {
        return tableManager.getRow(index);
    }

    // Добавя нова колона в края
    public void addColumn() {
        tableManager.addColumn();
    }

    // Връща колона по индекс (0-базиран)
    public Column getColumn(int index) {
        return tableManager.getColumn(index);
    }
}