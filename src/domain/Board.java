package domain;

import java.util.ArrayList;
import javax.swing.Timer;
import java.util.HashMap;

public class Board {

    private Character[][] characters;
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
        this.characters= new Character[ROWS][COLS];
        this.suns = 0;
        this.zombieOriginal = new ZombiesOriginal();
        // Inicializar timers
        initializeGameTimer();
        initializeSunGenerationTimer();
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

    }

    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLS() {
        return COLS;
    }

    // Añade una planta al tablero
    public void addPlant(String plantType, int row, int column) throws PoobVSZombiesExeption {
        // Verifica si las coordenadas están dentro de los límites del tablero
        if (row < 0 || row >= ROWS || column < 0 || column >= COLS) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }
    
        // Verifica si la celda está ocupada
        if (characters[row][column] != null) {
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
        characters[row][column] = plant;
        plant.updatePosition(row, column);  // Actualiza la posición de la planta
    
        suns -= plant.getSunCost();         // Reduce el costo en soles
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
    // Crea el zombie según el tipo proporcionado
    private Zombie createZombie(String zombieType, int row, int column) {
        if (zombieType.equals("ZombieBasic")) {
            return new BasicZombie(row, column);
        } else if (zombieType.equals("ZombieConehead")) {
            return new ConeheadZombie(row, column);
        } else if(zombieType.equals("ZombieBuckethead")){
            return new BucketheadZombie(row,column);
        }
        return null;
    }
    public void moveZombie(int row, int column) {
        // Verifica si las coordenadas están dentro de los límites
        if (row < 0 || row >= ROWS || column < 0 || column >= COLS) {
            System.out.println("Coordenadas fuera de rango: (" + row + ", " + column + ")");
            return;
        }

        // Obtén el zombie en la celda actual
        Character currentCell = characters[row][column];
        if (currentCell == null || !(currentCell instanceof Zombie)) {
            System.out.println(" no hay un zombie en la celda: (" + row + ", " + column + ")");
            return; // No hay nada que mover
        }
        Zombie zombie = (Zombie) currentCell;

        // Verifica si puede moverse a la siguiente celda
        int newColumn = column - 1; // El zombie se mueve hacia la izquierda
        if (newColumn < 0) {
            System.out.println("El zombie alcanzó el borde del tablero.");
            return; // El zombie no puede moverse fuera del tablero
        }


        // Mueve el zombie
        characters[row][newColumn] = zombie; // Actualiza la nueva posición
        characters[row][column] = null;      // Limpia la posición anterior
        zombie.move();                       // Ejecuta la lógica de movimiento del zombie
    }


    // Método para realizar la logica del ZombieOriginal
    public HashMap<String, int[]> gameOnePlayer() throws PoobVSZombiesExeption{
        HashMap<String,int[]> informacionZombie=zombieOriginal.attack();
        String zombieType = informacionZombie.keySet().iterator().next();
        int[] coordenadas =informacionZombie.get(zombieType);
        Character cell=characters[coordenadas[0]][coordenadas[1]];
        if (cell!=null){
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_OCUPATED);
        }

        System.out.println(zombieType);
        Zombie zombie=createZombie(zombieType, coordenadas[0],coordenadas[1]);
        characters[coordenadas[0]][coordenadas[1]]=zombie;

        return informacionZombie;
    }

    public void shovel(int row, int column) throws PoobVSZombiesExeption {
        // Verificar si la posición es válida
        if (row < 0 || row >= getROWS() || column < 0 || column >= getCOLS()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }
        Character cell = characters[row][column];
    
        if (cell == null || !(cell instanceof Plant)) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_EMPTY);
        }
    
        characters[row][column] = null;
    }

    public boolean damageZombie(int row, int column, String typeDamage) {
        Character character = characters[row][column];
        if (character instanceof Zombie) {  // Verifica si es una instancia de Zombie
            Zombie zombie = (Zombie) character;
            if ("Pea".equals(typeDamage)) {  // Comprobación de tipo de daño
                zombie.strikePea();  // Aplica el daño
            }
            
            // Si el zombie ya no está vivo, eliminarlo del tablero
            if (!zombie.isAlive()) {
                characters[row][column] = null;
                return false;  // Indicar que el zombie ha sido eliminado
            }
            
            return true;  // El zombie sigue vivo
        } else {

            return false;  // Si no hay zombi, retorna false
        }
    }


    public boolean ZombieAttack(int row, int col) {
        Character currentCharacter = characters[row][col];

        // Si la celda actual contiene un zombie
        if (currentCharacter instanceof Zombie) {
            Zombie zombie = (Zombie) currentCharacter;

            // Verificamos que la celda a la izquierda del zombie (su dirección de ataque)
            if (col > 0) {
                Character targetCharacter = characters[row][col - 1];

                // Si hay una planta en la celda adyacente
                if (targetCharacter instanceof Plant) {
                    Plant plant = (Plant) targetCharacter;

                    plant.receiveDamage(zombie.damage);

                    // Si la planta muere, eliminarla completamente del tablero
                    if (plant.health <= 0) {
                        characters[row][col - 1] = null;
                        return false;  // La planta ha sido eliminada
                    }

                    return true;  // La planta sigue viva
                }
            }
        }

        // Si no hay zombie o no hay una planta para atacar, retornar falso por defecto
        return false;
    }


    public void lawnmower(int row){
        for (int col = 0; col < characters[row].length; col++) {
            characters[row][col]=null;
        }
    }

    public String getNamePlayerOne() {
        return namePlayerOne;
    }


    public void removePlant(int row, int column) {
        characters[row][column]=null;
    }
}

