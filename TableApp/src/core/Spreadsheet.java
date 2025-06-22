package core;

import java.io.*;

/**
 * Основен клас, който управлява цялата логика на електронната таблица.
 * Използва назъбен масив за съхранение на данните,
 * което позволява всеки ред да има различна дължина.
 */
public class Spreadsheet {
    private Cell[][] data;
    private String currentFile = null;

    /**
     * Конструктор, който инициализира напълно празна таблица без редове и колони.
     */
    public Spreadsheet() {
        data = new Cell[0][];
    }

    /**
     * Зарежда таблица от файл, като презаписва текущото съдържание.
     * Всеки ред от файла се превръща в ред в таблицата с точния брой клетки.
     */
    public void loadFromFile(String filePath) throws IOException {
        data = new Cell[0][]; // Ресетва таблицата до напълно празно състояние
        currentFile = filePath;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIdx = 0;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",", -1);

                ensureRowCapacity(rowIdx);
                data[rowIdx] = new Cell[tokens.length]; // Създава ред с точната дължина, нужна за данните

                for (int colIdx = 0; colIdx < tokens.length; colIdx++) {
                    String token = tokens[colIdx].trim();
                    data[rowIdx][colIdx] = Cell.createCell(token, this);
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
            for (int r = 0; r < data.length; r++) {
                if (data[r] == null) {
                    writer.newLine(); // Празен ред в паметта -> празен ред във файла
                    continue;
                }
                for (int c = 0; c < data[r].length; c++) {
                    String value = data[r][c].getDisplay();
                    if (value.contains(",") || value.contains("\"")) {
                        value = value.replace("\\", "\\\\").replace("\"", "\\\"");
                        value = "\"" + value + "\"";
                    }
                    writer.write(value);
                    if (c < data[r].length - 1) writer.write(", ");
                }
                writer.newLine();
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

        for (int r = 0; r < height; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < width; c++) {
                String displayValue = "";
                if (data[r] != null && c < data[r].length && data[r][c] != null) {
                    displayValue = data[r][c].getDisplay();
                }
                sb.append(String.format("%-15s|", displayValue));
            }
            System.out.println(sb);
        }
    }

    /**
     * Изчиства цялата таблица и забравя името на текущия файл.
     */
    public void close() {
        data = new Cell[0][]; // Ресетва до напълно празно състояние
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
        data[rowIdx][colIdx] = Cell.createCell(value, this);
    }

    /**
     * Връща клетка по координати (ред и колона, започващи от 1). Ако няма такава, връща null.
     */
    public Cell getCell(int row, int col) {
        int rowIdx = row - 1;
        int colIdx = col - 1;
        if (rowIdx < 0 || colIdx < 0 || rowIdx >= data.length || data[rowIdx] == null || colIdx >= data[rowIdx].length) {
            return null;
        }
        return data[rowIdx][colIdx];
    }

    /** Осигурява съществуването на достатъчно редове в таблицата. */
    private void ensureRowCapacity(int rowIdx) {
        if (rowIdx >= data.length) {
            int newRows = rowIdx + 1;
            Cell[][] newData = new Cell[newRows][];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
    }

    /** Осигурява съществуването на клетка на даден ред и колона,
     * като разширява масива при нужда.
     */
    private void ensureCellCapacity(int rowIdx, int colIdx) {
        ensureRowCapacity(rowIdx);

        if (data[rowIdx] == null) {
            data[rowIdx] = new Cell[colIdx + 1];
        } else if (colIdx >= data[rowIdx].length) {
            Cell[] newRow = new Cell[colIdx + 1];
            System.arraycopy(data[rowIdx], 0, newRow, 0, data[rowIdx].length);
            data[rowIdx] = newRow;
        }

        // Попълва новосъздадените празни клетки, за да се избегне NullPointerException
        for (int i = 0; i <= colIdx; i++) {
            if (data[rowIdx][i] == null) {
                data[rowIdx][i] = new StringCell("");
            }
        }
    }

    /** Изчислява реалния брой използвани редове, за да не се отпечатват излишни празни редове. */
    private int getActualHeight() {
        for (int r = data.length - 1; r >= 0; r--) {
            if (data[r] != null) {
                for (Cell cell : data[r]) {
                    if (cell != null && !cell.getDisplay().isEmpty()) {
                        return r + 1;
                    }
                }
            }
        }
        return 0;
    }

    /** Изчислява максималната ширина на таблицата, за да може съдържанието да се подравни правилно. */
    private int getActualWidth() {
        int maxWidth = 0;
        for (int r = 0; r < data.length; r++) {
            if (data[r] != null) {
                maxWidth = Math.max(maxWidth, data[r].length);
            }
        }
        return maxWidth;
    }
}