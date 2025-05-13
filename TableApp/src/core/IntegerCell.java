package core;

/**
 * Клетка, съдържаща цяло число.
 */
public class IntegerCell extends Cell {
    private int value;

    public IntegerCell(int value) {
        this.value = value;
    }

    /**
     * Връща стойността на клетката като текст.
     */
    @Override
    public String getDisplay() {
        return Integer.toString(value);
    }

    /**
     * Връща стойността на клетката като число (double).
     */
    @Override
    public double getValue() {
        return value;
    }
}
