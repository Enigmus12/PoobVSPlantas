package domain;

import java.awt.Point;

public class Cell {
    private Character occupant;
    private Point position;
    private int row;
    private int column;
    
    /**
     * Constructor de la celda
     * @param row fila de la celda
     * @param col columna de la celda
     */
    public Cell(int row, int col) {
        this.position = new Point(row, col);
        this.occupant = null;
        this.column = col;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Character getOccupant() { 
        return occupant; 
    }

    public Point getPosition() { 
        return position; 
    }
    
    /**
     * Verifica si la celda está ocupada
     * @return true si hay un ocupante (planta o zombie)
     */
    public boolean isOccupied() { 
        return occupant != null; 
    }
    
    /**
     * Coloca un personaje en la celda con reglas específicas
     * @param character personaje a colocar
     * @return true si se pudo colocar, false si no
     */
    public boolean setOccupant(Character character) {
        // Si no hay ningún ocupante, se puede colocar
        if (occupant == null) {
            this.occupant = character;
            return true;
        }
        
        // Permitir múltiples zombies en la misma fila
        if (character instanceof Zombie && occupant instanceof Zombie) {
            return true;
        }
        
        // Permitir que un zombie ocupe una celda con una planta
        if (character instanceof Zombie && occupant instanceof Plant) {
            return true;
        }
        
        // No permitir colocar una planta en una celda con zombie
        if (character instanceof Plant && occupant instanceof Zombie) {
            return false;
        }
        
        return false;
    }
    
    /**
     * Libera la celda
     */
    public void clear() {
        this.occupant = null;
    }
}