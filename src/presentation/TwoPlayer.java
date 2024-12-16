package presentation;
import javax.swing.*;

import domain.Board;
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
    private Board board;
    public TwoPlayer(MainApp app){
        board=Board.getBoard();
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
            JOptionPane.showMessageDialog(this,
                    "La funcionalidad de dos jugadores está en Construccion.\n¡Próximamente estará disponible!",
                    "Funcionalidad en mantenimiento",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        
    };
    

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