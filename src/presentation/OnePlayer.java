package presentation;

import javax.swing.*;

import domain.Board;
import domain.PoobVSZombiesExeption;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class OnePlayer extends JFrame {
    private JTextField userName;
    private JPanel backgroundPanel;
    private JButton play;
    private JButton back;
    private MainApp mainApp;
    private Board board;
    public OnePlayer(MainApp app) {
        board=Board.getBoard();
        this.mainApp = app; // Recibir referencia de MainApp
        prepareElements();
        prepareActions();
    }
    private void makeButtonTransparent(JButton button) {
        button.setOpaque(false); // Hace que el fondo del botón sea transparente
        button.setContentAreaFilled(false); // Elimina el relleno del botón
        button.setBorderPainted(false); // Elimina el borde del botón
    }
    public void prepareElements() {
        setTitle("One Player");
        setSize(800, 600); // Tamaño inicial
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un JPanel personalizado para dibujar la imagen de fondo
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar y dibujar la imagen de fondo
                ImageIcon backgroundImage = new ImageIcon("images/onePlayer.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(null); // Usar layout absoluto
        setContentPane(backgroundPanel);

        // Crear campo de texto y botón
        userName = new JTextField("Enter your name here");
        back = new JButton();
        play = new JButton();
        userName.setOpaque(false);
        userName.setBorder(null);
        userName.setForeground(Color.WHITE); // Color del texto
        userName.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente personalizada


        // Agregar los componentes al panel
        backgroundPanel.add(play);
        backgroundPanel.add(back);
        backgroundPanel.add(userName);
        makeButtonTransparent(play);
        makeButtonTransparent(back);

        // Agregar un listener para redimensionar
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionComponents(); // Reposicionar dinámicamente
            }
        });

        repositionComponents(); // Posicionar inicialmente
    }

    private void repositionComponents() {
        // Obtener dimensiones del panel
        int panelWidth = backgroundPanel.getWidth();
        int panelHeight = backgroundPanel.getHeight();

        // Calcular posiciones relativas
        int userNameX = (int) (panelWidth * 0.7); // 70% del ancho
        int userNameY = (int) (panelHeight * 0.3);
        int backX = (int) (panelWidth*0.015);
        int backY = (int) (panelHeight*0.8);
        int playX = (int) (panelWidth*0.7);
        int playY = (int) (panelHeight*0.7);
        // Ajustar tamaño y posición de los componentes
        userName.setBounds(userNameX, userNameY, 180, 40);
        back.setBounds(backX,backY,75,30);

        play.setBounds(playX,playY,200,30);
    }


    public void prepareActions() {
        back.addActionListener(e -> mainApp.showMainMenu());
        play.addActionListener(e -> {
            String name = userName.getText().trim(); // Eliminar espacios al inicio y al final

            try {
                board.validateNameOnePlayer(name);
                mainApp.showScreenGame("OnePlayer");


            } catch (PoobVSZombiesExeption exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage(), 
                                            "Error de Validación", JOptionPane.WARNING_MESSAGE);
            }
        });

    }

    


}