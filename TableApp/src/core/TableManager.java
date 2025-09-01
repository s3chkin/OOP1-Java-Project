package core;

/**
 * Клас, който управлява структурата на таблицата.
 * Отговаря за редовете и колоните.
 */
public class TableManager {
    private Row firstRow;
    private Column firstColumn;

    public TableManager() {
        this.firstRow = null;
        this.firstColumn = null;
    }

    // Добавя нов ред в края
    public void addRow() {
        Row newRow = new Row();
        if (firstRow == null) {
            firstRow = newRow;
        } else {
            Row current = firstRow;
            while (current.getNextRow() != null) {
                current = current.getNextRow();
            }
            current.setNextRow(newRow);
        }
    }

    // Връща ред по индекс (0-базиран)
    public Row getRow(int index) {
        Row current = firstRow;
        int i = 0;
        while (current != null && i < index) {
            current = current.getNextRow();
            i++;
        }
        return current;
    }

    // Добавя нова колона в края
    public void addColumn() {
        Column newCol = new Column();
        if (firstColumn == null) {
            firstColumn = newCol;
        } else {
            Column current = firstColumn;
            while (current.getNextColumn() != null) {
                current = current.getNextColumn();
            }
            current.setNextColumn(newCol);
        }
    }

    // Връща колона по индекс (0-базиран)
    public Column getColumn(int index) {
        Column current = firstColumn;
        int i = 0;
        while (current != null && i < index) {
            current = current.getNextColumn();
            i++;
        }
        return current;
    }

    // Връща първия ред
    public Row getFirstRow() {
        return firstRow;
    }

    // Връща първата колона
    public Column getFirstColumn() {
        return firstColumn;
    }

    // Изчиства цялата таблица
    public void clear() {
        this.firstRow = null;
        this.firstColumn = null;
    }

    // Изчислява реалния брой използвани редове
    public int getActualHeight() {
        int height = 0;
        Row currentRow = firstRow;
        while (currentRow != null) {
            height++;
            currentRow = currentRow.getNextRow();
        }
        return height;
    }

    // Изчислява максималната ширина на таблицата
    public int getActualWidth() {
        int maxWidth = 0;
        Row currentRow = firstRow;
        while (currentRow != null) {
            int rowWidth = currentRow.getCellCount();
            maxWidth = Math.max(maxWidth, rowWidth);
            currentRow = currentRow.getNextRow();
        }
        return maxWidth;
    }
}
