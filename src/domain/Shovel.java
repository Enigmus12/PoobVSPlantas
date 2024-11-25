package domain;

public class Shovel {
    private Board board;

    /**
     * Constructor de la pala.
     * La pala interactúa con el singleton del tablero.
     */
    public Shovel() {
        this.board = Board.getBoard();
    }

    /**
     * Elimina una planta del tablero en la posición especificada.
     * 
     * @param row la fila de la planta
     * @param col la columna de la planta
     */
    public void removePlant(int row, int col) throws PoobVSZombiesExeption {
        // Verificar si la posición es válida
        if (row < 0 || row >= board.getRows() || col < 0 || col >= board.getCols()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }

        // Obtener la celda
        Cell cell = board.getCell(row, col);

        // Verificar si la celda está ocupada por una planta
        if (!cell.isOccupied() || !(cell.getOccupant() instanceof Plant)) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_EMPTY);
        }

        // Eliminar la planta de la celda y de los personajes activos
        Plant plant = (Plant) cell.getOccupant();
        board.getActiveCharacters().remove(plant);
        cell.clear();
    }
}