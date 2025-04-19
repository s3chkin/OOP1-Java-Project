public class IntegerCell extends Cell{
    private int value;

    public IntegerCell(int value) {
        this.value = value;
    }

    public String getDisplay() {
        return Integer.toString(value);
    }

    public double getValue() {
        return value;
    }
}
