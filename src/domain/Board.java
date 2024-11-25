package domain;

import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.Point;
import java.util.HashMap;

public class Board {

    private Cell[][] cells;
    private ArrayList<Character> activeCharacters;
    private int suns;
    private int rows=5;
    private int cols=10;
    private Timer gameTimer;
    private static final int TIMER_DELAY = 100; 
    private String namePlayerOne;
    private String namePlayerTwo;
    private ZombiesOriginal zombieOriginal;

    private static Board boardSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Board getBoard(){
        if(boardSingleton == null) {
            boardSingleton = new Board();
        }

        return boardSingleton;
    }


    // Nueva instancia de Timer para los soles
    private Timer sunGenerationTimer;

    private void initializeSunGenerationTimer() {
        sunGenerationTimer = new Timer(10 * 1000, e -> {
            generateSuns();
        });
        sunGenerationTimer.start(); // Inicia el temporizador
    }

    // Modifica el constructor de `Board` para llamar al nuevo Timer
    private Board() {
        namePlayerOne = "";
        namePlayerTwo = "";
        this.cells = new Cell[rows][cols];
        this.activeCharacters = new ArrayList<>();
        this.suns = 0;
        zombieOriginal = new ZombiesOriginal();

        // Inicializar las celdas
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }

        initializeGameTimer(); // Ya existente
        initializeSunGenerationTimer(); // Llama al nuevo Timer
    }


    /**
     * Inicializa el timer del juego
     */
    private void initializeGameTimer() {
        gameTimer = new Timer(TIMER_DELAY, e -> {

        });
        gameTimer.start();
    }
    

    
    /**
     * Intenta colocar una planta en una posición específica.
     * @param plant planta a colocar
     * @param row fila
     * @param column columna
     * @return true si se pudo colocar la planta.
     */
    public boolean placePlant(Plant plant, int row, int column) throws PoobVSZombiesExeption {
        if (row < 0 || row >= rows || column < 0 || column >= cols) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }
    
        Cell cell = cells[row][column];
    
        if (cell.isOccupied()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_OCUPATED);
        }
    
        if (!plant.canBePlanted(row, column)) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }
    
        if (suns < plant.getSunCost()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INSUFFICIENT_SUNS);
        }
    
        // Colocamos la planta en la celda.
        cell.setOccupant(plant);
        plant.updatePosition(row, column); // Actualizamos la posición de la planta.
        activeCharacters.add(plant);      // Añadimos la planta a los personajes activos.
    
        suns -= plant.getSunCost();       // Reducimos el costo en soles.
        return true;
    }

    public void updateGameState() {
        // Generar soles periódicamente
        generateSuns();

        // Actualizar las plantas
        updatePlants();



        // Verificar colisiones entre zombies y plantas
        checkCollisions();

        // Eliminar personajes muertos
        removeDeadCharacters();
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
                    // Generar soles si corresponde
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
    public Plant addPlant(String plantType, int row, int column) throws PoobVSZombiesExeption {


        Cell cell = cells[row][column];

        // Verifica si la celda está ocupada
        if (cell.isOccupied()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_OCUPATED);
        }

        Plant plant;

        // Crear la planta según el tipo
        switch (plantType) {
            case "Sunflower":
                plant = new Sunflower(row,column);
                break;
            case "Peashooter":
                plant = new Peashooter(row,column);
                break;
            case "WallNut":
                plant = new WallNut(row,column);
                break;
            case "PotatoMine":
                plant = new PotatoMine(row,column);
                break;
            case "EciPlant":
                plant = new EciPlant(row,column);
                break;
            default:
                throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_PLANT + plantType);
        }

        // Verifica si la planta puede ser plantada y si hay suficientes soles
        if (!plant.canBePlanted(row, column)) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }
        if (suns < plant.getSunCost()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INSUFFICIENT_SUNS);
        }

        // Coloca la planta en la celda
        cell.setOccupant(plant);
        plant.updatePosition(row, column); // Actualiza la posición de la planta
        activeCharacters.add(plant);      // Añade la planta a los personajes activos

        suns -= plant.getSunCost();       // Reduce el costo en soles
        return plant;                     // Retorna la planta creada
    }
    /**
        * Genera soles periódicamente en el juego
        */
    public void generateSuns() {
        suns += 25; // Incrementa la cantidad de soles en 25
    }


    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getSuns() { return suns; }
    public Cell getCell(int row, int col) { return cells[row][col]; }
    public ArrayList<Character> getActiveCharacters() { return activeCharacters; }


    public void validateNameOnePlayer(String name) throws PoobVSZombiesExeption {
        if (name == null || name.trim().isEmpty() || name.equals("Enter your name here") ) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME);
        }
        namePlayerOne=name;
    }

    public void validateNameTwoPlayers(String name1, String name2) throws PoobVSZombiesExeption {
        if (name1 == null || name1.trim().isEmpty()  ) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME + " para player one" );
        }
        if (name2 == null || name2.trim().isEmpty() ) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME + " para player one" );
        }
        namePlayerOne=name1;
        namePlayerTwo=name2;
    }
    public HashMap<String,int[]> gameOnePlayer(){
        return zombieOriginal.attack();
    }

}