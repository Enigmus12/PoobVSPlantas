package presentation;

import domain.Board;
import domain.Peashooter;
import domain.Plant;
import domain.Sunflower;
import domain.WallNut;
import domain.eciPlant;
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
        setTitle("Poob vs Zombies");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear paneles para cada región
        JPanel header = createHeader();
        JPanel leftPanel = createLeftGridPanel();
        JPanel rightPanel = createRightPanel();
        JPanel centerPanel = createGameBoard();

        // Agregar paneles al JFrame
        add(header, BorderLayout.NORTH);
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
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(10, 120));
        header.setBackground(Color.GRAY);
    
        // Panel para los botones de las plantas
        JPanel plantsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        plantsPanel.setBackground(Color.DARK_GRAY);
    
        JButton sunflowerButton = createPlantButton("Sunflower", "images/cartaSunFlower.jpg");
        JButton peashooterButton = createPlantButton("Peashooter", "images/cartaPeasShooter.jpg");
        JButton walnutButton = createPlantButton("WallNut", "images/cartaWallNut.jpg");
        JButton potatoMineButton = createPlantButton("PotatoMine", "images/cartaPotatoMine.jpg");
        JButton eciPlantMineButton = createPlantButton("eciPlant", "images/cartaeciPlant.jpg");
    
        plantsPanel.add(sunflowerButton);
        plantsPanel.add(peashooterButton);
        plantsPanel.add(walnutButton);
        plantsPanel.add(potatoMineButton);
        plantsPanel.add(eciPlantMineButton);
    
        // Panel para el contador de soles
        JPanel sunsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sunsPanel.setPreferredSize(new Dimension(200, 120));
        sunsPanel.setBackground(Color.GRAY);
    
        sunsLabel = new JLabel("Soles: " + board.getSuns());
        sunsLabel.setForeground(Color.YELLOW);
        sunsLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Fuente más grande y destacada
        sunsPanel.add(sunsLabel);
    
        // Agregar los paneles al header
        header.add(plantsPanel, BorderLayout.CENTER); // Botones al centro-izquierda
        header.add(sunsPanel, BorderLayout.EAST);    // Soles a la derecha
    
        return header;
    }
    
 

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(200, 0));
        rightPanel.setBackground(Color.DARK_GRAY);
    
        // Imagen de la Carretera
        ImageIcon roadIcon = new ImageIcon("images/carretera.png");
        Image scaledImage = roadIcon.getImage().getScaledInstance(200, 600, Image.SCALE_DEFAULT); 
        JLabel roadLabel = new JLabel(new ImageIcon(scaledImage));
    
        rightPanel.add(roadLabel, BorderLayout.CENTER);
        return rightPanel;
    }

    private JPanel createLeftGridPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, 0)); 
        leftPanel.setBackground(Color.LIGHT_GRAY);

        ImageIcon davesHouseIcon = new ImageIcon("images/Dave'sHouse.png");
        Image scaledImage = davesHouseIcon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
        JLabel davesHouseLabel = new JLabel(new ImageIcon(scaledImage));

        leftPanel.add(davesHouseLabel, BorderLayout.CENTER);
        return leftPanel;
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
        Image scaledImage = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH); // Ajustar tamaño
        button.setIcon(new ImageIcon(scaledImage));
        button.setPreferredSize(new Dimension(90, 110)); // Tamaño fijo para todos los botones
    
        // Oyente de selección
        button.addActionListener(e -> {
            selectedPlant = plantType;
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
                        case "WallNut":
                            plant = new WallNut(row, col);
                            break;  
                        case "PotatoMine":
                            plant = new PotatoMine(row, col);
                            break;
                        case "eciPlant":
                            plant = new eciPlant(row, col);
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
        ImageIcon icon = new ImageIcon("images/" + plantType + ".png");
        Image scaledImage = icon.getImage().getScaledInstance(
            button.getWidth(),
            button.getHeight(),
            Image.SCALE_SMOOTH
        );
        button.setIcon(new ImageIcon(scaledImage));
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