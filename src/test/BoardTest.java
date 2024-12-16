package test;

import domain.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class BoardTest {
    private Board board;

    @Before
    public void setUp() {
        // Obtiene la instancia singleton antes de cada prueba
        board = Board.getBoard();
    }

    @Test
    public void testSingletonInstance() {
        // Verifica que Board sea una instancia singleton
        Board anotherBoard = Board.getBoard();
        assertSame("Board debe ser un singleton", board, anotherBoard);
    }

    @Test
    public void testGenerateSuns() {
        // Verifica que el método generateSuns incremente el número de soles en 25
        int initialSuns = board.getSuns();
        board.generateSuns();
        assertEquals("Generar soles debe incrementar el total en 25", 
                     initialSuns + 25, board.getSuns());
    }

    @Test
    public void testValidateNameOnePlayer() throws PoobVSZombiesExeption {
        // Verifica que se pueda establecer correctamente el nombre del primer jugador
        board.validateNameOnePlayer("John");
        assertEquals("El nombre del jugador debe establecerse correctamente", 
                     "John", board.getNamePlayerOne());
    }

    @Test(expected = PoobVSZombiesExeption.class)
    public void testValidateNameOnePlayerInvalid() throws PoobVSZombiesExeption {
        // Verifica que lanzará una excepción si el nombre del primer jugador está vacío
        board.validateNameOnePlayer("");
    }

    @Test
    public void testValidateNameTwoPlayers() throws PoobVSZombiesExeption {
        // Verifica que se puedan establecer correctamente los nombres de ambos jugadores
        board.validateNameTwoPlayers("Player1", "Player2");
        assertEquals("El nombre del primer jugador debe establecerse", 
                     "Player1", board.getNamePlayerOne());
    }

    @Test(expected = PoobVSZombiesExeption.class)
    public void testValidateNameTwoPlayersInvalidFirstName() throws PoobVSZombiesExeption {
        // Verifica que lanzará una excepción si el nombre del primer jugador está vacío
        board.validateNameTwoPlayers("", "Player2");
    }

    @Test(expected = PoobVSZombiesExeption.class)
    public void testValidateNameTwoPlayersInvalidSecondName() throws PoobVSZombiesExeption {
        // Verifica que lanzará una excepción si el nombre del segundo jugador está vacío
        board.validateNameTwoPlayers("Player1", "");
    }

    @Test
    public void testAddPlant() throws PoobVSZombiesExeption {
        // Verifica que se pueda añadir una planta al tablero si hay soles suficientes
        while (board.getSuns() < 50) {
            board.generateSuns(); // Genera soles hasta tener suficientes
        }
        
        board.addPlant("SunFlower", 2, 3);
        // Se podrían añadir más verificaciones para asegurar que la planta esté correctamente posicionada
    }

    @Test(expected = PoobVSZombiesExeption.class)
    public void testAddPlantInvalidPosition() throws PoobVSZombiesExeption {
        // Verifica que lanzará una excepción si la posición para añadir la planta no es válida
        board.addPlant("SunFlower", -1, 10);
    }

    @Test(expected = PoobVSZombiesExeption.class)
    public void testAddPlantInsufficientSuns() throws PoobVSZombiesExeption {
        // Verifica que lanzará una excepción si no hay suficientes soles para añadir una planta
        while (board.getSuns() > 0) {
            // Restablece los soles a 0 simulando una situación sin soles
            board.generateSuns();
        }
        
        board.addPlant("SunFlower", 2, 3);
    }

    @Test(expected = PoobVSZombiesExeption.class)
    public void testShovelInvalidPosition() throws PoobVSZombiesExeption {
        // Verifica que lanzará una excepción si se intenta usar la pala en una posición no válida
        board.shovel(-1, 10);
    }
    
    @Test(expected = PoobVSZombiesExeption.class)
    public void testAddPlantToOccupiedCell() throws PoobVSZombiesExeption {
        // Verifica que lanzará una excepción si se intenta añadir una planta a una celda ya ocupada
        while (board.getSuns() < 100) {
            board.generateSuns(); // Genera soles hasta tener suficientes
        }

        board.addPlant("SunFlower", 2, 3); // Añade una planta a la celda
        board.addPlant("PeasShooter", 2, 3); // Intenta añadir otra planta en la misma celda
    }

    @Test
    public void testShovelPlant() throws PoobVSZombiesExeption {
        // Verifica que se pueda remover una planta del tablero usando la pala
        while (board.getSuns() < 50) {
            board.generateSuns(); // Genera soles hasta tener suficientes
        }

        board.addPlant("SunFlower", 2, 3); // Añade una planta
        board.shovel(2, 3); // Remueve la planta con la pala
    }

    @Test
    public void testRowsAndColumns() {
        // Verifica que el tablero tenga el número correcto de filas y columnas
        assertEquals("El tablero debe tener 5 filas", 5, Board.getROWS());
        assertEquals("El tablero debe tener 10 columnas", 10, Board.getCOLS());
    }
    
    // Test zombie damage
    @Test
    public void testDamageZombie() throws PoobVSZombiesExeption {
        // Add a zombie to the board first
        HashMap<String, int[]> zombieInfo = board.gameOnePlayer();
        String zombieType = zombieInfo.keySet().iterator().next();
        int[] coords = zombieInfo.get(zombieType);

        // Simulate damaging the zombie
        assertTrue("Zombie should take damage", board.damageZombie(coords[0], coords[1], "Pea"));
    }
    
}
