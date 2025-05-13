package core;

/**
 * Клетка, съдържаща дробно число.
 */
public class DoubleCell extends Cell {
    private double value;

    public DoubleCell(double value) {
        this.value = value;
    }

    /**
     * Връща стойността като текст.
     */
    @Override
    public String getDisplay() {
        return Double.toString(value);
    }

    /**
     * Връща стойността като число.
     */
    @Override
    public double getValue() {
        return value;
    }
}
