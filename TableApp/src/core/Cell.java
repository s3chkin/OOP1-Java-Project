package core;
/**
 * Абстрактен клас, който представя една клетка в електронната таблица.
 * Всеки подтип на Cell трябва да дефинира как се показва стойността.
 */

public abstract class Cell {
    /**
     * Връща стойността на клетката като текст, подходящ за визуализиране.
     */
    public abstract String getDisplay();
    public abstract double getValue();
}
