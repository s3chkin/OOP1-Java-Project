package core;

/**
 * Представлява клетка, която съдържа текстова стойност.
 */
public class StringCell extends Cell {
    private String value;

    /**
     * Създава клетка със зададена текстова стойност.
     */
    public StringCell(String value) {
        this.value = value;
    }

    /**
     * Показва текстовата стойност на клетката.
     */
    public String getDisplay() {
        return value;
    }

    /**
     * Опитва се да преобразува текстовата стойност в число.
     * Ако преобразуването не е възможно, стойността остава 0.0.
     */
    public double getValue() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
