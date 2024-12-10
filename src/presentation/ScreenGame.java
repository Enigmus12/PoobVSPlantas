package presentation;

import domain.Board;
import domain.PoobVSZombiesExeption;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class ScreenGame extends JFrame {
    private Board board;           // Singleton que maneja la lógica del juego
    private String selectedPlant;  // Almacena el tipo de planta seleccionada actualmente
    private MainApp app;
    private JLabel sunsLabel;      // Etiqueta para mostrar la cantidad de soles
    private static final int ROWS = 5;
    private static final int COLS = 10;
    private GameCell[][] cells;
    private int remainingTime; // Tiempo restante en segundos
    private JLabel timerLabel; // Para mostrar el tiempo restante
    private boolean shovelMode;
    private String gameMode;

    public ScreenGame(MainApp app,String modeGame ) {
        this.app = app; // Recibir referencia de MainApp
        this.board = Board.getBoard(); // Obtener instancia del tablero
        this.cells = new GameCell[ROWS][COLS]; // Inicializar matriz de celdas
        this.shovelMode = false;
        prepareElements();
        prepareActions();
        game();
    }


    private void prepareElements() {
        setTitle("Poob vs Zombies");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        remainingTime = 10 * 60;

        // Crear paneles
        JPanel centerPanel = createGameBoard();
        JPanel header = createHeader();
        JPanel leftPanel = createLeftGridPanel();
        JPanel rightPanel = createRightPanel();


        // Agregar paneles
        add(header, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        // Temporizador general del juego
        Timer gameTimer = new Timer(1000, e -> {
            updateSunsCounter(); // Actualiza los soles
            updateTimer();       // Actualiza el temporizador
        });
        gameTimer.start();
    }

    public void prepareActions() {

        // Cada celda del tablero pueda responder cuando el usuario haga clic en ella
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                GameCell cell = cells[row][col];
                cell.addActionListener(e -> handleCellClick(cell));
            }
        }

    }



    // acciones que deben realizarse cuando el usuario hace clic en una celda
    private void handleCellClick(GameCell cell) {
        if (shovelMode) {
            try {
                board.shovel(cell.getRow(), cell.getColumn());
                cell.removePlant();
                shovelMode = false; // Desactivar modo pala después de usar
                setCursor(Cursor.getDefaultCursor()); // Restaurar cursor
            } catch (PoobVSZombiesExeption exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        } else if (selectedPlant != null) {
            try {
                board.addPlant(selectedPlant, cell.getRow(), cell.getColumn());
                cell.addPlant(selectedPlant);
                cell.repaint();
                updateSunsCounter();
                
                // Restablecer el cursor al cursor predeterminado
                setCursor(Cursor.getDefaultCursor());
                
                // Limpiar la selección de planta
                selectedPlant = null;
            } catch (PoobVSZombiesExeption exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Seleccione una planta primero o use la pala.",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE);
        }
    }



    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(10, 120));
        header.setBackground(Color.GRAY);

        // Agregar paneles al header
        header.add(createSunPanel(), BorderLayout.WEST);
        header.add(createTimerPanel(), BorderLayout.EAST);
        header.add(createBackgroundPanel(), BorderLayout.CENTER);
        header.add(createShovelButton(), BorderLayout.EAST);

        return header;
    }

    private JPanel createSunPanel() {
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
        sunsLabel.setBounds(10,300,100,50);
        sunsLabel.setForeground(Color.YELLOW);
        sunsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sunPanel.add(sunsLabel);

        return sunPanel;
    }

    private JPanel createTimerPanel() {
        JPanel timerPanel = new JPanel();
        timerPanel.setOpaque(false);

        timerLabel = new JLabel(formatTime(remainingTime)); // Inicializar etiqueta del temporizador
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(timerLabel);

        return timerPanel;
    }

    private JPanel createBackgroundPanel() {
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
        String[][] plantData = {
                {"SunFlower", "images/cartaSunFlower.jpg", "images/SunFlowerCursor.png"},
                {"PeasShooter", "images/cartaPeasShooter.jpg", "images/PeasShooterCursor.png"},
                {"WallNut", "images/cartaWallNut.jpg", "images/WallNutCursor.png"},
                {"PotatoMine", "images/cartaPotatoMine.jpg", "images/PotatoMineCursor.png"},
                {"EciPlant", "images/cartaEciPlant.jpg", "images/EciPlantCursor.png"}
        };

        for (String[] data : plantData) {
            JButton plantButton = createPlantButton(data[0], data[1], data[2]);
            backgroundPanel.add(plantButton);
        }

        return backgroundPanel;
    }


    private JButton createShovelButton() {
        JButton shovelButton = new JButton();
        shovelButton.setFocusPainted(false);
        shovelButton.setBorderPainted(false);
        shovelButton.setContentAreaFilled(false);

        ImageIcon icon = new ImageIcon("images/Shovel.png");
        Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        shovelButton.setIcon(new ImageIcon(scaledImage));
        shovelButton.setPreferredSize(new Dimension(90, 110));

        shovelButton.addActionListener(e -> {
            shovelMode = true;
            selectedPlant = null;

            // Con esto cambiamos el mouse a una imagen personalizada
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image shovelImage = toolkit.getImage("images/ShovelCursor.png");
            Cursor customCursor = toolkit.createCustomCursor(
                shovelImage,
                new Point(0, 0), // Punto activo del cursor
                "ShovelCursor"
            );
            setCursor(customCursor); // Establecer el cursor personalizado

        });


        return shovelButton;
    }

    // Método para actualizar el tiempo restante
    private void updateTimer() {
        if (remainingTime > 0) {
            remainingTime--; // Decrementar el tiempo
            timerLabel.setText(formatTime(remainingTime)); // Actualizar etiqueta del temporizador
        } else {
            // Mostrar mensaje cuando el tiempo llega a cero
            JOptionPane.showMessageDialog(this, "¡El tiempo se acabó!", "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Salir del juego o manejar la lógica de fin
        }
    }

    // Formatear el tiempo como MM:SS
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(200, 0));
        rightPanel.setBackground(Color.DARK_GRAY);

        // Imagen de la carretera
        ImageIcon roadIcon = new ImageIcon("images/Carretera.png");
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

    private JButton createPlantButton(String plantType, String imagePath, String cursorImagePath) {
        JButton button = new JButton();
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    
        // Cargar la imagen para el botón
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setPreferredSize(new Dimension(90, 110));
    
        // oyente
        button.addActionListener(e -> {
            shovelMode = false; // Desactivar modo pala
            selectedPlant = plantType; // Seleccionar el tipo de planta
    
            // Cambiar el cursor a uno personalizado para la planta
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image cursorImage = toolkit.getImage(cursorImagePath);
            Cursor customCursor = toolkit.createCustomCursor(
                cursorImage,
                new Point(0, 0), // Punto activo del cursor
                plantType + "Cursor" // Nombre del cursor
            );
            setCursor(customCursor); 
        });
    
        return button;
    }

    private JPanel createGameBoard() {
        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 2, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("images/Tablero.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Crear GameCells para cada posición
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GameCell cell = new GameCell(i, j);
                cells[i][j] = cell;
                panel.add(cell);

                // Conectar horizontalmente
                if (j > 0) {
                    cells[i][j - 1].setNext(cell); // La celda a la izquierda apunta a la actual
                    cell.setPrevious(cells[i][j - 1]); // La celda actual apunta a la izquierda
                }

                // Las celdas en los bordes mantienen previous o next como null automáticamente
                if(j==0){
                    cell.addLawnMower(i);
                }
            }
        }

        return panel;
    }


    private void updateSunsCounter() {
        sunsLabel.setText("Soles: " + board.getSuns());
    }
    private void game(){
        if(app.getGameMode()=="OnePlayer"){
            gameOnePlayer();
        }
    }
    private void gameOnePlayer() {
        // Crear un Timer para generar zombies periódicamente
        Timer zombieSpawnTimer = new Timer(10 * 1000, e -> { // Cada 10 segundos
            // Obtener un zombie aleatorio desde el tablero
            HashMap<String, int[]> coordenadaZombie = board.gameOnePlayer();

            if (coordenadaZombie != null && !coordenadaZombie.isEmpty()) {
                // Obtener la única entrada del HashMap
                java.util.Map.Entry<String, int[]> entry = coordenadaZombie.entrySet().iterator().next();

                // Extraer la clave (tipo de zombie) y el valor (posición)
                String zombieType = entry.getKey();
                int[] position = entry.getValue();

                // Validar la posición antes de agregar el zombie
                if (position[0] >= 0 && position[0] < ROWS && position[1] >= 0 && position[1] < COLS) {
                    GameCell cell = cells[position[0]][position[1]];
                    cell.addZombie(zombieType);
                    cell.repaint();
                }
            }

            // Cambiar el intervalo del Timer a un valor aleatorio entre 10 y 15 segundos
            ((Timer) e.getSource()).setDelay((10 + new Random().nextInt(6)) * 1000); // Entre 10-15 segundos
        });

        zombieSpawnTimer.setInitialDelay(10 * 1000); // Primer zombie a los 10 segundos
        zombieSpawnTimer.setRepeats(true);          // Repetir indefinidamente
        zombieSpawnTimer.start();
    }

}