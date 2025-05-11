package core;

import java.util.*;
import java.io.*;

public class Spreadsheet {
    private List<List<Cell>> data = new ArrayList<>();
    private String currentFile = null;

    public void loadFromFile(String filePath) throws IOException {
        data.clear();
        currentFile = filePath;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Cell> row = new ArrayList<>();
                String[] tokens = line.split(",", -1);
                for (String token : tokens) {
                    token = token.trim();
                    if (token.isEmpty()) {
                        row.add(new StringCell(""));
                        continue;
                    }

                    // Опит за Integer
                    try {
                        int intVal = Integer.parseInt(token);
                        row.add(new IntegerCell(intVal));
                        continue;
                    } catch (NumberFormatException ignored) {}

                    // Опит за Double
                    try {
                        double doubleVal = Double.parseDouble(token);
                        row.add(new DoubleCell(doubleVal));
                        continue;
                    } catch (NumberFormatException ignored) {}

                    // Стринг с кавички
                    if (token.startsWith("\"") && token.endsWith("\"") && token.length() >= 2) {
                        String parsed = token.substring(1, token.length() - 1)
                                .replace("\\\"", "\"")
                                .replace("\\\\", "\\");
                        row.add(new StringCell(parsed));
                    } else {
                        // Стринг без кавички
                        row.add(new StringCell(token));
                    }
                }
                data.add(row);
            }
        }
    }


    public void save() throws IOException {
        if (currentFile == null) throw new IOException("No file loaded");
        saveAs(currentFile);
    }

    public void saveAs(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (List<Cell> row : data) {
                for (int i = 0; i < row.size(); i++) {
                    String value = row.get(i).getDisplay();
                    if (value.contains(",") || value.contains("\"")) {
                        value = value.replace("\\", "\\\\").replace("\"", "\\\"");
                        value = "\"" + value + "\"";
                    }
                    writer.write(value);
                    if (i < row.size() - 1) writer.write(", ");
                }
                writer.newLine();
            }
        }
    }

    public void print() {
        for (List<Cell> row : data) {
            StringBuilder sb = new StringBuilder();
            for (Cell cell : row) {
                sb.append(String.format("%-15s|", cell.getDisplay())); //за да се отпечата разстояние с 15 символа
            }
            System.out.println(sb);
        }
    }

    public void close() {
        data.clear();
        currentFile = null;
    }

    public void edit(int row, int col, String value) {
        while (data.size() < row) {
            data.add(new ArrayList<>());
        }
        List<Cell> rowData = data.get(row - 1);
        while (rowData.size() < col) {
            rowData.add(new StringCell(""));
        }

        // Опит за Integer
        try {
            int intValue = Integer.parseInt(value);
            rowData.set(col - 1, new IntegerCell(intValue));
            return;
        } catch (NumberFormatException ignored) {}

        // Опит за Double
        try {
            double doubleValue = Double.parseDouble(value);
            rowData.set(col - 1, new DoubleCell(doubleValue));
            return;
        } catch (NumberFormatException ignored) {}

        // Ако започва с кавичка, се парсва
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            String parsed = value.substring(1, value.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
            rowData.set(col - 1, new StringCell(parsed));
        } else {
            // Всичко останало — текст без кавички
            rowData.set(col - 1, new StringCell(value));
        }
    }

}