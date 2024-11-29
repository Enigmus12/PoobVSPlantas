package domain;

import java.awt.Point;


public class Cell {
    private Character occupant;
    private Point position;
    private boolean isOccupied;
    private int row;
    private int column;
    
    /**
     * Constructor de la celda
     * @param row fila de la celda
     * @param col columna de la celda
     */
    public Cell(int row, int col) {
        this.position = new Point(row, col);
        this.isOccupied = false;
        this.occupant = null;
        this.column=col;
        this.row=row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Character getOccupant() { return occupant; }
    public Point getPosition() { return position; }
    public boolean isOccupied() { return isOccupied; }
    
    /**
     * Coloca un personaje en la celda
     * @param character personaje a colocar
     * @return true si se pudo colocar, false si ya estaba ocupada
     */
    public boolean setOccupant(Character character) {
        if (!isOccupied) {
            this.occupant = character;
            this.isOccupied = true;
            return true;
        }
        return false;
    }
    
    /**
     * Libera la celda
     */
    public void clear() {
        this.occupant = null;
        this.isOccupied = false;
    }
}
