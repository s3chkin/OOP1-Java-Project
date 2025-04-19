public class StringCell extends Cell {
    private String value;

    public StringCell(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return value;
    }

    public double getValue() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}