package core;
/**
 * Абстрактен клас, който представя една клетка в електронната таблица.
 * Всеки подтип на Cell трябва да дефинира как се показва стойността.
 */

public abstract class Cell {
    /**
     * Връща стойността на клетката като текст, подходящ за визуализиране.
     */
    public abstract String getDisplay();
    public abstract double getValue();

    public static Cell createCell(String value, Spreadsheet spreadsheet) {
        // Опит за Integer
        try {
            int intValue = Integer.parseInt(value);
            return new IntegerCell(intValue);
        } catch (NumberFormatException ignored) {}

        // Опит за Double
        try {
            double doubleValue = Double.parseDouble(value);
            return new DoubleCell(doubleValue);
        } catch (NumberFormatException ignored) {}

        // Ако е формула
        if (value.startsWith("=")) {
            return new FormulaCell(value, spreadsheet);
        }

        // Ако започва с кавичка, се парсва
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            String parsed = value.substring(1, value.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
            return new StringCell(parsed);
        }

        // Всичко останало — текст без кавички или празен стринг
        return new StringCell(value);
    }
}
