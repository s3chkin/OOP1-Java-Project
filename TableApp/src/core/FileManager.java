package core;

import java.io.*;

/**
 * Клас, който управлява файловите операции за електронната таблица.
 * Отговаря за зареждане и записване на данни.
 */
public class FileManager {
    private String currentFile = null;
    private TableManager tableManager;

    public FileManager(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    /**
     * Зарежда таблица от файл, като презаписва текущото съдържание.
     */
    public void loadFromFile(String filePath) throws IOException {
        tableManager.clear(); // Ресетва таблицата
        currentFile = filePath;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIdx = 0;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",", -1);

                tableManager.addRow();
                Row currentRow = tableManager.getRow(rowIdx);
                for (int colIdx = 0; colIdx < tokens.length; colIdx++) {
                    String token = tokens[colIdx].trim();
                    currentRow.addCell(Cell.createCell(token, tableManager));
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
     */
    public void saveAs(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Row currentRow = tableManager.getFirstRow();
            while (currentRow != null) {
                Cell currentCell = currentRow.getFirstCell();
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
                currentRow = currentRow.getNextRow();
            }
        }
        currentFile = filePath;
    }

    /**
     * Връща името на текущия файл.
     */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * Изчиства текущия файл.
     */
    public void clearCurrentFile() {
        currentFile = null;
    }
}
