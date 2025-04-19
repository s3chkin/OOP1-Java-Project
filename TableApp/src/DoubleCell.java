public class DoubleCell extends Cell{
    private double value;

    public DoubleCell(double value) {
        this.value = value;
    }

    public String getDisplay() {
        return Double.toString(value);
    }

    public double getValue() {
        return value;
    }}
