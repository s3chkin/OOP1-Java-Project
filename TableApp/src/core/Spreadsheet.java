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
                    } else if (token.matches("[+-]?\\d+")) {
                        row.add(new IntegerCell(Integer.parseInt(token)));
                    } else if (token.matches("[+-]?\\d+\\.\\d+")) {
                        row.add(new DoubleCell(Double.parseDouble(token)));
                    } else if (token.startsWith("\"")) {
                        String parsed = token.replaceAll("\\\\\\\\", "\\").replaceAll("\\\\\"", "\"");
                        row.add(new StringCell(parsed.substring(1, parsed.length() - 1)));
                    } else {
                        throw new IOException("Unknown data type: " + token);
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
                sb.append(String.format("%-15s|", cell.getDisplay()));
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
        if (value.matches("[+-]?\\d+")) {
            rowData.set(col - 1, new IntegerCell(Integer.parseInt(value)));
        } else if (value.matches("[+-]?\\d+\\.\\d+")) {
            rowData.set(col - 1, new DoubleCell(Double.parseDouble(value)));
        } else if (value.startsWith("\"")) {
            String parsed = value.replaceAll("\\\\\\\\", "\\").replaceAll("\\\\\"", "\"");
            rowData.set(col - 1, new StringCell(parsed.substring(1, parsed.length() - 1)));
        } else {
            System.out.println("Invalid data");
        }
    }
}