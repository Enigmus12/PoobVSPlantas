package presentation;

import domain.Board;
import domain.Peashooter;
import domain.Plant;
import domain.Sunflower;
import domain.WallNut;
import domain.PoobVSZombiesExeption;
import domain.PotatoMine;

import javax.swing.*;
import java.awt.*;

public class ScreenGame extends JFrame {
    private Board board;           // Singleton que maneja la lógica del juego
    private String selectedPlant;  // Almacena el tipo de planta seleccionada actualmente
    private MainApp app;           
    private JLabel sunsLabel;      // Etiqueta para mostrar la cantidad de soles
    private static final int ROWS = 5; 
    private static final int COLS = 10;

    public ScreenGame(MainApp app) {
        this.app = app; // Recibir referencia de MainApp
        this.board = Board.getBoard(); // Obtener instancia del tablero
        prepareElements();
        prepareActions();
    }

    public void prepareElements() {
        setTitle("Plants vs Zombies");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Configurar layout principal
        setLayout(new BorderLayout());

        // Crear paneles para cada región
        JPanel header = createHeader();
        JPanel footer = createPanelWithBackground(Color.LIGHT_GRAY, "Pie de Página");
        JPanel leftPanel = createLeftGridPanel();
        JPanel rightPanel = createPanelWithBackground(Color.DARK_GRAY, "Panel Derecho");
        JPanel centerPanel = createGameBoard();

        // Agregar paneles al JFrame
        add(header, BorderLayout.NORTH);
        add(footer, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        // Timer para actualizar el contador de soles
        Timer sunUpdateTimer = new Timer(1000, e -> updateSunsCounter());
        sunUpdateTimer.start();
    }

    public void prepareActions() {
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.GRAY);
        sunsLabel = new JLabel("Soles: " + board.getSuns());
        sunsLabel.setForeground(Color.WHITE);
        sunsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(sunsLabel);
        return header;
    }

    private JPanel createPanelWithBackground(Color color, String text) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centrar el texto
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE); // Texto blanco
        panel.add(label);
        return panel;
    }

    private JPanel createLeftGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        gridPanel.setPreferredSize(new Dimension(150, 0));

        // Crear botón para Plantas

        JButton sunflowerButton = createPlantButton("Sunflower", "images/cartaSunFlower.png");
        gridPanel.add(sunflowerButton);

        JButton PeashooterButton = createPlantButton("Peashooter", "images/cartaPeaShooter.png");
        gridPanel.add(PeashooterButton);

        JButton WalNutButton = createPlantButton("WallNut", "images/cartaWallNut.png");
        gridPanel.add(WalNutButton);

        JButton PotatoMineButton = createPlantButton("PotatoMine", "images/cartaPotatoMine.png");
        gridPanel.add(PotatoMineButton);




        return gridPanel;
    }

    
     /**
     * Botón para seleccionar una planta específica
     * @param plantType Tipo de planta 
     * @param imagePath Ruta de la imagen para el botón
     * @return JButton configurado para la selección de planta
     */
    private JButton createPlantButton(String plantType, String imagePath) {
        JButton button = new JButton();
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // Cargar la imagen
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));

        // Oyente de selección
        button.addActionListener(e -> {
            selectedPlant = plantType;
            // Mostrar borde de selección
            button.setBorderPainted(true);
        });

        return button;
    }

    private JPanel createGameBoard() {
        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 2, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("images/tablero.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Crear botones para cada celda
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JButton cellButton = createCellButton(i, j);
                panel.add(cellButton);
            }
        }
    
        return panel;
    }


    /**
     * Cremos un botón para una celda específica del tablero
     * @param row Fila de la celda
     * @param col Columna de la celda
     * @return JButton configurado para la celda
     */
    private JButton createCellButton(final int row, final int col) {
        JButton button = new JButton();
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);

        button.addActionListener(e -> {
            if (selectedPlant != null) {
                try {
                    Plant plant = null;
                    switch (selectedPlant) {
                        case "Sunflower":
                            plant = new Sunflower(row, col);
                            break;
                        case "Peashooter":
                            plant = new Peashooter(row, col);
                            break;
                        case "Wallnut":
                            plant = new WallNut(row, col);
                            break;  
                        case "PotatoMine":
                            plant = new PotatoMine(row, col);
                            break;
                    
                    }
        
                    if (plant != null) {
                        boolean placed = board.placePlant(plant, row, col);
                        if (placed) {
                            updateCellVisual(button, selectedPlant);
                            selectedPlant = null; // Reset selection
                            updateSunsCounter(); // Actualizar contador de soles
                        }
                    }
                } catch (PoobVSZombiesExeption exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                } 
            }
        });
        

        return button;
    }

    /**
     * Actualizamos el tablero bueno la celda cuando se coloca una planta
     * @param button Botón de la celda a actualizar
     * @param plantType Tipo de planta colocada
     */
    private void updateCellVisual(JButton button, String plantType) {
        try {
            ImageIcon icon = new ImageIcon("images/Carta" + plantType + ".png");
            Image scaledImage = icon.getImage().getScaledInstance(
                button.getWidth(), 
                button.getHeight(), 
                Image.SCALE_SMOOTH
            );
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de la planta: " + e.getMessage());
        }
    }

    /**
     * Actualizamos el contador de soles mostrado en el header
    */
    private void updateSunsCounter() {
        sunsLabel.setText("Soles: " + board.getSuns());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenGame screenGame = new ScreenGame(null); // null porque no hay MainApp en este ejemplo
            screenGame.setVisible(true);
        });
    }
}