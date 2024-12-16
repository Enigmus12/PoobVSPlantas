package presentation;

import javax.swing.*;
import domain.Board;
/**
 * Clase `MainApp` que representa el controlador principal de la aplicación.
 * Esta clase es responsable de gestionar las diferentes ventanas del juego,
 * incluyendo el menú principal, las pantallas de un jugador, dos jugadores y el juego principal.
 */
public class MainApp {
    private MainMenu menu;           // Instancia del menú principal
    private OnePlayer onePlayer;     // Ventana para el modo de un jugador
    private TwoPlayer twoPlayer;     // Ventana para el modo de dos jugadores
    private ScreenGame screenGame;   // Pantalla principal del juego
    private Board board;             // Referencia al singleton de la lógica del juego
    private String gameMode;         // Modo de juego seleccionado (OnePlayer o TwoPlayer)

    /**
     * Constructor de la clase `MainApp`.
     * Inicializa los elementos de la aplicación y configura el modo de juego predeterminado.
     */
    public MainApp() {
        prepareElements(); // Inicializar los elementos principales
        board = Board.getBoard(); // Obtener la instancia del singleton Board
        gameMode = "OnePlayer";   // Modo de juego predeterminado
    }

    /**
     * Establece el modo de juego actual.
     * 
     * @param mode El modo de juego (por ejemplo, "OnePlayer" o "TwoPlayer").
     */
    public void setGameMode(String mode) {
        gameMode = mode;
    }

    /**
     * Obtiene el modo de juego actual.
     * 
     * @return El modo de juego actual.
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * Prepara e inicializa los elementos principales de la aplicación.
     * Muestra el menú principal al usuario.
     */
    public void prepareElements() {
        menu = new MainMenu(this); // Instanciar el menú principal
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configurar acción de cierre
        menu.setVisible(true); // Mostrar el menú principal
    }

    /**
     * Muestra el menú principal.
     * Oculta todas las demás ventanas activas.
     */
    public void showMainMenu() {
        // Ocultar otras ventanas activas
        if (onePlayer != null) onePlayer.setVisible(false);
        if (twoPlayer != null) twoPlayer.setVisible(false);

        // Mostrar el menú principal
        if (menu == null) {
            menu = new MainMenu(this);
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        menu.setVisible(true);
    }

    /**
     * Método auxiliar para mostrar una ventana específica.
     * Oculta todas las demás ventanas antes de mostrar la seleccionada.
     * 
     * @param ventanaAMostrar La ventana que se desea mostrar.
     */
    public void mostrarVentana(JFrame ventanaAMostrar) {
        // Ocultar todas las demás ventanas
        if (menu != null) menu.setVisible(false);
        if (onePlayer != null) onePlayer.setVisible(false);
        if (twoPlayer != null) twoPlayer.setVisible(false);

        // Mostrar la ventana deseada
        if (ventanaAMostrar != null) {
            ventanaAMostrar.setVisible(true);
        }
    }

    /**
     * Muestra la ventana para el modo de un jugador.
     * Inicializa la ventana si aún no ha sido creada.
     */
    public void showOnePlayer() {
        if (onePlayer == null) {
            onePlayer = new OnePlayer(this);
            onePlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        mostrarVentana(onePlayer);
    }

    /**
     * Muestra la ventana para el modo de dos jugadores.
     * Inicializa la ventana si aún no ha sido creada.
     */
    public void showTwoPlayer() {
        if (twoPlayer == null) {
            twoPlayer = new TwoPlayer(this);
            twoPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        mostrarVentana(twoPlayer);
    }

    /**
     * Muestra la pantalla principal del juego.
     * Inicializa una nueva instancia de la pantalla del juego.
     * 
     * @param mode El modo de juego (por ejemplo, "OnePlayer" o "TwoPlayer").
     */
    public void showScreenGame(String mode) {
        ScreenGame screenGame = new ScreenGame(this, mode);
        screenGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mostrarVentana(screenGame);
    }

    /**
     * Método principal de la aplicación.
     * Inicia la ejecución de la aplicación creando una instancia de `MainApp`.
     * 
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        MainApp app = new MainApp();
    }
}
