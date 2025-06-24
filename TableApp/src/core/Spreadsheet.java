package core;

import java.io.*;

/**
 * Основен клас, който управлява цялата логика на електронната таблица.
 * Използва назъбен масив за съхранение на данните,
 * което позволява всеки ред да има различна дължина.
 */
public class Spreadsheet {
    private Row row;
    private Column column;
    private String currentFile = null;

    /**
     * Конструктор, който инициализира напълно празна таблица без редове и колони.
     */
    public Spreadsheet() {
        this.row = null;
        this.column = null;
    }

    /**
     * Зарежда таблица от файл, като презаписва текущото съдържание.
     * Всеки ред от файла се превръща в ред в таблицата с точния брой клетки.
     */
    public void loadFromFile(String filePath) throws IOException {
        this.row = null; // Ресетва таблицата до напълно празно състояние
        currentFile = filePath;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIdx = 0;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",", -1);

                addRow();
                Row currentRow = getRow(rowIdx);
                for (int colIdx = 0; colIdx < tokens.length; colIdx++) {
                    String token = tokens[colIdx].trim();
                    currentRow.addCell(Cell.createCell(token, this));
                }
                rowIdx++;
            }
        }
    }

    /**
     * Записва текущата таблица във файла, от който е заредена.
     */
    public void save() throws IOException {
        if (currentFile == null) throw new IOException("No file loaded");
        saveAs(currentFile);
    }

    /**
     * Записва текущата таблица в нов файл.
     * Записват се само редовете и колоните, които съдържат данни.
     */
    public void saveAs(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Row currentRow = row;
            while (currentRow != null) {
                Cell currentCell = currentRow.firstCell;
                while (currentCell != null) {
                    String value = currentCell.getDisplay();
                    if (value.contains(",") || value.contains("\"")) {
                        value = value.replace("\\", "\\\\").replace("\"", "\\\"");
                        value = "\"" + value + "\"";
                    }
                    writer.write(value);
                    if (currentCell.getNextCell() != null) {
                        writer.write(", ");
                    }
                    currentCell = currentCell.getNextCell();
                }
                writer.newLine();
                currentRow = currentRow.nextRow;
            }
        }
    }

    /**
     * Отпечатва съдържанието на таблицата в конзолата.
     * Форматира изхода в табличен вид, подравнен по най-дългия ред.
     */
    public void print() {
        int height = getActualHeight();
        int width = getActualWidth(); // Намира максималната ширина за красиво форматиране

        Row currentRow = row;
        for (int r = 0; r < height; r++) {
            StringBuilder sb = new StringBuilder();
            Cell currentCell = currentRow != null ? currentRow.firstCell : null;
            for (int c = 0; c < width; c++) {
                String displayValue = "";
                if (currentCell != null) {
                    displayValue = currentCell.getDisplay();
                }
                sb.append(String.format("%-15s|", displayValue));
                currentCell = currentCell != null ? currentCell.getNextCell() : null;
            }
            System.out.println(sb);
            currentRow = currentRow != null ? currentRow.nextRow : null;
        }
    }

    /**
     * Изчиства цялата таблица и забравя името на текущия файл.
     */
    public void close() {
        this.row = null; // Ресетва до напълно празно състояние
        currentFile = null;
    }

    /**
     * Редактира съдържанието на една клетка.
     * Ако клетката не съществува, таблицата се разширява автоматично.
     */
    public void edit(int row, int col, String value) {
        int rowIdx = row - 1;
        int colIdx = col - 1;

        ensureCellCapacity(rowIdx, colIdx); // Уверява се, че масивът е достатъчно голям
        Row currentRow = getRow(rowIdx);
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
            // Заместваме съществуващата клетка
            if (prev == null) {
                newCell.setNextCell(current != null ? current.getNextCell() : null);
                currentRow.setFirstCell(newCell);
            } else {
                newCell.setNextCell(current != null ? current.getNextCell() : null);
                prev.setNextCell(newCell);
            }
        } else {
            // Ако няма такава клетка, добавяме в края
            if (prev == null) {
                currentRow.setFirstCell(newCell);
            } else {
                prev.setNextCell(newCell);
            }
        }
    }

    /**
     * Връща клетка по координати (ред и колона, започващи от 1). Ако няма такава, връща null.
     */
    public Cell getCell(int row, int col) {
        int rowIdx = row - 1;
        int colIdx = col - 1;
        if (rowIdx < 0 || colIdx < 0 || rowIdx >= getActualHeight() || this.row == null) {
            return null;
        }
        Row currentRow = getRow(rowIdx);
        return currentRow.getCell(colIdx);
    }

    /** Осигурява съществуването на достатъчно редове в таблицата. */
    private void ensureRowCapacity(int rowIdx) {
        if (rowIdx >= getActualHeight()) {
            addRow();
        }
    }

    /** Осигурява съществуването на клетка на даден ред и колона,
     * като разширява масива при нужда.
     */
    private void ensureCellCapacity(int rowIdx, int colIdx) {
        ensureRowCapacity(rowIdx);

        Row currentRow = getRow(rowIdx);
        if (currentRow.firstCell == null) {
            currentRow.firstCell = new StringCell("");
        } else if (colIdx >= getActualWidth()) {
            Cell currentCell = currentRow.firstCell;
            while (currentCell.getNextCell() != null) {
                currentCell = currentCell.getNextCell();
            }
            currentCell.setNextCell(new StringCell(""));
        }
    }

    /** Изчислява реалния брой използвани редове, за да не се отпечатват излишни празни редове. */
    private int getActualHeight() {
        int height = 0;
        Row currentRow = row;
        while (currentRow != null) {
            height++;
            currentRow = currentRow.nextRow;
        }
        return height;
    }

    /** Изчислява максималната ширина на таблицата, за да може съдържанието да се подравни правилно. */
    private int getActualWidth() {
        int maxWidth = 0;
        Row currentRow = row;
        while (currentRow != null) {
            int rowWidth = 0;
            Cell currentCell = currentRow.firstCell;
            while (currentCell != null) {
                rowWidth++;
                currentCell = currentCell.getNextCell();
            }
            maxWidth = Math.max(maxWidth, rowWidth);
            currentRow = currentRow.nextRow;
        }
        return maxWidth;
    }

    // Добавя нов ред в края
    public void addRow() {
        Row newRow = new Row();
        if (row == null) {
            row = newRow;
        } else {
            Row current = row;
            while (current.nextRow != null) {
                current = current.nextRow;
            }
            current.nextRow = newRow;
        }
    }

    // Връща ред по индекс (0-базиран)
    public Row getRow(int index) {
        Row current = row;
        int i = 0;
        while (current != null && i < index) {
            current = current.nextRow;
            i++;
        }
        return current;
    }

    // Добавя нова колона в края
    public void addColumn() {
        Column newCol = new Column();
        if (column == null) {
            column = newCol;
        } else {
            Column current = column;
            while (current.nextColumn != null) {
                current = current.nextColumn;
            }
            current.nextColumn = newCol;
        }
    }

    // Връща колона по индекс (0-базиран)
    public Column getColumn(int index) {
        Column current = column;
        int i = 0;
        while (current != null && i < index) {
            current = current.nextColumn;
            i++;
        }
        return current;
    }

    // Вътрешен клас Row
    public static class Row {
        private Cell firstCell;
        private Row nextRow;

        public Row() {
            this.firstCell = null;
            this.nextRow = null;
        }

        // Добавя клетка в края на реда
        public void addCell(Cell cell) {
            if (firstCell == null) {
                firstCell = cell;
            } else {
                Cell current = firstCell;
                while (current.getNextCell() != null) {
                    current = current.getNextCell();
                }
                current.setNextCell(cell);
            }
        }

        // Връща клетка по индекс (0-базиран)
        public Cell getCell(int index) {
            Cell current = firstCell;
            int i = 0;
            while (current != null && i < index) {
                current = current.getNextCell();
                i++;
            }
            return current;
        }

        public Cell getFirstCell() { return firstCell; }
        public Row getNextRow() { return nextRow; }
        public void setNextRow(Row nextRow) { this.nextRow = nextRow; }
        public void setFirstCell(Cell cell) { this.firstCell = cell; }
    }

    // Вътрешен клас Column
    public static class Column {
        private Cell firstCell;
        private Column nextColumn;

        public Column() {
            this.firstCell = null;
            this.nextColumn = null;
        }

        public Cell getFirstCell() { return firstCell; }
        public Column getNextColumn() { return nextColumn; }
        public void setNextColumn(Column nextColumn) { this.nextColumn = nextColumn; }
        public void setFirstCell(Cell cell) { this.firstCell = cell; }
    }
}