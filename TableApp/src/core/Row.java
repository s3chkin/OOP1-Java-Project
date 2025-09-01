package core;

/**
 * Клас, който представлява ред в електронната таблица.
 * Управлява клетките в един ред.
 */
public class Row {
    private Cell firstCell;
    private Row nextRow;

    public Row() {
        this.firstCell = null;
        this.nextRow = null;
    }

    // Добавя клетка в края на реда
    public void addCell(Cell cell) {
        if (firstCell == null) {
            firstCell = cell;
        } else {
            Cell current = firstCell;
            while (current.getNextCell() != null) {
                current = current.getNextCell();
            }
            current.setNextCell(cell);
        }
    }

    // Връща клетка по индекс (0-базиран)
    public Cell getCell(int index) {
        Cell current = firstCell;
        int i = 0;
        while (current != null && i < index) {
            current = current.getNextCell();
            i++;
        }
        return current;
    }

    // Връща първата клетка в реда
    public Cell getFirstCell() { 
        return firstCell; 
    }

    // Връща следващия ред
    public Row getNextRow() { 
        return nextRow; 
    }

    // Задава следващия ред
    public void setNextRow(Row nextRow) { 
        this.nextRow = nextRow; 
    }

    // Задава първата клетка
    public void setFirstCell(Cell cell) { 
        this.firstCell = cell; 
    }

    // Връща броя клетки в реда
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
