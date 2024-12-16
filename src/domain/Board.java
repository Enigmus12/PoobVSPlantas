package domain;

import javax.swing.Timer;
import java.util.HashMap;

/**
 * Clase `Board` que representa el tablero del juego Poob VS Zombies.
 * Implementa un patrón Singleton para mantener una única instancia del tablero durante el juego.
 * Maneja la lógica relacionada con las plantas, zombies, generación de soles y validación de nombres de jugadores.
 */
public class Board {

    private Character[][] characters; // Matriz que representa las celdas del tablero
    private int suns;                 // Cantidad de soles disponibles
    private static final int ROWS = 5; // Número de filas del tablero
    private static final int COLS = 10; // Número de columnas del tablero
    private Timer sunGenerationTimer; // Temporizador para la generación de soles
    private String namePlayerOne;     // Nombre del jugador 1
    private String namePlayerTwo;     // Nombre del jugador 2
    private ZombiesOriginal zombieOriginal; // Instancia para generar zombies
    private static Board boardSingleton;    // Instancia única del tablero (Singleton)

    /**
     * Obtiene la instancia única del tablero.
     *
     * @return Instancia del tablero.
     */
    public static Board getBoard() {
        if (boardSingleton == null) {
            boardSingleton = new Board();
        }
        return boardSingleton;
    }

    /**
     * Inicializa el temporizador para la generación de soles.
     */
    private void initializeSunGenerationTimer() {
        sunGenerationTimer = new Timer(10 * 1000, e -> generateSuns());
        sunGenerationTimer.start();
    }

    /**
     * Constructor privado de la clase `Board`.
     * Inicializa los elementos del tablero.
     */
    private Board() {
        namePlayerOne = "";
        namePlayerTwo = "";
        this.characters = new Character[ROWS][COLS];
        this.suns = 50;
        this.zombieOriginal = new ZombiesOriginal();
        initializeSunGenerationTimer(); // Inicia el temporizador de soles
    }

    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLS() {
        return COLS;
    }

    /**
     * Añade una planta al tablero en una posición específica.
     *
     * @param plantType Tipo de planta a añadir.
     * @param row Fila donde se colocará la planta.
     * @param column Columna donde se colocará la planta.
     * @throws PoobVSZombiesExeption Si la posición es inválida, la celda está ocupada o no hay suficientes soles.
     */
    public void addPlant(String plantType, int row, int column) throws PoobVSZombiesExeption {
        if (row < 0 || row >= ROWS || column < 0 || column >= COLS) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }

        if (characters[row][column] != null) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_OCUPATED);
        }

        Plant plant = createPlant(plantType, row, column);
        if (plant == null) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_PLANT + plantType);
        }

        if (!plant.canBePlanted(row, column)) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }

        if (suns < plant.getSunCost()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INSUFFICIENT_SUNS);
        }

        characters[row][column] = plant;
        plant.updatePosition(row, column);
        suns -= plant.getSunCost();
    }

    /**
     * Crea una planta según el tipo proporcionado.
     *
     * @param plantType Tipo de planta.
     * @param row Fila donde se colocará.
     * @param column Columna donde se colocará.
     * @return Objeto de tipo `Plant` creado.
     */
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
                return null;
        }
    }

    /**
     * Genera soles incrementando el contador en 25.
     */
    public void generateSuns() {
        suns += 25;
    }

    public int getSuns() {
        return suns;
    }

    /**
     * Valida el nombre del jugador 1.
     *
     * @param name Nombre a validar.
     * @throws PoobVSZombiesExeption Si el nombre es inválido.
     */
    public void validateNameOnePlayer(String name) throws PoobVSZombiesExeption {
        if (name == null || name.trim().isEmpty() || name.equals("Enter your name here")) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INVALID_NAME);
        }
        namePlayerOne = name;
    }

    /**
     * Valida los nombres de los dos jugadores.
     *
     * @param name1 Nombre del jugador 1.
     * @param name2 Nombre del jugador 2.
     * @throws PoobVSZombiesExeption Si algún nombre es inválido.
     */
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

    /**
     * Crea un zombie basado en el tipo especificado.
     *
     * @param zombieType Tipo de zombie.
     * @param row Fila donde se generará.
     * @param column Columna donde se generará.
     * @return Objeto `Zombie` creado.
     */
    private Zombie createZombie(String zombieType, int row, int column) {
        switch (zombieType) {
            case "ZombieBasic":
                return new BasicZombie(row, column);
            case "ZombieConehead":
                return new ConeheadZombie(row, column);
            case "ZombieBuckethead":
                return new BucketheadZombie(row, column);
            default:
                return null;
        }
    }

    /**
     * Mueve un zombie una celda hacia la izquierda.
     *
     * @param row Fila del zombie.
     * @param column Columna del zombie.
     */
    public void moveZombie(int row, int column) {
        if (row < 0 || row >= ROWS || column < 0 || column >= COLS) {
            return;
        }

        Character currentCell = characters[row][column];
        if (currentCell == null || !(currentCell instanceof Zombie)) {
            return;
        }

        Zombie zombie = (Zombie) currentCell;
        int newColumn = column - 1;

        if (newColumn < 0) {
            return;
        }

        characters[row][newColumn] = zombie;
        characters[row][column] = null;
        zombie.move();
    }

    /**
     * Genera un zombie original en el tablero.
     *
     * @return Información del zombie generado (tipo y posición).
     * @throws PoobVSZombiesExeption Si la celda está ocupada.
     */
    public HashMap<String, int[]> gameOnePlayer() throws PoobVSZombiesExeption {
        HashMap<String, int[]> informacionZombie = zombieOriginal.attack();
        String zombieType = informacionZombie.keySet().iterator().next();
        int[] coordenadas = informacionZombie.get(zombieType);
        Character cell = characters[coordenadas[0]][coordenadas[1]];

        if (cell != null) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_OCUPATED);
        }

        Zombie zombie = createZombie(zombieType, coordenadas[0], coordenadas[1]);
        characters[coordenadas[0]][coordenadas[1]] = zombie;

        return informacionZombie;
    }

    /**
     * Usa la pala para eliminar una planta en una posición específica.
     *
     * @param row Fila de la planta.
     * @param column Columna de la planta.
     * @throws PoobVSZombiesExeption Si la posición es inválida o está vacía.
     */
    public void shovel(int row, int column) throws PoobVSZombiesExeption {
        if (row < 0 || row >= getROWS() || column < 0 || column >= getCOLS()) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.INCORRECT_POSITION);
        }
        Character cell = characters[row][column];

        if (cell == null || !(cell instanceof Plant)) {
            throw new PoobVSZombiesExeption(PoobVSZombiesExeption.CELL_IS_EMPTY);
        }

        characters[row][column] = null;
    }

    /**
     * Aplica daño a un zombie en una posición específica del tablero.
     *
     * @param row       La fila donde está ubicado el zombie.
     * @param column    La columna donde está ubicado el zombie.
     * @param typeDamage Tipo de daño que se aplica (e.g., "Pea").
     * @return `true` si el zombie sigue vivo después del daño, `false` si ha sido eliminado.
     */
    public boolean damageZombie(int row, int column, String typeDamage) {
        Character character = characters[row][column];

        if (character instanceof Zombie) { // Verifica si el carácter es un zombie
            Zombie zombie = (Zombie) character;

            if ("Pea".equals(typeDamage)) { // Aplica daño si el tipo es "Pea"
                zombie.strikePea();
            }

            // Si el zombie no está vivo después del daño, se elimina del tablero
            if (!zombie.isAlive()) {
                characters[row][column] = null;
                return false; // El zombie ha sido eliminado
            }

            return true; // El zombie sigue vivo
        } else {
            return false; // No hay un zombie en la posición
        }
    }

    /**
     * Permite que un zombie ataque una planta en una celda adyacente a la izquierda.
     *
     * @param row La fila donde está el zombie.
     * @param col La columna donde está el zombie.
     * @return `true` si la planta sigue viva después del ataque, `false` si ha sido eliminada.
     */
    public boolean ZombieAttack(int row, int col) {
        Character currentCharacter = characters[row][col];

        if (currentCharacter instanceof Zombie) { // Si hay un zombie en la celda actual
            Zombie zombie = (Zombie) currentCharacter;

            if (col > 0) { // Verifica si hay una celda a la izquierda
                Character targetCharacter = characters[row][col - 1];

                if (targetCharacter instanceof Plant) { // Si la celda contiene una planta
                    Plant plant = (Plant) targetCharacter;

                    plant.receiveDamage(zombie.damage); // El zombie ataca la planta

                    if (plant.health <= 0) { // Si la planta muere
                        characters[row][col - 1] = null; // Elimina la planta del tablero
                        return false; // La planta ha sido eliminada
                    }

                    return true; // La planta sigue viva
                }
            }
        }

        return false; // No hay zombie o planta para atacar
    }

    /**
     * Usa la cortadora de césped en una fila específica, eliminando todos los objetos de esa fila.
     *
     * @param row La fila donde se activará la cortadora de césped.
     */
    public void lawnmower(int row) {
        for (int col = 0; col < characters[row].length; col++) {
            characters[row][col] = null; // Vacía todas las celdas de la fila
        }
    }

    /**
     * Obtiene el nombre del jugador uno.
     *
     * @return El nombre del jugador uno.
     */
    public String getNamePlayerOne() {
        return namePlayerOne;
    }

    /**
     * Elimina una planta en una posición específica del tablero.
     *
     * @param row    La fila donde está la planta.
     * @param column La columna donde está la planta.
     */
    public void removePlant(int row, int column) {
        characters[row][column] = null; // Vacía la celda específica
    }

    /**
     * Añade una cantidad específica de soles al contador de soles.
     *
     * @param sunAmount Cantidad de soles a añadir.
     */
    public void addSuns(int sunAmount) {
        suns += sunAmount; // Incrementa los soles disponibles
    }
}

