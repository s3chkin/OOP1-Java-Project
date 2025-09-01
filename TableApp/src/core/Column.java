package core;

/**
 * Клас, който представлява колона в електронната таблица.
 * Управлява клетките в една колона.
 */
public class Column {
    private Cell firstCell;
    private Column nextColumn;

    public Column() {
        this.firstCell = null;
        this.nextColumn = null;
    }

    // Връща първата клетка в колоната
    public Cell getFirstCell() { 
        return firstCell; 
    }

    // Връща следващата колона
    public Column getNextColumn() { 
        return nextColumn; 
    }

    // Задава следващата колона
    public void setNextColumn(Column nextColumn) { 
        this.nextColumn = nextColumn; 
    }

    // Задава първата клетка
    public void setFirstCell(Cell cell) { 
        this.firstCell = cell; 
    }

    // Връща броя клетки в колоната
    public int getCellCount() {
        int count = 0;
        Cell current = firstCell;
        while (current != null) {
            count++;
            current = current.getNextCell();
        }
        return count;
    }
}
