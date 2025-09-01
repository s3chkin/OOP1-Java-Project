package core;
/**
 * Абстрактен клас, който представя една клетка в електронната таблица.
 * Всеки подтип на Cell трябва да дефинира как се показва стойността.
 */

public abstract class Cell {
    private Cell nextCell;
    public Cell getNextCell() {
        return nextCell;
    }
    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }
    /** Връща стойността на клетката като текст, подходящ за визуализиране. */
    public abstract String getDisplay();
    public abstract double getValue();

    /**
     * Създава подходящия тип клетка според подадения текст.
     */
    public static Cell createCell(String value, Object context) {
        if (isInteger(value)) { // Ако е цяло число
            return new IntegerCell(Integer.parseInt(value));
        }
        if (isDouble(value)) { // Ако е дробно число
            return new DoubleCell(Double.parseDouble(value));
        }
        if (value.startsWith("=")) { // Ако е формула
            if (context instanceof Spreadsheet) {
                return new FormulaCell(value, (Spreadsheet) context);
            } else if (context instanceof TableManager) {
                // Създаваме временен Spreadsheet за FormulaCell
                Spreadsheet tempSheet = new Spreadsheet();
                return new FormulaCell(value, tempSheet);
            }
            return new StringCell(value); // Fallback
        }
        // Ако е текст с кавички
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            String parsed = value.substring(1, value.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
            return new StringCell(parsed);
        }
        // Всичко останало е текст
        return new StringCell(value);
    }

    /** Проверява дали стрингът е валидно цяло число (с минус по избор). */
    private static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) return false;
        int start = (s.charAt(0) == '-') ? 1 : 0;
        if (start == 1 && s.length() == 1) return false; //проверка за първия символ дали е -
        for (int i = start; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    /** Проверява дали стрингът е валидно дробно число (с опционален минус и точно една точка). */
    private static boolean isDouble(String s) {
        if (s == null || s.isEmpty()) return false;
        int start = (s.charAt(0) == '-') ? 1 : 0; //проверка за първия символ дали е -
        boolean pointSeen = false;
        int digits = 0;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') {
                if (pointSeen) return false; // повече от една точка
                pointSeen = true;
            } else if (Character.isDigit(c)) {
                digits++;
            } else {
                return false;
            }
        }
        return pointSeen && digits > 0;
    }
}

