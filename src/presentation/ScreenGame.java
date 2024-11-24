package presentation;

import domain.Board;
import domain.Plant;
import domain.PoobVSZombiesExeption;

import javax.swing.*;
import java.awt.*;

public class ScreenGame extends JFrame {
    private Board board;           // Singleton que maneja la lógica del juego
    private String selectedPlant;  // Almacena el tipo de planta seleccionada actualmente
    private MainApp app;
    private JLabel sunsLabel;      // Etiqueta para mostrar la cantidad de soles
    private static final int ROWS = 5;
    private static final int COLS = 10;
    private GameCell[][] cells;

    public ScreenGame(MainApp app) {
        this.app = app; // Recibir referencia de MainApp
        this.board = Board.getBoard(); // Obtener instancia del tablero
        this.cells = new GameCell[ROWS][COLS]; // Inicializar matriz de celdas
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
        // Asignar listeners a cada GameCell del tablero
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                GameCell cell = cells[row][col];
                cell.addActionListener(e -> {
                    if (selectedPlant != null) {
                        try {
                            // Intentar colocar la planta en la celda seleccionada
                            Plant plant = board.addPlant(selectedPlant, cell.getRow(), cell.getColumn());
                            cell.addPlant(selectedPlant); // Actualizar GameCell con la planta seleccionada
                            cell.repaint(); // Redibujar la celda
                            updateSunsCounter(); // Actualizar contador de soles
                            selectedPlant = null; // Limpiar selección de planta
                        } catch (PoobVSZombiesExeption ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Seleccione una planta primero.",
                                "Sin selección", JOptionPane.WARNING_MESSAGE);
                    }
                });
            }
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(10, 120));
        header.setBackground(Color.GRAY);
    
        // Panel para el contador de soles con imagen de fondo
        JPanel sunPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("images/TableroSol.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        sunPanel.setPreferredSize(new Dimension(100, 50)); 
        sunPanel.setOpaque(false);
    
        // Contador de soles
        sunsLabel = new JLabel("Soles: " + board.getSuns());
        sunsLabel.setForeground(Color.YELLOW);
        sunsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sunPanel.add(sunsLabel);
    
        // Agregar el panel del sol al lado izquierdo
        header.add(sunPanel, BorderLayout.WEST);
    
        // Panel de fondo con botones
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("images/TableroPlantas.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        backgroundPanel.setOpaque(false);
    
        // Botones de las plantas
        JButton sunflowerButton = createPlantButton("Sunflower", "images/cartaSunFlower.jpg");
        JButton peashooterButton = createPlantButton("Peashooter", "images/cartaPeasShooter.jpg");
        JButton walnutButton = createPlantButton("WallNut", "images/cartaWallNut.jpg");
        JButton potatoMineButton = createPlantButton("PotatoMine", "images/cartaPotatoMine.jpg");
        JButton eciPlantMineButton = createPlantButton("eciPlant", "images/cartaeciPlant.jpg");
    
        // Agregar botones al panel de fondo
        backgroundPanel.add(sunflowerButton);
        backgroundPanel.add(peashooterButton);
        backgroundPanel.add(walnutButton);
        backgroundPanel.add(potatoMineButton);
        backgroundPanel.add(eciPlantMineButton);
    
        // Panel para los botones al centro
        header.add(backgroundPanel, BorderLayout.CENTER);
    
        return header;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(200, 0));
        rightPanel.setBackground(Color.DARK_GRAY);

        // Imagen de la carretera
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

    private JButton createPlantButton(String plantType, String imagePath) {
        JButton button = new JButton();
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // Cargar la imagen
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setPreferredSize(new Dimension(90, 110));

        button.addActionListener(e -> selectedPlant = plantType);

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

        // Crear GameCells para cada posición
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GameCell cell = new GameCell(i, j);
                cells[i][j] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

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