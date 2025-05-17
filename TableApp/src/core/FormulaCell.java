package core;

public class FormulaCell extends Cell {
    private String formula;
    private Spreadsheet spreadsheet;

    public FormulaCell(String formula, Spreadsheet spreadsheet) {
        this.formula = formula;
        this.spreadsheet = spreadsheet;
    }

    @Override
    public String getDisplay() {
        double result = getValue();
        if (Double.isNaN(result)) return "ERROR";
        if (result == (long) result) return "" + (long) result;
        return "" + result;
    }

    @Override
    public double getValue() {
        try {
            String expr = formula.trim();
            if (expr.startsWith("=")) expr = expr.substring(1).trim();

            // Поддържаме само един оператор и два операнда
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

    private double convertStringToNumber(String str) {
        // Само ако е валидно число или валидно дробно число
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ignored) {}
        return 0.0;
    }
}