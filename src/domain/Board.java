package domain;

import java.util.ArrayList;
import javax.swing.Timer;
import java.util.HashMap;

public class Board {

    private Cell[][] cells;
    private ArrayList<Character> activeCharacters;
    private int suns;
    private static final int ROWS = 5;
    private static final int COLS = 10;
    private Timer gameTimer;
    private Timer sunGenerationTimer;
    private String namePlayerOne;
    private String namePlayerTwo;
    private ZombiesOriginal zombieOriginal;

    private static Board boardSingleton;

    // Singleton para obtener la instancia de Board
    public static Board getBoard() {
        if (boardSingleton == null) {
            boardSingleton = new Board();
        }
        return boardSingleton;
    }

    // Inicializa la generación de soles cada 10 segundos
    private void initializeSunGenerationTimer() {
        sunGenerationTimer = new Timer(10 * 1000, e -> generateSuns());
        sunGenerationTimer.start();
    }

    // Constructor de Board
    private Board() {
        namePlayerOne = "";
        namePlayerTwo = "";
        this.cells = new Cell[ROWS][COLS];
        this.activeCharacters = new ArrayList<>();
        this.suns = 0;
        this.zombieOriginal = new ZombiesOriginal();

        // Inicializar las celdas
        initializeCells();

        // Inicializar timers
        initializeGameTimer();
        initializeSunGenerationTimer();
    }

    // Inicializa las celdas del tablero
    private void initializeCells() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    // Inicializa el timer del juego
    private void initializeGameTimer() {
        gameTimer = new Timer(100, e -> updateGameState());
        gameTimer.start();
    }

    // Actualiza el estado del juego
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

    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLS() {
        return COLS;
    }

    // Actualiza las plantas
    private void updatePlants() {
        for (Character character : activeCharacters) {
            if (character instanceof Plant) {
                Plant plant = (Plant) character;
                if (plant instanceof SunFlower) {
                    suns += ((SunFlower) plant).generateSuns();
                } else if (plant instanceof PeasShooter) {
                    ((PeasShooter) plant).attack();
                }
            }
        }
    }

    // Elimina los personajes muertos
    private void removeDeadCharacters() {
        activeCharacters.removeIf(character -> {
            if (!character.isAlive()) {
                cells[character.getPositionX()][character.getPositionY()].clear();
                return true;  // Elimina el personaje de la lista.
            }
            return false;  // Mantén el personaje en la lista.
        });
    }

    // Verifica las colisiones entre zombies y plantas
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

    // Añade una planta al tablero
    public Plant addPlant(String plantType, int row, int column) throws PoobVSZombiesExeption {
        Cell cell = cells[row][column];

        // Verifica si la celda está ocupada
        if (cell.isOccupied()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_OCUPATED);
        }

        Plant plant = createPlant(plantType, row, column);
        if (plant == null) {
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
        plant.updatePosition(row, column);  // Actualiza la posición de la planta
        activeCharacters.add(plant);        // Añade la planta a los personajes activos
        suns -= plant.getSunCost();         // Reduce el costo en soles

        return plant;  // Retorna la planta creada
    }

    // Crea la planta según el tipo proporcionado
    private Plant createPlant(String plantType, int row, int column) {
        switch (plantType) {
            case "SunFlower":
                return new SunFlower(row, column);
            case "PeasShooter":
                return new PeasShooter(row, column);
            case "WallNut":
                return new WallNut(row, column);
            case "PotatoMine":
                return new PotatoMine(row, column);
            case "EciPlant":
                return new EciPlant(row, column);
            default:
                return null;  // Planta no válida
        }
    }

    // Genera soles periódicamente
    public void generateSuns() {
        suns += 25;  // Incrementa la cantidad de soles en 25
    }


    public int getSuns() { return suns; }
    public Cell getCell(int row, int col) { return cells[row][col]; }
    public ArrayList<Character> getActiveCharacters() { return activeCharacters; }

    // Validación de nombres de jugadores
    public void validateNameOnePlayer(String name) throws PoobVSZombiesExeption {
        if (name == null || name.trim().isEmpty() || name.equals("Enter your name here")) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME);
        }
        namePlayerOne = name;
    }

    public void validateNameTwoPlayers(String name1, String name2) throws PoobVSZombiesExeption {
        if (name1 == null || name1.trim().isEmpty()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME + " para player one");
        }
        if (name2 == null || name2.trim().isEmpty()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME + " para player two");
        }
        namePlayerOne = name1;
        namePlayerTwo = name2;
    }

    // Método para realizar la logica del ZombieOriginal
    public HashMap<String, int[]> gameOnePlayer() {
        return zombieOriginal.attack();
    }

    public void shovel(int row,int column) throws PoobVSZombiesExeption{
        // Verificar si la posición es válida
        if (row < 0 || row >= getROWS() || column < 0 || column >= getCOLS()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }

        // Obtener la celda
        Cell cell = getCell(row, column);

        // Verificar si la celda está ocupada por una planta
        if (!cell.isOccupied() || !(cell.getOccupant() instanceof Plant)) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_EMPTY);
        }

        // Eliminar la planta de la celda y de los personajes activos
        Plant plant = (Plant) cell.getOccupant();
        getActiveCharacters().remove(plant);
        cell.clear();
    }
    }

