package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainMenu extends JFrame {
    private JButton onePlayer;
    private JButton twoPlayer;
    private JButton machineVsMachine;
    private JButton exit;
    private JPanel backgroundPanel;
    private MainApp mainApp; // Referencia al controlador principal

    public MainMenu(MainApp app) {
        this.mainApp = app; // Recibir referencia de MainApp
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        setTitle("Poob VS Zombies");
        setSize(800, 600); // Tamaño inicial
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un JPanel personalizado para dibujar la imagen de fondo
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar y dibujar la imagen de fondo
                ImageIcon backgroundImage = new ImageIcon("images/menu.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(null);

        // Crear los botones
        onePlayer = new JButton();
        twoPlayer = new JButton();
        machineVsMachine = new JButton();
        exit = new JButton();

        makeButtonTransparent(onePlayer);
        makeButtonTransparent(twoPlayer);
        makeButtonTransparent(machineVsMachine);
        makeButtonTransparent(exit);


        // Agregar los botones al panel de fondo
        backgroundPanel.add(onePlayer);
        backgroundPanel.add(twoPlayer);
        backgroundPanel.add(machineVsMachine);
        backgroundPanel.add(exit);

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
    private void makeButtonTransparent(JButton button) {
        button.setOpaque(false); // Hace que el fondo del botón sea transparente
        button.setContentAreaFilled(false); // Elimina el relleno del botón
        button.setBorderPainted(false); // Elimina el borde del botón
    }

    private void repositionButtons() {
        // Obtener dimensiones del panel
        int panelWidth = backgroundPanel.getWidth();
        int panelHeight = backgroundPanel.getHeight();

        // Calcular posiciones relativas
        int onePlayerX = (int) (panelWidth * 0.55);
        int onePlayerY = (int) (panelHeight * 0.16);
        int twoPlayerX = (int) (panelWidth*0.55);
        int twoPlayerY = (int) (panelHeight*0.32);
        int machinesX = (int) (panelWidth*0.55);
        int machinesY = (int) (panelHeight*0.47);
        int exitX = (int) (panelWidth*0.55);
        int exitY = (int) (panelHeight*0.6);

        // Ajustar tamaño y posición de los componentes
        onePlayer.setBounds(onePlayerX, onePlayerY, 180, 40);
        twoPlayer.setBounds(twoPlayerX,twoPlayerY,180,40);
        machineVsMachine.setBounds(machinesX,machinesY,180,40);
        exit.setBounds(exitX,exitY,180,40);
    }


    private void prepareActions() {
        onePlayer.addActionListener(e -> mainApp.showOnePlayer());
        // Acción del botón "2 Players"
        twoPlayer.addActionListener(e -> mainApp.showTwoPlayer());

        // Acción del botón "Machine VS Machine"
        machineVsMachine.addActionListener(e -> mainApp.showScreenGame());

        // Acción del botón "Exit"
        exit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                    "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

}