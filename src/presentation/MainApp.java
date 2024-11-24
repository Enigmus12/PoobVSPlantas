package presentation;

import javax.swing.*;
import domain.Board;

public class MainApp {
    private MainMenu menu;
    private OnePlayer onePlayer;
    private TwoPlayer twoPlayer;
    private ScreenGame screenGame;
    private Board board;

    public MainApp() {
        prepareElements(); // Inicializar los elementos
        board = Board.getBoard();
    }



    public void prepareElements() {
        menu = new MainMenu(this); // Instanciar el menú principal
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configurar cierre
        menu.setVisible(true); // Mostrar el menú principal
    }

    public void showMainMenu() {
        // Ocultar todas las demás ventanas
        if (onePlayer != null) onePlayer.setVisible(false);
        if (twoPlayer != null) twoPlayer.setVisible(false);

        // Mostrar el menú principal
        if (menu == null) {
            menu = new MainMenu(this);
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        menu.setVisible(true);
    }

    public void showOnePlayer() {
        // Ocultar todas las demás ventanas
        if (menu != null) menu.setVisible(false);
        if (twoPlayer != null) twoPlayer.setVisible(false);

        // Mostrar la ventana de un jugador
        if (onePlayer == null) {
            onePlayer = new OnePlayer(this);
            onePlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        onePlayer.setVisible(true);
    }

    public void showTwoPlayer() {
        // Ocultar todas las demás ventanas
        if (menu != null) menu.setVisible(false);
        if (onePlayer != null) onePlayer.setVisible(false);

        // Mostrar la ventana de dos jugadores
        if (twoPlayer == null) {
            twoPlayer = new TwoPlayer(this);
            twoPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        twoPlayer.setVisible(true);
    }

    public void showScreenGame() {
        // Ocultar las otras ventanas
        if (menu != null) menu.setVisible(false); // Ocultar el menú principal
        if (onePlayer != null) onePlayer.setVisible(false); // Ocultar OnePlayer si está abierto
        if (twoPlayer != null) twoPlayer.setVisible(false); // Ocultar TwoPlayer si está abierto

        // Crear y mostrar el ScreenGame
        ScreenGame screenGame = new ScreenGame(this); // Crear la ventana del tablero
        screenGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configurar el cierre
        screenGame.setVisible(true); // Mostrar el tablero
    }





    public static void main(String[] args) {
        MainApp app = new MainApp();
    }
}