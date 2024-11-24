package presentation;
import javax.swing.*;

import domain.PoobVSZombiesExeption;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
public class TwoPlayer extends JFrame {

    private JTextField userNameOnePlayer;
    private JTextField userNameTwoPlayer;
    private JButton play;
    private JButton back;
    private JPanel backgroundPanel;
    private MainApp mainApp; // Referencia al controlador principal

    public TwoPlayer(MainApp app){
        this.mainApp=app;
        prepareElements();
        prepareActions();
    }
    private void makeButtonTransparent(JButton button) {
        button.setOpaque(false); // Hace que el fondo del botón sea transparente
        button.setContentAreaFilled(false); // Elimina el relleno del botón
        button.setBorderPainted(false); // Elimina el borde del botón
    }
    public void prepareElements(){
        setTitle("Two player");
        setSize(800, 600); // Tamaño inicial
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un JPanel personalizado para dibujar la imagen de fondo
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar y dibujar la imagen de fondo
                ImageIcon backgroundImage = new ImageIcon("images/twoPlayer.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(null);

        // Crear los botones
        userNameOnePlayer = new JTextField("Name player one");
        play = new JButton();
        userNameTwoPlayer= new JTextField("Name player two");
        back = new JButton();
        userNameOnePlayer.setOpaque(false);
        userNameOnePlayer.setBorder(null);
        userNameOnePlayer.setForeground(Color.WHITE); // Color del texto
        userNameOnePlayer.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente personalizada
        userNameTwoPlayer.setOpaque(false);
        userNameTwoPlayer.setBorder(null);
        userNameTwoPlayer.setForeground(Color.WHITE); // Color del texto
        userNameTwoPlayer.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente personalizada
        makeButtonTransparent(back);
        makeButtonTransparent(play);
        backgroundPanel.add(userNameOnePlayer);
        backgroundPanel.add(userNameTwoPlayer);
        backgroundPanel.add(play);
        backgroundPanel.add(back);

        // Establecer el panel como el contenido principal del JFrame
        setContentPane(backgroundPanel);

        // Reposicionar botones cuando cambie el tamaño de la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons();
            }
        });

        // Posicionar los botones inicialmente
        repositionButtons();
    }
    public void prepareActions() {
        back.addActionListener(e -> mainApp.showMainMenu());
play.addActionListener(e -> {
    String name1 = userNameOnePlayer.getText().trim(); // Eliminar espacios al inicio y al final
    String name2 = userNameTwoPlayer.getText().trim();

    try {
        mainApp.getGameController().validateNameTwoPlayers(name1, "para el Jugador 1"); // Validar nombre del jugador 1
        mainApp.getGameController().validateNameTwoPlayers(name2, "para el Jugador 2"); // Validar nombre del jugador 2

        // Si ambos nombres son válidos, proceder al juego
        ScreenGame screenGame = new ScreenGame();
        screenGame.setVisible(true); // Mostrar la nueva ventana

        // Ocultar la ventana actual
        this.setVisible(false);
        this.dispose(); // Liberar recursos de la ventana actual si no se volverá a usar
    } catch (PoobVSZombiesExeption ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Error de Validación", JOptionPane.WARNING_MESSAGE);
    }
});
    }

    public void repositionButtons(){
        // Obtener dimensiones del panel
        int panelWidth = backgroundPanel.getWidth();
        int panelHeight = backgroundPanel.getHeight();

        // Calcular posiciones relativas
        int userName1X = (int) (panelWidth * 0.125);
        int userName1Y = (int) (panelHeight * 0.315);
        int userName2X = (int) (panelWidth * 0.61);
        int userName2Y = (int) (panelHeight * 0.325);
        int backX = (int) (panelWidth* 0.015);
        int backY = (int) (panelHeight* 0.8);
        int playX = (int) (panelWidth*0.4);
        int playY = (int) (panelHeight*0.7);
        // Ajustar tamaño y posición de los componentes
        userNameOnePlayer.setBounds(userName1X, userName1Y, 180, 40);
        userNameTwoPlayer.setBounds(userName2X, userName2Y, 180, 40);
        back.setBounds(backX,backY,105,30);


        play.setBounds(playX,playY,200,30);
    }

}
