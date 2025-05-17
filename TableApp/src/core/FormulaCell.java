package core;

/**
 * Класът FormulaCell представлява клетка, която съдържа формула.
 * Формулата може да бъде проста аритметична операция между две стойности
 * (числа или препратки към други клетки) или една стойност.
 */
public class FormulaCell extends Cell {
    private String formula;
    private Spreadsheet spreadsheet;

    /**
     * Конструктор на клетка с формула.
     *
     * @param formula     формулата, която тази клетка ще изчислява (напр. "=R1C1 + R2C2")
     * @param spreadsheet препратка към електронната таблица, нужна за извличане на други клетки
     */
    public FormulaCell(String formula, Spreadsheet spreadsheet) {
        this.formula = formula;
        this.spreadsheet = spreadsheet;
    }

    /**
     * Връща стойността на клетката във вид, подходящ за показване.
     * Ако има грешка в изчислението, връща "ERROR".
     * Ако стойността е цяло число, връща без десетична точка.
     *
     * @return низова стойност за показване
     */
    @Override
    public String getDisplay() {
        double result = getValue();
        if (Double.isNaN(result)) return "ERROR";
        if (result == (long) result) return "" + (long) result;
        return "" + result;
    }

    /**
     * Изчислява стойността на формулата в клетката.
     * Поддържат се прости операции: +, -, *, / между два операнда.
     * Операндите могат да са числа или препратки към клетки.
     *
     * @return изчислената стойност или {@code Double.NaN} при грешка
     */
    @Override
    public double getValue() {
        try {
            String expr = formula.trim();
            if (expr.startsWith("=")) expr = expr.substring(1).trim();

            // Поддържа само един оператор и два операнда
            String op = null;
            if (expr.contains(" + ")) op = "+";
            else if (expr.contains(" - ")) op = "-";
            else if (expr.contains(" * ")) op = "*";
            else if (expr.contains(" / ")) op = "/";

            if (op != null) {
                String[] parts = expr.split(" \\" + op + " ");
                double left = parseOperand(parts[0]);
                double right = parseOperand(parts[1]);
                if (op.equals("+")) return left + right;
                if (op.equals("-")) return left - right;
                if (op.equals("*")) return left * right;
                if (op.equals("/")) {
                    if (right == 0) return Double.NaN;
                    return left / right;
                }
            } else {
                // Само един операнд
                return parseOperand(expr);
            }
        } catch (Exception e) {
            return Double.NaN;
        }
        return Double.NaN;
    }

    /**
     * Парсва даден операнд – може да е число, низ или препратка към друга клетка (формат R<row>C<col>).
     *
     * @param token текстов представител на операнда
     * @return стойността на операнда като число
     */
    private double parseOperand(String token) {
        token = token.trim();
        // Референция към клетка: R<N>C<M>
        if (token.startsWith("R") && token.contains("C")) {
            try {
                int r = Integer.parseInt(token.substring(1, token.indexOf('C')));
                int c = Integer.parseInt(token.substring(token.indexOf('C') + 1));
                Cell cell = spreadsheet.getCell(r, c);
                if (cell == null) return 0.0;
                if (cell instanceof StringCell) {
                    return convertStringToNumber(cell.getDisplay());
                }
                return cell.getValue();
            } catch (Exception e) {
                return 0.0;
            }
        }
        // Число
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException ignored) {}
        // Низ
        return convertStringToNumber(token);
    }

    /**
     * Конвертира низ към число, ако е възможно.
     * В противен случай връща 0.0.
     */
    private double convertStringToNumber(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ignored) {}
        return 0.0;
    }
}
