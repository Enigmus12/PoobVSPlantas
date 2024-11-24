package presentation;

import javax.swing.*;
import domain.GameController;

public class MainApp {
    private MainMenu menu;
    private OnePlayer onePlayer;
    private TwoPlayer twoPlayer;
    private GameController gameController;

    public MainApp() {
        prepareElements(); // Inicializar los elementos
        this.gameController = new GameController();
    }

    public GameController getGameController() {
        return gameController;
    }

    public void prepareElements() {
        menu = new MainMenu(this); // Instanciar el menú principal
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configurar cierre
        menu.setVisible(true); // Mostrar el menú principal
    }

    public void showMainMenu() {
        if (onePlayer != null) onePlayer.setVisible(false); // Ocultar ventana de OnePlayer
        if (twoPlayer != null) twoPlayer.setVisible(false); // Ocultar ventana de TwoPlayer
        if (menu == null) {
            menu = new MainMenu(this); // Crear la ventana del menú principal si no existe
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        menu.setVisible(true); // Mostrar menú principal
    }

    public void showOnePlayer() {
        if (menu != null) menu.setVisible(false); // Ocultar el menú principal
        if (twoPlayer != null) twoPlayer.setVisible(false); // Ocultar TwoPlayer si está abierto
        if (onePlayer == null) {
            onePlayer = new OnePlayer(this); // Crear ventana de OnePlayer si no existe
            onePlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        onePlayer.setVisible(true); // Mostrar OnePlayer
    }

    public void showTwoPlayer() {
        if (menu != null) menu.setVisible(false); // Ocultar el menú principal
        if (onePlayer != null) onePlayer.setVisible(false); // Ocultar OnePlayer si está abierto
        if (twoPlayer == null) {
            twoPlayer = new TwoPlayer(this); // Crear ventana de TwoPlayer si no existe
            twoPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        twoPlayer.setVisible(true); // Mostrar TwoPlayer
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
    }
}

