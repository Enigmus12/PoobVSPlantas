package presentation;

import javax.swing.*;
import domain.Board;

public class MainApp {
    private MainMenu menu;
    private OnePlayer onePlayer;
    private TwoPlayer twoPlayer;
    private ScreenGame screenGame;
    private Board board;
    private String gameMode;


    public MainApp() {
        prepareElements(); // Inicializar los elementos
        board = Board.getBoard();
        gameMode="OnePlayer";
    }

    public void setGameMode(String mode){
        gameMode=mode;
    }
    public String getGameMode(){
        return gameMode;
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

    public void showOnePlayer() {
        if (onePlayer == null) {
            onePlayer = new OnePlayer(this);
            onePlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        mostrarVentana(onePlayer);
    }

    public void showTwoPlayer() {
        if (twoPlayer == null) {
            twoPlayer = new TwoPlayer(this);
            twoPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        mostrarVentana(twoPlayer);
    }

    public void showScreenGame() {
        ScreenGame screenGame = new ScreenGame(this);
        screenGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mostrarVentana(screenGame);
    }





    public static void main(String[] args) {
        MainApp app = new MainApp();
    }
}