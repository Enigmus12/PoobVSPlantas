package domain;

import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.Point;

public class board {
    private Cell[][] cells;
    private ArrayList<Character> activeCharacters;
    private int suns;
    private int rows;
    private int cols;
    private Timer gameTimer;
    private static final int TIMER_DELAY = 100; 
    
    /**
     * Constructor del tablero
     * @param rows número de filas
     * @param cols número de columnas
     */
    public board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        this.activeCharacters = new ArrayList<>();
        this.suns = 50;
        
        // Inicializar las celdas
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
        
        initializeGameTimer();
    }
    
    /**
     * Inicializa el timer del juego
     */
    private void initializeGameTimer() {
        gameTimer = new Timer(TIMER_DELAY, e -> {
            updateGameState();
        });
        gameTimer.start();
    }
    
    /**
     * Actualiza el estado del juego
     */
    private void updateGameState() {
        moveZombies();
        updatePlants();
        checkCollisions();
        removeDeadCharacters();
        generateSuns();
    }
    
    /**
     * Intenta colocar una planta en una posición específica.
     * @param plant planta a colocar
     * @param row fila
     * @param col columna
     * @return true si se pudo colocar la planta.
     */
    public boolean placePlant(Plant plant, int row, int col) {
        // Verifica que la posición está dentro de los límites del tablero.
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }

        Cell cell = cells[row][col];
        // Verifica si la celda está ocupada y si la planta puede ser plantada.
        if (!cell.isOccupied() && plant.canBePlanted() && suns >= plant.getSunCost()) {
            // Coloca la planta en la celda.
            cell.setOccupant(plant);
            plant.updatePosition(row, col); // Actualiza la posición de la planta.
            activeCharacters.add(plant);   // Añade la planta a los personajes activos.
            suns -= plant.getSunCost();    // Reduce el costo en soles.
            return true;
        }

        return false;
    }

    
    /**
     * Añade un zombie al tablero en una fila específica
     * @param zombie zombie a añadir
     * @param row fila donde aparecerá
     */
    public void addZombie(Zombie zombie, int row) {
        if (row < 0 || row >= rows) return;
        
        zombie.updatePosition(row, cols - 1);
        cells[row][cols - 1].setOccupant(zombie);
        activeCharacters.add(zombie);
    }
    
    /**
     * Mueve los zombies en el tablero
     */
    private void moveZombies() {
        for (Character character : activeCharacters) {
            if (character instanceof Zombie) {
                Zombie zombie = (Zombie) character;
                Point oldPos = new Point(zombie.getPositionX(), zombie.getPositionY());
                zombie.advance();
                Point newPos = new Point(zombie.getPositionX(), zombie.getPositionY());
                
                // Actualizar las celdas
                if (!oldPos.equals(newPos)) {
                    cells[oldPos.x][oldPos.y].clear();
                    if (newPos.y >= 0) {
                        cells[newPos.x][newPos.y].setOccupant(zombie);
                    }
                }
            }
        }
    }
    
    /**
     * Actualiza el estado de todas las plantas
     */
    private void updatePlants() {
        for (Character character : activeCharacters) {
            if (character instanceof Plant) {
                Plant plant = (Plant) character;
                
                if (plant instanceof Sunflower) {
                    Sunflower sunflower = (Sunflower) plant;
                    suns += sunflower.generateSuns();
                }
                
                if (plant instanceof Peashooter) {
                    Peashooter peaShooter = (Peashooter) plant;
                    peaShooter.attack();
                }
            }
        }
    }
    
    /**
     * Revisa y elimina los personajes muertos.
     */
    private void removeDeadCharacters() {
        activeCharacters.removeIf(character -> {
            if (!character.isAlive()) {
                cells[character.getPositionX()][character.getPositionY()].clear();
                return true; // Elimina el personaje de la lista.
            }
            return false; // Mantén el personaje en la lista.
        });
    }

    
    /**
        * Verifica las colisiones entre personajes
    */
    private void checkCollisions() {
        for (Character character : activeCharacters) {
            if (character instanceof Zombie) {
                Zombie zombie = (Zombie) character;
                int row = zombie.getPositionX();
                int col = zombie.getPositionY();
                
                // Verificar si hay una planta en la misma posición
                if (col >= 0 && cells[row][col].isOccupied()) {
                    Character occupant = cells[row][col].getOccupant();
                    if (occupant instanceof Plant) {
                        zombie.attack();
                        ((Plant) occupant).receiveDamage(10); // Daño base del zombie
                    }
                }
            }
        }
    }
    
    /**
        * Genera soles periódicamente en el juego
        */
    private void generateSuns() {
        // Lógica para generar soles naturalmente durante el juego
        if (Math.random() < 0.05) { // 5% de probabilidad cada actualización
            suns += 25;
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getSuns() { return suns; }
    public Cell getCell(int row, int col) { return cells[row][col]; }
    public ArrayList<Character> getActiveCharacters() { return activeCharacters; }

}
